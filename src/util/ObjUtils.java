package util;

import java.io.*;

/**
 * Created by Agony on 2018/7/18.
 */
public class ObjUtils {

    public static byte[] objToByte(Obj obj) {
        byte[] buf = null;
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
             ObjectOutputStream oos = new ObjectOutputStream(bos)) {
            oos.writeObject(obj);
            buf = bos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return buf;
    }

    public static Obj byteToObj(byte[] buf) {
        Obj obj = null;
        try (ByteArrayInputStream bis = new ByteArrayInputStream(buf);
             ObjectInputStream ois = new ObjectInputStream(bis)) {
            obj = (Obj) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return obj;
    }
}
