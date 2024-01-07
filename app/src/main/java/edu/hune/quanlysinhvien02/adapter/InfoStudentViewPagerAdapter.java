package edu.hune.quanlysinhvien02.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import edu.hune.quanlysinhvien02.fragment.StudentInformationFragment;
import edu.hune.quanlysinhvien02.fragment.StudentTranscriptFragment;

public class InfoStudentViewPagerAdapter extends FragmentStatePagerAdapter {

    private String maSV;

    public InfoStudentViewPagerAdapter(@NonNull FragmentManager fm, int behavior, String maSV) {
        super(fm, behavior);
        this.maSV = maSV;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        if(position == 0){
            return StudentInformationFragment.newInstance(maSV);
        } else {
            return StudentTranscriptFragment.newInstance(maSV);
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        String title = "";
        if(position == 0){
            title = "Thông tin";
        } else if (position == 1) {
            title = "Bảng điểm";
        }
        return title;
    }
}
