package edu.hune.quanlysinhvien02.fragment;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

import edu.hune.quanlysinhvien02.R;
import edu.hune.quanlysinhvien02.database.MyDatabaseHelper;
import edu.hune.quanlysinhvien02.model.HeDaoTao;

public class AddSubjectsFragment extends Fragment {

    private View mView;
    private Spinner spKhoa, spHeDT;
    private EditText edtMaMon, edtTenMon, edtSoTC;
    private TextView tvTenKhoa, tvMaMon, tvTenMon, tvSoTC, tvHeDT;
    private Button btnThem;
    private MyDatabaseHelper mdb;
    private List<Khoa> mList;
    private List<MonHoc> mList1;

    private List<HeDaoTao> mListHeDaoTao;
    private ArrayAdapter<Khoa> mAdapter;

    private ArrayAdapter<HeDaoTao> mAdapterHeDT;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_add_subjects, container, false);
        mdb = new MyDatabaseHelper(getContext());

        addViews();
        doDuLieuMonHoc();
        doDuLieuKhoa();
        doDuLieuHeDaoTao();

        btnThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                them();
            }
        });

        return mView;
    }

    private void addViews() {
        spKhoa = mView.findViewById(R.id.spSubjectTenKhoa);
        spHeDT = mView.findViewById(R.id.spSubjectHeDT);

        edtMaMon = mView.findViewById(R.id.edtSubjectMaHP);
        edtTenMon = mView.findViewById(R.id.edtSubjectTenHP);
        edtSoTC = mView.findViewById(R.id.edtSubjectSoTC);

        tvTenKhoa = mView.findViewById(R.id.cbSubjectTenKhoa);
        tvMaMon = mView.findViewById(R.id.cbSubjectMaHP);
        tvTenMon = mView.findViewById(R.id.cbSubjectTenHP);
        tvSoTC = mView.findViewById(R.id.cbSubjectSoTC);
        tvHeDT = mView.findViewById(R.id.cbSubjectHeDT);

        btnThem = mView.findViewById(R.id.btnSubjectThem);
    }


    private void doDuLieuMonHoc() {
        SQLiteDatabase db = mdb.getReadableDatabase();
        mList1 = new ArrayList<>();

        if(db != null){

            String query = "SELECT m." + MyDatabaseHelper.getSubjectId() + ", m." + MyDatabaseHelper.getSubjectName() +
                    ", m." + MyDatabaseHelper.getSubjectTrainingSystem() +
                    " FROM " + MyDatabaseHelper.getFacultyTable() + " as k join " + MyDatabaseHelper.getSubjectTable() + " as m " +
                    " on k." + MyDatabaseHelper.getFacultyId() + " = " + " m." + MyDatabaseHelper.getSubjectFacultyId();


            Cursor c = db.rawQuery(query, null);
            if(c.getCount() != 0){
                while (c.moveToNext()){
                    mList1.add(new MonHoc(c.getString(0), c.getString(1), c.getString(2)));
                }
            }
            c.close();
        }


    }


    private void doDuLieuKhoa() {
        SQLiteDatabase db = mdb.getReadableDatabase();

        mList = new ArrayList<>();
        mList.add(new Khoa(0, ""));

        if(db != null){
            String query = "SELECT " + MyDatabaseHelper.getFacultyId() + ", " + MyDatabaseHelper.getFacultyName() +
                    " FROM " + MyDatabaseHelper.getFacultyTable();

            Cursor c = db.rawQuery(query, null);
            if(c.getCount() != 0){
                while (c.moveToNext()){
                    mList.add(new Khoa(c.getInt(0), c.getString(1)));
                }
            }
            c.close();
        }

        mAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, mList);
        spKhoa.setAdapter(mAdapter);
    }


    private void doDuLieuHeDaoTao() {

        SQLiteDatabase db = mdb.getReadableDatabase();

        mListHeDaoTao = new ArrayList<>();
        mListHeDaoTao.add(new HeDaoTao());

        if(db != null){
            String query = "SELECT " + MyDatabaseHelper.getTrainingSystemId() + ", " + MyDatabaseHelper.getTrainingSystemName() +
                    " FROM " + MyDatabaseHelper.getTrainingSystemTable();

            Cursor c= db.rawQuery(query, null);

            if(c.getCount() > 0){
                while (c.moveToNext()){
                    mListHeDaoTao.add(new HeDaoTao(c.getString(0), c.getString(1)));
                }
            }

            c.close();
            db.close();
        }

        mAdapterHeDT = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, mListHeDaoTao);
        spHeDT.setAdapter(mAdapterHeDT);
    }


    private void them() {
        String maHP = edtMaMon.getText().toString().trim();
        String tenHP = edtTenMon.getText().toString().trim();
        String soTC = edtSoTC.getText().toString();

        int check = 1;

        int posKhoa = spKhoa.getSelectedItemPosition();

        if(posKhoa == 0){
            check = 0;
            tvTenKhoa.setText("Vui lòng chọn khoa!");
        }else {
            tvTenKhoa.setText("");
        }

        if(maHP.equals("")){
            tvMaMon.setText("Vui lòng nhập mã học phần");
            check = 0;
        }else {
            int check1 = 1;
            for(MonHoc tmp : mList1){
                if(tmp.getId().equals(maHP)){
                    check = 0;
                    check1 = 0;
                    tvMaMon.setText("Mã học phần đã tồn tại");
                    break;
                }
            }

            if(check1 == 1){
                tvMaMon.setText("");
            }
        }


        int posHeDT = spHeDT.getSelectedItemPosition();
        if(posHeDT == 0){
            check = 0;
            tvHeDT.setText("Vui lòng chọn hệ đào tạo");
        }else {
            tvHeDT.setText("");
        }

        if(tenHP.equals("")){
            tvTenMon.setText("Vui lòng nhập tên học phần");
            check = 0;
        }else {
            int check1 = 1;
            for(MonHoc tmp : mList1){
                if(tmp.getTen().equals(tenHP) && mListHeDaoTao.get(posHeDT).getMaHDT().equals(tmp.getHeDaoTao())){
                    check = 0;
                    check1 = 0;
                    tvTenMon.setText("Tên học phần đã tồn tại");
                    break;
                }
            }

            if(check1 == 1){
                tvTenMon.setText("");
            }
        }

        if(soTC.equals("")){
            check = 0;
            tvSoTC.setText("Vui lòng số tín chỉ!");
        }else {
            tvSoTC.setText("");
        }

        if(check == 1){
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

            builder.setTitle("Thông báo");
            builder.setMessage("Bạn muốn thêm môn học mới có thông tin như sau: \n" +
                                "Tên khoa: " + mList.get(posKhoa).getTen() +
                                "\nMã học phần: " + maHP +
                                "\nTên học phần: " + tenHP +
                                "\nSố tín chỉ: " + soTC +
                                "\nHệ đào tạo: " + mListHeDaoTao.get(posHeDT).getTenHDT());

            builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    SQLiteDatabase db = mdb.getWritableDatabase();

                    ContentValues values = new ContentValues();
                    values.put(MyDatabaseHelper.getSubjectId(), maHP);
                    values.put(MyDatabaseHelper.getSubjectFacultyId(), mList.get(posKhoa).getId());
                    values.put(MyDatabaseHelper.getSubjectName(), tenHP);
                    values.put(MyDatabaseHelper.getSubjectCredit(), Integer.parseInt(soTC));
                    values.put(MyDatabaseHelper.getSubjectTrainingSystem(), mListHeDaoTao.get(posHeDT).getMaHDT());

                    long rs = db.insert(MyDatabaseHelper.getSubjectTable(), null, values);
                    if(rs == -1){
                        Toast.makeText(getContext(), "Add Subject Failed!", Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(getContext(), "Add Successful Subject!", Toast.LENGTH_SHORT).show();
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

    private class Khoa{
        private int id;
        private String ten;

        public Khoa() {

        }

        public Khoa(int id, String ten) {
            this.id = id;
            this.ten = ten;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getTen() {
            return ten;
        }

        public void setTen(String ten) {
            this.ten = ten;
        }

        @Override
        public String toString() {
            return ten;
        }
    }

    private class MonHoc{
        private String id, ten, heDaoTao;

        public MonHoc() {

        }

        public MonHoc(String id, String ten, String heDaoTao) {
            this.id = id;
            this.ten = ten;
            this.heDaoTao = heDaoTao;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTen() {
            return ten;
        }

        public void setTen(String ten) {
            this.ten = ten;
        }

        public String getHeDaoTao() {
            return heDaoTao;
        }

        public void setHeDaoTao(String heDaoTao) {
            this.heDaoTao = heDaoTao;
        }

        @Override
        public String toString() {
            return ten;
        }
    }
}