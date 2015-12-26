package cn.dev.application.utils;

import android.os.Environment;
import android.os.StatFs;
import android.text.format.Formatter;
import android.util.Log;

import java.io.File;

/**
 * 文件的处理
 * Created by air on 15/12/26.
 */
public class FileUtils {

    public static final String TAG = "File";

    /**
     * 获取可用的外部存储大小
     * @return
     */
    public static long getAvailableBytes(){
        File sdcardDir= Environment.getExternalStorageDirectory();
        StatFs statFs=new StatFs(sdcardDir.getPath());
        return statFs.getAvailableBytes();
    }

    /**
     * 判断是否有足够存储空间
     * @param needMB
     * @return
     */
    public static boolean isAvailable(long needMB){
        long needByte = needMB*1024*1024;
        return getAvailableBytes()>needByte;
    }

    /**
     * log example："sdcard total size：11GB  sdcard free size：10GB；
     */
    public static void logSDcardInfo(){
        File sdcardDir= Environment.getExternalStorageDirectory();
        StatFs statFs=new StatFs(sdcardDir.getPath());
//        long blockSizeLong = statFs.getBlockSizeLong();
//        long blockCountLong = statFs.getBlockCountLong();
//        long availableBlocksLong = statFs.getAvailableBlocksLong();
//        long freeBlocksLong = statFs.getFreeBlocksLong();
        long totalBytes = statFs.getTotalBytes();
        long availableBytes = statFs.getAvailableBytes();

        String s1 = Formatter.formatFileSize(UIUtils.getContext(), totalBytes);
        String s2 = Formatter.formatFileSize(UIUtils.getContext(), availableBytes);

        Log.i(TAG,"sdcard total size："+s1+"\tsdcard free size："+s2);
    }
}
