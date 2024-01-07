package edu.hune.quanlysinhvien02;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import edu.hune.quanlysinhvien02.database.MyDatabaseHelper;
import edu.hune.quanlysinhvien02.fragment.ChangePasswordFragment;
import edu.hune.quanlysinhvien02.fragment.FunctionalFragment;
import edu.hune.quanlysinhvien02.fragment.HistoryFragment;
import edu.hune.quanlysinhvien02.fragment.HomeFragment;
import edu.hune.quanlysinhvien02.fragment.MyProfileFragment;
import edu.hune.quanlysinhvien02.fragment.StatisticalFragment;

public class ManHinhChinhActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout mDrawer;
    private Toolbar mToolbar;
    private NavigationView mNavView;
    private MyDatabaseHelper mDB;

    private ImageView anh;
    private TextView email, ten;
    public static String emailThayDoi;

    private static final int FRAGMENT_HOME = 0;
    private static final int FRAGMENT_MY_PROFILE = 1;
    private static final int FRAGMENT_CHANGE_PASSWORD = 2;
    private static final int FRAGMENT_HISTORY = 3;

    private static final int FRAGMENT_STATISTICAL = 4;


    private int mCheckFragment = FRAGMENT_HOME;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_man_hinh_chinh);

        mDrawer = findViewById(R.id.drawer_layout);
        mToolbar = findViewById(R.id.toolbarChinh);
        mNavView = findViewById(R.id.navigation_view);

        anh = mNavView.getHeaderView(0).findViewById(R.id.nav_header_image);
        email = mNavView.getHeaderView(0).findViewById(R.id.nav_header_email);
        ten = mNavView.getHeaderView(0).findViewById(R.id.nav_header_name);

        mDB = new MyDatabaseHelper(this);
        xuLyToolBar();


        Intent intent = getIntent();
        emailThayDoi = intent.getStringExtra("email");

        mNavView.setNavigationItemSelectedListener(this);
        replaceFragment(new FunctionalFragment(emailThayDoi));
        mNavView.getMenu().findItem(R.id.menu_nav_home).setChecked(true);

        doDuLieu();
    }

    public void doDuLieu() {
        SQLiteDatabase db = mDB.getReadableDatabase();
        String query = "SELECT " + MyDatabaseHelper.getAccountImage() + ", " + MyDatabaseHelper.getAccountName() +
                " FROM " + MyDatabaseHelper.getAccountTable() +
                " WHERE " + MyDatabaseHelper.getAccountEmail() + " = \"" + emailThayDoi + "\"";
        byte []anh1 = null;
        String ten1 = "";
        if(db != null){
            Cursor c = db.rawQuery(query, null);
            if(c.getCount() != 0){
                while (c.moveToNext()){
                    anh1 = c.getBlob(0);
                    ten1 = c.getString(1);
                }
            }
            c.close();
            db.close();
        }
        if(anh1 == null){
            anh.setImageResource(R.drawable.avartar1);
            anh.setBackgroundColor(Color.parseColor("#a3c2c2"));
        }else {
            Bitmap factory = BitmapFactory.decodeByteArray(anh1, 0, anh1.length);
            anh.setImageBitmap(factory);
        }
        email.setText(emailThayDoi);
        ten.setText(ten1);
    }

    private void xuLyToolBar() {
        setSupportActionBar(mToolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Quản lý sinh viên");

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mDrawer, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawer.addDrawerListener(toggle);
        toggle.syncState();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        
        if(id == R.id.menu_nav_home){
            if(mCheckFragment != FRAGMENT_HOME){
                replaceFragment(new FunctionalFragment(emailThayDoi));
                mCheckFragment = FRAGMENT_HOME;
            }
        } else if (id == R.id.menu_nav_my_profile) {
            if(mCheckFragment != FRAGMENT_MY_PROFILE){
                replaceFragment(new MyProfileFragment(emailThayDoi));
                mCheckFragment = FRAGMENT_MY_PROFILE;
            }
        }else if (id == R.id.menu_nav_change_password) {
            if(mCheckFragment != FRAGMENT_CHANGE_PASSWORD){
                replaceFragment(new ChangePasswordFragment(emailThayDoi));
                mCheckFragment = FRAGMENT_CHANGE_PASSWORD;
            }
        }else if (id == R.id.menu_nav_history) {
            if(mCheckFragment != FRAGMENT_HISTORY){
                replaceFragment(new HistoryFragment());
                mCheckFragment = FRAGMENT_HISTORY;
            }
        }else if (id == R.id.menu_nav_log_out) {
            ActionBar actionBar = getSupportActionBar();
            actionBar.setTitle("Quản lý sinh viên");
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Thông báo!").setMessage("Bạn muốn đăng xuất tài khoản!");
            builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(ManHinhChinhActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finishAffinity();
                }
            });

            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });

            builder.create().show();
        } else if (id == R.id.menu_nav_statistical) {
            if(mCheckFragment != FRAGMENT_STATISTICAL){
                replaceFragment(new StatisticalFragment());
                mCheckFragment = FRAGMENT_STATISTICAL;
                ActionBar actionBar = getSupportActionBar();
                actionBar.setTitle("Thống kê");
            }
        }

        mDrawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void replaceFragment(Fragment fragment){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.context_frame, fragment);
        transaction.commit();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

//        if(requestCode == MyProfileFragment.PERMISSION_REQUEST_CODE){
//            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
//                //Quyền đã được cấp và cho phép truy cập vào thư viện
//                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                startActivityForResult(intent, MyProfileFragment.PICK_IMAGE_REQUEST_CODE);
//            }else{
//                Toast.makeText(this, "Vui lòng cấp quyền truy cập vào thư viện hệ thống! ", Toast.LENGTH_SHORT).show();
//            }
//        }

        if(requestCode == MyProfileFragment.CAMERA_PERMISSION_REQUEST_CODE){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, MyProfileFragment.CAMERA_REQUEST_CODE);
            }else {
                Toast.makeText(this, "Vui lòng cấp quyền truy cập vào CAMERA! ", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == MyProfileFragment.PICK_IMAGE_REQUEST_CODE && resultCode == RESULT_OK && data != null){
            Uri uri = data.getData();
            MyProfileFragment.anh.setImageURI(uri);
        }

        if(requestCode == MyProfileFragment.CAMERA_REQUEST_CODE && resultCode == RESULT_OK && data != null){
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            MyProfileFragment.anh.setImageBitmap(bitmap);
        }
    }


    @Override
    public void onBackPressed() {
        if(mDrawer.isDrawerOpen(GravityCompat.START)){
            mDrawer.closeDrawer(GravityCompat.START);
        }else {
            super.onBackPressed();
            finish();
        }
    }

}