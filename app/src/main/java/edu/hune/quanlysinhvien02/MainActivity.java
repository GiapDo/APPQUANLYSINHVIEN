package edu.hune.quanlysinhvien02;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import edu.hune.quanlysinhvien02.model.Util;

public class MainActivity extends AppCompatActivity {

    final String DATABASE_NAME = "StudentManagement.db";
    SQLiteDatabase database;

    private Button btnDangNhap, btnDangKy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        database = Util.initDatabase(this, DATABASE_NAME);

        btnDangNhap = findViewById(R.id.btnLogin01);
        btnDangKy = findViewById(R.id.btnSignup01);

        btnDangNhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        btnDangKy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SignupActivity.class);
                startActivity(intent);
            }
        });
    }
}