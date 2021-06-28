package com.arvin.bracelet.common.socket.utils;

import android.text.TextUtils;
import android.util.ArrayMap;

import java.util.Iterator;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.LineEncoder;
import io.netty.handler.codec.string.LineSeparator;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.CharsetUtil;

public class ChannelPipelineHelper {

	private static ArrayMap<String, ChannelHandler> arrayMap = new ArrayMap<>();

	public static void setChannelPipeline(ChannelPipeline pipeline) {
		init();
		Iterator iterator = arrayMap.keySet().iterator();
		while (iterator.hasNext()) {
			String key = (String) iterator.next();
			if (!TextUtils.isEmpty(key)) {
				pipeline.addLast(key, arrayMap.get(key));
			}
		}
	}

	private static void init() {
		if (arrayMap != null && arrayMap.size() > 0) {
			return;
		}
		// Decoders
		arrayMap.put("frameDecoder", new LineBasedFrameDecoder(1024 * 1024 * 1024));
		arrayMap.put("stringDecoder", new StringDecoder(CharsetUtil.UTF_8));

		// Encoder
		arrayMap.put("stringEncoder", new StringEncoder(CharsetUtil.UTF_8));
		arrayMap.put("lineEncoder", new LineEncoder(LineSeparator.UNIX, CharsetUtil.UTF_8));
	}
}
