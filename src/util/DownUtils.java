package util;

import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.Instant;

/**
 * Created by Agony on 2018/5/21.
 */
public class DownUtils {

    private String downPath;

    private String storePath;

    private int threadCount;

    private DownThread[] downThreads;

    private long fileSize;

    private long downSize;

    private class DownThread extends Thread {

        private long startPos;

        private long chunkSize;

        private RandomAccessFile rd;

        private long downLength;

        public DownThread(long startPos, long chunkSize, RandomAccessFile rd) {
            this.startPos = startPos;
            this.chunkSize = chunkSize;
            this.rd = rd;
        }

        @Override
        public void run() {
            try {
                URL url = new URL(downPath);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setRequestProperty("Accept", "*/*");
                connection.setRequestProperty("Charset", "utf-8");
                connection.setRequestProperty("Connection", "Keep-Alive");
                InputStream inputStream = connection.getInputStream();
                inputStream.skip(startPos);
                byte[] buff = new byte[1024 * 1024];
                int hasRead;
                while (downLength < chunkSize && (hasRead = inputStream.read(buff)) != -1) {
                    rd.write(buff, 0, hasRead);
                    downLength += hasRead;
                }
                rd.close();
                inputStream.close();
                System.out.println(getName() + " done.");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public DownUtils(String downPath, String storePath, int threadCount) {
        this.downPath = downPath;
        this.storePath = storePath;
        this.threadCount = threadCount;
        this.downThreads = new DownThread[threadCount];
    }

    public void download() throws Exception {
        URL url = new URL(downPath);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Accept", "*/*");
        connection.setRequestProperty("Charset", "utf-8");
        connection.setRequestProperty("Connection", "Keep-Alive");
        fileSize = connection.getContentLengthLong();
        connection.disconnect();
        long chunkSize = fileSize / threadCount + 1;
        RandomAccessFile rdt = new RandomAccessFile(storePath, "rw");
        rdt.setLength(fileSize);
        rdt.close();
        for (int i = 0; i < threadCount; i++) {
            long startPos = i * chunkSize;
            RandomAccessFile rd = new RandomAccessFile(storePath, "rw");
            rd.seek(startPos);
            downThreads[i] = new DownThread(startPos, chunkSize, rd);
            downThreads[i].start();
        }
        rdt.close();
    }

    public double getProgress() {
        for (int i = 0; i < threadCount; i++) {
            downSize += downThreads[i].downLength;
        }
        return downSize * 1.0 / fileSize;
    }

    public static void main(String[] args) throws Exception {
        String downPath = "http://192.168.100.134:35001/down/joom.dump";
        String storePath = "C:\\Users\\admin\\Desktop\\joom.dump";
        DownUtils downUtils = new DownUtils(downPath, storePath, 10);
        downUtils.download();
        new Thread(() -> {
            long start = Instant.now().getEpochSecond();
            long lastDownSize = 0;
            while (downUtils.getProgress() < 1) {
                System.out.println(String.format("progress: %f(%d/%d), lastSpeed: %fM/s", downUtils.getProgress(), lastDownSize, downUtils.fileSize, (downUtils.downSize - lastDownSize) * 1.0 / 1024 / 1024));
                lastDownSize = downUtils.downSize;
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("\ndone: " + (Instant.now().getEpochSecond() - start));
        }).start();
    }
}
