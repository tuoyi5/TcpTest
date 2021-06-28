package com.arvin.bracelet;

import android.os.Bundle;


import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;

import com.arvin.bracelet.common.action.NetworkClientAction;
import com.arvin.bracelet.common.action.NetworkServerAction;
import com.arvin.bracelet.common.model.DataBundle;

public class MainActivity extends AppCompatActivity {

	DataBundle mDataBundle;
	NetworkClientAction mNetworkClientAction;
	NetworkServerAction mNetworkServerAction;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		Button button = findViewById(R.id.test_button);
		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				mNetworkClientAction.execute();
			}
		});

		registerModel();
		init();
	}

	public void init() {
		if (mNetworkServerAction == null) {
			mNetworkServerAction = new NetworkServerAction(mDataBundle.getAppContext(), mDataBundle.getCloudDataManager());
			mNetworkServerAction.execute();
		}

		if (mNetworkClientAction == null) {
			mNetworkClientAction = new NetworkClientAction(mDataBundle.getAppContext(), mDataBundle.getCloudDataManager());
		}
	}


	public void registerModel() {
		mDataBundle = new DataBundle(this);
	}

}