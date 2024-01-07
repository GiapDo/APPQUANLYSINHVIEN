package edu.hune.quanlysinhvien02.fragment;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.time.LocalDateTime;

import edu.hune.quanlysinhvien02.R;
import edu.hune.quanlysinhvien02.database.MyDatabaseHelper;
import edu.hune.quanlysinhvien02.model.Util;


public class StudentInformationFragment extends Fragment {

    private View mView;

    private EditText edtMaSV, edtTenSV, edtNgaySinh, edtQueQuan, edtEmail, edtSDT;
    private RadioButton rdbNam, rdbNu;
    private TextView tvLop, tvKhoa, tvCbMa, tvCbten, tvCbNs, tvCbGioiTinh, tvCbQueQuan, tvCbEmail, tvCbSDT;
    private Button btnModify;

    private MyDatabaseHelper mdb;

    private String maSV;
    private int maKhoa, maLop;

    public StudentInformationFragment() {

    }


    public static Fragment newInstance(String maSV) {
        StudentInformationFragment fragment = new StudentInformationFragment();
        Bundle args = new Bundle();
        args.putString("maSV", maSV);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_student_information, container, false);

        edtMaSV = mView.findViewById(R.id.edtFgInfoMaSV);
        edtTenSV = mView.findViewById(R.id.edtFgInfoTenSV);
        edtNgaySinh = mView.findViewById(R.id.edtFgInfoNgaySinh);
        edtQueQuan = mView.findViewById(R.id.edtFgInfoQueQuan);
        edtEmail = mView.findViewById(R.id.edtFgInfoEmail);
        edtSDT = mView.findViewById(R.id.edtFgInfoPhoneNumber);
        tvLop = mView.findViewById(R.id.tvFgInfoLop);
        tvKhoa = mView.findViewById(R.id.tvFgInfoKhoa);

        rdbNam = mView.findViewById(R.id.rdbFgInfoNam);
        rdbNu = mView.findViewById(R.id.rdbFgInfoNu);

        tvCbMa = mView.findViewById(R.id.tvFgCbMaSV);
        tvCbten = mView.findViewById(R.id.tvFgCbTenSV);
        tvCbNs = mView.findViewById(R.id.tvFgCbNgaySinh);
        tvCbGioiTinh = mView.findViewById(R.id.tvFgCbGioiTinh);
        tvCbQueQuan = mView.findViewById(R.id.tvFgCbQue);
        tvCbEmail = mView.findViewById(R.id.tvFgCbEmail);
        tvCbSDT = mView.findViewById(R.id.tvFgCbSDT);

        btnModify = mView.findViewById(R.id.btnFgInfoCapNhat);

        mdb = new MyDatabaseHelper(mView.getContext());

        Bundle bundle = getArguments();
        maSV = (String) bundle.get("maSV");

        layDuLieu();

