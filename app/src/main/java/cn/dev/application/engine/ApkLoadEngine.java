package cn.dev.application.engine;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

import cn.dev.application.R;
import cn.dev.application.utils.FileUtils;
import cn.dev.application.utils.IOUtils;
import cn.dev.application.utils.UIUtils;

/**
 * 通过HttpURLConnection来实现apk的下载，安装。
 * Created by air on 2016/1/17.
 */
public class ApkLoadEngine {

    private Context mContext;
    private NotificationManager mNotifyManager;
    private NotificationCompat.Builder mBuilder;
    private RemoteViews remoteViews;
    private int notifyID = 9999;

    private String url;
    private File apkFile;
    private static Set<String> downCache = new HashSet<String>();
    private String apkName = "update.apk";

    private final int LOAD_OK = 200;
    private final int LOAD_ERROR = 300;
    private DownloadHandler mHandler;

    private class DownloadHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case LOAD_OK:
                    installApk();
                    break;
                case LOAD_ERROR:
                    showError();
                    break;
                default:
                    break;
            }
        }
    }

    public ApkLoadEngine(String url) {
        this.url = url;
        mContext = UIUtils.getContext();
        mNotifyManager =
                (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);

    }

    public void download() {
        if (downCache.contains(url)) {
            Toast.makeText(mContext, "正在下载中……", Toast.LENGTH_LONG).show();
            return;
        }
        if (!FileUtils.isAvailable(50, mContext.getFilesDir())) {
            Toast.makeText(mContext, "存储空间不足", Toast.LENGTH_LONG).show();
            return;
        }
        apkFile = new File(mContext.getFilesDir(), apkName);
        if (apkFile.exists()) {
            apkFile.delete();
        }
        mHandler = new DownloadHandler();
        notification();
        downloadApk();
    }

    private void notification() {
        mBuilder = new NotificationCompat.Builder(mContext);
        //大于4.0的用系统的通知栏，小于自定义
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            mBuilder.setContentTitle("xxx下载中……")
                    .setSmallIcon(android.R.drawable.stat_sys_download)
                    .setContentText("已下载0%")
                    .setTicker("正在下载")
                    .setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(),
                            R.mipmap.ic_launcher
                    ))
                    .setProgress(100, 0, false);
        } else {
            remoteViews = new RemoteViews(mContext.getPackageName(), R.layout.notification);
            remoteViews.setProgressBar(R.id.notice_progress, 100, 0, false);
            remoteViews.setTextViewText(R.id.notice_content, "正在下载中0%");
            remoteViews.setImageViewResource(R.id.notice_icon, R.mipmap.ic_launcher);
            mBuilder.setContent(remoteViews)
                    .setSmallIcon(android.R.drawable.stat_sys_download)
                    .setTicker("正在下载");
        }
        mNotifyManager.notify(notifyID, mBuilder.build());
    }

    private void updateNotification(String title,String content,int progress){
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            mBuilder.setContentTitle(title)
                    .setContentText(content)
                    .setProgress(100, progress, false);
        } else {
            remoteViews.setProgressBar(R.id.notice_progress, 100, progress, false);
            remoteViews.setTextViewText(R.id.notice_content, content);
            mBuilder.setContent(remoteViews);
        }
        mNotifyManager.notify(notifyID, mBuilder.build());
    }

    private void downloadApk() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                InputStream is = null;
                FileOutputStream outputStream = null;
                try {
                    downCache.add(url);
                    URL u = new URL(url);
                    HttpURLConnection conn = (HttpURLConnection) u.openConnection();
                    conn.setReadTimeout(10 * 1000);
                    conn.setConnectTimeout(15 * 1000);
                    conn.setRequestMethod("GET");
                    conn.setDoInput(true);
                    conn.connect();
                    int response = conn.getResponseCode();
                    if (response == 200) {
                        long receiveLength = 0;
                        int count = 0;
                        int contentLength = conn.getContentLength();
                        is = conn.getInputStream();
                        int length = -1;
                        byte[] buffer = new byte[8 * 1024];
                        outputStream = UIUtils.getContext().openFileOutput(apkName, Context.MODE_WORLD_READABLE);
                        while ((length = is.read(buffer)) != -1) {
                            outputStream.write(buffer, 0, length);
                            receiveLength += length;
                            if (count++ % 50 != 0) {
                                continue;
                            }
                            int progress = (int) (receiveLength * 100.0F / contentLength);
                            updateNotification("下载中……","正在下载中"+progress+"%",progress);
                        }
                        updateNotification("xxx","下载完成",100);
                        mHandler.sendEmptyMessage(LOAD_OK);
                    } else {
                        mHandler.sendEmptyMessage(LOAD_ERROR);
                    }
                } catch (IOException e) {
                    mHandler.sendEmptyMessage(LOAD_ERROR);
                } finally {
                    mNotifyManager.cancel(notifyID);
                    downCache.remove(url);
                    IOUtils.close(outputStream);
                    IOUtils.close(is);
                }
            }
        }).start();
    }

    private void installApk() {
        Uri uri = Uri.fromFile(apkFile);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(uri, "application/vnd.android.package-archive");
        UIUtils.getContext().startActivity(intent);
    }

    private void showError(){
        Toast.makeText(UIUtils.getContext(),"下载失败",Toast.LENGTH_LONG).show();
        if (apkFile.exists()){
            apkFile.delete();
        }
    }


}
