package edu.hune.quanlysinhvien02.fragment;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
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

public class AddTrainingSystemFragment extends Fragment {

    private View mView;

    private EditText edtId, edtName, edtTinChi, edtNote, edtDate;

    private TextView tvId, tvName, tvTinChi, tvNote, tvDate;

    private Button btnThem;

    public AddTrainingSystemFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_add_training_system, container, false);

        addViews();

        btnThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                themMoi();
            }
        });

        return mView;
    }

    private void themMoi() {


        String id = edtId.getText().toString().trim();
        String name = edtName.getText().toString().trim();
        String tinChi = edtTinChi.getText().toString();
        String note = edtNote.getText().toString().trim();
        String thoiGian = edtDate.getText().toString();

        int check = 1;

        if(id.equals("")){
            check = 0;
            tvId.setText("Vui lòng nhập thông tin!");
        }else {
            if(isMaChuongTrinhExists(id)){
                tvId.setText("Mã chương trình đã tồn tại!");
            }else {
                tvId.setText("");
            }
        }

        if(name.equals("")){
            check = 0;
            tvName.setText("Vui lòng nhập thông tin!");
        }else {
            if(isTenChuongTrinhExists(name)){
                tvName.setText("Tên chương trình đã tồn tại");
            }else {
                tvName.setText("");
            }
        }

        if(tinChi.equals("")){
            check = 0;
            tvTinChi.setText("Vui lòng nhập thông tin!");
        }else {
            tvTinChi.setText("");
        }

        if (note.equals("")){
            check = 0;
            tvNote.setText("Vui lòng nhập thông tin!");
        }else {
            tvNote.setText("");
        }

        if(thoiGian.equals("")){
            check = 0;
            tvDate.setText("Vui lòng nhập thông tin!");
        }else {
            tvDate.setText("");
        }

        if(check == 1){

            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle("Thông báo");
            builder.setMessage("Bạn muốn thêm một hệ đào tạo mới có thông tin: \nMã chương trình: " +
                                id + "\nTên chương trình: " +  name + "\nSố tín chỉ: " + tinChi + "\nMô tả: " + note + "\nThời gian: " + thoiGian + " năm");
            builder.setNegativeButton("YES", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    SQLiteDatabase db = (new MyDatabaseHelper(getContext())).getWritableDatabase();
                    ContentValues values = new ContentValues();

                    values.put(MyDatabaseHelper.getTrainingSystemId(), id);
                    values.put(MyDatabaseHelper.getTrainingSystemName(), name);
                    values.put(MyDatabaseHelper.getTrainingSystemCredit(), Integer.parseInt(tinChi));
                    values.put(MyDatabaseHelper.getTrainingSystemDescribe(), note);
                    values.put(MyDatabaseHelper.getTrainingSystemTime(), thoiGian + " năm");

                    long rs = db.insert(MyDatabaseHelper.getTrainingSystemTable(), null, values);

                    if(rs == -1){
                        Toast.makeText(getContext(), "Thêm thất bại", Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(getContext(), "Thêm thành công", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            builder.setPositiveButton("NO", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });

            builder.create().show();
        }


    }

    public boolean isMaChuongTrinhExists(String maChuongTrinh) {
        SQLiteDatabase db = (new MyDatabaseHelper(getContext())).getReadableDatabase();
        String query = "SELECT * FROM " + MyDatabaseHelper.getTrainingSystemTable() + " WHERE " + MyDatabaseHelper.getTrainingSystemName() + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{maChuongTrinh});
        boolean exists = cursor.moveToFirst();
        cursor.close();
        db.close();
        return exists;
    }

    public boolean isTenChuongTrinhExists(String tenChuongTrinh) {
        SQLiteDatabase db = (new MyDatabaseHelper(getContext())).getReadableDatabase();
        String query = "SELECT * FROM " + MyDatabaseHelper.getTrainingSystemTable() + " WHERE " + MyDatabaseHelper.getTrainingSystemName() + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{tenChuongTrinh});
        boolean exists = cursor.moveToFirst();
        cursor.close();
        db.close();
        return exists;
    }

    private void addViews() {

        edtId = mView.findViewById(R.id.edtSystemTId);
        edtName = mView.findViewById(R.id.edtSystemTName);
        edtTinChi = mView.findViewById(R.id.edtSystemTTinChi);
        edtNote = mView.findViewById(R.id.edtSystemTMoTa);
        edtDate = mView.findViewById(R.id.edtSystemTDate);

        tvId = mView.findViewById(R.id.cbSystemTId);
        tvName = mView.findViewById(R.id.cbSystemTName);
        tvNote = mView.findViewById(R.id.cbSystemTMoTa);
        tvTinChi = mView.findViewById(R.id.cbSystemTTinChi);
        tvDate = mView.findViewById(R.id.cbSystemTDate);

        btnThem = mView.findViewById(R.id.btnSystemTThem);
    }


}