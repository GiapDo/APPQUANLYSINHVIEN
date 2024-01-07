package edu.hune.quanlysinhvien02.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import edu.hune.quanlysinhvien02.ModifyStudentActivity;
import edu.hune.quanlysinhvien02.R;
import edu.hune.quanlysinhvien02.StudentInformationActivity;
import edu.hune.quanlysinhvien02.database.MyDatabaseHelper;
import edu.hune.quanlysinhvien02.model.ItemStudentDeModi;

public class ItemStudenModifyAdapter extends BaseAdapter {

    private Context context;
    private List<ItemStudentDeModi> modiList;

    public ItemStudenModifyAdapter(Context context, List<ItemStudentDeModi> modiList) {
        this.context = context;
        this.modiList = modiList;
    }

    public void setData(List<ItemStudentDeModi> modiList){
        this.modiList = modiList;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return modiList.size();
    }

    @Override
    public Object getItem(int position) {
        return modiList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private class StudentHolder{
        TextView tvMaSV, tvTenSV;
        ImageButton btnSua;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        StudentHolder holder = null;

        if(convertView == null){
            convertView = ((Activity)context).getLayoutInflater().inflate(R.layout.item_delete_modify_student, null);

            holder = new StudentHolder();
            holder.tvMaSV = convertView.findViewById(R.id.tvItemDeleteModifyMSV);
            holder.tvTenSV = convertView.findViewById(R.id.tvItemDeleteModifyTenSV);
            holder.btnSua = convertView.findViewById(R.id.imbtnDeModify);

            convertView.setTag(holder);
        }else {
            holder = (StudentHolder) convertView.getTag();
        }

        holder.tvMaSV.setText(modiList.get(position).getMaSV());
        holder.tvTenSV.setText(modiList.get(position).getTenSV());
        holder.btnSua.setImageResource(R.drawable.edit_modify);

        holder.btnSua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Truyền mã sinh viên để cho vào viewpager

                Intent intent = new Intent(v.getContext(), StudentInformationActivity.class);
                intent.putExtra("maSV", modiList.get(position).getMaSV());
                ((Activity)context).startActivityForResult(intent, 110);
            }
        });

        return convertView;
    }

}
