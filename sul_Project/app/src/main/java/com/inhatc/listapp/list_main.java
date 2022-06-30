package com.inhatc.listapp;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;


public class list_main extends AppCompatActivity {

    //region Unused variable
    SQLiteDatabase db;
    private String title, content, result, id;
    ArrayList<String> seqList = new ArrayList<>();
    String[] items = {"제목", "위치", "내용", "#키워드", "별점"};
    //endregion

    private SearchView searchView;
    private TextView emptyView;
    private MyDatabaseHelper databaseHelper;

    /* 검색 활용 */
    private final ListViewAdapter adapter = new ListViewAdapter();
    private final ArrayList<ListViewAdapterData> listDataSet = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_main);

        databaseHelper = new MyDatabaseHelper(this);

        searchView = findViewById(R.id.search_view);
        emptyView = findViewById(R.id.empty_view);
        findViewById(R.id.back_button).setOnClickListener(v -> onBackPressed());
        findViewById(R.id.add_button).setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), list_add.class);
            startActivity(intent);
        });

        adapter.setOnClickListener(data -> {
            Intent intent = new Intent(list_main.this, DetailActivity.class);

            /* putExtra의 첫 값은 식별 태그, 뒤에는 다음 화면에 넘길 값 */
            intent.putExtra("_id", String.valueOf(data.getId()));
            intent.putExtra("title", data.getTitle());
            intent.putExtra("content", data.getContent());
            intent.putExtra("result", data.getKeyword());

            startActivity(intent);
        });

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setAdapter(adapter);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                search(newText);
                return false;
            }
        });
    }

    private void getDataSetFromDatabase() {
        listDataSet.clear();

        //helper의 읽기모드 객체를 가져와 SQLiteDB에 담아 사용준비
        SQLiteDatabase database = databaseHelper.getReadableDatabase();

        //Cursor라는 그릇에 목록을 담아주기
        Cursor cursor = database.rawQuery("SELECT * FROM best_list", null);

        while (cursor.moveToNext()) {
            listDataSet.add(new ListViewAdapterData(cursor));
        }

        cursor.close();
    }

    private void search(String query) {
        emptyView.setText(query.isEmpty() ? "우측 하단의 + 버튼을 눌러 맛집을 추가해 보세요." : "검색 결과가 없습니다.");

        if (query.isEmpty()) {
            adapter.submitList(new ArrayList<>(listDataSet));
            emptyView.setVisibility(listDataSet.isEmpty() ? View.VISIBLE : View.GONE);
            return;
        }

        ArrayList<ListViewAdapterData> filteredDataSet = new ArrayList<>();
        for (ListViewAdapterData data : listDataSet) {
            if (data.getTitle().contains(query)) {
                filteredDataSet.add(data);
            }
        }

        adapter.submitList(filteredDataSet);
        emptyView.setVisibility(filteredDataSet.isEmpty() ? View.VISIBLE : View.GONE);
    }

    @Override
    protected void onResume() {
        super.onResume();

        getDataSetFromDatabase();
        search(searchView.getQuery().toString().trim());
    }
}