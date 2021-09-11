package com.example.mysmartgarden;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

public class introActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.intro); //xml , java 소스 연결
        Handler handler = new Handler();
        handler.postDelayed(new Runnable(){
            @Override
            public void run() {
                // 처음에 이름이 비어있다면, 이름과 식물의 종을 정하는 페이지로 감
                //Intent intent = new Intent (getApplicationContext(), PlantSpinerActivity.class);
                //startActivity(intent);

                //인트로 실행 후 바로 MainActivity로 넘어감.
                Intent intent = new Intent (getApplicationContext(), MainActivity.class);
               startActivity(intent);
                finish();

            }
        },1500); //1초 후 인트로 실행
    }

    @Override
    protected void onPause(){
        super.onPause();
        finish();
    }



}
