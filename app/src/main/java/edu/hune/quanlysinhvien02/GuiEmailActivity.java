package edu.hune.quanlysinhvien02;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class GuiEmailActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private EditText edtEmail, edtTieuDe, edtNoiDung;
    private Button btnGui;

    private String mEmail;

    private String mTieuDe, mNoiDung;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gui_email);

        mToolbar = findViewById(R.id.emailToolBar);
        edtEmail = findViewById(R.id.edtAEmail1);
        edtTieuDe = findViewById(R.id.edtATieuDe1);
        edtNoiDung = findViewById(R.id.edtANoiDung1);
        btnGui = findViewById(R.id.btnAGui1);

        edtEmail.setKeyListener(null);
        xuLyToolBar();
        layEmail();
        btnGui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTieuDe = edtTieuDe.getText().toString().trim();
                mNoiDung = edtNoiDung.getText().toString().trim();

                guiEmail();
            }
        });
    }

    private void guiEmail() {
        try {
            String emailGui = "dophuong.nd2003@gmail.com";
            String emailNhan = mEmail;

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
            mimeMessage.setSubject(mTieuDe);
            mimeMessage.setText(mNoiDung);

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
            Toast.makeText(this, "Gửi email thành công", Toast.LENGTH_SHORT).show();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void layEmail() {
        Intent intent = getIntent();

        if (intent != null){
            mEmail = intent.getStringExtra("emailSV");
        }

        edtEmail.setText(mEmail);
    }

    private void xuLyToolBar() {
        setSupportActionBar(mToolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Gửi email");
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
}