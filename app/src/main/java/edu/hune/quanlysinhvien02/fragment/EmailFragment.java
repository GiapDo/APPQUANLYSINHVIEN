package edu.hune.quanlysinhvien02.fragment;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.util.Properties;
import java.util.Random;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import edu.hune.quanlysinhvien02.R;
import edu.hune.quanlysinhvien02.database.MyDatabaseHelper;

public class EmailFragment extends Fragment {
    private View mView;
    private EditText edtEmail;
    private TextView tvCanhBao;
    private Button btnMa;

    private MyDatabaseHelper mdb;
    private String maOTP;

    public EmailFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_email, container, false);
        edtEmail = mView.findViewById(R.id.edtFragmentEmail);
        tvCanhBao = mView.findViewById(R.id.tvFragmentEmailCanhBao);
        btnMa = mView.findViewById(R.id.btnFragmentEmailMa);

        mdb = new MyDatabaseHelper(getContext());
        btnMa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                kiemEmailGuiMa();
            }
        });
        return mView;
    }

    private void kiemEmailGuiMa() {
        int check = kiemEmail();
        if(check == 1){
            layMa();

            //Gửi email

            sendEmail();

            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container,new VerificationFragment(edtEmail.getText().toString().trim()));
            transaction.addToBackStack(null);
            transaction.commit();
        }
    }

    private void sendEmail() {
        try {
            String emailGui = "dophuong.nd2003@gmail.com";
            String emailNhan = edtEmail.getText().toString().trim();

            String passGui = "onspvzmnkcewyvyn";
            String host = "smtp.gmail.com";

            Properties properties = new Properties();
            properties.put("mail.smtp.auth", "true");
            properties.put("mail.smtp.starttls.enable", "true");
            properties.put("mail.smtp.host", host);
            properties.put("mail.smtp.port", 587);

            Session session = Session.getInstance(properties, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(emailGui, passGui);
                }
            });

            MimeMessage mimeMessage = new MimeMessage(session);

            mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(emailNhan));
            mimeMessage.setFrom(new InternetAddress(emailGui, "APP QUẢN LÝ SINN VIÊN"));
            mimeMessage.setSubject("Mã xác thực");
            mimeMessage.setText(maOTP);

            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Transport.send(mimeMessage);
                    } catch (MessagingException e) {
                        e.printStackTrace();
                    }
                }
            });

            thread.start();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void layMa() {
        Random random = new Random();
        String number = "0123456789";
        StringBuilder ma = new StringBuilder(6);

        for (int i = 0; i < 6; i++){
            int index = random.nextInt(number.length());
            ma.append(index);
        }
        maOTP = ma.toString();
        
        SQLiteDatabase db = mdb.getWritableDatabase();
        if(db != null){
            ContentValues values = new ContentValues();
            values.put(MyDatabaseHelper.getAccountVerification(), maOTP);
            db.update(MyDatabaseHelper.getAccountTable(), values, MyDatabaseHelper.getAccountEmail() +" = ?", new String[]{edtEmail.getText().toString().trim()});
        }

    }

    private int kiemEmail() {
        int check = 1;
        String email = edtEmail.getText().toString().trim();
        if(email.equals("")){
            check = 0;
            tvCanhBao.setText("Vui lòng nhập email");
        }else {
            SQLiteDatabase db = mdb.getReadableDatabase();
            if (db != null) {
                String query = "SELECT " + MyDatabaseHelper.getAccountEmail() +
                        " FROM " + MyDatabaseHelper.getAccountTable();

                Cursor c = db.rawQuery(query, null);
                int check1 = 0;
                if (c.getCount() != 0) {
                    while (c.moveToNext()) {
                        if(email.equals(c.getString(0))){
                            check1 = 1;
                            tvCanhBao.setText("");
                            break;
                        }
                    }
                }
                c.close();
                db.close();
                if(check1 == 0){
                    tvCanhBao.setText("Tài khoản không tồn tại!");
                    check = 0;
                }
                
            }
        }
        return check;
    }
}