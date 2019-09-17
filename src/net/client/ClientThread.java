package net.client;

import util.CloseUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * Created by Agony on 2018/5/18.
 */
public class ClientThread implements Runnable {

    private Socket socket;
    private BufferedReader br;

    public ClientThread(Socket socket) throws IOException {
        this.socket = socket;
        br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    @Override
    public void run() {
        String content;
        try {
            while ((content = br.readLine()) != null) {
                System.out.println(content);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            CloseUtils.close(br);
        }
    }
}
