package client;

import util.Constants;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Scanner;

/**
 * Created by Agony on 2018/5/22.
 */
public class NClient {

    private Selector selector;

    private Charset charset = Charset.forName(Constants.UTF_8);

    private class ClientThread extends Thread {

        @Override
        public void run() {
            try {
                while (selector.select() > 0) {
                    for (SelectionKey selectionKey : selector.selectedKeys()) {
                        selector.selectedKeys().remove(selectionKey);
                        if (selectionKey.isReadable()) {
                            SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
                            ByteBuffer byteBuffer = ByteBuffer.allocate(Constants.BUFF_SIZE);
                            String content = "";
                            while (socketChannel.read(byteBuffer) > 0) {
                                socketChannel.read(byteBuffer);
                                byteBuffer.flip();
                                content += charset.decode(byteBuffer);
                            }
                            System.out.println("client read: " + content);
                            selectionKey.interestOps(SelectionKey.OP_READ);
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void init() throws IOException {
        selector = Selector.open();
        SocketChannel socketChannel = SocketChannel.open(new InetSocketAddress(Constants.REMOTE_IP, Constants.PORT));
        System.out.println("client connect server success.");
        socketChannel.configureBlocking(false);
        socketChannel.register(selector, SelectionKey.OP_READ);
        new ClientThread().start();
        Scanner scanner = new Scanner(System.in);
        System.out.println("let us chat, input something and keep going:");
        while (scanner.hasNext()) {
            String line = scanner.nextLine();
            socketChannel.write(charset.encode(line));
        }
    }

    public static void main(String[] args) throws IOException {
        new NClient().init();
    }
}
