package com.calypso.buetools.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.calypso.buetools.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by liting on 2019/4/18.
 */

public class ZhuanJieCJBAdapter extends RecyclerView.Adapter<ZhuanJieCJBAdapter.ZhuanJieBanViewHolder> {
    private List<Map<String,String>> datalist;
    public ZhuanJieCJBAdapter(){
        datalist = new ArrayList<>();
    }
    public void setDate(List<Map<String,String>> list){
        datalist = list;
        notifyDataSetChanged();
    }

    @Override
    public ZhuanJieBanViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.zhuanjie_recycle_view_item,parent,false);
        ZhuanJieBanViewHolder viewHolder = new ZhuanJieBanViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ZhuanJieBanViewHolder holder, final int position) {
        Map map = datalist.get(position);
        holder.show_deviceName.setText((position+1)+": ");
        holder.input_deviceId.setText(map.get("device_id").toString());
        holder.delete_Img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datalist.remove(position);
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return datalist.size();
    }
    class ZhuanJieBanViewHolder extends RecyclerView.ViewHolder{
        TextView show_deviceName;
        TextView input_deviceId;
        ImageView delete_Img;
        public ZhuanJieBanViewHolder(View itemView) {
            super(itemView);
            show_deviceName = itemView.findViewById(R.id.xsdevice_id_textView);
            input_deviceId = itemView.findViewById(R.id.input_xsDevice_id);
            delete_Img = itemView.findViewById(R.id.delete_imageView);
        }
    }
    public List<Map<String,String>> getDatalist(){
        return datalist;
    }
}
