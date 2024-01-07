package edu.hune.quanlysinhvien02.fragment;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
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

public class ChangePasswordFragment extends Fragment {
    private View mView;
    private String mEmail;

    private EditText edt1, edt2, edt3;
    private TextView tv1, tv2, tv3;
    private Button btnXAcNhan;

    public ChangePasswordFragment() {

    }

    public ChangePasswordFragment(String mEmail) {
        this.mEmail = mEmail;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_change_password, container, false);

        edt1 = mView.findViewById(R.id.edt_change_password_1);
        edt2 = mView.findViewById(R.id.edt_change_password_2);
        edt3 = mView.findViewById(R.id.edt_change_password_3);

        tv1 = mView.findViewById(R.id.tv_change_Pas_1);
        tv2 = mView.findViewById(R.id.tv_change_Pas_2);
        tv3 = mView.findViewById(R.id.tv_change_Pas_3);

        btnXAcNhan = mView.findViewById(R.id.btn_change_Pas);

        btnXAcNhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String oldPas = edt1.getText().toString();
                String newPas = edt2.getText().toString();
                String rePas = edt3.getText().toString();
                int check = kiemTra(oldPas, newPas, rePas);

                if(check == 1){
                    doiMatKhau(newPas);
                }
            }
        });

        return mView;
    }

    private void doiMatKhau(String newPas) {
        SQLiteDatabase db = (new MyDatabaseHelper(getContext())).getWritableDatabase();

        if(db != null) {
            ContentValues values = new ContentValues();
            values.put(MyDatabaseHelper.getAccountPassword(), newPas);
            long rs = db.update(MyDatabaseHelper.getAccountTable(), values, MyDatabaseHelper.getAccountEmail() + " = ?", new String[]{mEmail});

            if (rs == -1) {
                Toast.makeText(getContext(), "Thay đổi mật khẩu thất bại!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "Thay đổi mật khẩu thành công!", Toast.LENGTH_SHORT).show();
            }
        }

    }

    private int kiemTra(String oldPas, String newPas, String rePas) {
        int check = 1;
        String pas = "";

        SQLiteDatabase db = (new MyDatabaseHelper(getContext())).getReadableDatabase();

        if(db != null){
            String query = "SELECT " + MyDatabaseHelper.getAccountPassword() +
                           " FROM " + MyDatabaseHelper.getAccountTable() +
                           " WHERE " + MyDatabaseHelper.getAccountEmail() + " = \"" + mEmail + "\"";

            Cursor c = db.rawQuery(query, null);

            if(c.moveToFirst()){
                pas = c.getString(0);
            }

            c.close();
            db.close();
        }

        if(oldPas.equals("")){
            check = 0;
            tv1.setText("Không được để trống!");
        }else {
            if(!pas.equals(oldPas)){
                tv1.setText("Mật khẩu nhập sai vui lòng nhập lại!");
                check = 0;
            }else {
                tv1.setText("");
            }
        }

        if(newPas.equals("")){
            check = 0;
            tv2.setText("Vui lòng nhập đầy đủ");
        }else {
            if(newPas.length() < 8){
                check = 0;
                tv2.setText("Mật khẩu phải từ 8 kí tự trở lên");
            }else {
                if(Util.checkMatKhau(newPas)){
                    tv2.setText("");
                }else {
                    check = 0;
                    tv2.setText(" Mật khẩu phải gồm kí tự chữ thường, chữ hoa, kí tự số, và kí tự đặc biệt! ");
                }
            }
        }

        if(rePas.equals("")){
            check = 0;
            tv3.setText("Vui lòng nhập đầy đủ");
        }else {
            if(newPas.equals(rePas)){
                tv3.setText("");
            }else {
                check = 0;
                tv3.setText("Mật khẩu không trùng khớp!");
            }
        }

        return check;
    }


    @Override
    public void onResume() {
        super.onResume();
        ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        actionBar.setTitle("Đổi mật khẩu");
    }
}