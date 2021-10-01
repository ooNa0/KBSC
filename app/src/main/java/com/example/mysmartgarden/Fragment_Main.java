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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.github.matteobattilana.weather.PrecipType;
import com.github.matteobattilana.weather.WeatherView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
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
    ImageButton sun;//태양버튼
    ImageButton plant;// 식물버튼-> 물주기

    WeatherView weatherView;//뒤에 물 주는 배경

    private TextView notice;
    private TextView soilHumidityView;
    private TextView airHumidityView;
    private TextView temperatureView;
    private TextView waterView;

    private long day;

    // 값 xml 파일 값 settext를 위한 빌드업
    TextView plantName;
    TextView withday;

    Handler mhandler;
    private Fragment_Main view;

    public Fragment_Main(){

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
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
        day=dayCalculator(userSingleton.getEntry());

        withday.setText(String.valueOf(day));
        plantName.setText(userSingleton.getName());

        clickedSun=1;
        main_back=view.findViewById(R.id.main_back);//맨뒤 배경

        sun=view.findViewById(R.id.sun);//이미지 버튼
        plant=view.findViewById(R.id.plant);
        Glide.with(this).load(R.raw.sun).into(sun);
        Glide.with(this).load(R.raw.plant).into(plant);

        weatherView = view.findViewById(R.id.weather_view);

        writeWater(0);//초기화 -> 0:안줌 1:조금 줌 2:많이줌 <-누르는 시간에따라 결정해줌
        plant.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                writeWater(2);
                return false;
            }
        });

        plant.setOnTouchListener(onBtnTouchListener); // 물주는거 관련 버튼 리스너

        writeLight(1);//초기화

        sun.setOnClickListener(new View.OnClickListener(){//태양 클릭 시
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View view){
                if(clickedSun==1) {//킴->꺼짐
                    main_back.setBackgroundColor(Color.parseColor("#1B4537"));
                    Glide.with(view).load(R.raw.moon).into(sun);
                    sun.setBackgroundColor(Color.parseColor("#1B4537"));
                    writeLight(0);
                    clickedSun++;
                }

                else if(clickedSun==2){//꺼짐->킴
                    main_back.setBackgroundColor(Color.parseColor("#50A387"));
                    Glide.with(view).load(R.raw.sun).into(sun);
                    sun.setBackgroundColor(Color.parseColor("#50A387"));
                    clickedSun=1;
                    writeLight(1);
                }
            }
        });
        notice=view.findViewById(R.id.notice);//알림
        soilHumidityView=view.findViewById(R.id.soilHumidityView);//토양습도
        airHumidityView=view.findViewById(R.id.airHumidityView);//습도
        temperatureView=view.findViewById(R.id.temperatureView);//온도
        waterView=view.findViewById(R.id.waterView);//물통양

        notice.setText(Constant.PLANT_IS_HAPPY8);

        readData();

        DatabaseReference mDatabase;//리얼타임
        mDatabase= FirebaseDatabase.getInstance().getReference("data");

        ChildEventListener childEventListener;//상시대기 리스너
        childEventListener =new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }
            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Log.d("child", "onChildChanged:" + snapshot.getKey());
                Log.d("child", "onChildChanged:" + snapshot.getValue());

                switch(snapshot.getKey()){
                    case "humidity":
                        airHumidityView.setText(snapshot.getValue().toString() + "%");
                        if(Float.parseFloat(snapshot.getValue().toString()) > 70){
                            airHumidityView.setTextColor(Color.RED);
                            notice.setText(Constant.PLANT_IS_ENGRY3);
                        }else if(Float.parseFloat(snapshot.getValue().toString()) > 40){
                            airHumidityView.setTextColor(Color.BLUE);
                            notice.setText(Constant.PLANT_IS_ENGRY6);
                        }
                        else{
                            airHumidityView.setTextColor(Color.BLACK);
                            notice.setText(Constant.PLANT_IS_HAPPY3);
                        }
                        break;
                    case "soilwater":
                        if((Float.parseFloat(snapshot.getValue().toString()) > 1030)){
                            soilHumidityView.setText("건조");
                            soilHumidityView.setTextColor(Color.RED);
                            notice.setText(Constant.PLANT_IS_ENGRY1);
                        } else if(Float.parseFloat(snapshot.getValue().toString()) < 1000){
                            soilHumidityView.setText("습함");
                            soilHumidityView.setTextColor(Color.RED);
                            notice.setText(Constant.PLANT_IS_ENGRY4);
                        } else{
                            soilHumidityView.setText("적절");
                            soilHumidityView.setTextColor(Color.BLACK);
                            notice.setText(Constant.PLANT_IS_HAPPY4);
                        }
                        break;
                    case "temperature":
                        temperatureView.setText(snapshot.getValue().toString() + "℃");
                        if(Float.parseFloat(snapshot.getValue().toString()) > 26){
                            temperatureView.setTextColor(Color.RED);
                            notice.setText(Constant.PLANT_IS_HAPPY15);
                        }else if(Float.parseFloat(snapshot.getValue().toString()) > 13){
                            temperatureView.setTextColor(Color.BLUE);
                            notice.setText(Constant.PLANT_IS_ENGRY7);
                        }
                        else{
                            temperatureView.setTextColor(Color.BLACK);
                            notice.setText(Constant.PLANT_IS_HAPPY4);
                        }
                        break;
                    case "waterlevel":
                        if (snapshot.getValue().toString()=="1") {
                            waterView.setText("0");
                            waterView.setTextColor(Color.BLACK);
                        } else {
                            waterView.setText("X");
                            waterView.setTextColor(Color.RED);
                            notice.setText(Constant.PLANT_IS_ENGRY8);
                            }
                        break;
                    default:
                        break;
                }
                    }
            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }
            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(view.getContext(), "Fail to get data.", Toast.LENGTH_SHORT).show();
            }
        };
        mDatabase.addChildEventListener(childEventListener);
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    public void senseDegree(){
        if(Double.parseDouble(soilHumidityView.getText().toString())>=1000.0){//토양 센서 수치 1000이상이면 경고 ->물주세요
            soilHumidityView.setTextColor(Color.parseColor("#FF0057"));

        }
        else{
            soilHumidityView.setTextColor(Color.parseColor("#FF000000"));
        }

        if(Double.parseDouble(airHumidityView.getText().toString())>=70.0){//습도 센서 수치 70이상이면 경고
            airHumidityView.setTextColor(Color.parseColor("#FF0057"));

        }
        else{
            airHumidityView.setTextColor(Color.parseColor("#FF000000"));
        }

        if(Double.parseDouble(temperatureView.getText().toString())>=29.0){//온도 29도 이상 이면 경고 ->온도를 낮춰주세
            temperatureView.setTextColor(Color.parseColor("#FF0057"));

        }
        else{
            temperatureView.setTextColor(Color.parseColor("#FF000000"));
        }

        if(waterView.getText().toString().equals("X")){//없으면 경고 -> 물채워주세요;
            waterView.setTextColor(Color.parseColor("#FF0057"));

        }
        else{
            waterView.setTextColor(Color.parseColor("#FF000000"));
        }

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
                    writeWater(1);
                    break;

                case MotionEvent.ACTION_UP://때면
                    _isBtnDown = false;
                    weatherView.setWeatherData(PrecipType.CLEAR);
                    writeWater(0);
                    break;

                default:
                    break;
            }
            return false;
        }
    };

    public void readData(){
        DatabaseReference mDatabase;//리얼타임
        mDatabase= FirebaseDatabase.getInstance().getReference("data");

        mDatabase.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {
                    Log.d("firebase", String.valueOf(task.getResult().getValue()));
                    State state= task.getResult().getValue(State.class);
                    soilHumidityView.setText(state.getSoilwater().toString());
                    airHumidityView.setText(state.getHumidity().toString());
                    temperatureView.setText(state.getTemperature().toString());
                    if(state.getWaterlevel()==0){
                        waterView.setText("X");
                    }
                    else{
                        waterView.setText("O");
                    }

                }
            }
        });
    }

    public void writeLight(int state){
        FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
        DatabaseReference mDbRef = mDatabase.getReference("post/light");
        mDbRef.setValue(state);
    }

    public void writeWater(int n){
        FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
        DatabaseReference mDbRef = mDatabase.getReference("post/water");
        mDbRef.setValue(n);
    }

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

