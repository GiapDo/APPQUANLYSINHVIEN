package edu.hune.quanlysinhvien02;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.time.LocalDateTime;

import edu.hune.quanlysinhvien02.database.MyDatabaseHelper;
import edu.hune.quanlysinhvien02.model.Util;

public class LoginActivity extends AppCompatActivity {

    private EditText edtTen, edtMatKhau;
    private TextView tvCBten, tvCBMK, tvQuenMK;
    private Button btnDangNhap;
    private MyDatabaseHelper helper;
    private String Email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edtTen = findViewById(R.id.edtLoginAccount);
        edtMatKhau = findViewById(R.id.edtLoginPassword);
        tvCBten = findViewById(R.id.tvLoginCanhBaoTen);
        tvCBMK = findViewById(R.id.tvLoginCanhBaoPas);
        btnDangNhap = findViewById(R.id.btnLogin02);
        tvQuenMK = findViewById(R.id.tvLoginForgotPasss);
        helper = new MyDatabaseHelper(LoginActivity.this);

        btnDangNhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dangNhap();
            }
        });
        
        tvQuenMK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chuyen();
            }
        });

    }

    private void chuyen() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Thông báo");
        builder.setMessage("Bạn muốn khôi phục mật khẩu");
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
                startActivity(intent);
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

    private void dangNhap() {
        String ten = edtTen.getText().toString().trim();
        String mk = edtMatKhau.getText().toString();

        if(ten.equals("") || mk.equals("")){
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
        }else {
            Cursor cursor = helper.readAllAccount();
            int check = 1;
            if(cursor.getCount() == 0){
                Toast.makeText(this, "No data!", Toast.LENGTH_SHORT).show();
            } else{
                int res = checkIsLogin(cursor, ten, mk);
                if(res == 1){
                    tvCBten.setText("");
                    tvCBMK.setText("");
                } else if (res == 2) {
                    tvCBten.setText("");
                    tvCBMK.setText("Nhập sai mật khẩu");
                    check = 0;
                } else if (res == 3) {
                    tvCBten.setText("Không tồn tại tài khoản!");
                    tvCBMK.setText("");
                    check = 0;
                }
            }

            if(check == 1){
                SQLiteDatabase db = (new MyDatabaseHelper(this)).getWritableDatabase();

                if(db != null){
                    ContentValues values = new ContentValues();
                    values.put(MyDatabaseHelper.getHistoryName(), "Đăng nhập thành công");
                    values.put(MyDatabaseHelper.getHistoryTime(), Util.toStringDateTime(LocalDateTime.now()));
                    values.put(MyDatabaseHelper.getHistoryType(), 2);

                    db.insert(MyDatabaseHelper.getHistoryTable(), null, values);
                }

                Intent intent = new Intent(LoginActivity.this, ManHinhChinhActivity.class);
                intent.putExtra("email", Email);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finishAffinity();
            }
        }
    }

    private int checkIsLogin(Cursor c, String ten, String mk){
        while (c.moveToNext()){
            if(ten.equals(c.getString(1))) {
                if (mk.equals(c.getString(3))){
                    Email = c.getString(2);
                    return 1;
                } else{
                    return 2;
                }
            }
        }
        return 3;
    }


}