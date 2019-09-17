package net.server;

import util.Constants;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Agony on 2018/5/23.
 */
public class AServer {

    private static List<AsynchronousSocketChannel> aSocketChannels = new ArrayList<>();

    private Charset charset = Charset.forName(Constants.UTF_8);

    private class AcceptHandler implements CompletionHandler<AsynchronousSocketChannel, Object> {

        private ByteBuffer byteBuffer = ByteBuffer.allocate(Constants.BUFF_SIZE);

        private AsynchronousServerSocketChannel aServerSocketChannel;

        public AcceptHandler(AsynchronousServerSocketChannel aServerSocketChannel) {
            this.aServerSocketChannel = aServerSocketChannel;
        }

        @Override
        public void completed(AsynchronousSocketChannel aSocketChannel, Object attachment) {
            aSocketChannels.add(aSocketChannel);
            aServerSocketChannel.accept(null, this);
            aSocketChannel.read(byteBuffer, null, new CompletionHandler<Integer, Object>() {
                @Override
                public void completed(Integer result, Object attachment) {
                    byteBuffer.flip();
                    String content = charset.decode(byteBuffer).toString();
                    System.out.println("net.server read: " + content);
                    for (AsynchronousSocketChannel aSocketChannel : aSocketChannels) {
                        try {
                            aSocketChannel.write(ByteBuffer.wrap(content.getBytes(Constants.UTF_8))).get();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    byteBuffer.clear();
                    aSocketChannel.read(byteBuffer, null, this);
                }

                @Override
                public void failed(Throwable exc, Object attachment) {
                    exc.printStackTrace();
                    aSocketChannels.remove(aSocketChannel);
                }
            });
        }

        @Override
        public void failed(Throwable exc, Object attachment) {
            exc.printStackTrace();
        }
    }

    public void startListen() throws Exception {
        ExecutorService executorService = Executors.newFixedThreadPool(Constants.THREAD_SIZE);
        AsynchronousChannelGroup aChannelGroup = AsynchronousChannelGroup.withThreadPool(executorService);
        AsynchronousServerSocketChannel aServerSocketChannel = AsynchronousServerSocketChannel.open(aChannelGroup).bind(new InetSocketAddress(Constants.IP, Constants.PORT));
        aServerSocketChannel.accept(null, new AcceptHandler(aServerSocketChannel));
        this.wait();
    }

    public static void main(String[] args) throws Exception {
        new AServer().startListen();
    }
}
