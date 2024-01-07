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

public class AddClassFragment extends Fragment {

    private View mView;

    private Spinner spKhoa, spKhoaDT, spHeDT;
    private EditText edtMaLop, edtTenLop;
    private TextView tvKhoa, tvKhoaDT, tvMaLop, tvTenLop, tvHDT;
    private Button btnThem;

    private MyDatabaseHelper mdb;
    private List<Khoa> mListKhoa;
    private List<KhoaDaoTao> mListKhoaDT;
    private List<HeDaoTao> mListHeDT;
    private ArrayAdapter<Khoa> mAdapterKhoa;
    private ArrayAdapter<KhoaDaoTao> mAdapterKhoaDT;
    private ArrayAdapter<HeDaoTao> mAdapterHeDT;
    private Khoa mKhoa;
    private KhoaDaoTao mKhoaDT;

    private HeDaoTao mHeDaoTao;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_add_class, container, false);

        mdb = new MyDatabaseHelper(getContext());

        addViews();
        doDuLieuKhoa();
        doDuLieuKhoaDT();
        doDuLieuHeDT();

        btnThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                them();
            }
        });
        return mView;
    }

    private void addViews() {
        spKhoa = mView.findViewById(R.id.spClassFgKhoa);
        spKhoaDT = mView.findViewById(R.id.spClassFgKhoaDT);
        spHeDT = mView.findViewById(R.id.spClassHeDT);

        edtMaLop = mView.findViewById(R.id.edtClassMaLop);
        edtTenLop = mView.findViewById(R.id.edtClassTenLop);

        tvKhoa = mView.findViewById(R.id.cbClassKhoa);
        tvKhoaDT = mView.findViewById(R.id.cbClassKhoaDT);
        tvMaLop = mView.findViewById(R.id.cbClassMaLop);
        tvTenLop = mView.findViewById(R.id.cbClassTenLop);
        tvHDT = mView.findViewById(R.id.cbClassHeDT);

        btnThem = mView.findViewById(R.id.btnClassThem);
    }

    private void doDuLieuHeDT() {
        SQLiteDatabase db = mdb.getReadableDatabase();
        mListHeDT = new ArrayList<>();
        mListHeDT.add(new HeDaoTao());
        if(db != null){

            String query = "SELECT " + MyDatabaseHelper.getTrainingSystemId() + ", " + MyDatabaseHelper.getTrainingSystemName() +
                            " FROM " + MyDatabaseHelper.getTrainingSystemTable();

            Cursor c= db.rawQuery(query, null);

            if(c.getCount() > 0){
                while (c.moveToNext()){
                    mListHeDT.add(new HeDaoTao(c.getString(0), c.getString(1)));
                }
            }

            c.close();
            db.close();
        }

        mAdapterHeDT = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, mListHeDT);
        spHeDT.setAdapter(mAdapterHeDT);
    }

    private void doDuLieuKhoa(){
        SQLiteDatabase db = mdb.getReadableDatabase();
        mListKhoa = new ArrayList<>();
        mListKhoa.add(new Khoa(0, ""));
        if(db != null){
            String query = "SELECT " + MyDatabaseHelper.getFacultyId() + ", " + MyDatabaseHelper.getFacultyName() +
                            " FROM " + MyDatabaseHelper.getFacultyTable();

            Cursor c = db.rawQuery(query, null);

            if(c.getCount() != 0){
                while (c.moveToNext()){
                    mListKhoa.add(new Khoa(c.getInt(0), c.getString(1)));
                }
            }
            c.close();
        }

        mAdapterKhoa = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, mListKhoa);
        spKhoa.setAdapter(mAdapterKhoa);


    }

    private void doDuLieuKhoaDT() {
        SQLiteDatabase db = mdb.getReadableDatabase();

        mListKhoaDT = new ArrayList<>();
        mListKhoaDT.add(new KhoaDaoTao(0, ""));

        if(db != null){
            String query = "SELECT " + MyDatabaseHelper.getTrainingCourseId() + ", " + MyDatabaseHelper.getTrainingCourseName() +
                          " FROM " + MyDatabaseHelper.getTrainingCourseTable();

            Cursor c = db.rawQuery(query, null);

            if(c.getCount() != 0){
                while (c.moveToNext()){
                    mListKhoaDT.add(new KhoaDaoTao(c.getInt(0), c.getString(1)));
                }
            }

            c.close();
        }

        mAdapterKhoaDT = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, mListKhoaDT);
        spKhoaDT.setAdapter(mAdapterKhoaDT);
    }


    private void them() {

        String maLop = edtMaLop.getText().toString();
        String tenLop = edtTenLop.getText().toString().trim();

        int check = 1;
        SQLiteDatabase db = mdb.getWritableDatabase();

        int posKhoa = spKhoa.getSelectedItemPosition();
        if(posKhoa == 0){
            check = 0;
            tvKhoa.setText("Vui lòng chọn khoa");
        }else {
            tvKhoa.setText("");
            mKhoa = mListKhoa.get(posKhoa);
        }

        int posKhoaDT = spKhoaDT.getSelectedItemPosition();
        if(posKhoaDT == 0){
            check = 0;
            tvKhoaDT.setText("Vui lòng chọn khóa đào tạo");
        }else {
            tvKhoaDT.setText("");
            mKhoaDT = mListKhoaDT.get(posKhoaDT);
        }

        int posHeDT = spHeDT.getSelectedItemPosition();
        if(posHeDT == 0){
            check = 0;
            tvHDT.setText("Vui lòng chọn hệ đào tạo");
        }else {
            tvHDT.setText("");
            mHeDaoTao = mListHeDT.get(posHeDT);
        }

        if(maLop.equals("")){
            check = 0;
            tvMaLop.setText("Vui lòng nhập mã lớp");
        }else {

            int check1 = 1;
            String query = "SELECT " + MyDatabaseHelper.getClassId() + " FROM " + MyDatabaseHelper.getClassTable();
            Cursor c = db.rawQuery(query, null);
            if(c.getCount() != 0){
                while (c.moveToNext()){
                    if(maLop.equals(String.valueOf(c.getInt(0)))){
                        check = 0;
                        check1 = 0;
                        tvMaLop.setText("Mã lớp đã tồn tại");
                        break;
                    }
                }
            }
            c.close();
            if(check1 == 1){
                tvMaLop.setText("");
            }
        }

        if(tenLop.equals("")){
            check = 0;
            tvTenLop.setText("Vui lòng nhập tên lớp");
        }else {

            int check1 = 1;
            String query = "SELECT " + MyDatabaseHelper.getClassName() + ", " + MyDatabaseHelper.getClassFacultyId() + " FROM " + MyDatabaseHelper.getClassTable();
            Cursor c = db.rawQuery(query, null);
            if(c.getCount() != 0){
                while (c.moveToNext()){
                    if(tenLop.equals(c.getString(0)) && (mKhoa.getId() == c.getInt(1))){
                        check = 0;
                        check1 = 0;
                        tvTenLop.setText("Tên lớp đã tồn tại");
                        break;
                    }
                }
            }
            c.close();
            if(check1 == 1){
                tvTenLop.setText("");
            }
        }


        if(check == 1){
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

            builder.setTitle("Thông báo!");
            builder.setMessage("Bạn muốn thêm lớp mới có thông tin như sau: \n" +
                                "Khoa: " + mKhoa.getTen() +
                                "\nKhóa đào tạo: " + mKhoaDT.getTen() +
                                "\nMã lớp: " + maLop +
                                "\nTên lớp: " + tenLop +
                                "\nHệ đào tạo: " + mHeDaoTao.getTenHDT());

            builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    ContentValues values = new ContentValues();
                    values.put(MyDatabaseHelper.getClassId(), Integer.parseInt(maLop));
                    values.put(MyDatabaseHelper.getClassFacultyId(), mKhoa.getId());
                    values.put(MyDatabaseHelper.getClassFacultyTrainingId(), mKhoaDT.getId());
                    values.put(MyDatabaseHelper.getClassName(), tenLop);
                    values.put(MyDatabaseHelper.getTrainingSystemId(), mHeDaoTao.getMaHDT());

                    long rs = db.insert(MyDatabaseHelper.getClassTable(), null, values);
                    if(rs == -1){
                        Toast.makeText(getContext(), "Add Class Failed!", Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(getContext(), "Add successful Class!", Toast.LENGTH_SHORT).show();
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

    private class KhoaDaoTao{
        private int id;
        private String ten;

        public KhoaDaoTao() {

        }

        public KhoaDaoTao(int id, String ten) {
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

}