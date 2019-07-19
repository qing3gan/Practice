package util;

import java.io.Closeable;
import java.io.IOException;

/**
 * Created by Agony on 2018/5/18.
 */
public class CloseUtils {

    public static void close(Closeable closeable){
        if(closeable!=null){
            try {
                closeable.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
