package edu.hune.quanlysinhvien02;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.time.LocalDateTime;

import edu.hune.quanlysinhvien02.database.MyDatabaseHelper;
import edu.hune.quanlysinhvien02.model.Account;
import edu.hune.quanlysinhvien02.model.History;
import edu.hune.quanlysinhvien02.model.Util;

public class SignupActivity extends AppCompatActivity {

    private EditText edtTen, edtEmail, edtMatKhau, edtMatKhau01;
    private TextView tvCBTen, tvCBEmail, tvCbMK, tvCbRMK;
    private Button btnDangKy;
    private MyDatabaseHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        edtTen = findViewById(R.id.edtSignupUser);
        edtEmail = findViewById(R.id.edtSignupEmail);
        edtMatKhau = findViewById(R.id.edtSignupPassword);
        edtMatKhau01 = findViewById(R.id.edtSignupRePassword);
        tvCBTen = findViewById(R.id.tvSignupCanhBaoTen);
        tvCBEmail = findViewById(R.id.tvSignupCanhBaoEmail);
        tvCbMK = findViewById(R.id.tvSignupCanhBaoMatKhau);
        tvCbRMK = findViewById(R.id.tvSignupCanhBaoMatKhau01);
        btnDangKy = findViewById(R.id.btnSignup02);
        helper = new MyDatabaseHelper(SignupActivity.this);

        btnDangKy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dangKy();
            }
        });
    }

    private void dangKy() {
        String ten = edtTen.getText().toString().trim();
        String email = edtEmail.getText().toString().trim();
        String mk = edtMatKhau.getText().toString();
        String mk01 = edtMatKhau01.getText().toString();
        Cursor cursor = helper.readAllAccount();


        if(ten.equals("") || email.equals("") || mk.equals("") || mk01.equals("")){
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
        } else {
            int check = 1;
            if(!email.contains("@gmail.com")){
                tvCBEmail.setText("Email nhập không hợp lệ!");
                check = 0;
            }else {
                tvCBEmail.setText("");
            }

            if(mk.length() < 8){
                tvCbMK.setText("Mật khẩu phải 8 kí tự trở lên!");
                check = 0;
            }else {
                if(Util.checkMatKhau(mk) == false){
                    tvCbMK.setText("Gồm kí tự chữ thường, hoa, kí tự số, kí tự đặc biệt!");
                    check = 0;
                }else {
                    tvCbMK.setText("");
                }
            }

            if(mk.equals(mk01)){
                tvCbRMK.setText("");
            }else {
                check = 0;
                tvCbRMK.setText("Mật khẩu không khớp!");
            }

            if(cursor.getCount() == 0){
                Toast.makeText(this, "NO DATA", Toast.LENGTH_SHORT).show();
            }else {
                while (cursor.moveToNext()){
                    if(ten.equals(cursor.getString(1))){
                        tvCBTen.setText("Tên tài khoản đã tồn tại!");
                        check = 0;
                    }

                    if(email.equals(cursor.getString(2))){
                        tvCBEmail.setText("Email đã được đăng ký!");
                        check = 0;
                    }
                }
                cursor.close();
            }


            if(check == 1){
                Account account = new Account(ten, email, mk);
                if(helper.addAccount(account) == -1){
                    Toast.makeText(this, "Add Account Failed!", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(this, "Successfully Added Account", Toast.LENGTH_SHORT).show();
                    tvCBTen.setText("");
                    tvCBEmail.setText("");
                    tvCbMK.setText("");
                    tvCbRMK.setText("");

                    SQLiteDatabase db = (new MyDatabaseHelper(this)).getWritableDatabase();

                    if(db != null){
                        ContentValues values = new ContentValues();
                        values.put(MyDatabaseHelper.getHistoryName(), "Tạo thành công một tài khoản mới");
                        values.put(MyDatabaseHelper.getHistoryTime(), Util.toStringDateTime(LocalDateTime.now()));
                        values.put(MyDatabaseHelper.getHistoryType(), 1);

                        db.insert(MyDatabaseHelper.getHistoryTable(), null, values);
                    }

                }
            }


        }
    }
}