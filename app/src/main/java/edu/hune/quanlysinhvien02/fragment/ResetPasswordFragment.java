package edu.hune.quanlysinhvien02.fragment;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import edu.hune.quanlysinhvien02.ManHinhChinhActivity;
import edu.hune.quanlysinhvien02.R;
import edu.hune.quanlysinhvien02.database.MyDatabaseHelper;
import edu.hune.quanlysinhvien02.model.Util;

public class ResetPasswordFragment extends Fragment {

    private View mView;
    private String email;
    private EditText edtEmail, edtPass, edtRePass;
    private TextView tvPass, tvRePass;
    private Button btnXacNhan;

    public ResetPasswordFragment() {

    }

    public ResetPasswordFragment(String email) {
        this.email = email;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_reset_password, container, false);

        edtEmail = mView.findViewById(R.id.edtResetFragmentEmail);
        edtPass = mView.findViewById(R.id.edtResetFragmentPass);
        edtRePass = mView.findViewById(R.id.edtResetFragmentRePass);

        tvPass = mView.findViewById(R.id.tvCanhBaoResetPass);
        tvRePass = mView.findViewById(R.id.tvCanhBaoResetRePass);

        btnXacNhan = mView.findViewById(R.id.btnResetPassword);

        edtEmail.setText(email);

        btnXacNhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetPassword();
            }
        });
        return mView;
    }

    private void resetPassword() {
        String mk = edtPass.getText().toString();
        String mk1 = edtRePass.getText().toString();

        int check = kiemTra(mk, mk1);

        if(check == 1){
            doiMatKhau(mk);
        }
    }

    private void doiMatKhau(String mk) {
        SQLiteDatabase db = (new MyDatabaseHelper(getContext())).getWritableDatabase();
        if(db != null){
            ContentValues values = new ContentValues();
            values.put(MyDatabaseHelper.getAccountPassword(), mk);
            long rs = db.update(MyDatabaseHelper.getAccountTable(), values, MyDatabaseHelper.getAccountEmail() + " = ?", new String[]{email});
            if(rs == -1){
                Toast.makeText(getContext(), "Thất bại", Toast.LENGTH_SHORT).show();
            }else {
                Intent intent = new Intent(getActivity(), ManHinhChinhActivity.class);
                intent.putExtra("email", email);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                getActivity().finishAffinity();
            }
        }

    }

    private int kiemTra(String mk, String mk1) {
        int check = 1;
        if(mk.equals("")){
            tvPass.setText("Vui nhập mật khẩu mới");
            check = 0;
        }else {
            if(Util.checkMatKhau(mk) == true){
                tvPass.setText("");
                if(!mk.equals(mk1)){
                    tvRePass.setText("Mật khẩu nhập không khớp!");
                    check = 0;
                }else {
                    tvRePass.setText("");
                }
            }else {
                tvPass.setText("Mật khẩu phải ít nhất 8 kí tự, gồm chữ thường, hoa, số và kí tự đặc biệt!");
                check = 0;
            }
        }

        if(mk1.equals("")){
            check = 0;
            mk1.equals("Vui lòng nhập lại mật khẩu!");
        }

        return check;
    }
}