package edu.hune.quanlysinhvien02.fragment;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

import edu.hune.quanlysinhvien02.R;
import edu.hune.quanlysinhvien02.adapter.ItemDiemAdapter;
import edu.hune.quanlysinhvien02.database.MyDatabaseHelper;
import edu.hune.quanlysinhvien02.model.ItemDiemFg;
import edu.hune.quanlysinhvien02.model.Util;


public class StudentTranscriptFragment extends Fragment {

    private View mView;

    private TextView tvTenSV, tvMaSV, tvDiemHe10, tvDiemHe4,
            tvXlHocLuc, tvXlThang4, tvXlThang10, tvThiLai, tvSoTinChi;
    private ListView lvDSDiem;

    private String maSV;
    
    private MyDatabaseHelper mdb;
    private List<ItemDiemFg> mList;
    private ItemDiemAdapter diemAdapter;


    public StudentTranscriptFragment() {

    }


    public static Fragment newInstance(String maSV) {
        StudentTranscriptFragment fragment = new StudentTranscriptFragment();
        Bundle args = new Bundle();
        args.putString("maSV", maSV);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        maSV = bundle.getString("maSV");

        mView = inflater.inflate(R.layout.fragment_student_transcript, container, false);

        tvTenSV = mView.findViewById(R.id.tvFgDiemTenSV);
        tvMaSV = mView.findViewById(R.id.tvFgDiemMaSV);
        tvDiemHe10 = mView.findViewById(R.id.tvFgDiemDTB);
        tvDiemHe4 = mView.findViewById(R.id.tvFgDiemDHe4);
        tvXlHocLuc = mView.findViewById(R.id.tvFgDiemXlHocLuc);
        tvXlThang4 = mView.findViewById(R.id.tvFgDiemXlThang4);
        tvXlThang10 = mView.findViewById(R.id.tvFgDiemXlThang10);
        tvThiLai = mView.findViewById(R.id.tvFgDiemThiLai);
        tvSoTinChi = mView.findViewById(R.id.tvFgDiemSoTinChi);
        lvDSDiem = mView.findViewById(R.id.lvFgDiemDsDiem);
        
        mdb = new MyDatabaseHelper(mView.getContext());
        doDuLieuVaoList();
        diemAdapter = new ItemDiemAdapter(getContext(), mList);
        lvDSDiem.setAdapter(diemAdapter);

        tinhDiem();
        
        return mView;
    }

    private void doDuLieuVaoList() {
        mList = new ArrayList<>();
        Cursor c = mdb.readDiem1(maSV);

        if(c.getCount() != 0){
            while (c.moveToNext()){
                ItemDiemFg tmp = new ItemDiemFg(String.valueOf(c.getInt(8)),c.getString(1), c.getString(0),
                        c.getString(3), c.getString(7), c.getInt(2),
                        !c.isNull(4) ? c.getDouble(4) : -1,
                        !c.isNull(5) ? c.getDouble(5) : -1,
                        !c.isNull(6) ? c.getDouble(6) : -1);
                mList.add(tmp);
            }
        }
        c.close();
    }

    private void tinhDiem(){
        double diem10 = 0, diem4 = 0;
        int soTinChi = 0;
        int thiLai = 0;
        for(ItemDiemFg tmp : mList){
            if(tmp.getDiemCC() != -1 && tmp.getDiemDK() != -1 && tmp.getDiemThi() != -1){
                diem10 += (tmp.getDiemCC() * 0.1 + tmp.getDiemDK() * 0.3 + tmp.getDiemThi() * 0.6) * tmp.getSoTinChi();
                double d4 = Util.getDiem4(tmp.getDiemCC() * 0.1 + tmp.getDiemDK() * 0.3 + tmp.getDiemThi() * 0.6);

                if(d4 == 0){
                    thiLai += 1;
                }

                diem4 += d4 * tmp.getSoTinChi();
                soTinChi += tmp.getSoTinChi();
            }
        }

        if(soTinChi != 0) {
            double diemTB = diem10 / soTinChi;
            double diemHe4 = diem4 / soTinChi;

            String thang10 = "";
            if (diemTB >= 8.5) {
                thang10 = "Giỏi";
            } else if (diemTB >= 7) {
                thang10 = "Khá";
            } else if (diemTB >= 5.5) {
                thang10 = "Trung bình";
            } else {
                thang10 = "Trung bình yếu";
            }

            String thang4 = "";

            if (diemHe4 >= 3.6) {
                thang4 = "Xuất sắc";
            } else if (diemHe4 >= 3.2) {
                thang4 = "Giỏi";
            } else if (diemHe4 >= 2.5) {
                thang4 = "Khá";
            } else if (diemHe4 >= 2) {
                thang4 = "Trung bình";
            } else {
                thang4 = "Yếu";
            }

            String hocLuc = "";
            if (diemHe4 >= 2) {
                hocLuc = "Bình thường";
            } else {
                hocLuc = "Yếu";
            }
            tvDiemHe10.setText(Util.chuanDiem2STP(diemTB));
            tvDiemHe4.setText(Util.chuanDiem2STP(diemHe4));
            tvXlHocLuc.setText(hocLuc);
            tvXlThang10.setText(thang10);
            tvXlThang4.setText(thang4);
        }else {
            tvDiemHe10.setText("");
            tvDiemHe4.setText("");
            tvXlHocLuc.setText("");
            tvXlThang10.setText("");
            tvXlThang4.setText("");
        }

        Cursor c = mdb.readStudent(maSV);
        if(c.getCount() != 0){
            while (c.moveToNext()) {
                tvTenSV.setText(c.getString(2));
            }
        }
        c.close();

        tvMaSV.setText(maSV);
        tvThiLai.setText(String.valueOf(thiLai));
        tvSoTinChi.setText(String.valueOf(soTinChi));

    }

    public void resetData(){
        doDuLieuVaoList();
        diemAdapter.CapNhat(mList);
    }

}