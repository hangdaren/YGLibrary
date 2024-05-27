package com.yougan.mylibrary;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanResult;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.List;

public class BleScanResService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Notification(YgBleScanFunc.getInstance().getNotify(), YgBleScanFunc.getInstance().getsIcon(),YgBleScanFunc.getInstance().getmTitle(),YgBleScanFunc.getInstance().getCls());
        YgBleScanFunc.getInstance().start();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent ==null ||intent.getAction() == null) {
            return START_STICKY;
        }
        //获取返回的错误码
        int errorCode = intent.getIntExtra(BluetoothLeScanner.EXTRA_ERROR_CODE, -1);//ScanSettings.SCAN_FAILED_*
        //获取到的蓝牙设备的回调类型
        if (errorCode == -1) {
            //扫描到蓝牙设备信息
            List<ScanResult> scanResults = (List<ScanResult>) intent.getSerializableExtra(BluetoothLeScanner.EXTRA_LIST_SCAN_RESULT);
            if (scanResults != null) {
                for (ScanResult result : scanResults) {
                    //打印所有设备的地址
                    String address = result.getDevice().getAddress();
                    Log.i("haha", "device address " + address);

                }
            }
        } else {
            //此处为扫描失败的错误处理

        }
        return START_STICKY;
    }

    public void Notification(String aMessage,int sIcon,String mTitle,Class<?> cls) {
        final int NOTIFY_ID = 1;
        String name = "IBC_SERVICE_CHANNEL";
        String id = "IBC_SERVICE_CHANNEL_1"; // The user-visible name of the channel.
        String description = "IBC_SERVICE_CHANNEL_SHOW"; // The user-visible description of the channel.

        Intent intent;
        PendingIntent pendingIntent;
        Notification.Builder  builder = null;
        NotificationManager notifManager = null;
        if (notifManager == null) {
            notifManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = notifManager.getNotificationChannel(id);
            if (mChannel == null) {
                mChannel = new NotificationChannel(id, name, importance);
                mChannel.setDescription(description);
                mChannel.enableVibration(false);
                mChannel.enableLights(false);
                //mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
                notifManager.createNotificationChannel(mChannel);
            }
            builder = new Notification.Builder(this, mChannel.getId());
            intent = new Intent(this, cls);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_MUTABLE);

            builder.setContentTitle(aMessage)  // required
                    .setSmallIcon(sIcon) // required
                    .setContentText(mTitle)  // required
                    .setDefaults(Notification.DEFAULT_LIGHTS)
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent)
                    .setChannelId(id)
                    .setTicker(aMessage);
            builder.build().sound = null;
            builder.build().vibrate = null;
        }
        Notification notification = builder.build();
        notification.sound = null;
        notification.vibrate = null;
        startForeground(NOTIFY_ID, notification);
    }
}
