package edu.hune.quanlysinhvien02.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import edu.hune.quanlysinhvien02.R;
import edu.hune.quanlysinhvien02.model.History;
import edu.hune.quanlysinhvien02.model.Util;

public class HistoryAdapter extends BaseAdapter {
    private Context mContext;
    private List<History> mList;

    public HistoryAdapter(Context mContext, List<History> mList) {
        this.mContext = mContext;
        this.mList = mList;
    }

    public void setData(List<History> mList){
        this.mList = mList;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private class HistoryHolder{
        TextView date, content;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        HistoryHolder holder = null;
        if(convertView == null){
            convertView = ((Activity)mContext).getLayoutInflater().inflate(R.layout.item_history, null);

            holder = new HistoryHolder();
            holder.date = convertView.findViewById(R.id.item_history_time);
            holder.content = convertView.findViewById(R.id.item_history_content);

            convertView.setTag(holder);
        }else {
            holder = (HistoryHolder) convertView.getTag();
        }

        holder.date.setText(Util.toStringDateTime(mList.get(position).getDate()));
        holder.content.setText(mList.get(position).getContent());

        return convertView;
    }
}
