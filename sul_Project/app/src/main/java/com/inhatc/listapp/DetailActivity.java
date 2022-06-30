package com.inhatc.listapp;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


public class DetailActivity extends AppCompatActivity {
    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 0;
    private TextView id;
    private ImageView img;
    private EditText title, star, content, keyword, map;
    String board_seq = "";
    SQLiteDatabase db;
    Bitmap bitmap;
    Button btnDelete, btnUpdate;
    MyDatabaseHelper databaseHelper;
    int num; // 받은 아이디
    String imgName = "osz.png";

    /* 이미지 불러오기 (파일) */
    public int getRawResIdByName(String resName) {
        String pkgName = this.getPackageName();
        // Return 0 if not found.
        int resID = this.getResources().getIdentifier(resName, "raw", pkgName);
        Log.i("AndroidVideoView", "Res Name: " + resName + "==> Res ID = " + resID);
        return resID;
    }

    /* 디테일 화면 (상세 내용) */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

//       if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
//               != PackageManager.PERMISSION_GRANTED) {
//
//           // Should we show an explanation?
//           if (shouldShowRequestPermissionRationale(
//                   Manifest.permission.READ_EXTERNAL_STORAGE)) {
//               // Explain to the user why we need to read the contacts
//           }
//
//           requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
//
//           return;
//       }

        databaseHelper = new MyDatabaseHelper(this);
        // 쓰기 가능한 SQLiteDatabase 인스턴스 구함
        db = databaseHelper.getWritableDatabase();

        /* xml과 id와 연결 */
        id = (EditText) findViewById(R.id.id_tv);
        title = (EditText) findViewById(R.id.title_tv);         // 제목
        star = (EditText) findViewById(R.id.star_tv);           // 별점
        keyword = (EditText) findViewById(R.id.keyword_tv);     // 키워드
        map = (EditText) findViewById(R.id.map_tv);             // 위치
        content = (EditText) findViewById(R.id.content_tv);     // 내용
        img = (ImageView) findViewById(R.id.imageView2);        // 이미지
        board_seq = getIntent().getStringExtra("board_seq");

        btnDelete = findViewById(R.id.btnDelete);  // 삭제하기
        btnUpdate = findViewById(R.id.btnUpdate);  // 수정하기

        // 보내온 Intent를 얻는다
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        InitData();


        // 새로 추가된 부분, select
        num = Integer.parseInt(bundle.getString("_id"));
        Log.d("snow", num + "|| printID");
        DBSearch(num);  // 넘긴 id 값 받음

        /* 삭제하기 */
        DeleteData();
        /* 수정하기 */
        UpdateData();

        /* DB 닫기 */
        db.close();
        databaseHelper.close();

    }

    private void InitData() {

        // 해당 게시물의 데이터를 읽어오는 함수, 파라미터로 보드 번호를 넘김
        LoadBoard loadBoard = new LoadBoard();
        loadBoard.execute(board_seq);

    }


    class LoadBoard extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            Log.d(TAG, "onPreExecute");
        }
    }

    // SELECT * FROM best_list WHERE _id == num
    void DBSearch(Integer id) {
        Cursor cursor = null;

        try {
            /* SQL 구문 */
            cursor = db.query("best_list", null, "_id" + " == ?", new String[]{id.toString()}, null, null, null);

            if (cursor != null) {
                while (cursor.moveToNext()) {

                    String titles = cursor.getString(1);
                    String places = cursor.getString(2);
                    String contents = cursor.getString(3);
                    String keywords = cursor.getString(4);
                    Float stars = cursor.getFloat(5);
                    String imgs = cursor.getString(6);

                    /* detail.xml 연결하여 텍스트뷰 출력 */
                    Log.d(TAG, ", title: " + titles + ", place: " + places + ", content: " + contents + ", keyword: " + keywords + ", star: " + stars + ", img: " + imgs);
                    title.setText(titles);        // 제목
                    star.setText(stars + "");       // 별점
                    keyword.setText(keywords);    // 키워드
                    map.setText(places);          // 위치
                    content.setText(contents);    // 내용

                    try {
                        String imgpath = getCacheDir() + "/" + imgs;   // 내부 저장소에 저장되어 있는 이미지 경로
                        Bitmap bm = BitmapFactory.decodeFile(imgpath);
                        img.setImageBitmap(bm);   // 내부 저장소에 저장된 이미지를 이미지뷰에 셋
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), "파일 로드 실패", Toast.LENGTH_SHORT).show();
                    }

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public void DeleteData() {
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer deleteRows = databaseHelper.deleteData(num + "");
                if (deleteRows > 0) {
                    Toast.makeText(DetailActivity.this, "Data Deleted", Toast.LENGTH_LONG).show();
                    finish();

                } else {
                    Toast.makeText(DetailActivity.this, "Data not Deleted", Toast.LENGTH_LONG).show();
                    Log.d(TAG, ", id: " + num + ", deleteRows: " + deleteRows);
                }
            }
        });
    }

    public void UpdateData() {
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isUpdated = databaseHelper.updateData(
                        num + "",
                        title.getText().toString(),
                        map.getText().toString(),
                        content.getText().toString(),
                        keyword.getText().toString(),
                        Float.parseFloat(star.getText().toString())
                );
                if (isUpdated == true) {
                    Toast.makeText(DetailActivity.this, "Data Updated", Toast.LENGTH_LONG).show();
                    finish();

                } else {
                    Toast.makeText(DetailActivity.this, "Data not Updated", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}





