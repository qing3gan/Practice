package client;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.MessageToByteEncoder;
import util.Obj;
import util.ObjUtils;

/**
 * Created by Agony on 2018/7/17.
 */
public class NettyClient {
    public static void main(String[] args) throws InterruptedException {
        EventLoopGroup eventExecutors = null;
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group((eventExecutors = new NioEventLoopGroup()))
                    .channel(NioSocketChannel.class)
                    .remoteAddress("localhost", 8888)
                    .handler(new ChannelInitializer<Channel>() {
                        @Override
                        protected void initChannel(Channel channel) throws Exception {
                            channel.pipeline().addLast(new MessageToByteEncoder<Obj>() {
                                @Override
                                protected void encode(ChannelHandlerContext channelHandlerContext, Obj obj, ByteBuf byteBuf) throws Exception {
                                    byte[] buf = ObjUtils.objToByte(obj);
                                    byteBuf.writeBytes(buf);
                                    channelHandlerContext.flush();
                                }
                            });
                            channel.pipeline().addLast(new SimpleChannelInboundHandler<ByteBuf>() {
                                @Override
                                public void channelActive(ChannelHandlerContext ctx) throws Exception {
                                    System.out.println("client connecting and writing...");
//                                    byte[] req = "QUERY TIME ORDER".getBytes();
//                                    ByteBuf buf = Unpooled.buffer(req.length);
//                                    buf.writeBytes(req);
//                                    ctx.writeAndFlush(buf);
                                    Obj obj = new Obj("attr");
                                    ctx.writeAndFlush(obj);
                                }

                                @Override
                                protected void channelRead0(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf) throws Exception {
                                    System.out.println("client reading...");
                                    byte[] req = new byte[byteBuf.readableBytes()];
                                    byteBuf.readBytes(req);
                                    System.out.println(String.format("client read [%s]", new String(req, "utf-8")));
                                }

                                @Override
                                public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
                                    cause.printStackTrace();
                                    ctx.close();
                                }
                            });
                        }
                    });
            ChannelFuture channelFuture = bootstrap.connect().sync();
            System.out.println("client connect success.");
            channelFuture.channel().closeFuture().sync();
            System.out.println("client disconnect.");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            eventExecutors.shutdownGracefully().sync();
        }
    }
}
