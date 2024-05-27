package com.yougan.mylibrary;

import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanSettings;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.List;

public class YgBleScanFunc {
    private static YgBleScanFunc instance;

    private BluetoothAdapter bluetoothAdapter;
    private PendingIntent callbackIntent;
    private List<ScanFilter> scanFilterList;
    private ScanSettings settings;

    // 私有构造函数
    private YgBleScanFunc() {
    }

    // 公共的静态方法，用于获取实例
    public static YgBleScanFunc getInstance() {
        if (instance == null) {
            // 同步代码块，确保线程安全
            synchronized (YgBleScanFunc.class) {
                // 再次检查实例是否为null，防止多实例创建
                if (instance == null) {
                    instance = new YgBleScanFunc();
                }
            }
        }
        return instance;
    }
    public String getNotify() {
        return notify;
    }

    public void setNotify(String notify) {
        this.notify = notify;
    }

    private String notify;

    public String getmTitle() {
        return mTitle;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public int getsIcon() {
        return sIcon;
    }

    public void setsIcon(int sIcon) {
        this.sIcon = sIcon;
    }

    public Class<?> getCls() {
        return cls;
    }

    public void setCls(Class<?> cls) {
        this.cls = cls;
    }

    private String mTitle;
    private int sIcon;
    private Class<?> cls;

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void init(Context context){
        //BluetoothManager是向蓝牙设备通讯的入口
        BluetoothManager bluetoothManager = (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
        bluetoothAdapter = bluetoothManager.getAdapter();
        scanFilterList = new ArrayList<>();
        //指定蓝牙的方式，这里设置的ScanSettings.SCAN_MODE_LOW_LATENCY是比较高频率的扫描方式
        ScanSettings.Builder settingBuilder = new ScanSettings.Builder();
        settingBuilder.setPhy(BluetoothDevice.PHY_LE_1M);
        settingBuilder.setReportDelay(0);
//        settingBuilder.setNumOfMatches(ScanSettings.MATCH_NUM_ONE_ADVERTISEMENT);
        settingBuilder.setScanMode(ScanSettings.SCAN_MODE_LOW_POWER);
        settingBuilder.setMatchMode(ScanSettings.MATCH_MODE_STICKY);
        settingBuilder.setCallbackType(ScanSettings.CALLBACK_TYPE_ALL_MATCHES);
        settingBuilder.setLegacy(false);
        settings = settingBuilder.build();
        //指定扫描到蓝牙后是以什么方式通知到app端，这里将以可见服务的形式进行启动
        callbackIntent = PendingIntent.getForegroundService(
                context,
                1,
                new Intent("com.hungrytree.receiver.BleService").setPackage(context.getPackageName()),
                PendingIntent.FLAG_MUTABLE);
    }

    public void setScanFilterList(ScanFilter scanFilter){
        scanFilterList.add(scanFilter);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void start(){
        bluetoothAdapter.getBluetoothLeScanner().stopScan(callbackIntent);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                bluetoothAdapter.getBluetoothLeScanner().startScan(scanFilterList, settings, callbackIntent);
            }
        },1000);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void stop(){
        bluetoothAdapter.getBluetoothLeScanner().stopScan(callbackIntent);
    }
}
