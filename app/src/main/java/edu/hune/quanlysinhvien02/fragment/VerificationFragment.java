package edu.hune.quanlysinhvien02.fragment;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import edu.hune.quanlysinhvien02.R;
import edu.hune.quanlysinhvien02.database.MyDatabaseHelper;

public class VerificationFragment extends Fragment {

    private View mView;
    private EditText edt1, edt2, edt3, edt4, edt5, edt6;

    private TextView canhBao;
    private Button btnXacNhan;
    private MyDatabaseHelper mdb;
    private String emailXacMinh;
    public VerificationFragment() {

    }
    public VerificationFragment(String emailXacMinh) {
        this.emailXacMinh = emailXacMinh;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_verification, container, false);

        edt1 = mView.findViewById(R.id.maOTP1);
        edt2 = mView.findViewById(R.id.maOTP2);
        edt3 = mView.findViewById(R.id.maOTP3);
        edt4 = mView.findViewById(R.id.maOTP4);
        edt5 = mView.findViewById(R.id.maOTP5);
        edt6 = mView.findViewById(R.id.maOTP6);

        canhBao = mView.findViewById(R.id.tvFragmentCanhBaoOTP);
        btnXacNhan = mView.findViewById(R.id.btnFragmentXacNhanOTP);

        mdb = new MyDatabaseHelper(getContext());

        edt1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() == 1){
                    edt2.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        edt2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() == 1){
                    edt3.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        edt3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() == 1){
                    edt4.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        edt4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() == 1){
                    edt5.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        edt5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() == 1){
                    edt6.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        btnXacNhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StringBuilder s = new StringBuilder();
                s.append(edt1.getText().toString());
                s.append(edt2.getText().toString());
                s.append(edt3.getText().toString());
                s.append(edt4.getText().toString());
                s.append(edt5.getText().toString());
                s.append(edt6.getText().toString());
                String ma = s.toString();
                kiemTraMaThayDoiMatKhau(ma);
            }
        });

        return mView;
    }

    private void kiemTraMaThayDoiMatKhau(String ma) {
        int check = kiemTraOTP(ma);
        if(check == 1){
            FragmentTransaction transaction  = getParentFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, new ResetPasswordFragment(emailXacMinh));
            transaction.addToBackStack(null);
            transaction.commit();
        }
    }

    private int kiemTraOTP(String ma) {
        int check = 1;
        if(ma.length() < 6){
            canhBao.setText("Vui lòng nhập đầy đủ!");
            check = 0;
        }else {
            SQLiteDatabase db = mdb.getReadableDatabase();
            if(db != null){
                String query = "SELECT " + MyDatabaseHelper.getAccountVerification() +
                                " FROM " + MyDatabaseHelper.getAccountTable() +
                                " WHERE " + MyDatabaseHelper.getAccountEmail() + " = ?";

                Cursor c = db.rawQuery(query, new String[]{emailXacMinh});
                int cn = 0;
                if(c.getCount() != 0){
                    while (c.moveToNext()){
                        if(c.getString(0).equals(ma)){
                            canhBao.setText("");
                            cn = 1;
                        }
                    }
                }
                c.close();
                db.close();
                if(cn != 1){
                    check = 0;
                    canhBao.setText("Mã OTP Không chính xác!");
                }
            }
        }
        return check;
    }
}