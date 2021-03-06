package com.levent_j.nssz.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.levent_j.nssz.entry.Device;
import com.levent_j.nssz.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by levent_j on 16-5-6.
 */
public class DeviceAdapter extends RecyclerView.Adapter<DeviceAdapter.mViewHolder>{
    private Context context;
    private List<Device> deviceList;
    private final LayoutInflater layoutInflater;


    public DeviceAdapter(Context context){
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
        deviceList = new ArrayList<>();
    }

    @Override
    public mViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.item_device,parent,false);
        return new mViewHolder(view);
    }

    @Override
    public void onBindViewHolder(mViewHolder holder, int position) {
        Device device = deviceList.get(position);
        holder.dName.setText("标签卡编号："+device.getDeviceNumber()+"号");
        holder.dTemp.setText("当前温度:"+device.getTemperature()+"."+device.getTemperatureDecimal()+"℃");
        holder.dHum.setText("当前湿度:"+device.getHumidity()+"%");
    }

    @Override
    public int getItemCount() {
        return deviceList.size();
    }

    public void updateDeviceList(List<Device> list){
        deviceList.clear();
        deviceList.addAll(list);
        notifyDataSetChanged();
    }

    class mViewHolder extends RecyclerView.ViewHolder{
        @Bind(R.id.tv_device_name)
        TextView dName;
        @Bind(R.id.tv_device_t)
        TextView dTemp;
        @Bind(R.id.tv_device_w)
        TextView dHum;

        public mViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
