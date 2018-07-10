//package com.small.saasuser.adapter;
//
//import android.content.Context;
// 
//import android.support.v7.widget.RecyclerView;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.*;
//
//import java.util.List;
//import java.util.Map;
//
//import com.small.saasuser.activity.R;
//
///**
// * Created by Administrator on 2016/9/26.
// */
//public class MessageCenterAdapter extends RecyclerView.Adapter<MessageCenterAdapter.ItemViewHolder> {
//    private List<Map<String, Object>> data;
//    private LayoutInflater mInflater;
//
//    public MessageCenterAdapter(Context context, List<Map<String, Object>> data) {
//        this.data = data;
//        mInflater = LayoutInflater.from(context);
//    }
//
//    @Override
//    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        ItemViewHolder holder = new ItemViewHolder(mInflater.inflate(R.layout.msg_item, parent, false));
//        return holder;
//    }
//
//    @Override
//    public void onBindViewHolder(ItemViewHolder holder, int position) {
//        Map<String, Object> map = data.get(position);
//        String content = map.get("content").toString();
//        String type = map.get("type").toString();
//        String date = map.get("date").toString();
//        if (type.equals("积分动态")) {
//
//            holder.tv_msg_title.setText("积分动态");
//            holder.iv_msg.setImageResource(R.drawable.taxi);
//        } else if (type.equals("促销提醒")) {
//            holder.tv_msg_title.setText("促销提醒");
//            holder.iv_msg.setImageResource(R.drawable.taxi);
//        } else if (type.equals("发货通知")) {
//            holder.tv_msg_title.setText("发货通知");
//            holder.iv_msg.setImageResource(R.drawable.taxi);
//        } else if (type.equals("退款通知")) {
//            holder.tv_msg_title.setText("退款通知");
//            holder.iv_msg.setImageResource(R.drawable.taxi);
//        } else if (type.equals("团购预告")) {
//            holder.tv_msg_title.setText("团购预告");
//            holder.iv_msg.setImageResource(R.drawable.taxi);
//        } else if (type.equals("生日礼品信息")) {
//            holder.tv_msg_title.setText("生日礼品信息");
//            holder.iv_msg.setImageResource(R.drawable.taxi);
//        }
//
//        holder.tv_msg_content.setText(content);
//        holder.tv_msg_date.setText(date);
//
//    }
//
//    @Override
//    public int getItemCount() {
//        Log.i("xiaopang",data.size()+"");
//        return data.size();
//
//    }
//    class ItemViewHolder extends RecyclerView.ViewHolder {
//
//        public TextView tv_msg_title, tv_msg_content, tv_msg_date;
//        public ImageView iv_msg;
//
//        public ItemViewHolder(View itemView) {
//            super(itemView);
//            tv_msg_title = (TextView) itemView.findViewById(R.id.tv_msg_title);
//            tv_msg_content = (TextView) itemView.findViewById(R.id.tv_msg_content);
//            tv_msg_date = (TextView) itemView.findViewById(R.id.tv_msg_date);
//            iv_msg = (ImageView) itemView.findViewById(R.id.iv_msg);
//        }
//    }
//
//
//
//
//
//}
