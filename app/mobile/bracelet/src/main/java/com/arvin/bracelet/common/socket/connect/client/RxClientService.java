package com.arvin.bracelet.common.socket.connect.client;


import android.util.Log;

import com.arvin.bracelet.common.socket.connect.server.RxServerHandler;
import com.arvin.bracelet.common.socket.data.ConnectionModel;
import com.arvin.bracelet.common.socket.utils.ChannelPipelineHelper;


import java.nio.charset.Charset;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.bytes.ByteArrayEncoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.stream.ChunkedWriteHandler;

public class RxClientService {

	private static final String TAG = "RxTcpService";
	private ConnectionModel connectionModel;

	private NioEventLoopGroup group;
	private Bootstrap bootstrap;
	private Channel channel;
	boolean isConnect = false;

	public void setConnectionModel(ConnectionModel connectionModel) {
		this.connectionModel = connectionModel;
	}

	public boolean isConnect() {
		return isConnect;
	}

	public void connectServer() {
		android.util.Log.d("arvinn", "connectServer start: "
			+ connectionModel.getServerIP() + " port: " + connectionModel.getPort());
		group = new NioEventLoopGroup();
		bootstrap = new Bootstrap()
			.remoteAddress(connectionModel.getServerIP(), connectionModel.getPort())
			.group(group)
			.channel(NioSocketChannel.class)
			.option(ChannelOption.SO_KEEPALIVE, true)
			.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 3000)
			.handler(new ChannelInitializer<SocketChannel>() {
				@Override
				protected void initChannel(SocketChannel ch) throws Exception {
					Log.i("arvinn","正在连接中...");
					ch.pipeline().addLast(new StringEncoder(Charset.forName("UTF-8")));
					ch.pipeline().addLast(new RxClientHandler());
					ch.pipeline().addLast(new ByteArrayEncoder());
					ch.pipeline().addLast(new ChunkedWriteHandler());
				}
			});

		Log.i("arvinn","RxClientService start： " + 22222222);
		try {
			channel = bootstrap.connect().sync().channel();
			isConnect = channel.isActive();
			android.util.Log.d("arvinn", "connectServer isConnect: " + isConnect);
		} catch (Exception e) {
			Log.e("arvinn--", e.getMessage());
			isConnect = false;
		} finally {
			//graceFully();
		}
	}

	public void close() {
		try {
			channel.closeFuture().sync();
		} catch (Exception e) {
		}
		isConnect = false;
	}

	public void graceFully() {
		try {
			group.shutdownGracefully().sync();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void reconnect(Throwable e) {
		//延迟spacingTime秒后进行重连
		isConnect = false;
		connectServer();
	}

	public boolean send(String value) {
		if (channel == null) {
			return false;
		}

		if (!isConnect) {
			return false;
		}

		channel.writeAndFlush(value);
		return true;
	}
}
