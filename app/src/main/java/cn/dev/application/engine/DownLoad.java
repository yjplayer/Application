package cn.dev.application.engine;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import com.loopj.android.http.FileAsyncHttpResponseHandler;

import java.io.File;
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
 * Created by air on 16/1/15.
 */
public class DownLoad {

    private NotificationManager mNotifyManager;
    private NotificationCompat.Builder mBuilder;
    private int notifyID = 999;
    private int oldPrgress = 3;

    public DownLoad(String url) {
        this.url = url;

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

    private String url = "http://p.gdown.baidu.com/c10e754d2c07d06165a7fa77f828ff56253660db7aa48730409b871c662531415a979901a915832372946cd73b5af6284df8f986121bc76839ee05b565e7275c3b40c5e2767c18aa60de2434625777ed38f55195ab71a5f629a80630d3d92ea8b2c0ca7d85810c56f5e2eccd3fda703b97799b69371a48f5b978286c8a939045e76dce7129114084441670b5b360ff9c4cc7ad98926f35d09ad6041a841fd9d1c78aea881d6081919024f81b18c95d0ed9fa2e0d0d81805af7012ecdde03125f0daa4deff75c9c1c9d29645460a141c03c01ad0e57d30d09f92dc1b8d0437d6544e906e1f146c2282093102d7e6c0d3ef5a7e5aeae3de57bf5a94af9e413976f250cd3f662fedd0031352b9410f892b75aee705cfd2c4a490470e74d95835a9fabaea301eebf607f3ce0c87c8791a6f0a3ba11adb9f775ddb8420f96f1a4d67c";
    private File file;
    private static ArrayList<String> downCache = new ArrayList<String>();

    public void download() {
        if (!checkEnvironment()) {
            return;
        }

        HttpClient.getInstance().load(url, new FileAsyncHttpResponseHandler(file) {

            @Override
            public void onProgress(long bytesWritten, long totalSize) {
                super.onProgress(bytesWritten, totalSize);
//                XLog.i(String.format("Progress %d from %d (%2.0f%%)", bytesWritten, totalSize, (totalSize > 0) ? (bytesWritten * 1.0 / totalSize) * 100 : -1));
                int progress = (int) ((bytesWritten * 1.0 / totalSize) * 100);
                if (progress - oldPrgress > 7){
                    mBuilder.setProgress(100,progress,false);
                    mBuilder.setContentText("已下载"+progress+"%");
                    mNotifyManager.notify(notifyID,mBuilder.build());
                    oldPrgress = progress;
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, File file) {
                file.delete();
                downCache.remove(url);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, File file) {
                XLog.i(file.toString());
                mBuilder.setProgress(0,0,false);
                mBuilder.setContentText("下载完成")
                .setContentTitle("xxx");
                mNotifyManager.notify(notifyID,mBuilder.build());
                installApk();
                downCache.remove(url);
            }
        });
    }

    /**
     * 安装apk
     */
    private void installApk() {
        Uri uri = Uri.fromFile(file);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(uri, "application/vnd.android.package-archive");
        UIUtils.getContext().startActivity(intent);
    }

    private class DownloadApkTask extends AsyncTask<String, Integer, Void> {

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            for (Integer i : values) {
                XLog.i("onProgressUpdate:" + i);
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(String... params) {
            downloadApk(url);
            return null;
        }
    }

    private void downloadApk(String apkUrl) {
        InputStream is = null;
        FileOutputStream outputStream = null;
        try {
            URL url = new URL(apkUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10 * 1000);
            conn.setConnectTimeout(15 * 1000);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            conn.connect();
            int response = conn.getResponseCode();
            XLog.i("The response is: " + response);
            if (response == 200) {
                is = conn.getInputStream();
                int len = -1;
                byte[] buffer = new byte[8 * 1024];
                outputStream = new FileOutputStream(file);
                while ((len = is.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, len);
                }
                XLog.i("down over:");
            } else {

            }
        } catch (IOException e) {
            XLog.e("down error:" + e);
        } finally {
            IOUtils.close(outputStream);
            IOUtils.close(is);
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
        downCache.add(url);

        if (!FileUtils.isExternalStorageWritable()) {
            Toast.makeText(UIUtils.getContext(), "存储空间不存在", Toast.LENGTH_LONG).show();
            return false;
        }

        File fileDir = FileUtils.createFile(null, Environment.getExternalStorageDirectory().getPath()
                + File.separator + "应用名字" + File.separator + "download");
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
