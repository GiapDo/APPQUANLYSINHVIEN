package edu.hune.quanlysinhvien02.fragment;

import static androidx.core.content.PermissionChecker.checkSelfPermission;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import edu.hune.quanlysinhvien02.ManHinhChinhActivity;
import edu.hune.quanlysinhvien02.R;
import edu.hune.quanlysinhvien02.database.MyDatabaseHelper;


public class MyProfileFragment extends Fragment {

    public static final int PERMISSION_REQUEST_CODE = 1;
    public static final int PICK_IMAGE_REQUEST_CODE = 2;

    public static final int CAMERA_REQUEST_CODE = 3;
    public static final int CAMERA_PERMISSION_REQUEST_CODE = 4;

    public static ImageView anh;
    private EditText edtEmail, edtName;
    private Button btnCapNhat;

    private String mEmail;

    private View mView;

    private MyDatabaseHelper mDB;

    private ManHinhChinhActivity mActivity;
    public MyProfileFragment() {

    }

    public MyProfileFragment(String mEmail) {
        this.mEmail = mEmail;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_my_profile, container, false);

        anh = mView.findViewById(R.id.my_profile_image);
        edtEmail = mView.findViewById(R.id.my_profile_email);
        edtName = mView.findViewById(R.id.my_profile_name);
        btnCapNhat = mView.findViewById(R.id.my_profile_cap_nhat);
        mActivity = (ManHinhChinhActivity) getActivity();
        mDB = new MyDatabaseHelper(getContext());

        anh.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Thông báo!").setMessage("Bạn muốn thay đổi ảnh đại diện!");
                builder.setNegativeButton("Chọn ảnh", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        xuLyQuyenChonAnhTuThuVien();
                    }
                });
                
                builder.setPositiveButton("Chụp ảnh", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        xuLyQuyenChupAnh();
                    }
                });

                builder.setNeutralButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.create().show();

                return true;
            }
        });

        layDuLieu();

        layAnhTuSqlite();

        btnCapNhat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Thông báo!").setMessage("Bạn muốn cập nhật tài khoản");
                builder.setNegativeButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        capNhat();
                        mActivity.doDuLieu();
                    }
                });

                builder.setPositiveButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.create().show();

            }
        });
        return mView;
    }

    private void xuLyQuyenChupAnh() {
        if(ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED){
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            getActivity().startActivityForResult(intent, CAMERA_REQUEST_CODE);
        }else {
            requestPermissions(new String[]{Manifest.permission.CAMERA},CAMERA_PERMISSION_REQUEST_CODE);
        }
    }

    private void capNhat() {
        String email = edtEmail.getText().toString().trim();
        String name = edtName.getText().toString().trim();

        int check = checkCapNhat(email, name);
        if(check == 1){
            Toast.makeText(getContext(), "Tên tài khoản đã tồn tại" , Toast.LENGTH_SHORT).show();
        }

        if(check == 2){
            Toast.makeText(getContext(), "Email đã tồn tại", Toast.LENGTH_SHORT).show();
        }

        if(check == 0){

            //Lấy ảnh từ imageview
            Drawable drawable = anh.getDrawable();
            Bitmap imageBitmap = null;
            if (drawable instanceof BitmapDrawable) {
                imageBitmap = ((BitmapDrawable) drawable).getBitmap();
            }


            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
            byte[] imageData = byteArrayOutputStream.toByteArray();

            SQLiteDatabase db = mDB.getWritableDatabase();

            ManHinhChinhActivity.emailThayDoi = email;
            FunctionalFragment.mEmail = email;

            ContentValues values = new ContentValues();
            values.put(MyDatabaseHelper.getAccountEmail(), email);
            values.put(MyDatabaseHelper.getAccountName(), name);
            values.put(MyDatabaseHelper.getAccountImage(), imageData);
            if(db != null){
                long rs = db.update(MyDatabaseHelper.getAccountTable(), values, MyDatabaseHelper.getAccountEmail() + " = ?", new String[]{mEmail});
                if(rs == -1){
                    Toast.makeText(getContext(), "Cập nhật thất bại!", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(getContext(), "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                }
            }
            mEmail = email;

        }

    }

    private void layDuLieu() {
        SQLiteDatabase db = mDB.getReadableDatabase();

        String query = "SELECT " + MyDatabaseHelper.getAccountEmail() + ", " + MyDatabaseHelper.getAccountName() +
                       " FROM " + MyDatabaseHelper.getAccountTable() +
                        " WHERE " + MyDatabaseHelper.getAccountEmail() + " = \"" + mEmail + "\"";

        if(db != null) {
            Cursor c = db.rawQuery(query, null);
            if (c.getCount() != 0) {
                while (c.moveToNext()) {
                    edtEmail.setText(!c.isNull(0) ? c.getString(0) : "");
                    edtName.setText(!c.isNull(1) ? c.getString(1) : "");
                }
            }
            c.close();
            db.close();
        }
    }

    private int checkCapNhat(String email, String ten){
        SQLiteDatabase db = mDB.getReadableDatabase();

        String query = "SELECT " + MyDatabaseHelper.getAccountEmail() + ", " + MyDatabaseHelper.getAccountName() +
                " FROM " + MyDatabaseHelper.getAccountTable() +
                " WHERE " + MyDatabaseHelper.getAccountEmail() + " NOT LIKE '" + mEmail + "'";

        if(db != null){
            Cursor c = db.rawQuery(query, null);
            if (c.getCount() != 0) {
                while (c.moveToNext()) {
                    if(ten.equals(c.getString(1))){
                        return 1;
                    }

                    if (email.equals(c.getString(0))){
                        return 2;
                    }
                }
            }
            c.close();
            db.close();
        }
        return 0;
    }

    public void  layAnhTuSqlite() {
        byte []anh1 = getImageDataFromDatabase();
        if(anh1 != null){
            Bitmap factory = BitmapFactory.decodeByteArray(anh1, 0, anh1.length);
            anh.setImageBitmap(factory);
        }else{
            anh.setImageResource(R.drawable.avartar1);
            anh.setBackgroundColor(Color.parseColor("#a3c2c2"));
        }
    }

    private byte[] getImageDataFromDatabase(){
        byte[] anh1 = null;
        SQLiteDatabase db = mDB.getReadableDatabase();

        if(db != null){
            String query = "SELECT " + MyDatabaseHelper.getAccountImage() +
                           " FROM " + MyDatabaseHelper.getAccountTable() +
                           " WHERE " + MyDatabaseHelper.getAccountEmail() + " = \"" + mEmail + "\"";

            Cursor c = db.rawQuery(query, null);

            if(c.getCount() != 0){
                while (c.moveToNext()){
                    anh1 = c.getBlob(0);
                }
            }
            c.close();
            db.close();
        }
        return anh1;
    }

    private void xuLyQuyenChonAnhTuThuVien() {
//        if(ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            getActivity().startActivityForResult(intent, PICK_IMAGE_REQUEST_CODE);
//        }else {
//            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
//        }
    }

    @Override
    public void onResume() {
        super.onResume();
        ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        actionBar.setTitle("Thông tin tài khoản");
    }
}