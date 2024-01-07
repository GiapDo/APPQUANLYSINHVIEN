package edu.hune.quanlysinhvien02.fragment;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import edu.hune.quanlysinhvien02.R;
import edu.hune.quanlysinhvien02.adapter.LopTinChiAdapter;
import edu.hune.quanlysinhvien02.database.MyDatabaseHelper;
import edu.hune.quanlysinhvien02.model.ItemStudentDeModi;
import edu.hune.quanlysinhvien02.model.ItemStudentLopTC;
import edu.hune.quanlysinhvien02.model.LopTinChi;


public class ThemSinhVienVaoLopTCFragment extends Fragment {

    private View mView;
    private Spinner spinner1;
    private SearchView mSearchView;
    private ListView mListView;
    private Button btnThem;

    private LopTinChiAdapter mAdapter1;
    private List<LopTinChi> mList1;
    private List<ItemStudentLopTC> mList2;
    private String maLopTc;

    private MyDatabaseHelper mdb;

    public ThemSinhVienVaoLopTCFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_them_sinh_vien_vao_lop_t_c, container, false);
        mdb = new MyDatabaseHelper(getContext());

        spinner1 = mView.findViewById(R.id.spLopTC);
        mSearchView = mView.findViewById(R.id.svLopTc);
        mListView = mView.findViewById(R.id.listViewLopTC);
        btnThem = mView.findViewById(R.id.btnLopTC);

        doDuLieuLenSp();

        ArrayAdapter<LopTinChi> mAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, mList1);
        spinner1.setAdapter(mAdapter);



        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                laySinhVien(position);
                if(mList2.size() > 0) {
                    maLopTc = mList1.get(position).getMaLopTC();
                    mAdapter1 = new LopTinChiAdapter(getContext(), mList2);
                    mListView.setAdapter(mAdapter1);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btnThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<ItemStudentLopTC> listsvmoi = mAdapter1.getSelectedItems();
                SQLiteDatabase db = mdb.getWritableDatabase();
                for(ItemStudentLopTC tmp : listsvmoi){
                    ContentValues values = new ContentValues();
                    values.put(MyDatabaseHelper.getScoresStudentId(), tmp.getMasv());
                    values.put(MyDatabaseHelper.getScoresCreditClassId(), maLopTc);
                    values.put(MyDatabaseHelper.getScoresNote(), "");
                    db.insert(MyDatabaseHelper.getBangDiem(), null, values);
                    mList2.removeIf(t -> t.equals(tmp));
                    mAdapter1.setData(mList2);
                }
            }
        });

        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                svTimKiem(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                svTimKiem(newText);
                return false;
            }
        });

        return mView;
    }

    private void svTimKiem(String text){

        ArrayList<ItemStudentLopTC> newList = new ArrayList<>();
        if (!text.equals("")) {
            for (ItemStudentLopTC tmp : mList2) {
                if (String.valueOf(tmp.getMasv()).contains(text) || tmp.getTensv().toLowerCase().contains(text.toLowerCase())) {
                    newList.add(tmp);
                }
            }
        } else {
            if(mAdapter1 != null) {
                mAdapter1.setData(mList2);
            }
        }

        if (!text.equals("") && !newList.isEmpty()) {
            if(mAdapter1 != null) {
                mAdapter1.setData(newList);
            }
        }
    }

    private void laySinhVien(int position) {
        mList2 = new ArrayList<>();

        List<Integer> daTonTai = new ArrayList<>();

        if(position > 0){
            SQLiteDatabase db = mdb.getReadableDatabase();

            String query = "SELECT s." + MyDatabaseHelper.getMaSV() +
                    " FROM " + MyDatabaseHelper.getBangSV() + " as s join " + MyDatabaseHelper.getBangDiem() + " as d " +
                    " on s." + MyDatabaseHelper.getMaSV() + " = d." + MyDatabaseHelper.getScoresStudentId() +
                    " WHERE d." + MyDatabaseHelper.getScoresCreditClassId() + " = \"" + mList1.get(position).getMaLopTC() + "\"";

            String query1 = "SELECT " + MyDatabaseHelper.getMaSV() + ", " + MyDatabaseHelper.getTenSV() +
                    " FROM " + MyDatabaseHelper.getBangSV() ;


            if(db != null){
                Cursor c = db.rawQuery(query, null);
                if(c.getCount() != 0){
                    while (c.moveToNext()){
                        daTonTai.add(c.getInt(0));
                    }
                }

                Cursor c1 = db.rawQuery(query1, null);
                if(c1.getCount() != 0){
                    while (c1.moveToNext()){
                        mList2.add(new ItemStudentLopTC(c1.getInt(0), c1.getString(1), false));
                    }
                }

                c.close();
                c1.close();
                db.close();
            }
        }

        List<ItemStudentLopTC> lis1 = new ArrayList<>();

        for(ItemStudentLopTC tmp : mList2){
            if(!daTonTai.contains(tmp.getMasv())){
                lis1.add(tmp);
            }
        }

        mList2 = lis1;

    }

    private void doDuLieuLenSp() {
        mList1 = new ArrayList<>();
        mList1.add(new LopTinChi());

        String query = "SELECT k." + MyDatabaseHelper.getFacultyId() +
                            ", l." + MyDatabaseHelper.getCreditClassMaxNbs() + ", l." + MyDatabaseHelper.getCreditClassId() +
                       " FROM " + MyDatabaseHelper.getFacultyTable() + " as k join " + MyDatabaseHelper.getSubjectTable() + " as m " +
                            " on k." + MyDatabaseHelper.getFacultyId() + " = m." + MyDatabaseHelper.getSubjectFacultyId() +
                            " join " + MyDatabaseHelper.getCreditClassTable() + " as l" +
                            " on m." + MyDatabaseHelper.getSubjectId() + " = l." + MyDatabaseHelper.getCreditClassSubjectId() ;

        SQLiteDatabase db = mdb.getReadableDatabase();
        if(db != null){
            Cursor c = db.rawQuery(query, null);
            if(c.getCount() != 0){
                while (c.moveToNext()){
                    mList1.add(new LopTinChi(c.getInt(0), c.getInt(1), c.getString(2)));
                }
            }
            c.close();
            db.close();
        }
    }
}