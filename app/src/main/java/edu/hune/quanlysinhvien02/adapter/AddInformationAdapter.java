package edu.hune.quanlysinhvien02.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import edu.hune.quanlysinhvien02.fragment.AddClassFragment;
import edu.hune.quanlysinhvien02.fragment.AddCreditClassFragment;
import edu.hune.quanlysinhvien02.fragment.AddFacultyFragment;
import edu.hune.quanlysinhvien02.fragment.AddSubjectsFragment;
import edu.hune.quanlysinhvien02.fragment.AddTrainingSystemFragment;
import edu.hune.quanlysinhvien02.fragment.MoreTrainingCoursesFragment;
import edu.hune.quanlysinhvien02.fragment.ThemSinhVienVaoLopTCFragment;

public class AddInformationAdapter extends FragmentStatePagerAdapter {
    public AddInformationAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        if(position == 0){
            return new MoreTrainingCoursesFragment();
        } else if (position == 1) {
            return new AddTrainingSystemFragment();
        } else if (position == 2) {
            return new AddFacultyFragment();
        } else if (position == 3) {
            return new AddClassFragment();
        } else if (position == 4) {
            return new AddCreditClassFragment();
        }else if(position == 5) {
            return new AddSubjectsFragment();
        } else if (position == 6) {
            return new ThemSinhVienVaoLopTCFragment();
        } else {
            return new MoreTrainingCoursesFragment();
        }
    }

    @Override
    public int getCount() {
        return 7;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        String title = "";
        if(position == 0){
            title = "Khóa đào tạo";
        }else if (position == 1) {
            title = "Hệ đào tạo";
        } else if (position == 2) {
            title = "Khoa";
        } else if (position == 3) {
            title = "Lớp";
        } else if (position == 4) {
            title = "Lớp tín chỉ";
        } else if (position == 5) {
            title = "Học phần";
        } else if (position == 6) {
            title = "Thêm sinh viên vào lớp tín chỉ";
        }
        return title;
    }
}
