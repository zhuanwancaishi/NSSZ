package com.levent_j.nssz.activity;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.levent_j.nssz.R;
import com.levent_j.nssz.base.BaseActivity;

import java.io.IOException;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by levent_j on 16-5-5.
 */
public class PairActivity extends BaseActivity{
    @Bind(R.id.lv_new_list)
    ListView mNewListView;
    @Bind(R.id.lv_paired_list)
    ListView mPairedListView;

    private BluetoothSocket mSocket;

    /**获取本机的bluetoothAdapter*/
    private BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

    /**搜索设备相关adapter*/
    private BluetoothAdapter mSearchBtAdapter;
    private ArrayAdapter<String> mPairedAdapter;
    private ArrayAdapter<String> mNewAdapter;

    private String mDeviceMacAddress;

    private static final int REQUEST_FINE_LOCATION=0;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_pair;
    }

    @Override
    protected void init() {
        if (bluetoothAdapter ==null){
            Toa("该手机不支持蓝牙");
        }

        //获取权限
        mayRequestLocation();

        /**开启蓝牙*/
        new Thread(){
            @Override
            public void run() {
                super.run();
                if (!bluetoothAdapter.isEnabled()){
                    bluetoothAdapter.enable();
                }
            }
        }.start();

        /**初始化adapter*/
        mPairedAdapter = new ArrayAdapter<>(this,R.layout.device_name);
        mNewAdapter = new ArrayAdapter<>(this,R.layout.device_name);

        /**填充数据*/
        mPairedListView.setAdapter(mPairedAdapter);
        mNewListView.setAdapter(mNewAdapter);

        /**设置共同的监听器*/
        mPairedListView.setOnItemClickListener(mDeviceClickListener);
        mNewListView.setOnItemClickListener(mDeviceClickListener);

        /**注册广播接收器*/
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        this.registerReceiver(mReceiver,filter);
        filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        this.registerReceiver(mReceiver,filter);

        mSearchBtAdapter = BluetoothAdapter.getDefaultAdapter();
    }

    private void mayRequestLocation() {
        if (Build.VERSION.SDK_INT >= 23) {
            int checkCallPhonePermission = ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION);
            if(checkCallPhonePermission != PackageManager.PERMISSION_GRANTED){
                //判断是否需要 向用户解释，为什么要申请该权限
                if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_COARSE_LOCATION))
                    Toast.makeText(this,"需要权限", Toast.LENGTH_SHORT).show();

                ActivityCompat.requestPermissions(this ,new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},REQUEST_FINE_LOCATION);
                return;
            }else{

            }
        } else {

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private AdapterView.OnItemClickListener mDeviceClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            /**准备链接设备，关闭查找*/
            mSearchBtAdapter.cancelDiscovery();
            /**获取设备信息*/
            String info = ((TextView)view).getText().toString();
            String address = info.substring(info.length()-17);
            mDeviceMacAddress = address;
            /**将mac地址传给MainActivity*/
            MainActivity.mDeviceMacAddress = mDeviceMacAddress;
            startActivity(new Intent(PairActivity.this,MainActivity.class));
        }
    };


    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)){
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if (device.getBondState() != BluetoothDevice.BOND_BONDED){
                    mNewAdapter.add(device.getName()+"\n"+device.getAddress());
                }else {
                    mPairedAdapter.add("设备名："+device.getName()+"\n"+"MAC地址："+device.getAddress());
                }
            }else  if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)){
                if (mNewAdapter.getCount() == 0){
                    String noDevices = "未找到新设备";
                    mNewAdapter.add(noDevices);
                }
            }
        }
    };

    @OnClick(R.id.fab_discovery)
    public void onDiscovery(View view){
        //搜索
        if (!bluetoothAdapter.isEnabled()){
            Snackbar.make(view,"打开蓝牙中",Snackbar.LENGTH_SHORT).show();
        }else {
            if (mSocket==null){
                /**进行搜索*/
                Snackbar.make(view,"正在搜索设备",Snackbar.LENGTH_SHORT).show();
                if (mSearchBtAdapter.isDiscovering()){
                    mSearchBtAdapter.cancelDiscovery();
                }
                mSearchBtAdapter.startDiscovery();
//                SearchDevices();
            }else {
//                //关闭Socket
                try {
//                    inputStream.close();
                    mSocket.close();
                    mSocket = null;
//                    bRun = false;
                } catch (IOException e) {
//                    e.printStackTrace();
                }

            }
        }
    }

    private void SearchDevices() {
        if (mSearchBtAdapter.isDiscovering()){
            mSearchBtAdapter.cancelDiscovery();
        }
        mSearchBtAdapter.startDiscovery();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReceiver);
    }
}
