package com.example.mysmartgarden;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EdgeEffect;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.github.matteobattilana.weather.PrecipType;
import com.github.matteobattilana.weather.WeatherView;

public class MainActivity extends AppCompatActivity {

    int clickedSun;
    private boolean _isBtnDown;
    LinearLayout main_back;

    Toolbar toolbar;
    ActionBar actionBar;

    ImageButton sun;
    ImageButton plant;

    WeatherView weatherView;

    TextView notice,info1,info2,info3,info4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        clickedSun=1;
        main_back=findViewById(R.id.main_back);//맨뒤 배경

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);//기본 제목을 없애줍니다.
        actionBar.setDisplayHomeAsUpEnabled(true);

        sun=findViewById(R.id.sun);//이미지 버튼
        plant=findViewById(R.id.plant);
        Glide.with(this).load(R.raw.sun).into(sun);
        Glide.with(this).load(R.raw.plant).into(plant);

        weatherView = findViewById(R.id.weather_view);

        plant.setOnTouchListener(onBtnTouchListener);

        sun.setOnClickListener(new View.OnClickListener(){//태양 클릭 시
            @Override
            public void onClick(View view){
                if(clickedSun==1) {//최대->중
                    main_back.setBackgroundColor(getColor(R.color.gray1));
                    clickedSun++;
                }
                else if(clickedSun==2){//중->소
                    main_back.setBackgroundColor(getColor(R.color.gray2));
                    clickedSun++;
                }
                else if(clickedSun==3){//소->꺼짐
                    main_back.setBackgroundColor(getColor(R.color.gray3));
                    clickedSun++;
                }
                else if(clickedSun==4){//꺼짐->최대
                    main_back.setBackgroundColor(Color.WHITE);
                    clickedSun=1;
                }

            }


        });

        notice=findViewById(R.id.notice);//알림
        info1=findViewById(R.id.info1);//토양습도
        info2=findViewById(R.id.info2);//습도
        info3=findViewById(R.id.info3);//온도
        info4=findViewById(R.id.info4);//물통양

    }

    private void onBtnDown()
    {

        TouchThread kThread = new TouchThread();
        kThread.start();
    }

    private Handler touchHandler = new Handler()
    {
        public void handleMessage(Message msg)
        {
            Log.d("MainActivity", "click");
        }
    };

    private class TouchThread extends Thread
    {
        @Override
        public void run() {
            super.run();
            while(_isBtnDown)
            {
                touchHandler.sendEmptyMessage(9876);

                try{
                    Thread.sleep(200);
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        }
    }

    private View.OnTouchListener onBtnTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()){
                case MotionEvent.ACTION_DOWN://누르
                    _isBtnDown = true;
                    weatherView.setWeatherData(PrecipType.RAIN);
                    onBtnDown();
                    break;

                case MotionEvent.ACTION_UP://때면
                    _isBtnDown = false;
                    weatherView.setWeatherData(PrecipType.CLEAR);
                    break;

                default:
                    break;
            }
            return false;
        }
    };


}