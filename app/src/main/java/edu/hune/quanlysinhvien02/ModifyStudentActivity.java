package edu.hune.quanlysinhvien02;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import edu.hune.quanlysinhvien02.adapter.ItemStudenModifyAdapter;
import edu.hune.quanlysinhvien02.database.MyDatabaseHelper;
import edu.hune.quanlysinhvien02.model.ItemStudentDeModi;

public class ModifyStudentActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private SearchView searchView;
    private ListView listView;

    private List<ItemStudentDeModi> mList;
    private ItemStudenModifyAdapter adapter;

    private MyDatabaseHelper mdb;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_student);

        toolbar = findViewById(R.id.tbModifyStudent);
        searchView = findViewById(R.id.svModifyStudent);
        listView = findViewById(R.id.lvModifyStudent);

        mdb = new MyDatabaseHelper(ModifyStudentActivity.this);

        //Xử lý toolbar
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Sửa thông tin sinh viên");
        actionBar.setDisplayHomeAsUpEnabled(true);

        doDuLieuVaoList();

        adapter = new ItemStudenModifyAdapter(ModifyStudentActivity.this, mList);
        listView.setAdapter(adapter);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                timKiem(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                timKiem(newText);
                return false;
            }
        });
    }

    private void timKiem(String text) {
        ArrayList<ItemStudentDeModi> list = new ArrayList<>();
        if (!text.equals("")) {
            for (ItemStudentDeModi tmp : mList) {
                if (tmp.getMaSV().contains(text) || tmp.getTenSV().toLowerCase().contains(text.toLowerCase())) {
                    list.add(tmp);
                }
            }
        } else {
            doDuLieuVaoList();
            adapter.setData(mList);
        }

        if (!list.isEmpty() && !text.equals("")) {
            adapter.setData(list);
        }
    }

    private void doDuLieuVaoList() {

        mList = new ArrayList<>();

        Cursor c = mdb.readItemListStudent();
        if (c.getCount() != 0) {
            while (c.moveToNext()) {
                ItemStudentDeModi tmp = new ItemStudentDeModi(String.valueOf(c.getInt(0)), c.getString(1));
                mList.add(tmp);
            }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 110 && resultCode == 200) {
            doDuLieuVaoList();
            adapter.setData(mList);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId() == android.R.id.home){
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}