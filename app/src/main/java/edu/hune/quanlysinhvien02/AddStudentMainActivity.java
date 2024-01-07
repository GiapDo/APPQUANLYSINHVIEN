package edu.hune.quanlysinhvien02;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.hune.quanlysinhvien02.database.MyDatabaseHelper;
import edu.hune.quanlysinhvien02.model.Util;

public class AddStudentMainActivity extends AppCompatActivity {

    private Toolbar tbar;

    private EditText edtMaSV, edtTenSV, edtNgaySinh, edtQueQuan, edtEmail, edtSDT;
    private RadioButton rdbNam, rdbNu;
    private Spinner spKhoa, spLop;
    private TextView tvMaSV, tvTenSV, tvGioiTinh, tvNgaySinh, tvQueQuan, tvEmail, tvSDT, tvKhoa, tvLop;

    private Button btnThem;

    private MyDatabaseHelper mdh;

    //lưu mã lớp, mã khoa lần đầu đổ dữ liệu
    private List<Khoa> mkhoa;
    private List<Lop> mlop;

    //kiểm tra xem có chọn ko
    private int posLop = 0, posKhoa;
    //    Lưu mã lớp , mã khoa tưng ứng sau khi chọn
    private List<Khoa> maKhoa;
    private List<Lop> maLop;

    private Lop layLop;

    private boolean isFirstSelection = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_student_main);
        mdh = new MyDatabaseHelper(AddStudentMainActivity.this);


        addViews();
        xuLyTooBar();
        layDuLieuKhoaLop();
        doDuLieuKhoa();
