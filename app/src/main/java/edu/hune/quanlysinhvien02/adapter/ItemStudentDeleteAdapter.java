package edu.hune.quanlysinhvien02.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.time.LocalDateTime;
import java.util.List;

import edu.hune.quanlysinhvien02.R;
import edu.hune.quanlysinhvien02.database.MyDatabaseHelper;
import edu.hune.quanlysinhvien02.model.ItemStudentDeModi;
import edu.hune.quanlysinhvien02.model.Util;

public class ItemStudentDeleteAdapter extends BaseAdapter {

    private Context context;
    private List<ItemStudentDeModi> modiList;

    private MyDatabaseHelper db;

    public ItemStudentDeleteAdapter(Context context, List<ItemStudentDeModi> modiList) {
        this.context = context;
        this.modiList = modiList;
        db = new MyDatabaseHelper(context);
    }

    public void setData(List<ItemStudentDeModi> list){
        modiList = list;
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
        ImageButton btnXoa;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        StudentHolder holder = null;

        if(convertView == null){
            convertView = ((Activity)context).getLayoutInflater().inflate(R.layout.item_delete_modify_student, null);

            holder = new StudentHolder();
            holder.tvMaSV = convertView.findViewById(R.id.tvItemDeleteModifyMSV);
            holder.tvTenSV = convertView.findViewById(R.id.tvItemDeleteModifyTenSV);
            holder.btnXoa = convertView.findViewById(R.id.imbtnDeModify);

            convertView.setTag(holder);
        }else {
            holder = (StudentHolder) convertView.getTag();
        }

        holder.tvMaSV.setText(modiList.get(position).getMaSV());
        holder.tvTenSV.setText(modiList.get(position).getTenSV());
        holder.btnXoa.setImageResource(R.drawable.baseline_person_remove);

        holder.btnXoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                xoaSV(position);
            }
        });

        convertView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                xoaSV(position);
                return true;
            }
        });

        return convertView;
    }

    private void xoaSV(int idx) {
        SQLiteDatabase sd = db.getWritableDatabase();
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Thông báo!");
        builder.setMessage("Bạn muốn xóa sinh viên: \n\tMã sinh viên" + modiList.get(idx).getMaSV() + "\n\tTên sinh viên: " + modiList.get(idx).getTenSV());

        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                sd.beginTransaction();

                try {
                    sd.delete(MyDatabaseHelper.getBangDiem(), MyDatabaseHelper.getMaSV() + " = ?", new String[]{modiList.get(idx).getMaSV()});

                    sd.delete(MyDatabaseHelper.getBangSV(), MyDatabaseHelper.getMaSV() + " = ?", new String[]{modiList.get(idx).getMaSV()});

                    sd.setTransactionSuccessful();
                    Toast.makeText(context, "Deletion successful!", Toast.LENGTH_SHORT).show();
                    modiList.remove(idx);
                    notifyDataSetChanged();

                    ContentValues values = new ContentValues();
                    values.put(MyDatabaseHelper.getHistoryName(), "Xóa thành công một sinh viên");
                    values.put(MyDatabaseHelper.getHistoryTime(), Util.toStringDateTime(LocalDateTime.now()));
                    values.put(MyDatabaseHelper.getHistoryType(), 6);

                    sd.insert(MyDatabaseHelper.getHistoryTable(), null, values);
                }catch (SQLException e){
                    Toast.makeText(context, "Deletion failed!", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }finally {
                    sd.endTransaction();
                }
            }
        });

        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.create().show();
    }
}
