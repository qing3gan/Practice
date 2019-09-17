package net.server;

import util.CloseUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

/**
 * Created by Agony on 2018/5/18.
 */
public class ServerThread implements Runnable {

    private Socket socket;
    private BufferedReader br;

    public ServerThread(Socket socket) throws IOException {
        this.socket = socket;
        br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    @Override
    public void run() {
        try {
            String content;
            while ((content = br.readLine()) != null) {
                for (Socket socket : Server.clients) {
                    PrintStream ps = new PrintStream(socket.getOutputStream());
                    ps.println(content);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            Server.clients.remove(socket);
            CloseUtils.close(br);
            CloseUtils.close(socket);
        }
    }
}
