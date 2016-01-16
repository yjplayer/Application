package cn.dev.application.engine;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import com.loopj.android.http.FileAsyncHttpResponseHandler;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import cn.dev.application.R;
import cn.dev.application.loopjnet.HttpClient;
import cn.dev.application.utils.FileUtils;
import cn.dev.application.utils.IOUtils;
import cn.dev.application.utils.UIUtils;
import cn.dev.application.utils.XLog;
import cz.msebera.android.httpclient.Header;

/**
 * apk的下载与更新
 * Created by air on 16/1/15.
 */
public class DownLoad {

    private NotificationManager mNotifyManager;
    private NotificationCompat.Builder mBuilder;
    private int notifyID = 9999;
    private int oldProgress = 3;

    public DownLoad(String url) {
        this.url = url;
    }

    private String url;
    private File file;
    private File installFile;
    private static ArrayList<String> downCache = new ArrayList<String>();
    private String apkName = "update.apk";

    public void download() {
        if (!checkEnvironment()) {
            return;
        }
        notification();
        HttpClient.getInstance().load(url, new FileAsyncHttpResponseHandler(file) {

            @Override
            public void onStart() {
                XLog.i("loading start");
                downCache.add(url);
            }

            @Override
            public void onProgress(long bytesWritten, long totalSize) {
                super.onProgress(bytesWritten, totalSize);
                int progress = (int) ((bytesWritten * 1.0 / totalSize) * 100);
                if (progress - oldProgress > 7){
                    mBuilder.setProgress(100,progress,false)
                            .setContentTitle("下载中……")
                            .setContentText("已下载"+progress+"%");
                    mNotifyManager.notify(notifyID,mBuilder.build());
                    oldProgress = progress;
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, File file) {
                mNotifyManager.cancel(notifyID);
                file.delete();
                downCache.remove(url);
                Toast.makeText(UIUtils.getContext(),"下载失败",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, File file) {
                XLog.i(file.toString());
                mBuilder.setProgress(0,0,false)
                .setContentTitle("xxx")
                .setContentText("下载完成");
                mNotifyManager.notify(notifyID,mBuilder.build());
                mNotifyManager.cancel(notifyID);
                downCache.remove(url);
                installApk();
            }
        });
    }

    private void notification() {
        mNotifyManager =
                (NotificationManager) UIUtils.getContext().getSystemService(Context.NOTIFICATION_SERVICE);
        mBuilder = new NotificationCompat.Builder(UIUtils.getContext());
        mBuilder.setContentTitle("xxx下载中……")
                .setContentText("已下载0%")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setTicker("开始下载")
                .setProgress(100,0,false);
        mNotifyManager.notify(notifyID,mBuilder.build());
    }

    /*public void updateNotification(String title,String content){
        mBuilder.setContentText(title)
                .setContentTitle(content);
        mNotifyManager.notify(notifyID,mBuilder.build());
    }*/

    /**
     * 安装apk
     */
    private void installApk() {
        try {
            installFile = new File(UIUtils.getContext().getFilesDir(),apkName);
            if (installFile.exists()){
                installFile.delete();
            }
            copyForInstallCanRead();
            Uri uri = Uri.fromFile(installFile);
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.addCategory(Intent.CATEGORY_DEFAULT);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setDataAndType(uri, "application/vnd.android.package-archive");
            UIUtils.getContext().startActivity(intent);
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(UIUtils.getContext(),"下载安装失败",Toast.LENGTH_LONG).show();
        }
    }

    /**
     * 修改apk的读取权限，使Install程序可以读取。
     */
    private void copyForInstallCanRead() throws IOException {
        FileInputStream fileInputStream = null;
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = UIUtils.getContext().openFileOutput("update.apk", Context.MODE_WORLD_READABLE);
            fileInputStream = new FileInputStream(file);
            int length = -1;
            byte[] buffer = new byte[8*1024];
            while ((length = fileInputStream.read(buffer))!=-1){
                fileOutputStream.write(buffer,0,length);
            }
            FileUtils.logFileInfo(UIUtils.getContext().getFilesDir());
            file.delete();
            FileUtils.logFileInfo(UIUtils.getContext().getFilesDir());
        } finally {
            IOUtils.close(fileInputStream);
            IOUtils.close(fileOutputStream);
        }
    }

    public void downloadByHttpURLConnection(){
        if (downCache.contains(url)) {
            Toast.makeText(UIUtils.getContext(), "正在下载中……", Toast.LENGTH_LONG).show();
            return;
        }

        if (!FileUtils.isExternalStorageWritable()) {
            Toast.makeText(UIUtils.getContext(), "存储空间不存在", Toast.LENGTH_LONG).show();
            return ;
        }

        if (!FileUtils.isAvailable(50, UIUtils.getContext().getFilesDir())) {
            Toast.makeText(UIUtils.getContext(), "存储空间不足", Toast.LENGTH_LONG).show();
            return;
        }
        installFile = new File(UIUtils.getContext().getFilesDir(),apkName);
        if (installFile.exists()){
            installFile.delete();
        }
        mHandler = new DownloadHandler();
        notification();
        downloadFile();
    }

    private final int LOAD_OK = 200;
    private final int LOAD_ERROR = 300;
    private DownloadHandler mHandler;
    private class DownloadHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case LOAD_OK:
                    install();
                    break;
                case LOAD_ERROR:
                    showError();
                    break;
                default:
                    break;
            }
        }
    }

    private void install() {
        Uri uri = Uri.fromFile(installFile);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(uri, "application/vnd.android.package-archive");
        UIUtils.getContext().startActivity(intent);
    }

    private void downloadFile(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                InputStream is = null;
                FileOutputStream outputStream = null;
                try {
                    downCache.add(url);
                    outputStream = UIUtils.getContext().openFileOutput(apkName,Context.MODE_WORLD_READABLE);
                    URL u = new URL(url);
                    HttpURLConnection conn = (HttpURLConnection) u.openConnection();
                    conn.setReadTimeout(10 * 1000);
                    conn.setConnectTimeout(15 * 1000);
                    conn.setRequestMethod("GET");
                    conn.setDoInput(true);
                    conn.connect();
                    int response = conn.getResponseCode();
                    if (response == 200) {
                        int contentLength = conn.getContentLength();
                        is = conn.getInputStream();
                        int len = -1;
                        byte[] buffer = new byte[8 * 1024];
                        outputStream = new FileOutputStream(file);
                        while ((len = is.read(buffer)) != -1) {
                            outputStream.write(buffer, 0, len);
                        }
                        mHandler.sendEmptyMessage(LOAD_OK);
                    } else {
                        mHandler.sendEmptyMessage(LOAD_ERROR);
                    }
                } catch (IOException e) {
                        mHandler.sendEmptyMessage(LOAD_ERROR);
                } finally {
                    downCache.remove(url);
                    IOUtils.close(outputStream);
                    IOUtils.close(is);
                }
            }
        }).start();
    }

    private void showError(){
        Toast.makeText(UIUtils.getContext(),"下载失败",Toast.LENGTH_LONG).show();
        if (installFile.exists()){
            installFile.delete();
        }
    }

    /**
     * 1：是否已经在下载
     * 2：存储空间大小是否大于50M
     * 3：删除原同名文件
     *
     * @return
     */
    public boolean checkEnvironment() {

        if (downCache.contains(url)) {
            Toast.makeText(UIUtils.getContext(), "正在下载中……", Toast.LENGTH_LONG).show();
            return false;
        }

        if (!FileUtils.isExternalStorageWritable()) {
            Toast.makeText(UIUtils.getContext(), "存储空间不存在", Toast.LENGTH_LONG).show();
            return false;
        }

        File fileDir = FileUtils.createFile(null, UIUtils.getContext().getFilesDir().getAbsolutePath()
                + File.separator + "download");
        if (!FileUtils.isAvailable(50, fileDir)) {
            Toast.makeText(UIUtils.getContext(), "存储空间不足", Toast.LENGTH_LONG).show();
            return false;
        }

        file = new File(fileDir, "update.apk");
        if (file.exists()) {
            file.delete();
        }
        return true;
    }
}
