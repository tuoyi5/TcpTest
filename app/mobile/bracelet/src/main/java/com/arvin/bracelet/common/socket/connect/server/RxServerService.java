package com.arvin.bracelet.common.socket.connect.server;

import android.util.Log;

import java.nio.charset.Charset;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.bytes.ByteArrayEncoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.LoggingHandler;

public class RxServerService {

	private final int port;

	private Channel channel;
	private ChannelFuture mChannelFuture;
	EventLoopGroup bossGroup, workGroup;

	public RxServerService(int port) {
		this.port = port;
	}

	public void start() throws Exception {
		Log.d("arvinn","RxServerService start： " + port);
		bossGroup = new NioEventLoopGroup();
		workGroup = new NioEventLoopGroup();
		try {
			Log.i("arvinn","RxServerService start： " + port);
			ServerBootstrap serverBootstrap = new ServerBootstrap();
			serverBootstrap.group(bossGroup, workGroup) // 绑定线程池
				.childOption(ChannelOption.SO_KEEPALIVE, true)
				.option(ChannelOption.SO_BACKLOG, 1024)
				.channel(NioServerSocketChannel.class) // 指定使用的channel
				.localAddress(this.port)// 绑定监听端口
				.handler(new LoggingHandler())
				.childHandler(new ChannelInitializer<SocketChannel>() {
					@Override
					protected void initChannel(SocketChannel ch) throws Exception {
						Log.i("arvinn","报告");
						Log.d("arvinn","信息：有一客户端链接到本服务端");
						Log.d("arvinn","IP:" + ch.localAddress().getHostName());
						Log.d("arvinn","Port:" + ch.localAddress().getPort());
						Log.d("arvinn","报告完毕");

						ch.pipeline().addLast(new StringEncoder(Charset.forName("UTF-8")));
						ch.pipeline().addLast(new RxServerHandler());
						ch.pipeline().addLast(new ByteArrayEncoder());
					}
				});

			Log.i("arvinn","RxServerService start： " + 22222222);
			mChannelFuture = serverBootstrap.bind(this.port).sync(); // 服务器异步创建绑定
			Log.i("arvinn", "RxServerService 启动正在监听---： ");
			Log.i("arvinn", "RxServerService 启动正在监听： " + mChannelFuture.channel().localAddress());
		} catch (Exception e) {
			Log.e("arvinn", e.getMessage());
		} finally {
			Log.d("arvinn","RxServerService end---： " );
		}
	}

	private void closeFuture() {
		try {
			mChannelFuture.channel().closeFuture().sync(); // 关闭服务器通道
		} catch (Exception e) {
			Log.e("arvinn", e.getMessage());
			e.printStackTrace();
		}
	}

	private void graceFully(){
		try {
			workGroup.shutdownGracefully().sync(); // 释放线程池资源
			bossGroup.shutdownGracefully().sync();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
