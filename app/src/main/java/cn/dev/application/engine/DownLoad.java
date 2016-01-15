package cn.dev.application.engine;

import com.loopj.android.http.FileAsyncHttpResponseHandler;

import java.io.File;
import java.util.ArrayList;

import cn.dev.application.loopjnet.HttpClient;
import cn.dev.application.utils.FileUtils;
import cn.dev.application.utils.UIUtils;
import cn.dev.application.utils.XLog;
import cz.msebera.android.httpclient.Header;

/**
 * Created by air on 16/1/15.
 */
public class DownLoad {

    private String url = "http://p.gdown.baidu.com/c10e754d2c07d06165a7fa77f828ff56253660db7aa48730409b871c662531415a979901a915832372946cd73b5af6284df8f986121bc76839ee05b565e7275c3b40c5e2767c18aa60de2434625777ed38f55195ab71a5f629a80630d3d92ea8b2c0ca7d85810c56f5e2eccd3fda703b97799b69371a48f5b978286c8a939045e76dce7129114084441670b5b360ff9c4cc7ad98926f35d09ad6041a841fd9d1c78aea881d6081919024f81b18c95d0ed9fa2e0d0d81805af7012ecdde03125f0daa4deff75c9c1c9d29645460a141c03c01ad0e57d30d09f92dc1b8d0437d6544e906e1f146c2282093102d7e6c0d3ef5a7e5aeae3de57bf5a94af9e413976f250cd3f662fedd0031352b9410f892b75aee705cfd2c4a490470e74d95835a9fabaea301eebf607f3ce0c87c8791a6f0a3ba11adb9f775ddb8420f96f1a4d67c";

    private File file;
    private static ArrayList<String> urls = new ArrayList<String>();

    public void download(){
        if (!check()){
            return;
        }
        HttpClient.getInstance().load(url, new FileAsyncHttpResponseHandler(file) {

            @Override
            public void onProgress(long bytesWritten, long totalSize) {
                super.onProgress(bytesWritten, totalSize);
                XLog.i(String.format("Progress %d from %d (%2.0f%%)", bytesWritten, totalSize, (totalSize > 0) ? (bytesWritten * 1.0 / totalSize) * 100 : -1));
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, File file) {
                file.delete();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, File file) {
                XLog.i(file.toString());
            }
        });
    }

    public boolean check(){
        File filesDir = UIUtils.getContext().getFilesDir();
        if (FileUtils.isAvailable(50,filesDir)){
            file = new File(filesDir,"update.apk");
            if (urls.contains(url))return false;
            urls.add(url);
            if (file.exists()){
                file.delete();
            }
            return true;
        }
        return false;
    }
}
