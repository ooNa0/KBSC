package com.example.mysmartgarden;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import java.text.DecimalFormat;

public class introActivity extends AppCompatActivity {

    private DAO DAO = new DAO();
    private boolean isNewer;
    private String DeviceID;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.intro); //xml , java 소스 연결
        Handler handler = new Handler();
        handler.postDelayed(new Runnable(){
            @Override
            public void run() {
                DeviceID = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
                isNewer = DAO.isUserExist(DeviceID);
                // ture라면 DeviceID가 이미 있는 것, false라면 없는 것
                if(!isNewer){ // 만약에 디바이스가 처음 등록하는 것이라면?
                    // 처음에 이름이 비어있다면, 이름과 식물의 종을 정하는 페이지로 감
                    intent = new Intent (getApplicationContext(), PlantSpinerActivity.class);

                    // 데이터를 새로 입력받고, 문서로 데이터를 저장해준다.
                    DAO.SaveUserID("user", DeviceID, "난용이", "상추", "123.21.32.123");
                }
                else{
                    // 인트로 실행 후 바로 MainActivity로 넘어감.
                    intent = new Intent (getApplicationContext(), MainActivity.class);
                }
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
