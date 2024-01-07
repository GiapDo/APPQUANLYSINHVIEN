package edu.hune.quanlysinhvien02.fragment;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import edu.hune.quanlysinhvien02.R;
import edu.hune.quanlysinhvien02.database.MyDatabaseHelper;
import edu.hune.quanlysinhvien02.model.ItemStudentList;
import edu.hune.quanlysinhvien02.model.Util;

public class StatisticalFragment extends Fragment {

    private View mView;
    private TextView tvTongSV, tvTongSVTuan, tvTongSVThang;

    private ImageButton btnDs;

    private SQLiteDatabase db;

    private List<ItemStudentList> mList;



    public StatisticalFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.fragment_statistical, container, false);
        db = (new MyDatabaseHelper(getContext())).getReadableDatabase();

        tvTongSV = mView.findViewById(R.id.tvTongSoSV);
        tvTongSVTuan = mView.findViewById(R.id.tvTongSoSVTuan);
        tvTongSVThang = mView.findViewById(R.id.tvTongSoSVThang);

        btnDs = mView.findViewById(R.id.taiDanhSachSinhVien);

        tvTongSV.setText(String.valueOf(tongSV()));
        tvTongSVTuan.setText(String.valueOf(tongSVTuan()));
        tvTongSVThang.setText(String.valueOf(tongSVThang()));

        layDuLieu();

        btnDs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exportDataToExcel(mList);
            }
        });

        return mView;
    }

    private void exportDataToExcel(List<ItemStudentList> listDanhSach) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String timestamp = sdf.format(new Date());
        String fileName = "danh_sach_sinh_vien_" + timestamp + ".xls";

        File downloadDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        // Tạo file Excel mới trong thư mục của ứng dụng
        File file = new File(downloadDirectory, fileName);
        try {
            file.createNewFile();
            FileOutputStream outputStream = new FileOutputStream(file);

            // Tạo workbook và sheet
            Workbook workbook = new HSSFWorkbook();
            Sheet sheet = workbook.createSheet("Danh sách sinh viên");

            // Đặt tiêu đề cho cột
            Row headerRow = sheet.createRow(0); // Hàng đầu tiên
            Cell cellHeaderSTT = headerRow.createCell(0); // Cột 0
            Cell cellHeaderMaSinhVien = headerRow.createCell(1); // Cột 1
            Cell cellHeaderTenSinhVien = headerRow.createCell(2); // Cột 2
            Cell cellHeaderGPA = headerRow.createCell(3); // Cột 3
            Cell cellHeaderLop = headerRow.createCell(4); // Cột 4
            Cell cellHeaderKhoa = headerRow.createCell(5); // Cột 5
            Cell cellHeaderEmail = headerRow.createCell(6); // Cột 6
            Cell cellHeaderSDT = headerRow.createCell(7); // Cột 7

            cellHeaderSTT.setCellValue("STT"); // Tiêu đề cột 0
            cellHeaderMaSinhVien.setCellValue("Mã Sinh Viên"); // Tiêu đề cột 1
            cellHeaderTenSinhVien.setCellValue("Tên Sinh Viên"); // Tiêu đề cột 2
            cellHeaderGPA.setCellValue("GPA"); // Tiêu đề cột 3
            cellHeaderLop.setCellValue("Lớp"); // Tiêu đề cột 4
            cellHeaderKhoa.setCellValue("Khoa"); // Tiêu đề cột 5
            cellHeaderEmail.setCellValue("Email"); // Tiêu đề cột 6
            cellHeaderSDT.setCellValue("SDT"); // Tiêu đề cột 7

            int rowNumber = 1; // Bắt đầu từ hàng thứ 2 (sau tiêu đề cột)
            int stt = 1; // Số thứ tự bắt đầu từ 1

            for (ItemStudentList tmp : listDanhSach) {
                // Tạo row mới trong sheet và thêm thông tin sinh viên
                Row row = sheet.createRow(rowNumber++);
                Cell cellSTT = row.createCell(0); // Cột 0
                Cell cellMaSinhVien = row.createCell(1); // Cột 1
                Cell cellTenSinhVien = row.createCell(2); // Cột 2
                Cell cellGPA = row.createCell(3); // Cột 3
                Cell cellLop = row.createCell(4); // Cột 4
                Cell cellKhoa = row.createCell(5); // Cột 5
                Cell cellEmail = row.createCell(6); // Cột 6
                Cell cellSDT = row.createCell(7); // Cột 7

                cellSTT.setCellValue(stt++); // Gán số thứ tự vào cell STT
                cellMaSinhVien.setCellValue(tmp.getMaSV());
                cellTenSinhVien.setCellValue(tmp.getTenSV());
                cellGPA.setCellValue(tmp.getDiem());
                cellLop.setCellValue(tmp.getLop());
                cellKhoa.setCellValue(tmp.getKhoa());
                cellEmail.setCellValue(tmp.getEmail());
                cellSDT.setCellValue(tmp.getSdt());
            }

            // Ghi workbook vào file
            workbook.write(outputStream);
            workbook.close();
            outputStream.close();

            Toast.makeText(getContext(), "Tải xuống thành công", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "Tải xuống thất bại", Toast.LENGTH_SHORT).show();

        }
    }

    private void layDuLieu() {
        mList = new ArrayList<>();
        Cursor cs = (new MyDatabaseHelper(getContext())).readItemListStudent();
        if(cs.getCount() != 0){
            while (cs.moveToNext()){
                ItemStudentList sinhVien = new ItemStudentList(String.valueOf(cs.getInt(0)), cs.getString(1), cs.getString(2),
                        cs.getString(3), cs.getString(4), cs.getString(5), getActivity());

                mList.add(sinhVien);
            }
        }
        cs.close();
    }

    private int tongSV(){
        Cursor c = null;
        int cnt = 0;

        String query = "SELECT COUNT(*) FROM " + MyDatabaseHelper.getBangSV();
        c = db.rawQuery(query, null);
        if(c != null && c.moveToNext()){
            cnt = c.getInt(0);
        }
        return cnt;
    }

    private int tongSVTuan(){
        LocalDateTime currentDateTime = LocalDateTime.now();
        LocalDateTime startOfWeek = currentDateTime.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)).with(LocalTime.MIN);
        LocalDateTime endOfWeek = currentDateTime.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY)).with(LocalTime.MAX);

        SQLiteDatabase db = (new MyDatabaseHelper(getContext())).getReadableDatabase();
        int cnt = 0;

        if(db != null){
            String query = "SELECT COUNT(*) FROM " +
                    MyDatabaseHelper.getHistoryTable() +
                    " WHERE " + MyDatabaseHelper.getHistoryType() + " = 5 AND " + MyDatabaseHelper.getHistoryTime() + " BETWEEN ? AND ?";

            String[] selectionArgs = {Util.toStringDateTime(startOfWeek), Util.toStringDateTime(endOfWeek)};

            Cursor c = db.rawQuery(query, selectionArgs);

            if(c.moveToFirst()){
                cnt = c.getInt(0);
            }

            c.close();
            db.close();
        }

        return cnt;
    }

    private int tongSVThang(){
        LocalDateTime currentDateTime = LocalDateTime.now();
        LocalDateTime startOfMonth = currentDateTime.with(TemporalAdjusters.firstDayOfMonth()).with(LocalTime.MIN);
        LocalDateTime endOfMonth = currentDateTime.with(TemporalAdjusters.lastDayOfMonth()).with(LocalTime.MAX);


        SQLiteDatabase db = (new MyDatabaseHelper(getContext())).getReadableDatabase();
        int cnt = 0;

        if(db != null){
            String query = "SELECT COUNT(*) FROM " +
                    MyDatabaseHelper.getHistoryTable() +
                    " WHERE " + MyDatabaseHelper.getHistoryType() + " = 5 AND " + MyDatabaseHelper.getHistoryTime() + " BETWEEN ? AND ?";

            String[] selectionArgs = {Util.toStringDateTime(startOfMonth), Util.toStringDateTime(endOfMonth)};

            Cursor c = db.rawQuery(query, selectionArgs);

            if(c.moveToFirst()){
                cnt = c.getInt(0);
            }
            c.close();
            db.close();
        }

        return cnt;
    }


}