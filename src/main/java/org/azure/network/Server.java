package org.azure.network;

import com.google.inject.Injector;
import com.netflix.governator.annotations.AutoBindSingleton;
import com.netflix.governator.annotations.Configuration;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.azure.network.codec.Decoder;
import org.azure.network.codec.Encoder;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

@AutoBindSingleton
public class Server {
    private static final Logger logger = LogManager.getLogger(Server.class);
    private static Injector injector;
    @Configuration(value = "org.azure.network.Server.port")
    private int port;
    @Configuration(value = "org.azure.network.Server.host")
    private String host;

    public static Injector getInjector() {
        return injector;
    }

    public static void setInjector(Injector injector) {
        Server.injector = injector;
    }

    private static final ThreadFactory factory = new ThreadFactory() {
        private final ThreadFactory wrapped = Executors.defaultThreadFactory();

        public Thread newThread(final Runnable r) {
            final Thread t = wrapped.newThread(r);
            t.setDaemon(true);
            return t;
        }
    };

    public void startServer() {
        EventLoopGroup bossGroup = new NioEventLoopGroup(Runtime.getRuntime().availableProcessors(), factory);
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 10)
                            //.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 10000)
                    .childOption((ChannelOption.SO_KEEPALIVE), true)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(
                                    new LoggingHandler(LogLevel.INFO),
                                    new Encoder(),
                                    new Decoder(),
                                    new ServerHandler());
                        }
                    });

            Channel ch = b.bind(new InetSocketAddress(host, port)).sync().channel();
            logger.info("Successfully established a socket connection on {}:{}", this.host, this.port);
            ch.closeFuture().sync();
        } catch (final ChannelException ex) {
            logger.error("Failed to establish a socket connection on {}:{}", this.host, this.port);
            System.exit(1);
        } catch (InterruptedException e) {
            logger.error("Interrupted while establishing a socket connection on {}:{}", this.host, this.port);
            System.exit(1);
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }
}
