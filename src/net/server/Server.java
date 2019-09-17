package net.server;

import util.Constants;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Agony on 2018/5/18.
 */
public class Server {

    public static final List<Socket> clients = Collections.synchronizedList(new ArrayList<>());

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(Constants.PORT)) {
            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("socket connected.");
                clients.add(socket);
                new Thread(new ServerThread(socket)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
