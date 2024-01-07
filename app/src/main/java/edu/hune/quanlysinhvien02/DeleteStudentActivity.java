package edu.hune.quanlysinhvien02;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;

import android.database.Cursor;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import edu.hune.quanlysinhvien02.adapter.ItemStudentDeleteAdapter;
import edu.hune.quanlysinhvien02.database.MyDatabaseHelper;
import edu.hune.quanlysinhvien02.model.ItemStudentDeModi;

public class DeleteStudentActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private SearchView svTimKiem;
    private ListView listView;

    private List<ItemStudentDeModi> mList;
    private ItemStudentDeleteAdapter adapter;

    private MyDatabaseHelper mdb;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_student);

        toolbar = findViewById(R.id.tbDeleteStudent);
        svTimKiem = findViewById(R.id.svDeleteTimKiem);
        listView = findViewById(R.id.lvDeleteStudent);
        mdb = new MyDatabaseHelper(DeleteStudentActivity.this);

        xuLyTooBar();

        //đổ dữ liệu lên adapter
        doDuLieuListView();
        adapter = new ItemStudentDeleteAdapter(DeleteStudentActivity.this, mList);
        listView.setAdapter(adapter);

        svTimKiem.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
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
    }

    private void svTimKiem(String newText) {
        ArrayList<ItemStudentDeModi> newList = new ArrayList<>();
        if (!newText.equals("")) {
            for (ItemStudentDeModi tmp : mList) {
                if (tmp.getMaSV().contains(newText) || tmp.getTenSV().toLowerCase().contains(newText.toLowerCase())) {
                    newList.add(tmp);
                }
            }
        } else {
            doDuLieuListView();
            adapter.setData(mList);
        }

        if (!newText.equals("") && !newList.isEmpty()) {
            adapter.setData(newList);
        }
    }

    private void doDuLieuListView() {

        Cursor cursor = mdb.readItemListStudent();

        mList = new ArrayList<>();

        if (cursor.getCount() != 0) {
            while (cursor.moveToNext()) {
                mList.add(new ItemStudentDeModi(String.valueOf(cursor.getInt(0)), cursor.getString(1)));
            }
        }
    }

    private void xuLyTooBar() {
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();

        actionBar.setTitle("Xóa sinh viên");
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}