package util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FileUtil {

    public static void main(String[] args) throws IOException {
        String source = "C:\\Users\\Agony\\Documents\\VanDyke\\Config\\Sessions\\joom";
        Files.list(Paths.get(source)).parallel().map(path -> {
            int index = path.getFileName().toString().indexOf("_Task8");
            if (index > 0) {
                return path.getFileName().toString().substring(0, index);
            }
            return null;
        }).forEach(client -> {
            if (client != null && !"null".equals(client)) {
                System.out.println("'" + client + "',");
            }
        });
    }
}
