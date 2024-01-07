package edu.hune.quanlysinhvien02.fragment;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import edu.hune.quanlysinhvien02.R;
import edu.hune.quanlysinhvien02.adapter.HistoryAdapter;
import edu.hune.quanlysinhvien02.database.MyDatabaseHelper;
import edu.hune.quanlysinhvien02.model.History;
import edu.hune.quanlysinhvien02.model.Util;

public class HistoryFragment extends Fragment {

    private View mView;
    private CheckBox cbXapXepThoiGian;
    private ListView mListView;

    private HistoryAdapter mAdapter;

    private List<History> mList;

    public HistoryFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_history, container, false);

        cbXapXepThoiGian = mView.findViewById(R.id.cbXapSepTheoThoiGian);
        mListView = mView.findViewById(R.id.history_list_view);

        layDuLieu();
        mAdapter = new HistoryAdapter(getContext(), mList);
        mListView.setAdapter(mAdapter);

        cbXapXepThoiGian.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    xapXep();
                }else {
                    khoiPhuc();
                }
            }
        });

        return mView;
    }

    private void khoiPhuc() {
        layDuLieu();
        mAdapter.setData(mList);
    }

    private void xapXep() {
        Collections.sort(mList, new Comparator<History>() {
            @Override
            public int compare(History o1, History o2) {
                return o2.getDate().compareTo(o1.getDate());
            }
        });

        mAdapter.setData(mList);
    }

    private void layDuLieu(){
        mList = new ArrayList<>();

        SQLiteDatabase db = (new MyDatabaseHelper(getContext())).getReadableDatabase();

        if(db != null){
            String query = "SELECT " + MyDatabaseHelper.getHistoryType() + ", " + MyDatabaseHelper.getHistoryName() + ", " + MyDatabaseHelper.getHistoryTime() +
                            " FROM " + MyDatabaseHelper.getHistoryTable();

            Cursor c = db.rawQuery(query, null);
            if(c.getCount() != 0){
                while (c.moveToNext()){
                    History history = new History(Util.toDateTime(c.getString(2)), c.getString(1), c.getInt(0));
                    mList.add(history);
                }
            }
            c.close();
            db.close();
        }

    }



    @Override
    public void onResume() {
        super.onResume();
        ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        actionBar.setTitle("Lịch sử");
    }
}