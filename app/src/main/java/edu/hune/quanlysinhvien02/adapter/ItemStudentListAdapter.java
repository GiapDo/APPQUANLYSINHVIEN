package edu.hune.quanlysinhvien02.adapter;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.time.LocalDateTime;
import java.util.List;

import edu.hune.quanlysinhvien02.GuiEmailActivity;
import edu.hune.quanlysinhvien02.R;
import edu.hune.quanlysinhvien02.StudentInformationActivity;
import edu.hune.quanlysinhvien02.database.MyDatabaseHelper;
import edu.hune.quanlysinhvien02.model.ItemStudentList;
import edu.hune.quanlysinhvien02.model.Util;

public class ItemStudentListAdapter extends BaseAdapter {

    private static final int REQUEST_PHONE_CALL = 1;

    private Context context;
    private List<ItemStudentList> mlist;

    private MyDatabaseHelper mdb;

    public void setFilteredList(List<ItemStudentList> list){
        this.mlist = list;
        notifyDataSetChanged();
    }

    public ItemStudentListAdapter(Context context, List<ItemStudentList> mlist) {
        this.context = context;
        this.mlist = mlist;
        this.mdb = new MyDatabaseHelper(context);
    }

    @Override
    public int getCount() {
        return mlist.size();
    }

    @Override
    public Object getItem(int position) {
        return mlist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private class ItemStudentHolder{
        TextView maSV, tenSV, gpa, lop, khoa, email, std;

        ImageButton ibXoa, ibSua, ibPhone, ibEmail;


    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ItemStudentHolder holder = null;
        if(convertView == null){

            convertView = ((Activity)(context)).getLayoutInflater().inflate(R.layout.item_list_student, null);

            holder = new ItemStudentHolder();
            holder.maSV = convertView.findViewById(R.id.tvItemListMSV);
            holder.tenSV = convertView.findViewById(R.id.tvItemListTenSV);
            holder.gpa = convertView.findViewById(R.id.tvItemListGPA);
            holder.lop = convertView.findViewById(R.id.tvItemListLop);
            holder.khoa = convertView.findViewById(R.id.tvItemListKhoa);
            holder.email = convertView.findViewById(R.id.tvItemListEmail);
            holder.std = convertView.findViewById(R.id.tvItemListPhone);
            holder.ibXoa = convertView.findViewById(R.id.imbtnListRemove);
            holder.ibSua = convertView.findViewById(R.id.imbtnListInfor);
            holder.ibPhone = convertView.findViewById(R.id.imbtnListPhone);
            holder.ibEmail = convertView.findViewById(R.id.imbtnListEmail);
            convertView.setTag(holder);

        }else {
            holder = (ItemStudentHolder) convertView.getTag();
        }

        holder.maSV.setText(mlist.get(position).getMaSV());
        holder.tenSV.setText(mlist.get(position).getTenSV());
        holder.gpa.setText(mlist.get(position).getDiem());
        holder.lop.setText(mlist.get(position).getLop());
        holder.khoa.setText(mlist.get(position).getKhoa());
        holder.email.setText(mlist.get(position).getEmail());
        holder.std.setText(mlist.get(position).getSdt());

        holder.ibXoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                xoaSV(mlist.get(position).getMaSV(), mlist.get(position).getTenSV(), position);
            }
        });

        holder.ibSua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, StudentInformationActivity.class);
                intent.putExtra("maSV", mlist.get(position).getMaSV());
                ((Activity)context).startActivityForResult(intent, 100);
            }
        });

        holder.ibPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("tel:" + mlist.get(position).getSdt());

                if(context.checkSelfPermission(Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED){
                    Intent intent = new Intent(Intent.ACTION_CALL, uri);
                    context.startActivity(intent);
                }else {
                    ((Activity)context).requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, REQUEST_PHONE_CALL);
                }
            }
        });

        holder.ibEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, GuiEmailActivity.class);
                intent.putExtra("emailSV", mlist.get(position).getEmail());
                context.startActivity(intent);
            }
        });

        return convertView;
    }

    private void xoaSV(String maSV, String tenSV, int position) {

        SQLiteDatabase db = mdb.getWritableDatabase();
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Thông báo!");
        builder.setMessage("Bạn chắc chắn muốn xóa sinh viên: " + "\n\tTên: " + tenSV + "\n\tMã sinh viên: " + maSV);
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                db.beginTransaction();

                try {
                    db.delete(MyDatabaseHelper.getBangDiem(), MyDatabaseHelper.getMaSV() + " = ?", new String[]{maSV});

                    db.delete(MyDatabaseHelper.getBangSV(), MyDatabaseHelper.getMaSV() + " = ?", new String[]{maSV});

                    db.setTransactionSuccessful();

                    Toast.makeText(context, "Deletion successful!", Toast.LENGTH_SHORT).show();

                    mlist.remove(position);
                    notifyDataSetChanged();

                    ContentValues values = new ContentValues();
                    values.put(MyDatabaseHelper.getHistoryName(), "Xóa một sinh viên thành công");
                    values.put(MyDatabaseHelper.getHistoryTime(), Util.toStringDateTime(LocalDateTime.now()));
                    values.put(MyDatabaseHelper.getHistoryType(), 6);

                    db.insert(MyDatabaseHelper.getHistoryTable(), null, values);
                }catch (SQLException e){
                    Toast.makeText(context, "Deletion failed!", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }finally {
                    db.endTransaction();
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
