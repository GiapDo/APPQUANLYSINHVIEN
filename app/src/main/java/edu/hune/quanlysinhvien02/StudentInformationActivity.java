package edu.hune.quanlysinhvien02;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;

import edu.hune.quanlysinhvien02.adapter.InfoStudentViewPagerAdapter;
import edu.hune.quanlysinhvien02.fragment.StudentTranscriptFragment;

public class StudentInformationActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private InfoStudentViewPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_information);

        tabLayout = findViewById(R.id.tbInfoStudent);
        viewPager = findViewById(R.id.vpgInfoStudent);

        Intent intent = getIntent();
        String maSV = intent.getStringExtra("maSV");

        adapter = new InfoStudentViewPagerAdapter(getSupportFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT, maSV);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);


    }

    @Override
    public void onBackPressed() {
        setResult(200, new Intent());
        super.onBackPressed();
    }

}