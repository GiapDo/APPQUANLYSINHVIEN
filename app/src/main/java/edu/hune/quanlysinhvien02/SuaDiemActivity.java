package edu.hune.quanlysinhvien02;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.time.LocalDateTime;

import edu.hune.quanlysinhvien02.database.MyDatabaseHelper;
import edu.hune.quanlysinhvien02.fragment.StudentTranscriptFragment;
import edu.hune.quanlysinhvien02.model.ItemDiemFg;
import edu.hune.quanlysinhvien02.model.Util;

public class SuaDiemActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView tvMaSV, tvTenSV, tvMaLopTC, tvMaHP, tvTenHP, tvSoTC;
    private EditText edtDiemCC, edtDiemDK, edtDiemThi, edtGhiChu;
    private Button btnXacNhan;

    private TextView cbDiemCC, cbDiemDK, cbDiemThi;

    private String maSV, maLopTC, tenSV;

    private ItemDiemFg mDiem;

    private MyDatabaseHelper mdb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sua_diem);

        mdb = new MyDatabaseHelper(SuaDiemActivity.this);

        toolbar = findViewById(R.id.tbSuaDiem);
        tvMaSV = findViewById(R.id.tvSuaDiemMaSV);
        tvTenSV = findViewById(R.id.tvSuaDiemTenSV);
        tvMaLopTC = findViewById(R.id.tvSuaDiemMaLopTC);
        tvMaHP = findViewById(R.id.tvSuaDiemMaHP);
        tvTenHP = findViewById(R.id.tvSuaDiemTenHP);
        tvSoTC = findViewById(R.id.tvSuaDiemSoTC);
        edtDiemCC = findViewById(R.id.edtSuaDiemDiemCC);
        edtDiemDK = findViewById(R.id.edtSuaDiemDiemDK);
        edtDiemThi = findViewById(R.id.edtSuaDiemDiemThi);
        edtGhiChu = findViewById(R.id.edtSuaDiemGhiChu);
        cbDiemCC = findViewById(R.id.tvCBSuaDiemDiemCC);
        cbDiemDK = findViewById(R.id.tvCBSuaDiemDiemDK);
        cbDiemThi = findViewById(R.id.tvCBSuaDiemDiemThi);
        btnXacNhan = findViewById(R.id.btnSuaDiemXacNhan);

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Sửa điểm");
        actionBar.setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        maSV = intent.getStringExtra("maSV");
        maLopTC = intent.getStringExtra("maLopTC");


        docDuLieu();
        btnXacNhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                suaDiem();
            }
        });
    }

    private void suaDiem() {
        SQLiteDatabase db = mdb.getWritableDatabase();
        String diemCC = edtDiemCC.getText().toString();
        String diemDK = edtDiemDK.getText().toString();
        String diemThi = edtDiemThi.getText().toString();
        String ghichu = edtGhiChu.getText().toString().trim();

        int check = 1;

        if(Double.parseDouble(diemCC) > 10 || Double.parseDouble(diemCC) < 0){
            cbDiemCC.setText("Điểm chuyên cần nhập không hợp lệ!");
            check = 0;
        }else {
            cbDiemCC.setText("");
        }

        if(Double.parseDouble(diemDK) > 10 || Double.parseDouble(diemDK) < 0){
            cbDiemDK.setText("Điểm điều kiện nhập không hợp lệ!");
            check = 0;
        }else {
            cbDiemDK.setText("");
        }

        if(Double.parseDouble(diemThi) > 10 || Double.parseDouble(diemThi) < 0){
            cbDiemThi.setText("Điểm thi nhập không hợp lệ!");
            check = 0;
        }else {
            cbDiemThi.setText("");
        }

        if (check == 1) {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Thông báo!");
            builder.setMessage("Bạn muốn thay đổi: " +
                    "\n Điểm chuyên cần: " + diemCC +
                    "\nĐiểm điều kiện: " + diemDK +
                    "\nĐiểm thi: " + diemThi +
                    "\nGhi chú: " + ghichu);

            builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    db.beginTransaction();
                    try {
                        ContentValues values = new ContentValues();
                        if (diemCC.equals("")) {
                            values.putNull(MyDatabaseHelper.getScoresAttendance());
                        } else {
                            values.put(MyDatabaseHelper.getScoresAttendance(), Double.parseDouble(diemCC));
                        }

                        if (diemDK.equals("")) {
                            values.putNull(MyDatabaseHelper.getScoresConditional());
                        } else {
                            values.put(MyDatabaseHelper.getScoresConditional(), Double.parseDouble(diemDK));
                        }

                        if (diemThi.equals("")) {
                            values.putNull(MyDatabaseHelper.getScoresExam());
                        } else {
                            values.put(MyDatabaseHelper.getScoresExam(), Double.parseDouble(diemThi));
                        }

                        values.put(MyDatabaseHelper.getScoresNote(), ghichu);

                        db.update(MyDatabaseHelper.getBangDiem(), values,
                                MyDatabaseHelper.getMaSV() + " = ? AND " + MyDatabaseHelper.getScoresCreditClassId() + " = ? ",
                                new String[]{maSV, maLopTC});

                        db.setTransactionSuccessful();
                        Toast.makeText(SuaDiemActivity.this, "Update successful!", Toast.LENGTH_SHORT).show();

                        ContentValues values1 = new ContentValues();
                        values.put(MyDatabaseHelper.getHistoryName(), "Sửa thông tin sinh viên thành công");
                        values.put(MyDatabaseHelper.getHistoryTime(), Util.toStringDateTime(LocalDateTime.now()));
                        values.put(MyDatabaseHelper.getHistoryType(), 4);

                        db.insert(MyDatabaseHelper.getHistoryTable(), null, values);

                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(SuaDiemActivity.this, "Failed to update!", Toast.LENGTH_SHORT).show();
                    } finally {
                        db.endTransaction();
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

    private void docDuLieu() {
        SQLiteDatabase db = mdb.getWritableDatabase();

        Cursor c = mdb.readDiem2(maSV, maLopTC);
        if(c.getCount() != 0){
            while (c.moveToNext()){
                mDiem = new ItemDiemFg(String.valueOf(c.getInt(8)),c.getString(1), c.getString(0),
                        c.getString(3), c.getString(7), c.getInt(2),
                        !c.isNull(4) ? c.getDouble(4) : -1,
                        !c.isNull(5) ? c.getDouble(5) : -1,
                        !c.isNull(6) ? c.getDouble(6) : -1);
            }
        }
        c.close();

        String query = "SELECT " + MyDatabaseHelper.getTenSV() +
                " FROM " + MyDatabaseHelper.getBangSV() +
                " WHERE " + MyDatabaseHelper.getMaSV() + " = " + maSV;

        Cursor c1 = db.rawQuery(query, null);
        if(c1.getCount() != 0){
            while (c1.moveToNext()){
                tenSV = c1.getString(0);
            }
        }
        c1.close();



        tvMaSV.setText(mDiem.getMaSV());
        tvTenSV.setText(tenSV);
        tvMaLopTC.setText(mDiem.getMaLopTC());
        tvMaHP.setText(mDiem.getMaHp());
        tvTenHP.setText(mDiem.getTenHP());
        tvSoTC.setText(String.valueOf(mDiem.getSoTinChi()));

        if(mDiem.getDiemCC() == -1){
            edtDiemCC.setText("");
        }else {
            edtDiemCC.setText(String.valueOf(mDiem.getDiemCC()));
        }

        if(mDiem.getDiemDK() == -1){
            edtDiemDK.setText("");
        }else {
            edtDiemDK.setText(String.valueOf(mDiem.getDiemDK()));
        }

        if(mDiem.getDiemThi() == -1){
            edtDiemThi.setText("");
        }else {
            edtDiemThi.setText(String.valueOf(mDiem.getDiemThi()));
        }

        edtGhiChu.setText(mDiem.getGhiChu());

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId() == android.R.id.home){
            StudentTranscriptFragment fragment = new StudentTranscriptFragment();

            fragment.resetData();

            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        StudentTranscriptFragment fragment = new StudentTranscriptFragment();

        fragment.resetData();
        super.onBackPressed();
    }
}