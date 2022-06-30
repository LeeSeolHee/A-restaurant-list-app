package com.inhatc.listapp;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //맛집 리스트 버튼 클릭시 액티비티 전환
        Button btnlist = (Button) findViewById(R.id.btn_list);
        btnlist.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(getApplicationContext(), list_main.class);
                startActivity(intent);
            }
        });

        //맵 버튼 클릭시 액티비티 전환
        Button btnmap = (Button) findViewById(R.id.btn_map);
        btnmap.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(getApplicationContext(), Activity_Map.class);
                startActivity(intent);
            }
        });

        //음식추천 버튼 클릭시 액티비티 전환
        Button btnweather = (Button) findViewById(R.id.btn_menu);
        btnweather.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(getApplicationContext(), Weather.class);
                startActivity(intent);
            }
        });

    }
}