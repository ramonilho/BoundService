package com.ramonilho.boundservice;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    BoundService mBoundService;
    boolean mServiceBound = false;

    @BindView(R.id.btPrintTimestamp)
    Button btPrintTimestamp;

    @BindView(R.id.btStopService)
    Button btStopService;

    @BindView(R.id.tvTimestamp)
    TextView tvTimestamp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

    }

    @OnClick(R.id.btPrintTimestamp)
    public void btPrintTimestamp(View view) {
        Log.i("MainActivity", "btPrintTimestamp");
        if (mServiceBound) {
            Log.i("MainActivity", "mBoundService.getTimestamp(): "+mBoundService.getTimestamp());
            tvTimestamp.setText(mBoundService.getTimestamp());
        }

    }

    @OnClick(R.id.btStopService)
    public void btStopService(View view) {
        if (mServiceBound) {
            unbindService(mServiceConnection);
            mServiceBound = false;
        }
        Intent intent = new Intent(MainActivity.this, BoundService.class);
        stopService(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = new Intent(this, BoundService.class);
        startService(intent);
        bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mServiceBound) {
            unbindService(mServiceConnection);
            mServiceBound = false;
        }
    }

    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            BoundService.MyBinder myBinder = (BoundService.MyBinder) service;
            mBoundService = myBinder.getService();
            mServiceBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mServiceBound = false;
        }
    };
}
