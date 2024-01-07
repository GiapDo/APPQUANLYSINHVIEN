package edu.hune.quanlysinhvien02.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import edu.hune.quanlysinhvien02.R;
import edu.hune.quanlysinhvien02.model.ItemStudentLopTC;

public class LopTinChiAdapter extends BaseAdapter {

    private Context mContext;
    private List<ItemStudentLopTC> mList;

    public LopTinChiAdapter(Context mContext, List<ItemStudentLopTC> mList) {
        this.mContext = mContext;
        this.mList = mList;
    }

    public void setData(List<ItemStudentLopTC> mList){
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

    private class StudentViewHolder{
        private CheckBox cbxLuaChon;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        StudentViewHolder holder = null;

        if(convertView == null){
            convertView = ((Activity)mContext).getLayoutInflater().inflate(R.layout.lop_tin_chi_layout, null);
            holder = new StudentViewHolder();

            holder.cbxLuaChon = convertView.findViewById(R.id.cbxLopTinChi);

            convertView.setTag(holder);

        }else {
            holder = (StudentViewHolder) convertView.getTag();
        }

        holder.cbxLuaChon.setText(mList.get(position).toString());

        if(mList.get(position).isTrangThai() == true){
            holder.cbxLuaChon.setChecked(true);
        }else {
            holder.cbxLuaChon.setChecked(false);
        }

        holder.cbxLuaChon.setOnCheckedChangeListener((buttonView, isChecked) -> {
            mList.get(position).setTrangThai(isChecked);
        });

        return convertView;
    }

    public List<ItemStudentLopTC> getSelectedItems() {
        List<ItemStudentLopTC> selectedItems = new ArrayList<>();

        for (int i = 0; i < getCount(); i++) {
            ItemStudentLopTC item = (ItemStudentLopTC) getItem(i);
            if (item.isTrangThai()) {
                selectedItems.add(mList.get(i));
            }
        }

        return selectedItems;
    }

}
