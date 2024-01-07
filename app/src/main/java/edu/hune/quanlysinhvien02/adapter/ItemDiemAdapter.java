package edu.hune.quanlysinhvien02.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import edu.hune.quanlysinhvien02.R;
import edu.hune.quanlysinhvien02.SuaDiemActivity;
import edu.hune.quanlysinhvien02.database.MyDatabaseHelper;
import edu.hune.quanlysinhvien02.model.ItemDiemFg;
import edu.hune.quanlysinhvien02.model.Util;

public class ItemDiemAdapter extends BaseAdapter {

    private Context mContext;
    private List<ItemDiemFg> mList;
    private MyDatabaseHelper mdb;

    public ItemDiemAdapter(Context mContext, List<ItemDiemFg> mList) {
        this.mContext = mContext;
        this.mList = mList;
        mdb = new MyDatabaseHelper(mContext);
    }

    public void CapNhat(List<ItemDiemFg> mList){
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

    private class DiemHolder{
        TextView tenHP, maHP, soTinChi, diem10, diemChu, diem4;
        TextView tvDiemCC, tvDiemDK, tvDiemThi, tvGhiChu;
        ImageButton imgThayDoi;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        DiemHolder holder = null;

        if(convertView == null){
            convertView = ((Activity)mContext).getLayoutInflater().inflate(R.layout.item_diem, null);

            holder = new DiemHolder();

            holder.tenHP = convertView.findViewById(R.id.tvItemDiemTenHP);
            holder.maHP = convertView.findViewById(R.id.tvItemDiemMaHP);
            holder.soTinChi = convertView.findViewById(R.id.tvItemDiemSoTinChi);
            holder.diem10 = convertView.findViewById(R.id.tvItemDiemHe10);
            holder.diemChu = convertView.findViewById(R.id.tvItemDiemHeChu);
            holder.diem4 = convertView.findViewById(R.id.tvItemDiemHe4);
            holder.tvDiemCC = convertView.findViewById(R.id.tvItemDiemCC);
            holder.tvDiemDK = convertView.findViewById(R.id.tvItemDiemDK);
            holder.tvDiemThi = convertView.findViewById(R.id.tvItemDiemThi);
            holder.tvGhiChu = convertView.findViewById(R.id.tvItemDiemGhiChu);
            holder.imgThayDoi = convertView.findViewById(R.id.imbtnItemDiemThayDoi);

            convertView.setTag(holder);

        }else {
            holder = (DiemHolder) convertView.getTag();
        }

        holder.tenHP.setText(mList.get(position).getTenHP());
        holder.maHP.setText(mList.get(position).getMaHp());
        holder.soTinChi.setText(String.valueOf(mList.get(position).getSoTinChi()));

        if(mList.get(position).getDiemCC() != -1 && mList.get(position).getDiemDK() != -1 && mList.get(position).getDiemThi() != -1) {
            holder.diem10.setText(Util.chuanDiem2STP(Util.getDiem10(mList.get(position).getDiemCC(), mList.get(position).getDiemDK(), mList.get(position).getDiemThi())));
            holder.diemChu.setText(Util.getDiemChu(Util.getDiem10(mList.get(position).getDiemCC(), mList.get(position).getDiemDK(), mList.get(position).getDiemThi())));
            holder.diem4.setText(Util.chuanDiem2STP(Util.getDiem4(Util.getDiem10(mList.get(position).getDiemCC(), mList.get(position).getDiemDK(), mList.get(position).getDiemThi()))));
        }else {
            holder.diem10.setText("");
            holder.diemChu.setText("");
            holder.diem4.setText("");
        }

        if(mList.get(position).getDiemCC() != -1) {
            holder.tvDiemCC.setText(String.valueOf(mList.get(position).getDiemCC()));
        }else {
            holder.tvDiemCC.setText("");
        }

        if(mList.get(position).getDiemDK() != -1) {
            holder.tvDiemDK.setText(String.valueOf(mList.get(position).getDiemDK()));
        }else {
            holder.tvDiemDK.setText("");
        }

        if(mList.get(position).getDiemThi() != -1) {
            holder.tvDiemThi.setText(String.valueOf(mList.get(position).getDiemThi()));
        }else {
            holder.tvDiemThi.setText("");
        }
        holder.tvGhiChu.setText(String.valueOf(mList.get(position).getGhiChu()));

        holder.imgThayDoi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, SuaDiemActivity.class);
                intent.putExtra("maSV", mList.get(position).getMaSV());
                intent.putExtra("maLopTC", mList.get(position).getMaLopTC());
                ((Activity)mContext).startActivityForResult(intent, 110);
            }
        });

        return convertView;
    }

}
