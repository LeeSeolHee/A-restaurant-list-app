package com.inhatc.listapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class MyDatabaseHelper extends SQLiteOpenHelper {


    private Context context;
    private static final String DATABASE_NAME = "bestlist.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "best_list";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_TITLE = "title";
    private static final String COLUMN_PLACE = "place";
    private static final String COLUMN_CONTENT = "content";
    private static final String COLUMN_RESULT = "result";
    private static final String COLUMN_STARRESULT = "star_result";
    private static final String COLUMN_IMG = "img";

    private static String TAG = "DataBaseHelper";
    private static String DB_PATH = "";
    private SQLiteDatabase mDataBase;

    public MyDatabaseHelper(@Nullable Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_NAME
                + " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_TITLE + " TEXT, "
                + COLUMN_PLACE + " TEXT, "
                + COLUMN_CONTENT + " TEXT, "
                + COLUMN_RESULT + " TEXT, "
                + COLUMN_STARRESULT + " FLOAT, "
                + COLUMN_IMG + " TEXT ); ";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    void addBook(String title, String place, String content, String result, float star_result, String imageview)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_TITLE, title);
        cv.put(COLUMN_PLACE, place);
        cv.put(COLUMN_CONTENT, content);
        cv.put(COLUMN_RESULT, result);
        cv.put(COLUMN_STARRESULT, star_result);
        cv.put(COLUMN_IMG, imageview);

        long results = db.insert(TABLE_NAME, null, cv);
        if (results == -1)
        {
            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(context, "데이터 추가 성공", Toast.LENGTH_SHORT).show();
        }
    }

    public boolean updateData(String id, String title, String place, String content, String result, float star_result){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_ID, id);
        contentValues.put(COLUMN_TITLE, title);
        contentValues.put(COLUMN_PLACE, place);
        contentValues.put(COLUMN_CONTENT, content);
        contentValues.put(COLUMN_RESULT, result);
        contentValues.put(COLUMN_STARRESULT, star_result);
        db.update(TABLE_NAME, contentValues,"_id == ?", new String[]{id});
        return true;
    }

    public Integer deleteData(String id){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME, "_id == ?", new String[]{id});
    }

}
