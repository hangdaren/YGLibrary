package com.yougan.mylibrary;

import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanSettings;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class BootBroadcastReceiver extends BroadcastReceiver {
    public static final String ACTION = "android.intent.action.BOOT_COMPLETED";
    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals(ACTION)){
//            Intent myIntent = new Intent();
//            myIntent.setClass(context,MainActivity.class);
//            myIntent.setAction("android.intent.action.MAIN");
//            myIntent.addCategory("android.intent.category.LAUNCHER");
//            myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            context.startActivity(myIntent);
            Log.i("zxccc1", "BootBroadcastReceiver ");
        }
    }

}
