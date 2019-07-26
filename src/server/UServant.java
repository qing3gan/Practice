package server;

import util.CloseUtils;
import util.Constants;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;
import java.util.Scanner;

/**
 * Created by Agony on 2018/5/24.
 */
public class UServant implements Runnable {

    private MulticastSocket multicastSocket;

    private InetAddress broadcastAddress = InetAddress.getByName(Constants.BROADCAST_IP);

    private byte[] inBuff = new byte[Constants.DATAGRAM_SIZE];

    private DatagramPacket inPack = new DatagramPacket(inBuff, inBuff.length);

    public UServant() throws UnknownHostException {
    }

    public void init() throws IOException {
        multicastSocket = new MulticastSocket(Constants.BROADCAST_PORT);
        multicastSocket.joinGroup(broadcastAddress);
        multicastSocket.setLoopbackMode(true);
        multicastSocket.setTimeToLive(1);
        DatagramPacket outPack = new DatagramPacket(new byte[0], 0, broadcastAddress, Constants.BROADCAST_PORT);
        new Thread(this).start();
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            byte[] buff = scanner.next().getBytes(Constants.UTF_8);
            outPack.setData(buff);
            multicastSocket.send(outPack);
        }
    }

    @Override
    public void run() {
        try {
            while (true) {
                multicastSocket.receive(inPack);
                System.out.println("multicast read: " + new String(inBuff, 0, inPack.getLength()));
            }
        } catch (IOException e) {
            e.printStackTrace();
            try {
                multicastSocket.leaveGroup(broadcastAddress);
                CloseUtils.close(multicastSocket);
                System.exit(1);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }

    public static void main(String[] args) throws IOException {
        new UServant().init();
    }
}
