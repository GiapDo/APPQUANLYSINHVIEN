package edu.hune.quanlysinhvien02;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.hune.quanlysinhvien02.adapter.ItemStudentListAdapter;
import edu.hune.quanlysinhvien02.database.MyDatabaseHelper;
import edu.hune.quanlysinhvien02.model.ItemStudentList;
import edu.hune.quanlysinhvien02.model.Util;

public class ListStudentActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private SearchView searchView;
    private ListView mView;

    private MyDatabaseHelper myDB;

    private List<ItemStudentList> mlist;
    private ItemStudentListAdapter adapter;
    private int selectedMenuItemId = -1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_student);

        toolbar = findViewById(R.id.tbListStudent);
        searchView = findViewById(R.id.svListTimKiem);
        mView = findViewById(R.id.lvListStudent);
        myDB = new MyDatabaseHelper(ListStudentActivity.this);

        xuLyTooBar();
        doDulieuVaoList();
        doDulieuVaoAdapter(mlist);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filterList(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterList(newText);
                return false;
            }
        });


    }


    private void filterList(String newText) {
        List<ItemStudentList> list = new ArrayList<>();
        if(!newText.equals("")) {
            for (ItemStudentList tmp : mlist) {
                if (tmp.getMaSV().contains(newText) || tmp.getTenSV().toLowerCase().contains(newText.toLowerCase())) {
                    list.add(tmp);
                }
            }
        }else {
            doDulieuVaoList();
            adapter.setFilteredList(mlist);
        }

        if(!list.isEmpty() && !newText.equals("")){
            adapter.setFilteredList(list);
        }

    }

    private void doDulieuVaoAdapter(List<ItemStudentList> list) {
        adapter = new ItemStudentListAdapter(this, list);
        mView.setAdapter(adapter);
    }

    private void doDulieuVaoList() {

        mlist = new ArrayList<>();
        Cursor cs = myDB.readItemListStudent();
        if(cs.getCount() != 0){
            while (cs.moveToNext()){
                ItemStudentList sinhVien = new ItemStudentList(String.valueOf(cs.getInt(0)), cs.getString(1), cs.getString(2),
                                        cs.getString(3), cs.getString(4), cs.getString(5), ListStudentActivity.this);

                mlist.add(sinhVien);
            }
        }
        cs.close();
    }

    private void xuLyTooBar() {
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Danh sách sinh viên");
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_bao_cao, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        if(item.getItemId() == android.R.id.home){
            finish();
            return true;
        }
        if (selectedMenuItemId != -1 && selectedMenuItemId != id) {
            MenuItem previousMenuItem = toolbar.getMenu().findItem(selectedMenuItemId);
            if (previousMenuItem != null) {
                previousMenuItem.setChecked(false);
            }
        }

        item.setChecked(true);
        selectedMenuItemId = id;

        if(id == R.id.menu_bao_cao_danh_sach){
            doDulieuVaoList();
            adapter.setFilteredList(mlist);
        }else if(id == R.id.menu_bao_cao_moi_tao){
            laySinhVienMoiTao();
        } else if (id == R.id.menu_bao_cao_gpa) {
            layGPACao();
        }


        return super.onOptionsItemSelected(item);
    }

    private void laySinhVienMoiTao() {
        LocalDateTime currentDateTime = LocalDateTime.now();
        LocalDateTime startOfWeek = currentDateTime.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)).with(LocalTime.MIN);
        LocalDateTime endOfWeek = currentDateTime.with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY)).with(LocalTime.MAX);

        SQLiteDatabase db = myDB.getReadableDatabase();
        if(db != null){
            String query = "SELECT " + MyDatabaseHelper.getHistoryNote() +
                    " FROM " +  MyDatabaseHelper.getHistoryTable() +
                    " WHERE " + MyDatabaseHelper.getHistoryType() + " = 5 AND " + MyDatabaseHelper.getHistoryTime() + " BETWEEN ? AND ?";

            String[] selectionArgs = {Util.toStringDateTime(startOfWeek), Util.toStringDateTime(endOfWeek)};

            Cursor c = db.rawQuery(query, selectionArgs);
            List<String> listMaSV = new ArrayList<>();
            if(c.getCount() != 0){
                while (c.moveToNext()){
                    listMaSV.add(c.getString(0));
                }
            }

            List<ItemStudentList> tmp =   laySinhVienMoi(mlist, listMaSV);

            adapter.setFilteredList(tmp);

            c.close();
            db.close();
        }
    }

    private List<ItemStudentList> laySinhVienMoi(List<ItemStudentList> mlist, List<String> listMaSV) {

        Map<String, ItemStudentList> map = new HashMap<>();

        for(ItemStudentList tmp : mlist){
            map.put(tmp.getMaSV(), tmp);
        }

        List<ItemStudentList> listMoi = new ArrayList<>();
        for(String id : listMaSV){
            ItemStudentList tmp1 = map.get(id);
            if(tmp1 != null){
                listMoi.add(tmp1);
            }
        }

        return listMoi;

    }

    private void layGPACao() {
        List<ItemStudentList> list = new ArrayList<>();
        for(ItemStudentList tmp : mlist){
            if(!tmp.getDiem().equals("")) {
                if (Double.parseDouble(tmp.getDiem()) >= 3.6) {
                    list.add(tmp);
                }
            }
        }
        adapter.setFilteredList(list);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode ==200 && requestCode == 100){
            doDulieuVaoList();
            adapter.setFilteredList(mlist);

        }
    }
}