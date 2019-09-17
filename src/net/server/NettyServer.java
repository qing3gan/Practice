package net.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import util.Obj;
import util.ObjUtils;

import java.time.LocalDateTime;

/**
 * Created by Agony on 2018/7/17.
 */
public class NettyServer {
    public static void main(String[] args) throws InterruptedException {
        EventLoopGroup eventExecutors = null;
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group((eventExecutors = new NioEventLoopGroup()))
                    .channel(NioServerSocketChannel.class)
                    .localAddress("localhost", 8888)
                    .childHandler(new ChannelInitializer<Channel>() {
                        @Override
                        protected void initChannel(Channel channel) throws Exception {
                            channel.pipeline().addLast(new ChannelInboundHandlerAdapter() {
                                @Override
                                public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                    System.out.println("net.server reading...");
                                    ByteBuf _req = (ByteBuf) msg;
                                    byte[] req = new byte[_req.readableBytes()];
                                    _req.readBytes(req);
//                                    String body = new String(req, "utf-8");
//                                    System.out.println(String.format("net.server read [%s]", body));
                                    Obj obj = ObjUtils.byteToObj(req);
                                    System.out.println(obj);
                                    ctx.fireChannelRead(obj);
                                }
                            });
                            channel.pipeline().addLast(new ChannelOutboundHandlerAdapter() {
                                @Override
                                public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
                                    System.out.println(String.format("net.server write [%s]", msg));
                                    ctx.write(msg);
                                }
                            });
                            channel.pipeline().addLast(new ChannelInboundHandlerAdapter() {
                                @Override
                                public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                    System.out.println("net.server reading again...");
                                    System.out.println("net.server writing...");
                                    ByteBuf resp = Unpooled.copiedBuffer(LocalDateTime.now().toString().getBytes());
                                    ctx.write(resp);
                                }

                                @Override
                                public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
                                    System.out.println("net.server read done.");
                                    ctx.flush();
                                }

                                @Override
                                public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
                                    cause.printStackTrace();
                                    ctx.close();
                                }
                            });
                        }
                    });
            ChannelFuture channelFuture = serverBootstrap.bind().sync();
            System.out.println("Server listen on port: " + channelFuture.channel().localAddress());
            channelFuture.channel().closeFuture().sync();
            System.out.println("Server stop.");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            eventExecutors.shutdownGracefully().sync();
        }
    }
}
