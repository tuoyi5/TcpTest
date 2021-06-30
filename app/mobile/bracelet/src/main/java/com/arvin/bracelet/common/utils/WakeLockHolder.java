package com.arvin.bracelet.common.utils;

import android.content.Context;
import android.os.PowerManager;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by sujinlin on 2021/6/29.
 */

public class WakeLockHolder {
	private volatile PowerManager.WakeLock wakeLock;
	private AtomicInteger wakeLockCounting = new AtomicInteger(0);
	private volatile boolean referenceCounted = true;

	public WakeLockHolder() {
	}

	public WakeLockHolder(boolean ref) {
		referenceCounted = ref;
	}

	public synchronized void acquireWakeLock(Context context, final String tag, int ms) {
		try {
			if (wakeLock == null) {
				PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
				wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, tag);
				wakeLock.setReferenceCounted(referenceCounted);
			}
			if (wakeLock != null) {
				if (ms > 0) {
					wakeLock.acquire(ms);
				} else {
					wakeLock.acquire();
				}
				if (referenceCounted) {
					wakeLockCounting.incrementAndGet();
				} else {
					wakeLockCounting.set(1);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public synchronized void releaseWakeLock() {
		try {
			if (wakeLock != null) {
				if (wakeLock.isHeld()) {
					wakeLock.release();
				}

				wakeLockCounting.decrementAndGet();
				if (wakeLockCounting.get() <= 0 || !referenceCounted) {
					wakeLockCounting.set(0);
					wakeLock = null;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
