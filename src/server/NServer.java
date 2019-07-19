package server;

import util.CloseUtils;
import util.Constants;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.Charset;

/**
 * Created by Agony on 2018/5/21.
 */
public class NServer {

    private Selector selector;

    private Charset charset = Charset.forName(Constants.UTF_8);

    public void init() throws IOException {
        selector = Selector.open();
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.bind(new InetSocketAddress(Constants.IP, Constants.PORT));
        serverSocketChannel.configureBlocking(false);
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        while (selector.select() > 0) {
            for (SelectionKey selectionKey : selector.selectedKeys()) {
                selector.selectedKeys().remove(selectionKey);
                if (selectionKey.isAcceptable()) {
                    SocketChannel socketChannel = serverSocketChannel.accept();
                    System.out.println("client connect.");
                    socketChannel.configureBlocking(false);
                    socketChannel.register(selector, SelectionKey.OP_READ);
                    selectionKey.interestOps(SelectionKey.OP_ACCEPT);
                }
                if (selectionKey.isReadable()) {
                    SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
                    ByteBuffer byteBuffer = ByteBuffer.allocate(Constants.BUFF_SIZE);
                    String content = "";
                    try {
                        while (socketChannel.read(byteBuffer) > 0) {
                            byteBuffer.flip();
                            content += charset.decode(byteBuffer);
                        }
                        System.out.println("server read: " + content);
                        selectionKey.interestOps(SelectionKey.OP_READ);
                    } catch (IOException e) {
                        e.printStackTrace();
                        selectionKey.cancel();
                        CloseUtils.close(selectionKey.channel());
                    }
                    if (content.length() > 0) {
                        for (SelectionKey selectionKey1 : selector.keys()) {
                            Channel channel = selectionKey1.channel();
                            if (channel instanceof SocketChannel) {
                                SocketChannel socketChannel1 = (SocketChannel) channel;
                                socketChannel1.write(charset.encode(content));
                            }
                        }
                    }
                }
            }
        }
    }

    public static void main(String[] args) throws IOException {
        new NServer().init();
    }
}
