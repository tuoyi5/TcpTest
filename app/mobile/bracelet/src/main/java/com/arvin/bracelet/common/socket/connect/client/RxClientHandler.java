package com.arvin.bracelet.common.socket.connect.client;

import android.util.Log;

import com.arvin.bracelet.common.rx.RxCallback;

import java.nio.charset.Charset;

import androidx.annotation.NonNull;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;


public class RxClientHandler extends SimpleChannelInboundHandler<ByteBuf> {


	RxCallback<String> mRxCallback;

	public RxClientHandler(RxCallback<String> rxCallback) {
		mRxCallback = rxCallback;
	}
	/**
	 * 向服务端发送数据
	 */
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		Log.w("arvinn", "客户端与服务端通道-开启：" + ctx.channel().localAddress() + "channelActive");

		String sendInfo = "Hello 这里是客户端  你好啊！";
		Log.w("arvinn", "客户端准备发送的数据包：" + sendInfo);
		ctx.writeAndFlush(Unpooled.copiedBuffer(sendInfo, CharsetUtil.UTF_8)); // 必须有flush
	}

	/**
	 * channelInactive
	 * <p>
	 * channel 通道 Inactive 不活跃的
	 * <p>
	 * 当客户端主动断开服务端的链接后，这个通道就是不活跃的。也就是说客户端与服务端的关闭了通信通道并且不可以传输数据
	 */
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		Log.w("arvinn", "客户端与服务端通道-关闭：" + ctx.channel().localAddress() + "channelInactive");
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
		Log.w("arvinn", "读取客户端通道信息..");
		ByteBuf byteBuf = msg.readBytes(msg.readableBytes());
		Log.w("arvinn",
			"客户端接收到的服务端信息:" + ByteBufUtil.hexDump(byteBuf) + "; 数据包为:" + byteBuf.toString(Charset.forName("utf-8")));
		onNext(byteBuf.toString(Charset.forName("utf-8")));
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		ctx.close().sync();
		Log.w("arvinn", "异常退出:" + cause.getMessage());
	}

	public void onNext(String object) {
		if (mRxCallback == null) {
			return;
		}
		mRxCallback.onNext(object);
	}
}
