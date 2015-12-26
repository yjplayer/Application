package cn.dev.application.utils;

import java.io.Closeable;
import java.io.IOException;

/**
 * Created by air on 15/12/26.
 */
public class IOUtils {

    /**
     * 关闭资源
     * @param closeable
     */
    public void close(Closeable closeable){
        if (closeable != null){
            try {
                closeable.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
