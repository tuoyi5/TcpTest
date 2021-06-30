package com.arvin.bracelet.common.socket.connect.client;


import android.util.Log;

import com.arvin.bracelet.common.socket.data.ConnectionModel;

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

	private RxClientHandler mRxClientHandler;

	public void setConnectionModel(ConnectionModel connectionModel) {
		this.connectionModel = connectionModel;
	}

	public void setConnectServer(RxClientHandler rxClientHandler) {
		mRxClientHandler = rxClientHandler;
	}

	public void connectServer() {
		if (channel != null && channel.isActive()) {
			android.util.Log.i("arvinn", "connectServer isActive: " + channel.isActive());
			return;
		}
		android.util.Log.d("arvinn", "connectServer connectServer: "
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
					ch.pipeline().addLast(mRxClientHandler);
					ch.pipeline().addLast(new ByteArrayEncoder());
					ch.pipeline().addLast(new ChunkedWriteHandler());
				}
			});

		Log.i("arvinn","RxClientService start： " + 22222222);
		try {
			channel = bootstrap.connect().sync().channel();
			showState();
		} catch (Exception e) {
			Log.e("arvinn--", e.getMessage());
		} finally {
			//closeFuture();
		}
	}

	public void showState() {
		if (channel == null) {
			return;
		}
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append(" isActive:").append(channel.isActive())
			.append("; isOpen:").append(channel.isOpen())
			.append("; isRegistered:").append(channel.isRegistered())
			.append("; isWritable:").append(channel.isWritable());
		android.util.Log.d("arvinn", "connectServer: " + stringBuffer.toString());
	}

	public boolean isConnect() {
		showState();
		return channel == null ? false : channel.isActive();
	}

	public void closeFuture() {
		showState();
		try {
			channel.closeFuture().sync();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	//退出连接
	public void shutdownFully() {
		showState();
		try {
			group.shutdownGracefully().sync();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
		}
	}

	//重新连接
	public void reConnect() {
		showState();
		if (channel != null || !channel.isActive()) {
			return;
		}
		connectServer();
	}

	//发送消息
	public boolean sendMessage(String value) {
		showState();
		if (channel == null) {
			return false;
		}

		if (!channel.isActive()) {
			return false;
		}

		channel.writeAndFlush(value);
		return true;
	}
}
