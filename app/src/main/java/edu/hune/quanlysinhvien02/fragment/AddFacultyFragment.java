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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import edu.hune.quanlysinhvien02.R;
import edu.hune.quanlysinhvien02.database.MyDatabaseHelper;

public class AddFacultyFragment extends Fragment {

    private View mView;
    private EditText edtMaKhoa, edtTenKhoa, edtDiaChi, edtSDT;
    private TextView cbMaKhoa, cbTenKhoa, cbDiaChi, cbSDT;
    private Button btnThem;
    
    private MyDatabaseHelper mdb;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_add_faculty, container, false);

        addViews();
        
        mdb = new MyDatabaseHelper(getContext());
        
        btnThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                them();
            }
        });
        return mView;
    }

    private void them() {
        String maKhoa = edtMaKhoa.getText().toString();
        String tenKhoa = edtTenKhoa.getText().toString().trim();
        String diaChi = edtDiaChi.getText().toString().trim();
        String sdt = edtSDT.getText().toString();

        SQLiteDatabase db = mdb.getWritableDatabase();
        
        int check = 1;
        
        if(maKhoa.equals("")){
            cbMaKhoa.setText("Vui lòng nhập mã khoa!");
            check = 0;
        }else {
            String query = "SELECT " + MyDatabaseHelper.getFacultyId() + " FROM " + MyDatabaseHelper.getFacultyTable() ;
            Cursor cursor = db.rawQuery(query, null);
            int chek1 = 1;
            if(cursor.getCount() != 0){
                while (cursor.moveToNext()){
                    if(maKhoa.equals(String.valueOf(cursor.getInt(0)))){
                        check = 0;
                        chek1 = 0;
                        cbMaKhoa.setText("Mã khoa đã tồn tại!");
                        break;
                    }
                }
            }
            if(chek1 == 1){
                cbMaKhoa.setText("");
            }
        }
        
        if(tenKhoa.equals("")){
            cbTenKhoa.setText("Vui lòng nhập tên khoa");
            check = 0;
        }else {
            String query = "SELECT " + MyDatabaseHelper.getFacultyName() + " FROM " + MyDatabaseHelper.getFacultyTable() ;
            Cursor cursor = db.rawQuery(query, null);
            int chek1 = 1;
            if(cursor.getCount() != 0){
                while (cursor.moveToNext()){
                    if(maKhoa.equals(cursor.getString(0))){
                        check = 0;
                        chek1 = 0;
                        cbTenKhoa.setText("Tên khoa đã tồn tại!");
                        break;
                    }
                }
            }
            if(chek1 == 1){
                cbTenKhoa.setText("");
            }
        }
        
        if(diaChi.equals("")){
            cbDiaChi.setText("Vui lòng nhập địa chỉ!");
            check = 0;
        }else {
            cbDiaChi.setText("");
        }
        
        if(sdt.equals("")){
            cbSDT.setText("Vui lòng nhập số điện thoại!");
            check = 0;
        }else {
            cbSDT.setText("");
        }
        
        if(check == 1){
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle("Thông báo");
            builder.setMessage("Bạn muốn thêm khoa có thông tin như sau: \n" +
                                "Mã khoa: " + maKhoa +
                                "\nTên khoa: " + tenKhoa +
                                "\nĐịa chi: " + diaChi +
                                "\nSố điện thoại: " + sdt);
            builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    ContentValues values = new ContentValues();
                    values.put(MyDatabaseHelper.getFacultyId(), Integer.parseInt(maKhoa));
                    values.put(MyDatabaseHelper.getFacultyName(), tenKhoa);
                    values.put(MyDatabaseHelper.getFacultyAddress(), diaChi);
                    values.put(MyDatabaseHelper.getFacultyPhoneNumber(), sdt);
                    long rs = db.insert(MyDatabaseHelper.getFacultyTable(), null, values);
                    if(rs == -1){
                        Toast.makeText(getContext(), "Add Faculty Failed!", Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(getContext(), "Successfully Added Faculty", Toast.LENGTH_SHORT).show();
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
        edtMaKhoa = mView.findViewById(R.id.edtFacultyMaKhoa);
        edtTenKhoa = mView.findViewById(R.id.edtFacultyTenKhoa);
        edtDiaChi = mView.findViewById(R.id.edtFacultyDiaChi);
        edtSDT = mView.findViewById(R.id.edtFacultySDT);

        cbMaKhoa = mView.findViewById(R.id.cbFacultyMaKhoa);
        cbTenKhoa = mView.findViewById(R.id.cbFacultyTenKhoa);
        cbDiaChi = mView.findViewById(R.id.cbFacultyDiaChi);
        cbSDT = mView.findViewById(R.id.cbFacultySDT);

        btnThem = mView.findViewById(R.id.btnFacultyThem);
    }
}