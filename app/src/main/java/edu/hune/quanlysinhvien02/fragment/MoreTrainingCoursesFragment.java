package edu.hune.quanlysinhvien02.fragment;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import edu.hune.quanlysinhvien02.R;
import edu.hune.quanlysinhvien02.database.MyDatabaseHelper;
import edu.hune.quanlysinhvien02.model.Util;

public class MoreTrainingCoursesFragment extends Fragment {

    private View mView;
    private EditText edtMaKDT, edtTenKDT, edtNgayBD, edtNgayKT;
    private TextView cbMaKDT, cbTenKDT, cbNgayBD, cbNgayKT;
    private Button btnThem;

    private MyDatabaseHelper mdb;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_more_training_courses, container, false);
        mdb = new MyDatabaseHelper(getContext());

        addViews();

        btnThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                them();
            }
        });
        return mView;
    }

    private void them() {
        String maKhoa = edtMaKDT.getText().toString();
        String tenKhoa = edtTenKDT.getText().toString().trim();
        String ngayBD = edtNgayBD.getText().toString();
        String ngayKT = edtNgayKT.getText().toString();
        String  date1 = "";
        String date2 = "";

        SQLiteDatabase db = mdb.getWritableDatabase();

        int check = 1;

        if(maKhoa.equals("")){
            cbMaKDT.setText("Vui lòng nhập mã khóa đào tạo!");
            check = 0;
        }else {
            String query = "SELECT " + MyDatabaseHelper.getTrainingCourseId() + " FROM " + MyDatabaseHelper.getTrainingCourseTable() ;
            Cursor cursor = db.rawQuery(query, null);
            int chek1 = 1;
            if(cursor.getCount() != 0){
                while (cursor.moveToNext()){
                    if(maKhoa.equals(String.valueOf(cursor.getInt(0)))){
                        check = 0;
                        chek1 = 0;
                        cbMaKDT.setText("Mã khóa đào tạo đã tồn tại!");
                        break;
                    }
                }
            }
            if(chek1 == 1){
                cbMaKDT.setText("");
            }
        }

        if(tenKhoa.equals("")){
            cbTenKDT.setText("Vui lòng nhập tên khóa đào tạo");
            check = 0;
        }else {
            String query = "SELECT " + MyDatabaseHelper.getTrainingCourseName() + " FROM " + MyDatabaseHelper.getTrainingCourseTable() ;
            Cursor cursor = db.rawQuery(query, null);
            int chek1 = 1;
            if(cursor.getCount() != 0){
                while (cursor.moveToNext()){
                    if(maKhoa.equals(cursor.getString(0))){
                        check = 0;
                        chek1 = 0;
                        cbTenKDT.setText("Tên khóa đào tạo đã tồn tại!");
                        break;
                    }
                }
            }
            if(chek1 == 1){
                cbTenKDT.setText("");
            }
        }

        if(ngayBD.equals("")){
            cbNgayBD.setText("Vui lòng nhập ngày bắt đầu!");
            check = 0;
        }else {
            try {
                date1 = Util.toStringDate(Util.toDate(ngayBD));
                cbNgayBD.setText("");
            }catch (Exception e){
                e.printStackTrace();
                cbNgayBD.setText("Ngày bắt đầu nhập không hợp lệ!");
                check = 0;
            }
        }

        if(ngayBD.equals("")){
            cbNgayKT.setText("Vui lòng nhập ngày kết thúc!");
            check = 0;
        }else {
            try {
                date2 = Util.toStringDate(Util.toDate(ngayKT));
                cbNgayKT.setText("");
            }catch (Exception e){
                e.printStackTrace();
                cbNgayKT.setText("Ngày kết thúc nhập không hợp lệ!");
                check = 0;
            }
        }

        if(check == 1){
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle("Thông báo");
            builder.setMessage("Bạn muốn thêm khóa đào tạo có thông tin như sau: \n" +
                    "Mã khoa: " + maKhoa +
                    "\nTên khoa: " + tenKhoa +
                    "\nNgày bắt đầu: " + date1 +
                    "\nNgày kết thúc: " + date2);
            builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    ContentValues values = new ContentValues();
                    values.put(MyDatabaseHelper.getTrainingCourseId(), Integer.parseInt(maKhoa));
                    values.put(MyDatabaseHelper.getTrainingCourseName(), tenKhoa);
                    values.put(MyDatabaseHelper.getTrainingCourseStartDay(), ngayBD);
                    values.put(MyDatabaseHelper.getTrainingCourseEndDay(), ngayKT);
                    long rs = db.insert(MyDatabaseHelper.getTrainingCourseTable(), null, values);
                    if(rs == -1){
                        Toast.makeText(getContext(), "Add Training Course Failed!", Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(getContext(), "Successfully Added Training Course", Toast.LENGTH_SHORT).show();
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

    private void addViews() {
        edtMaKDT = mView.findViewById(R.id.edtTrainingMaKhoaDT);
        edtTenKDT = mView.findViewById(R.id.edtTrainingTenKhoaDT);
        edtNgayBD = mView.findViewById(R.id.edtTrainingStartDay);
        edtNgayKT = mView.findViewById(R.id.edtTrainingEndDay);

        cbMaKDT = mView.findViewById(R.id.cbTrainingMaKhoaDT);
        cbTenKDT = mView.findViewById(R.id.cbTrainingTenKhoaDT);
        cbNgayBD = mView.findViewById(R.id.cbTrainingStartDay);
        cbNgayKT = mView.findViewById(R.id.cbTrainingEndDay);

        btnThem = mView.findViewById(R.id.btnTrainingThem);
    }
}