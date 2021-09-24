package com.example.mysmartgarden;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.github.matteobattilana.weather.PrecipType;
import com.github.matteobattilana.weather.WeatherView;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Fragment_Main extends Fragment {

    int clickedSun;
    private boolean _isBtnDown;
    LinearLayout main_back;

    Singleton userSingleton = Singleton.getInstance();

    FirebaseFirestore db;
    State state=new State();
    ImageButton sun;//태양버튼
    ImageButton plant;// 식물버튼-> 물주기

    WeatherView weatherView;//뒤에 물 주는 배경

    public TextView notice,info1,info2,info3,info4;

    // 값 xml 파일 값 settext를 위한 빌드업
    TextView plantName;
    TextView withday;

    Handler mhandler;

    public Fragment_Main(){

    }

    @Override
    public void onCreate(Bundle savedInstanceState) { //몰라 무시해 -> 네?ㅋㅋㅋㅋㅋㅋㅋㅋㅋ
        super.onCreate(savedInstanceState);
    }

    // Inflate the view for the fragment based on layout XML
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,//view 처리해주는곳
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_fragment,container,false);

        return view;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstancdState){//왠만한건 다여기서함
        super.onViewCreated(view,savedInstancdState);

        db = FirebaseFirestore.getInstance();

        mhandler= new Handler();

        withday=view.findViewById(R.id.day);// 함께한 일자
        plantName=view.findViewById(R.id.HelloPlantname);// 이름
        long day=dayCalculator(userSingleton.getEntry());

        withday.setText(String.valueOf(day));
        plantName.setText(userSingleton.getName());

        clickedSun=1;
        main_back=view.findViewById(R.id.main_back);//맨뒤 배경

        sun=view.findViewById(R.id.sun);//이미지 버튼
        plant=view.findViewById(R.id.plant);
        Glide.with(this).load(R.raw.sun).into(sun);
        Glide.with(this).load(R.raw.plant).into(plant);

        weatherView = view.findViewById(R.id.weather_view);

        plant.setOnTouchListener(onBtnTouchListener); // 물주는거 관련 버튼 리스너

        sun.setOnClickListener(new View.OnClickListener(){//태양 클릭 시
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View view){
                if(clickedSun==1) {//킴->꺼짐
                    main_back.setBackgroundColor(Color.parseColor("#1B4537"));
                    Glide.with(view).load(R.raw.moon).into(sun);
                    sun.setBackgroundColor(Color.parseColor("#1B4537"));
                    clickedSun++;
                }

                else if(clickedSun==2){//꺼짐->킴
                    main_back.setBackgroundColor(Color.parseColor("#50A387"));
                    Glide.with(view).load(R.raw.sun).into(sun);
                    sun.setBackgroundColor(Color.parseColor("#50A387"));
                    clickedSun=1;
                }
            }
        });
        notice=view.findViewById(R.id.notice);//알림
        info1=view.findViewById(R.id.info1);//토양습도
        info2=view.findViewById(R.id.info2);//습도
        info3=view.findViewById(R.id.info3);//온도
        info4=view.findViewById(R.id.info4);//물통양



        Thread thread1 = new Thread(new Runnable(){
            @Override public void run() { // UI 작업 수행 X
                try {

                    StringBuilder outputBuilder = new StringBuilder();



                    URL url = new URL( "http://192.168.186.194/");

                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                    BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    String temp;
                    String a = null;
                    while((temp=br.readLine())!=null){
                        System.out.println(temp);
                        a+=temp;
                    }
                    Log.d("TAG",a);

                    String in2=a.substring(13,15);//    대기습도
                    String in3 =a.substring(31,35);//온도
                    String in1 = a.substring(42,43);//토양
                    String in4;
                    if(a.substring(43).equals("Not FUll Water")){
                        in4="X";
                    }
                    else{
                        in4="O";
                    }
                    Log.d("test",in1);
                    Log.d("test",in2);
                    Log.d("test",in3);
                    Log.d("test",in4);



                    mhandler.post(new Runnable(){
                        @Override public void run() {
                            // UI 작업 수행 O
                            info1.setText(in1);
                            info2.setText(in2);
                            info3.setText(in3);
                            info4.setText(in4);
                        }
                    });


                    urlConnection.disconnect();
                    br.close();


                } catch (Exception e) {

                    e.printStackTrace();


                }


            }
        });
        thread1.start();



        /*info1.setText(state.getInfo1());
        info2.setText(state.getInfo2());
        info3.setText(state.getInfo3());
        info4.setText(state.getInfo4());*/
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    private void onBtnDown()
    {

        Fragment_Main.TouchThread kThread = new Fragment_Main.TouchThread();
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

    public long dayCalculator(String date2){
        Calendar cal = Calendar.getInstance();
        Date nowDate = cal.getTime();
        SimpleDateFormat dataformat = new SimpleDateFormat("yyyy/MM/dd");
        String date1 = dataformat.format(nowDate); //날짜1
        Date format1 = null;
        try {
            format1 = new SimpleDateFormat("yyyy/MM/dd").parse(date1);
            Date format2 = new SimpleDateFormat("yyyy/MM/dd").parse(date2);

            long diffSec = (format1.getTime() - format2.getTime()) / 1000; //초 차이

            long diffDays = diffSec / (24*60*60); //일자수 차이

            System.out.println(diffSec + "초 차이");
            System.out.println(diffDays + "일 차이");

            return diffDays;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

}