        btnModify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                thayDoi();
            }
        });

        return mView;
    }

    private void thayDoi() {
        String ten = edtTenSV.getText().toString();
        String masv = edtMaSV.getText().toString();
        String ngaySinh = edtNgaySinh.getText().toString();
        String que = edtQueQuan.getText().toString();
        String email = edtEmail.getText().toString();
        String sdt = edtSDT.getText().toString();
        String date = "";

        int check = kiemTraRong(ten, masv, ngaySinh, que, email, sdt, date);

        if(check == 1){
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle("Thông Báo!");
            builder.setMessage("Bạn thực sự muốn thay đổi thông tin!");
            builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    SQLiteDatabase db = mdb.getWritableDatabase();

                    String gT = "";
                    if(rdbNam.isChecked()){
                        gT = "Nam";
                    }else {
                        gT = "Nữ";
                    }

                    ContentValues values = new ContentValues();
                    values.put(MyDatabaseHelper.getMaSV(), masv);
                    values.put(MyDatabaseHelper.getTenSV(), ten);
                    values.put(MyDatabaseHelper.getStudentGender(), gT);
                    values.put(MyDatabaseHelper.getStudentBirthday(), Util.toStringDate(Util.toDate(ngaySinh)));
                    values.put(MyDatabaseHelper.getStudentNativeLand(), que);
                    values.put(MyDatabaseHelper.getStudentEmail(), email);
                    values.put(MyDatabaseHelper.getStudentPhoneNumber(), sdt);

                    long rs = db.update(MyDatabaseHelper.getBangSV(), values, MyDatabaseHelper.getMaSV() + " = ?", new String[]{maSV});;

                    if(rs == -1) {
                        Toast.makeText(getContext(), "Failed To Updated!", Toast.LENGTH_SHORT).show();
                    }else {
                        ContentValues values1 = new ContentValues();
                        values1.put(MyDatabaseHelper.getHistoryName(), "Sửa thông tinh sinh viên thành công");
                        values1.put(MyDatabaseHelper.getHistoryTime(), Util.toStringDateTime(LocalDateTime.now()));
                        values1.put(MyDatabaseHelper.getHistoryType(), 7);

                        db.insert(MyDatabaseHelper.getHistoryTable(), null, values1);
                        Toast.makeText(getContext(), "Successfully Updated!", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });

            builder.create().show();
        }


    }

    private int kiemTraRong(String ten, String masv, String ngaySinh, String que, String email, String sdt, String date) {
        int check = 1;

        if (ten.equals("")) {
            tvCbten.setText("Không được để trống!");
            check = 0;
        } else {
            tvCbten.setText("");
        }

        if (masv.equals("")) {
            tvCbMa.setText("Không được để trống!");
            check = 0;
        } else {
            int ck = 1;
            if (!maSV.equals(masv)) {
                Cursor c = mdb.readMaSV();
                if (c.getCount() != 0) {
                    while (c.moveToNext()) {
                        if (String.valueOf(c.getInt(0)).equals(masv)) {
                            check = 0;
                            ck = 0;
                            tvCbMa.setText("Mã sinh viên đã tồn tại!");
                            break;
                        }
                    }
                }
                c.close();
            }

            if (ck == 1) {
                tvCbMa.setText("");
            }
        }

        if (ngaySinh.equals("")) {
            tvCbNs.setText("Không được để trống!");
            check = 0;
        } else {
            try {
                date = Util.toStringDate(Util.toDate(ngaySinh));
                tvCbNs.setText("");
            } catch (Exception e) {
                e.printStackTrace();
                tvCbNs.setText("Ngày sinh không hợp lệ");
                check = 0;
            }
        }

        if (!rdbNam.isChecked() && !rdbNu.isChecked()) {
            tvCbGioiTinh.setText("Vui lòng chọn!");
            check = 0;
        } else {
            tvCbGioiTinh.setText("");
        }

        if (que.equals("")) {
            tvCbQueQuan.setText("Không được để trống");
            check = 0;
        } else {
            tvCbQueQuan.setText("");
        }

        if (email.equals("")) {
            tvCbEmail.setText("Không được để trống");
            check = 0;
        } else {
            if (email.contains("@gmail.com") || email.contains("@hnue.edu.vn")) {
                tvCbEmail.setText("");
            } else {
                check = 0;
                tvCbEmail.setText("Email nhập không hợp lệ!");
            }
        }

        if (sdt.equals("")) {
            tvCbSDT.setText("Không được để trống");
            check = 0;
        } else {
            tvCbSDT.setText("");
        }

        return check;
    }

    public void layDuLieu(){
        Cursor c = mdb.readStudent(maSV);

        if(c.getCount() != 0){
            while (c.moveToNext()){
                edtMaSV.setText(String.valueOf(c.getInt(0)));
                edtTenSV.setText(c.getString(2));
                edtNgaySinh.setText(c.getString(4));
                edtQueQuan.setText(c.getString(5));
                edtEmail.setText(c.getString(6));
                edtSDT.setText(c.getString(7));
                tvLop.setText(c.getString(8));
                tvKhoa.setText(c.getString(9));

                if(c.getString(3).equals("Nam")){
                    rdbNam.setChecked(true);
                }else {
                    rdbNu.setChecked(true);
                }

                maLop = c.getInt(1);
                maKhoa = c.getInt(10);

            }
        }
    }


}