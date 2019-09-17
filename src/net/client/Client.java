package net.client;

import util.Constants;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

/**
 * Created by Agony on 2018/5/18.
 */
public class Client {
    public static void main(String[] args) {
        try (Socket socket = new Socket(Constants.REMOTE_IP, Constants.PORT);
             PrintStream ps = new PrintStream(socket.getOutputStream());
             BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
            new Thread(new ClientThread(socket)).start();
            String line;
            while ((line = br.readLine()) != null) {
                ps.println(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
