package edu.hune.quanlysinhvien02.fragment;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

import edu.hune.quanlysinhvien02.R;
import edu.hune.quanlysinhvien02.database.MyDatabaseHelper;

public class AddCreditClassFragment extends Fragment {

    private View mView;

    private Spinner spKhoa, spHocPhan;
    private EditText edtMaLopTC, edtSoLuong, edtPhongHoc, edtNamHoc;
    private RadioButton rdb1, rdb2, rdb3;
    private Button btnThem;

    private MyDatabaseHelper mdb;

    private List<MonHoc> mList;
    private List<KhoaDaoTao> mList1;

    private ArrayAdapter<MonHoc> mAdapterMonHoc;
    private ArrayAdapter<KhoaDaoTao> mAdapterKhoaDT;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_add_credit_class, container, false);

        mdb = new MyDatabaseHelper(getContext());

        addViews();
        doDuLieu();

        btnThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                them();
            }
        });
        return mView;
    }

    private void them() {
    }

    private void doDuLieu() {
        mList = new ArrayList<>();
        mList.add(new MonHoc("", ""));
        mList1 = new ArrayList<>();
        mList1.add(new KhoaDaoTao(0, ""));

        SQLiteDatabase db = mdb.getReadableDatabase();

        if(db != null){
            String query = "SELECT " + MyDatabaseHelper.getTrainingCourseId() + ", " + MyDatabaseHelper.getTrainingCourseName() +
                           " FROM " + MyDatabaseHelper.getTrainingCourseTable();

            Cursor c = db.rawQuery(query, null);
            if(c.getCount() != 0){
                while (c.moveToNext()){
                    mList1.add(new KhoaDaoTao(c.getInt(0), c.getString(1)));
                }
            }
            c.close();

            String query1 = "SELECT " + MyDatabaseHelper.getSubjectId() + ", " + MyDatabaseHelper.getSubjectName() +
                    " FROM " + MyDatabaseHelper.getSubjectTable();

            Cursor c1 = db.rawQuery(query1, null);

            if(c1.getCount() != 0){
                while (c1.moveToNext()){
                    mList.add(new MonHoc(c1.getString(0), c1.getString(1)));
                }
            }
            c1.close();
        }

        mAdapterMonHoc = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, mList);
        mAdapterKhoaDT = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, mList1);

        spHocPhan.setAdapter(mAdapterMonHoc);
        spKhoa.setAdapter(mAdapterKhoaDT);
    }

    private void addViews() {

        spKhoa = mView.findViewById(R.id.spCreditFgKhoaDT);
        spHocPhan = mView.findViewById(R.id.spCreditFgTenHP);

        edtMaLopTC = mView.findViewById(R.id.edtCreditMaLopTC);
        edtSoLuong = mView.findViewById(R.id.edtCreditSoLuongMax);
        edtPhongHoc = mView.findViewById(R.id.edtCreditPhongHoc);
        edtNamHoc = mView.findViewById(R.id.edtCreditNam);

        rdb1 = mView.findViewById(R.id.rdbCredit1);
        rdb2 = mView.findViewById(R.id.rdbCredit2);
        rdb3 = mView.findViewById(R.id.rdbCredit3);

        btnThem = mView.findViewById(R.id.btnCreditThem);
    }

    private class MonHoc{
        private String id, ten;

        public MonHoc() {

        }

        public MonHoc(String id, String ten) {
            this.id = id;
            this.ten = ten;
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