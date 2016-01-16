package cn.dev.application.utils;

import android.os.Environment;
import android.os.StatFs;
import android.text.TextUtils;
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

    public static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    public static boolean isAvailable(long needMB,File file){
        long needByte = needMB*1024*1024;
        StatFs statFs=new StatFs(file.getPath());
        long blockSize = statFs.getBlockSize();
        long availableBlocks = statFs.getAvailableBlocks();
        XLog.i(blockSize + "");
        XLog.i(availableBlocks +"");
        XLog.i(availableBlocks*blockSize + "");
        String s1 = Formatter.formatFileSize(UIUtils.getContext(), (availableBlocks*blockSize));
        XLog.i("free size："+ s1);
        return availableBlocks*blockSize>needByte;
    }

    public static void logFileInfo(File file){
//        XLog.i(file.getAbsolutePath());
        Log.i(TAG,file.getAbsolutePath());

        if (file.exists()){
            File[] files = file.listFiles();
            for (File f : files){
                if (f.isDirectory()){
                    logFileInfo(f);
                }else {
                    Log.i(TAG,f.getPath() + "\t" + f.getName());
//                    XLog.i(f.getName());
                }
            }
        }
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

    /**
     * 删除文件夹下的所有文件或者某一个文件
     * @param name 文件名，若为null，删除目录下所有文件
     * @param str
     */
    public static void deleteFile(String name,String... str) {
        try {
            if (! exists(name,str)){
                return;
            }
            File file = createFile(name,str);
            if (!TextUtils.isEmpty(name)){
                file.delete();
                return;
            }
            File[] files = file.listFiles();
            for (int i = 0; i < files.length; i++) {
                if (!files[i].isDirectory()) {
                    files[i].delete();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 创建文件夹或者文件
     * @param name 文件名
     * @param str  文件各级目录
     * @return
     */
    public static File createFile(String name, String... str) {
        File folder = new File(getFilePath(str));
        if (!folder.exists()) {
            folder.mkdirs();
        }
        return TextUtils.isEmpty(name) ? folder : new File(folder, name);
    }

    /**
     * 判断文件夹或者文件是否存在
     * @param name 文件名
     * @param str  文件各级目录
     */
    public static boolean exists(String name,String... str){
        String path = getFilePath(str);
        return TextUtils.isEmpty(name) ? new File(path).exists():new File(path,name).exists();
    }

    /**
     * 将每级目录名拼接成文件夹目录
     * @param str  文件各级目录
     */
    public static String getFilePath(String... str){
        StringBuffer sb = new StringBuffer();
        if (str == null) {
            throw new RuntimeException("文件目录不能为null");
        }
        for (int i = 0; i < str.length; i++) {
            sb.append(str[i] + File.separator);
        }
        return sb.toString();
    }
}
