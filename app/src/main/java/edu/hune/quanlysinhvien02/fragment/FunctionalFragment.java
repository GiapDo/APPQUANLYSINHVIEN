package edu.hune.quanlysinhvien02.fragment;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import edu.hune.quanlysinhvien02.AddInformationActivity;
import edu.hune.quanlysinhvien02.AddStudentMainActivity;
import edu.hune.quanlysinhvien02.DeleteStudentActivity;
import edu.hune.quanlysinhvien02.ListStudentActivity;
import edu.hune.quanlysinhvien02.ModifyStudentActivity;
import edu.hune.quanlysinhvien02.R;
import edu.hune.quanlysinhvien02.database.MyDatabaseHelper;

public class FunctionalFragment extends Fragment {

    private ImageView avatar;
    private TextView tvName, tvThoiGian;
    private GridView gvList;
    private View mView;
    public static String mEmail;

    private String email;
    public FunctionalFragment() {

    }

    public FunctionalFragment(String email) {
        this.email = email;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_functional, container, false);

        avatar = mView.findViewById(R.id.functional_avatar);
        tvName = mView.findViewById(R.id.functional_name);
        tvThoiGian = mView.findViewById(R.id.functional_time);

        gvList = mView.findViewById(R.id.gv_list_functional);

        mEmail = email;

        Object []activities = {ListStudentActivity.class, AddStudentMainActivity.class, ModifyStudentActivity.class, DeleteStudentActivity.class, AddInformationActivity.class};

        doDuLieu();
        doDuLieu1();
        String tg = xacDinhThoiGian();
        tvThoiGian.setText(tg);

        gvList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getContext(), (Class<?>) activities[position]);
                startActivity(intent);
            }
        });
        return mView;
    }

    private String xacDinhThoiGian() {
        LocalDateTime date = LocalDateTime.now();

        int gio = date.getHour();

        if(gio >= 4 && gio < 12){
            return "Good morning";
        }else if(gio >= 12 && gio < 18){
            return "Good afternoon";
        } else {
            return "Good evening";
        }
    }

    private void doDuLieu() {
        String []from = {"image", "name"};
        int []to = {R.id.ivItemFunctional, R.id.tvItemFunctional};

        int []ig = {R.drawable.ic_list_student, R.drawable.ic_more_student, R.drawable.modify_sv, R.drawable.ic_delete_student, R.drawable.logo3};
        String []ten = {"Danh sách sinh viên", "Thêm sinh viên", "Sửa thông tin sinh viên", "Xóa sinh viên", "Các chức năng khác"};

        List<HashMap<String, Object>> list = new ArrayList<>();

        for(int i = 0; i < ig.length; i++){
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("image", ig[i]);
            hashMap.put("name", ten[i]);

            list.add(hashMap);
        }

        SimpleAdapter adapter = new SimpleAdapter(getContext(), list, R.layout.item_functional, from, to);

        gvList.setAdapter(adapter);

    }

    public void doDuLieu1(){
        SQLiteDatabase db = (new MyDatabaseHelper(getContext())).getReadableDatabase();
        String query = "SELECT " + MyDatabaseHelper.getAccountImage() + ", " + MyDatabaseHelper.getAccountName() +
                " FROM " + MyDatabaseHelper.getAccountTable() +
                " WHERE " + MyDatabaseHelper.getAccountEmail() + " = \"" + mEmail + "\"";
        byte []anh1 = null;
        String ten1 = "";
        if(db != null){
            Cursor c = db.rawQuery(query, null);
            if(c.getCount() != 0){
                while (c.moveToNext()){
                    anh1 = c.getBlob(0);
                    ten1 = c.getString(1);
                }
            }
            c.close();
            db.close();
        }
        if(anh1 == null){
            avatar.setImageResource(R.drawable.avartar1);
            avatar.setBackgroundColor(Color.parseColor("#a3c2c2"));
        }else {
            Bitmap factory = BitmapFactory.decodeByteArray(anh1, 0, anh1.length);
            avatar.setImageBitmap(factory);
        }
        tvName.setText("Hello " + ten1);
    }



    @Override
    public void onResume() {
        super.onResume();

        ActionBar actionBar =((AppCompatActivity)getActivity()).getSupportActionBar();
        actionBar.setTitle("Quản lý sinh viên");
    }
}