package com.arvin.bracelet.ui;

import android.os.Bundle;


import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;

import com.arvin.bracelet.R;
import com.arvin.bracelet.service.manager.TcpServiceManager;

public class MainActivity extends AppCompatActivity {

	TcpServiceManager mTcpServiceManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		Button button = findViewById(R.id.test_button);
		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				sendMessage("发送测试消息");
			}
		});

		initTcpServiceManager();
		mTcpServiceManager.bindService(this);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		mTcpServiceManager.unbindService(this);
	}

	public void initTcpServiceManager(){
		mTcpServiceManager = new TcpServiceManager(this);
	};

	public void sendMessage(String value) {
		mTcpServiceManager.sendMessage(value);
	}
}