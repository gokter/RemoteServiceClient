package com.flyingh.remoteserviceclient;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.flyingh.aidl.StudentQuery;

public class MainActivity extends Activity {
	private EditText idText;
	private TextView resultView;
	private StudentQuery binder;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		idText = (EditText) findViewById(R.id.id);
		resultView = (TextView) findViewById(R.id.result);
		bindService(new Intent("com.flyingh.student.query"), new ServiceConnection() {

			@Override
			public void onServiceDisconnected(ComponentName name) {
				binder = null;
			}

			@Override
			public void onServiceConnected(ComponentName name, IBinder service) {
				binder = StudentQuery.Stub.asInterface(service);
			}
		}, BIND_AUTO_CREATE);

	}

	public void query(View view) {
		try {
			String name = binder.query(Integer.valueOf(idText.getText().toString()));
			if (name != null && !"".equals(name.trim())) {
				resultView.setText(name);
			} else {
				resultView.setText("no result");
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