//        doDuLieuLop();


        spKhoa.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                posKhoa = position;

                if (isFirstSelection) {
                    isFirstSelection = false;
                    return;
                }

                if (position != 0) {
                    tvKhoa.setText("");
                    HashMap<String, String> map = null;
                    if (spKhoa.getAdapter().getCount() < mkhoa.size()) {
                        map = mdh.readLopTheoKhoa(String.valueOf(maKhoa.get(position).getMaKhoa()));
                    } else {
                        map = mdh.readLopTheoKhoa(String.valueOf(mkhoa.get(position).getMaKhoa()));
                    }
                    maLop = new ArrayList<>();
                    maLop.add(new Lop(0, ""));
                    if (map != null) {
                        for (Map.Entry<String, String> entry : map.entrySet()) {
                            maLop.add(new Lop(Integer.parseInt(entry.getKey()), entry.getValue()));
                        }
                    }
                    if (maLop.size() == 1) {
                        maLop.set(0, new Lop(0, "Khoa chưa có lớp học, vui lòng tạo lớp học mới!"));
                    }
                    ArrayAdapter<Lop> adapter = new ArrayAdapter<>(AddStudentMainActivity.this, android.R.layout.simple_list_item_1, maLop);
                    spLop.setAdapter(adapter);
                } else {
                    ArrayAdapter<Lop> adapter = new ArrayAdapter<>(AddStudentMainActivity.this, android.R.layout.simple_list_item_1, mlop);
                    spLop.setAdapter(adapter);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                posKhoa = 0;
            }
        });

        spLop.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                posLop = 0;

                if (isFirstSelection) {
                    isFirstSelection = false;
                    return;
                }

                if (!spKhoa.getSelectedItem().toString().equals("")) {
                    posLop = position;
                    if (position != 0) {
                        layLop = maLop.get(position);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                posLop = 0;
            }
        });


        btnThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                themSinhVien();
            }
        });
    }

    private void xuLyTooBar() {
        setSupportActionBar(tbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Thêm sinh viên");
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void themSinhVien() {
        Cursor cs = mdh.readMaSV();
        String maSV = edtMaSV.getText().toString().trim();
        String tenSV = edtTenSV.getText().toString().trim();
        String ns = edtNgaySinh.getText().toString();
        String queQuan = edtQueQuan.getText().toString().trim();
        String email = edtEmail.getText().toString().trim();
        String sdt = edtSDT.getText().toString();
        String date = "";
        int check = 0;


        if (maSV.equals("")) {
            check = 0;
            tvMaSV.setText("Nhập mã sinh viên!");
        } else {
            int check1 = 1;
            while (cs.moveToNext()) {
                if (maSV.equals(String.valueOf(cs.getInt(0)))) {
                    check1 = 0;
                    tvMaSV.setText("Mã sinh viên đã tồn tại");
                    check = 0;
                    break;
                }
            }
            if (check1 == 1) {
                check = 1;
                tvMaSV.setText("");
            }
        }

        if (tenSV.equals("")) {
            check = 0;
            tvTenSV.setText("Nhập tên sinh viên!");
        } else {
            check = 1;
            tvTenSV.setText("");
        }

        if (ns.equals("")) {
            check = 0;
            tvNgaySinh.setText("Nhập ngày sinh!");
        } else {
            int check1 = 0;
            try {
                date = Util.toStringDate(Util.toDate(ns));
                check1 = 1;
                tvNgaySinh.setText("");
            } catch (Exception e) {
                e.printStackTrace();
                tvNgaySinh.setText("Nhập ngày sinh không hợp lệ!");
                check1 = 0;
                check = 0;
            }

            if (check1 == 1) {
                check = 1;
                tvNgaySinh.setText("");
            }
        }

        if (queQuan.equals("")) {
            check = 0;
            tvQueQuan.setText("Nhập quê quán!");
        } else {
            check = 1;
            tvQueQuan.setText("");
        }

        if (email.equals("")) {
            check = 0;
            tvEmail.setText("Nhập Email!");
        } else {
            int check1 = 0;
            if (email.contains("@gmail.com") || email.contains("@hnue.edu.vn")) {
                tvEmail.setText("");
                check1 = 1;
            } else {
                check1 = 0;
                check = 0;
                tvEmail.setText("Nhập email không hợp lệ!");
            }
            if (check1 == 1) {
                check = 1;
                tvEmail.setText("");
            }

        }

        if (sdt.equals("")) {
            check = 0;
            tvSDT.setText("Nhập số điện thoại!");
        } else {
            check = 1;
            tvSDT.setText("");
        }

        if (!rdbNam.isChecked() && !rdbNu.isChecked()) {
            tvGioiTinh.setText("Vui lòng chọn giới tính!");
            check = 0;
        } else {
            check = 1;
            tvGioiTinh.setText("");
        }

        if (posKhoa == 0) {
            tvKhoa.setText("Vui lòng chọn khoa!");
            check = 0;
        } else {
            check = 1;
            tvKhoa.setText("");
        }

        if (posLop == 0) {
            tvLop.setText("Vui lòng chọn lớp!");
            check = 0;
        } else {
            check = 1;
            tvLop.setText("");
        }


        if (check == 1) {
            String gioiTinh = "";
            if (rdbNam.isChecked()) {
                gioiTinh = "Nam";
            } else {
                gioiTinh = "Nữ";
            }

            SQLiteDatabase db = mdh.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(MyDatabaseHelper.getMaSV(), Integer.parseInt(maSV));
            values.put(MyDatabaseHelper.getStudentClassId(), layLop.getMaLop());
            values.put(MyDatabaseHelper.getTenSV(), tenSV);
            values.put(MyDatabaseHelper.getStudentGender(), gioiTinh);
            values.put(MyDatabaseHelper.getStudentBirthday(), date);
            values.put(MyDatabaseHelper.getStudentNativeLand(), queQuan);
            values.put(MyDatabaseHelper.getStudentEmail(), email);
            values.put(MyDatabaseHelper.getStudentPhoneNumber(), sdt);

            long rs = db.insert(MyDatabaseHelper.getBangSV(), null, values);
            if (rs == -1) {
                Toast.makeText(this, "Add Students Failed!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Add successful Students!", Toast.LENGTH_SHORT).show();
                ContentValues values1 = new ContentValues();
                values1.put(MyDatabaseHelper.getHistoryName(), "Thêm mới thành công 1 sinh viên");
                values1.put(MyDatabaseHelper.getHistoryTime(), Util.toStringDateTime(LocalDateTime.now()));
                values1.put(MyDatabaseHelper.getHistoryType(), 5);
                values1.put(MyDatabaseHelper.getHistoryNote(), maSV);
                db.insert(MyDatabaseHelper.getHistoryTable(), null, values1);
            }
        }

    }

    private void doDuLieuLop() {
        ArrayAdapter<Lop> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, mlop);
        spLop.setAdapter(adapter);
    }

    private void doDuLieuKhoa() {
        ArrayAdapter<Khoa> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, mkhoa);
        spKhoa.setAdapter(adapter);

    }

    private void layDuLieuKhoaLop() {
        SQLiteDatabase db = mdh.getReadableDatabase();
        mkhoa = new ArrayList<>();
        mkhoa.add(new Khoa(0, ""));
        mlop = new ArrayList<>();
        mlop.add(new Lop(0, ""));

        String query = "SELECT " + MyDatabaseHelper.getClassId() + ", " + MyDatabaseHelper.getClassName() +
                " FROM " + MyDatabaseHelper.getClassTable();

        String query1 = "SELECT " + MyDatabaseHelper.getFacultyId() + ", " + MyDatabaseHelper.getFacultyName() +
                " FROM " + MyDatabaseHelper.getFacultyTable();

        Cursor cs = null;
        Cursor cs1 = null;
        if (db != null) {
            cs = db.rawQuery(query, null);
            cs1 = db.rawQuery(query1, null);
        }

        if (cs.getCount() != 0) {
            while (cs.moveToNext()) {
                Lop lop = new Lop(cs.getInt(0), cs.getString(1));
                mlop.add(lop);
            }
        }
        cs.close();

        if (cs1.getCount() != 0) {
            while (cs1.moveToNext()) {
                Khoa khoa = new Khoa(cs1.getInt(0), cs1.getString(1));
                mkhoa.add(khoa);
            }
        }
    }

    private void addViews() {
        tbar = findViewById(R.id.tbAddStudent);
        edtMaSV = findViewById(R.id.edtAddMSV);
        edtTenSV = findViewById(R.id.edtAddTenSV);
        edtNgaySinh = findViewById(R.id.edtAddNgaySinh);
        edtQueQuan = findViewById(R.id.edtAddQueQuan);
        edtEmail = findViewById(R.id.edtAddEmail);
        edtSDT = findViewById(R.id.edtAddPhone);
        rdbNam = findViewById(R.id.rdbAddNam);
        rdbNu = findViewById(R.id.rdbAddNu);
        spKhoa = findViewById(R.id.spAddKhoa);
        spLop = findViewById(R.id.spAddLop);
        tvMaSV = findViewById(R.id.cbAddMaSV);
        tvTenSV = findViewById(R.id.cbAddTenSV);
        tvGioiTinh = findViewById(R.id.cbAddGioiTinh);
        tvNgaySinh = findViewById(R.id.cbAddNgaySinh);
        tvQueQuan = findViewById(R.id.cbAddQueQuan);
        tvEmail = findViewById(R.id.cbAddEmail);
        tvSDT = findViewById(R.id.cbAddPhone);
        tvKhoa = findViewById(R.id.cbAddKhoa);
        tvLop = findViewById(R.id.cbAddLop);
        btnThem = findViewById(R.id.btnAddThem);
    }


    private class Khoa {
        private int maKhoa;
        private String tenKhoa;

        public Khoa(int maKhoa, String tenKhoa) {
            this.maKhoa = maKhoa;
            this.tenKhoa = tenKhoa;
        }

        public int getMaKhoa() {
            return maKhoa;
        }

        public void setMaKhoa(int maKhoa) {
            this.maKhoa = maKhoa;
        }

        public String getTenKhoa() {
            return tenKhoa;
        }

        public void setTenKhoa(String tenKhoa) {
            this.tenKhoa = tenKhoa;
        }

        @Override
        public String toString() {
            return tenKhoa;
        }
    }

    private class Lop {
        private int maLop;
        private String tenLop;

        public Lop(int maLop, String tenLop) {
            this.maLop = maLop;
            this.tenLop = tenLop;
        }

        public int getMaLop() {
            return maLop;
        }

        public void setMaLop(int maLop) {
            this.maLop = maLop;
        }

        public String getTenLop() {
            return tenLop;
        }

        public void setTenLop(String tenLop) {
            this.tenLop = tenLop;
        }

        @Override
        public String toString() {
            return tenLop;
        }
    }

}