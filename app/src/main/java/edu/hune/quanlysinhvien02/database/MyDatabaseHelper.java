package edu.hune.quanlysinhvien02.database;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.hune.quanlysinhvien02.adapter.ItemStudentListAdapter;
import edu.hune.quanlysinhvien02.model.Account;
import edu.hune.quanlysinhvien02.model.ItemDiemFg;
import edu.hune.quanlysinhvien02.model.ItemStudentList;

public class MyDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "StudentManagement.db";
    private static final int DATABASE_VERSION = 1;
    private Context context;

    //Bảng ACCOUNT
    private static final String ACCOUNT_TABLE = "Account";
    private static final String ACCOUNT_ID = "AccountID";
    private static final String ACCOUNT_NAME = "Username";
    private static final String ACCOUNT_EMAIL = "Email";
    private static final String ACCOUNT_PASSWORD = "Password";
    private static final String ACCOUNT_IMAGE = "Image";
    private static final String ACCOUNT_VERIFICATION =  "Verification";
    //Bảng khoa
    private static final String FACULTY_TABLE = "KHOA";
    private static final String FACULTY_ID = "MaKhoa";
    private static final String FACULTY_NAME = "TenKhoa";
    private static final String FACULTY_ADDRESS = "DiaChi";
    private static final String FACULTY_PHONE_NUMBER = "SDT";

    //Bảng khóa đào tạo
    private static final String TRAINING_COURSE_TABLE = "KHOA_DAO_TAO";
    private static final String TRAINING_COURSE_ID = "MaKhoaDT";
    private static final String TRAINING_COURSE_NAME = "TenKhoaDT";
    private static final String TRAINING_COURSE_START_DAY = "NgayBatDau";
    private static final String TRAINING_COURSE_END_DAY = "NgayKetThuc";

    //Bảng hệ đào tạo
    private static final String TRAINING_SYSTEM_TABLE = "HE_DAO_TAO";
    private static final String TRAINING_SYSTEM_ID = "MaChuongTrinh";
    private static final String TRAINING_SYSTEM_NAME = "TenChuongTrinh";
    private static final String TRAINING_SYSTEM_CREDIT = "SoTinChi";
    private static final String TRAINING_SYSTEM_DESCRIBE = "MoTa";
    private static final String TRAINING_SYSTEM_TIME = "ThoiGianHocTap";

    //Bảng lớp
    private static final String CLASS_TABLE = "LOP";
    private static final String CLASS_ID = "MaLop";
    private static final String CLASS_FACULTY_ID = "MaKhoa";
    private static final String CLASS_FACULTY_TRAINING_ID = "MaKhoaDT";
    private static final String CLASS_NAME = "TenLop";
    private static final String CLASS_TRAINING_SYSTEM_ID = "MaChuongTrinh";

    //Bảng Sinh viên
    private static final String STUDENT_TABLE = "SINH_VIEN";
    private static final String STUDENT_CLASS_ID = "MaLop";
    private static final String STUDENT_ID = "MaSV";
    private static final String STUDENT_NAME = "TenSV";
    private static final String STUDENT_GENDER = "GioiTinh";
    private static final String STUDENT_BIRTHDAY = "NgaySinh";
    private static final String STUDENT_NATIVE_LAND = "QueQuan";
    private static final String STUDENT_EMAIL = "Email";
    private static final String STUDENT_PHONE_NUMBER = "SDT";

    //Bảng môn học
    private static final String SUBJECT_TABLE = "MON_HOC";
    private static final String SUBJECT_ID = "MaMon";
    private static final String SUBJECT_FACULTY_ID = "MaKhoa";
    private static final String SUBJECT_NAME = "TenMon";
    private static final String SUBJECT_CREDIT ="SoTinChi";
    private static final String SUBJECT_TRAINING_SYSTEM_ID = "MaChuongTrinh";

    //Bảng lớp tín chỉ
    private static final String CREDIT_CLASS_TABLE = "LOP_TIN_CHI";
    private static final String CREDIT_CLASS_ID = "MaLopTC";
    private static final String CREDIT_CLASS_SUBJECT_ID = "MaMon";
    private static final String CREDIT_CLASS_TRAINING_ID = "MaKhoaDT";
    private static final String CREDIT_CLASS_MAX_NBS = "SoLuongSV_Max";
    private static final String CREDIT_CLASS_CLASSROOM = "PhongHoc";
    private static final String CREDIT_CLASS_SCHOOL_YEAR = "NamHoc";
    private static final String CREDIT_CLASS_SEMESTER = "HocKy";

    //Bảng điểm
    private static final String SCORES_TABLE = "DIEM";
    private static final String SCORES_STUDENT_ID = "MaSV";
    private static final String SCORES_CREDIT_CLASS_ID = "MaLopTC";
    private static final String SCORES_ATTENDANCE = "DiemCC";
    private static final String SCORES_CONDITIONAL = "DiemDK";
    private static final String SCORES_EXAM = "DiemThi";
    private static final String SCORES_NOTE = "GhiChu";


    //Bảng loại
    public static final String TYPE_TABLE = "LOAI";
    public static final String TYPE_ID = "IdLoai";
    public static final String TYPE_NAME = "TenLoai";

    //Bảng history
    private static final String HISTORY_TABLE = "LICH_SU";
    private static final String HISTORY_ID = "IdLichSu";
    private static final String HISTORY_NAME = "TenLichSu";
    private static final String HISTORY_TIME = "ThoiGian";
    private static final String HISTORY_TYPE = "LoaiLichSu";
    private static final String HISTORY_NOTE = "GhiChu";

    //Tạo bảng admin
    private static final String QueryCreateAccountTable = "CREATE TABLE " + ACCOUNT_TABLE + "( " +
                                                    ACCOUNT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                                                    ACCOUNT_NAME + " TEXT NOT NULL, " +
                                                    ACCOUNT_EMAIL + " TEXT NOT NULL, " +
                                                    ACCOUNT_PASSWORD + " TEXT NOT NULL, " +
                                                    ACCOUNT_IMAGE + " BLOB, " +
                                                    ACCOUNT_VERIFICATION + " TEXT)";

    //Tạo bảng khoa
    private static final String QueryCreateFacultyTable = "CREATE TABLE " + FACULTY_TABLE + "( " +
                                                    FACULTY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                                                    FACULTY_NAME + " TEXT, " +
                                                    FACULTY_ADDRESS + " TEXT, " +
                                                    FACULTY_PHONE_NUMBER + " TEXT)";

    //Tạo bảng khóa đào tạo
    private static final String QueryCreateTrainingCourseTable = "CREATE TABLE " + TRAINING_COURSE_TABLE + "( " +
                                                                        TRAINING_COURSE_ID + " INTEGER PRIMARY KEY, " +
                                                                        TRAINING_COURSE_NAME + " TEXT NOT NULL, " +
                                                                        TRAINING_COURSE_START_DAY + " TEXT, " +
                                                                        TRAINING_COURSE_END_DAY + " TEXT)";

    //Tạo bảng hệ đào tạo
    private static final String QueryCreateTrainingSystemTable = "CREATE TABLE " + TRAINING_SYSTEM_TABLE + "( " +
            TRAINING_SYSTEM_ID + " TEXT PRIMARY KEY, " +
            TRAINING_SYSTEM_NAME + " TEXT, " +
            TRAINING_SYSTEM_CREDIT + " INTEGER, " +
            TRAINING_SYSTEM_DESCRIBE + " TEXT, " +
            TRAINING_SYSTEM_TIME + " TEXT)";

    //Tạo bảng lớp
    private static final String QueryCreateClassTable = "CREATE TABLE " + CLASS_TABLE + "( " +
                                                    CLASS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                                                    CLASS_FACULTY_ID + " INTEGER NOT NULL, " +
                                                    CLASS_FACULTY_TRAINING_ID + " INTEGER NOT NULL, " +
                                                    CLASS_NAME + " TEXT, " +
                                                    CLASS_TRAINING_SYSTEM_ID + " TEXT, " +
                                                    "FOREIGN KEY ( " + CLASS_FACULTY_ID + " ) REFERENCES " + FACULTY_TABLE + " ( " + FACULTY_ID + " ), " +
                                                    "FOREIGN KEY (" + CLASS_FACULTY_TRAINING_ID + ") REFERENCES " + TRAINING_COURSE_TABLE + " ( " + TRAINING_COURSE_ID + "), " +
                                                     "FOREIGN KEY (" + CLASS_TRAINING_SYSTEM_ID +  ") REFERENCES " + TRAINING_SYSTEM_TABLE + " ( " + TRAINING_SYSTEM_ID + "))";

    //Tạo bảng sinh viên
    private static final String QueryCreateStudentTable = "CREATE TABLE " + STUDENT_TABLE + "( " +
                                                    STUDENT_ID + " INTEGER PRIMARY KEY, " +
                                                    STUDENT_CLASS_ID + " INTEGER NOT NULL, " +
                                                    STUDENT_NAME + " TEXT, " +
                                                    STUDENT_GENDER + " TEXT, " +
                                                    STUDENT_BIRTHDAY + " TEXT, " +
                                                    STUDENT_NATIVE_LAND + " TEXT, " +
                                                    STUDENT_EMAIL + " TEXT, " +
                                                    STUDENT_PHONE_NUMBER + " TEXT, " +
                                                    "FOREIGN KEY ( " + STUDENT_CLASS_ID + " ) REFERENCES " + CLASS_TABLE + " ( " + CLASS_ID + "))";

    //Tạo bảng môn học
    private static final String QueryCreateSubjectTable = "CREATE TABLE " + SUBJECT_TABLE + "( " +
                                                                SUBJECT_ID + " TEXT PRIMARY KEY, " +
                                                                SUBJECT_FACULTY_ID + " INTEGER NOT NULL, " +
                                                                SUBJECT_NAME + " TEXT, " +
                                                                SUBJECT_CREDIT + " INTEGER, " +
                                                                SUBJECT_TRAINING_SYSTEM_ID + " TEXT NOT NULL, " +
                                                                "FOREIGN KEY (" + SUBJECT_FACULTY_ID + ") REFERENCES " + FACULTY_TABLE + "(" + FACULTY_ID + "), " +
                                                                "FOREIGN KEY (" + SUBJECT_TRAINING_SYSTEM_ID + ") REFERENCES " + TRAINING_SYSTEM_TABLE + "(" + TRAINING_SYSTEM_ID + "))";

    //Tạo bảng lớp tín chỉ
    private static final String QueryCreateCreditClassTable = "CREATE TABLE " + CREDIT_CLASS_TABLE + " (" +
                                                                    CREDIT_CLASS_ID + " TEXT PRIMARY KEY, " +
                                                                    CREDIT_CLASS_SUBJECT_ID + " TEXT NOT NULL, " +
                                                                    CREDIT_CLASS_TRAINING_ID + " INTEGER NOT NULL, " +
                                                                    CREDIT_CLASS_MAX_NBS + " INTEGER, " +
                                                                    CREDIT_CLASS_CLASSROOM + " TEXT, " +
                                                                    CREDIT_CLASS_SCHOOL_YEAR + " INTEGER, " +
                                                                    CREDIT_CLASS_SEMESTER + " INTEGER, " +
                                                                    "FOREIGN KEY (" + CREDIT_CLASS_SUBJECT_ID + ") REFERENCES " + SUBJECT_TABLE + '(' + SUBJECT_ID + "), " +
                                                                    "FOREIGN KEY (" + CREDIT_CLASS_TRAINING_ID + ") REFERENCES " + TRAINING_COURSE_TABLE + '(' + TRAINING_COURSE_ID + "))";

    //Tạo bảng điểm
    private static final String QueryCreateScoresTable = "CREATE TABLE " + SCORES_TABLE + " (" +
                                                                    SCORES_STUDENT_ID + " INTEGER NOT NULL, " +
                                                                    SCORES_CREDIT_CLASS_ID + " TEXT NOT NULL, " +
                                                                    SCORES_ATTENDANCE + " REAL, " +
                                                                    SCORES_CONDITIONAL + " REAL, " +
                                                                    SCORES_EXAM + " REAL, " +
                                                                    SCORES_NOTE + " TEXT, " +
                                                                    "PRIMARY KEY (" + SCORES_STUDENT_ID + ", " + SCORES_CREDIT_CLASS_ID + "), " +
                                                                    "FOREIGN KEY (" + SCORES_STUDENT_ID + ") REFERENCES " + STUDENT_TABLE + "(" + STUDENT_ID + "), " +
                                                                    "FOREIGN KEY (" + SCORES_CREDIT_CLASS_ID + ") REFERENCES " + CREDIT_CLASS_TABLE + "(" + CREDIT_CLASS_ID + "))";


    //Tạo Bảng loại lịch sử
    private static final String QueryCreateTypeTable = "CREATE TABLE " + TYPE_TABLE + " ( " +
                                                                        TYPE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                                                                        TYPE_TABLE + " TEXT)";


    //Tạo Bảng lịch sử
    private static final String QueryCreateHistoryTable = "CREATE TABLE " + HISTORY_TABLE + " ( " +
                                                                            HISTORY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                                                                            HISTORY_NAME + " TEXT, " +
                                                                            HISTORY_TIME + " TEXT, " +
                                                                            HISTORY_TYPE + " INTEGER NOT NULL, " +
                                                                            HISTORY_NOTE + " TEXT, " +
                                                                            "FOREIGN KEY (" + HISTORY_TYPE + ") REFERENCES " + TYPE_TABLE + "(" + TYPE_ID+"))";


    public MyDatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(QueryCreateAccountTable);
        db.execSQL(QueryCreateTrainingCourseTable);
        db.execSQL(QueryCreateFacultyTable);
        db.execSQL(QueryCreateTrainingSystemTable);
        db.execSQL(QueryCreateClassTable);
        db.execSQL(QueryCreateStudentTable);
        db.execSQL(QueryCreateSubjectTable);
        db.execSQL(QueryCreateCreditClassTable);
        db.execSQL(QueryCreateScoresTable);
        db.execSQL(QueryCreateTypeTable);
        db.execSQL(QueryCreateHistoryTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public Cursor readAllAccount(){
        String queryAccount = "SELECT * FROM " + ACCOUNT_TABLE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        if(db != null){
            cursor = db.rawQuery(queryAccount, null);
        }
        return cursor;
    }



    public long addAccount(Account account){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(ACCOUNT_NAME, account.getName());
        cv.put(ACCOUNT_EMAIL, account.getEmail());
        cv.put(ACCOUNT_PASSWORD, account.getPassword());

        return db.insert(ACCOUNT_TABLE, null, cv);
    }

    public Cursor readItemListStudent(){
        String queryStudent = "SELECT s." + STUDENT_ID + ", s." + STUDENT_NAME + ", l." + CLASS_NAME + ", k." + FACULTY_NAME + ", s." + STUDENT_EMAIL +
                                        ", s." + STUDENT_PHONE_NUMBER +
                               " FROM " + FACULTY_TABLE + " as k join " + CLASS_TABLE + " as l " +
                                        " on k." + FACULTY_ID + " = l." + CLASS_FACULTY_ID +
                                    " join " + STUDENT_TABLE + " as s " +
                                        " on l." + CLASS_ID + " = s." + STUDENT_CLASS_ID ;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if(db != null){
            cursor = db.rawQuery(queryStudent, null);
        }
        return cursor;
    }

    public Cursor readStudent(String maSV){
        String query = "SELECT s.*, l." + CLASS_NAME + ", k." + FACULTY_NAME + ", k." + FACULTY_ID +
                " FROM " + STUDENT_TABLE + " as s join " + CLASS_TABLE + " as l " +
                    " on s." + STUDENT_CLASS_ID + " = l." + CLASS_ID +
                " join " + FACULTY_TABLE + " as k" +
                    " on l." + CLASS_FACULTY_ID + " = k." + FACULTY_ID +
                " where s." + STUDENT_ID + " = " + maSV;

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;

        if(db != null){
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }

    public Cursor readDiem(String maSV){
        String queryDiem = "SELECT d." + SCORES_ATTENDANCE + ", d." + SCORES_CONDITIONAL + ", d." + SCORES_EXAM + ", m." + SUBJECT_CREDIT +
                           " FROM " + SCORES_TABLE + " as d" + " join " + CREDIT_CLASS_TABLE + " as l" +
                                    " on " + " d." + SCORES_CREDIT_CLASS_ID + " = l." + CREDIT_CLASS_ID +
                                " join " + SUBJECT_TABLE + " as m " +
                                    " on l." + CREDIT_CLASS_SUBJECT_ID + " = m." + SUBJECT_ID +
                            " where d." + SCORES_STUDENT_ID + " = " + maSV;

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if(db != null){
            cursor = db.rawQuery(queryDiem, null);
        }
        return cursor;
    }

    public Cursor readDiem1(String maSV){
        String query = "SELECT m." + SUBJECT_ID + ", m." + SUBJECT_NAME + ", m." + SUBJECT_CREDIT +
                                ", l." + CREDIT_CLASS_ID + ", d." + SCORES_ATTENDANCE + ", d." +
                                SCORES_CONDITIONAL + ", d." + SCORES_EXAM + ", d." + SCORES_NOTE + ", d." + SCORES_STUDENT_ID +
                        " FROM " + SUBJECT_TABLE + " as m join " + CREDIT_CLASS_TABLE + " as l " +
                                " on m." + SUBJECT_ID + " = l." + CREDIT_CLASS_SUBJECT_ID +
                        " JOIN " + SCORES_TABLE + " as d " +
                                " on l." + CREDIT_CLASS_ID + " = d." + SCORES_CREDIT_CLASS_ID +
                        " WHERE d." + SCORES_STUDENT_ID + " = " + maSV;

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if(db != null){
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }

    public Cursor readDiem2(String maSV, String maLopTC){
        String query = "SELECT m." + SUBJECT_ID + ", m." + SUBJECT_NAME + ", m." + SUBJECT_CREDIT +
                ", l." + CREDIT_CLASS_ID + ", d." + SCORES_ATTENDANCE + ", d." +
                SCORES_CONDITIONAL + ", d." + SCORES_EXAM + ", d." + SCORES_NOTE + ", d." + SCORES_STUDENT_ID +
                " FROM " + SUBJECT_TABLE + " as m join " + CREDIT_CLASS_TABLE + " as l " +
                " on m." + SUBJECT_ID + " = l." + CREDIT_CLASS_SUBJECT_ID +
                " JOIN " + SCORES_TABLE + " as d " +
                " on l." + CREDIT_CLASS_ID + " = d." + SCORES_CREDIT_CLASS_ID +
                " WHERE d." + SCORES_STUDENT_ID + " = " + maSV + " AND d." +SCORES_CREDIT_CLASS_ID + " = \"" + maLopTC + "\"";

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if(db != null){
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }



    public Cursor readMaSV(){
        String query = "SELECT " + STUDENT_ID  + " FROM " + STUDENT_TABLE;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        if(db != null){
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }

    public HashMap<String, String> readLopTheoKhoa(String maKhoa){
        HashMap<String, String> classInfo = new HashMap<>();
        String query = "SELECT " + CLASS_ID + ", " + CLASS_NAME +
                       " FROM " + CLASS_TABLE + " WHERE " + CLASS_FACULTY_ID + " = ?";
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if(db != null){
            cursor = db.rawQuery(query, new String[]{maKhoa});
        }
        if(cursor.getCount() != 0){
            while (cursor.moveToNext()){
                classInfo.put(String.valueOf(cursor.getInt(0)), cursor.getString(1));
            }
        }
        cursor.close();
        db.close();
        return classInfo;
    }

    public HashMap<String, String> readKhoaTheoLop(String tenLop){
        Map<String, String> facultyInfo = new HashMap<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT " + FACULTY_ID + ", " + FACULTY_NAME +
                        " FROM " + FACULTY_TABLE +
                        " WHERE " + FACULTY_ID + " IN ( " +
                                    " SELECT " + CLASS_FACULTY_ID +
                                    " FROM " + CLASS_TABLE +
                                    " WHERE " + CLASS_NAME + " = ?)";

        Cursor cursor = null;
        if(db != null){
            cursor = db.rawQuery(query, new String[]{tenLop});
        }

        if(cursor.getCount() != 0){
            while (cursor.moveToNext()){
                facultyInfo.put(String.valueOf(cursor.getInt(0)), cursor.getString(1));
            }
        }
        cursor.close();

        return (HashMap<String, String>) facultyInfo;

    }


    public static String getBangDiem(){
        return SCORES_TABLE;
    }

    public static String getMaSV(){
        return STUDENT_ID;
    }

    public static String getBangSV(){
        return STUDENT_TABLE;
    }

    public static String getTenSV(){
        return STUDENT_NAME;
    }

    public static String getStudentClassId(){
        return STUDENT_CLASS_ID;
    }

    public static String getStudentGender(){
        return STUDENT_GENDER;
    }

    public static String getStudentBirthday(){
        return STUDENT_BIRTHDAY;
    }

    public static String getStudentNativeLand(){
        return STUDENT_NATIVE_LAND;
    }

    public static String getStudentEmail(){
        return STUDENT_EMAIL;
    }

    public static String getStudentPhoneNumber(){
        return STUDENT_PHONE_NUMBER;
    }

    public static String getClassId(){
        return CLASS_ID;
    }

    public static String getClassName(){
        return CLASS_NAME;
    }

    public static String getClassTable(){
        return CLASS_TABLE;
    }

    public static String getClassFacultyId(){
        return CLASS_FACULTY_ID;
    }

    public static String getClassTrainingSystem(){
        return CLASS_FACULTY_TRAINING_ID;
    }

    public static String getClassFacultyTrainingId(){
        return CLASS_FACULTY_TRAINING_ID;
    }

    public static String getFacultyTable(){
        return FACULTY_TABLE;
    }

    public static String getFacultyId(){
        return FACULTY_ID;
    }

    public static String getFacultyName(){
        return FACULTY_NAME;
    }

    public static String getFacultyAddress(){
        return FACULTY_ADDRESS;
    }

    public static String getFacultyPhoneNumber(){
        return FACULTY_PHONE_NUMBER;
    }

    public static String getScoresAttendance(){
        return SCORES_ATTENDANCE;
    }

    public static String getScoresConditional(){
        return SCORES_CONDITIONAL;
    }

    public static String getScoresExam(){
        return SCORES_EXAM;
    }

    public static String getScoresNote(){
        return SCORES_NOTE;
    }

    public static String getScoresCreditClassId(){
        return SCORES_CREDIT_CLASS_ID;
    }

    public static String getTrainingCourseTable(){
        return TRAINING_COURSE_TABLE;
    }

    public static String getTrainingCourseId(){
        return TRAINING_COURSE_ID;
    }

    public static String getTrainingCourseName(){
        return TRAINING_COURSE_NAME;
    }

    public static String getTrainingCourseStartDay(){
        return TRAINING_COURSE_START_DAY;
    }

    public static String getTrainingCourseEndDay(){
        return TRAINING_COURSE_END_DAY;
    }

    public static String getSubjectTable(){
        return SUBJECT_TABLE;
    }

    public static String getSubjectId(){
        return SUBJECT_ID;
    }

    public static String getSubjectFacultyId(){
        return SUBJECT_FACULTY_ID;
    }

    public static String getSubjectName(){
        return SUBJECT_NAME;
    }

    public static String getSubjectCredit(){
        return SUBJECT_CREDIT;
    }

    public static String getSubjectTrainingSystem(){
        return SUBJECT_TRAINING_SYSTEM_ID;
    }

    public static String getAccountTable(){
        return ACCOUNT_TABLE;
    }

    public static String getAccountEmail(){
        return ACCOUNT_EMAIL;
    }

    public static String getAccountId(){
        return ACCOUNT_ID;
    }

    public static String getAccountVerification(){
        return ACCOUNT_VERIFICATION;
    }
    public static String getAccountPassword(){
        return ACCOUNT_PASSWORD;
    }

    public  static String getHistoryTable(){
        return HISTORY_TABLE;
    }

    public static String getHistoryName(){
        return HISTORY_NAME;
    }

    public static String getHistoryTime(){
        return HISTORY_TIME;
    }

    public static String getHistoryType(){
        return HISTORY_TYPE;
    }
    public static String getHistoryNote(){
        return HISTORY_NOTE;
    }
    public static String getAccountImage(){
        return ACCOUNT_IMAGE;
    }
    public static String getAccountName(){
        return ACCOUNT_NAME;
    }

    public static String getCreditClassTable(){
        return CREDIT_CLASS_TABLE;
    }

    public static String getCreditClassId(){
        return CREDIT_CLASS_ID;
    }

    public static String getCreditClassSubjectId(){
        return CREDIT_CLASS_SUBJECT_ID;
    }

    public static String getCreditClassMaxNbs(){
        return CREDIT_CLASS_MAX_NBS;
    }

    public static String getScoresStudentId(){
        return SCORES_STUDENT_ID;
    }

    public static String getTrainingSystemTable(){
        return TRAINING_SYSTEM_TABLE;
    }

    public static String getTrainingSystemId(){
        return TRAINING_SYSTEM_ID;
    }

    public static String getTrainingSystemName(){
        return TRAINING_SYSTEM_NAME;
    }

    public static String getTrainingSystemCredit(){
        return TRAINING_SYSTEM_CREDIT;
    }

    public static String getTrainingSystemDescribe(){
        return TRAINING_SYSTEM_DESCRIBE;
    }

    public static String getTrainingSystemTime(){
        return TRAINING_SYSTEM_TIME;
    }
}
