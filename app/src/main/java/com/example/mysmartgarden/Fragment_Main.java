package com.example.mysmartgarden;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.github.matteobattilana.weather.PrecipType;
import com.github.matteobattilana.weather.WeatherView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Fragment_Main extends Fragment {

    int clickedSun;
    private boolean _isBtnDown;
    LinearLayout main_back;


    FirebaseFirestore db;

    ImageButton sun;
    ImageButton plant;

    WeatherView weatherView;

    TextView withday,notice,info1,info2,info3,info4;

    List<User> list;

    String name,ip,species;


    public Fragment_Main(){

    }


    // Store instance variables ba
    // sed on arguments passed
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    // Inflate the view for the fragment based on layout XML
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_fragment,container,false);

        return view;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstancdState){
        super.onViewCreated(view,savedInstancdState);

        list=new ArrayList<>();

        db = FirebaseFirestore.getInstance();

        withday=view.findViewById(R.id.withday);

        DocumentReference docRef = db.collection("user").document("RWX5pwrnPCqHsxiLGPEe");
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        User user = document.toObject(User.class);
                        Log.d("TAG", "DocumentSnapshot data: " + document.getData());



                        name=user.getName();

                        withday.setText(name+"와 함게한지\n 13일");
                        Log.d("TAG",name);

                    } else {
                        Log.d("TAG", "No such document");
                    }
                } else {
                    Log.d("TAG", "get failed with ", task.getException());
                }
            }
        });

        clickedSun=1;
        main_back=view.findViewById(R.id.main_back);//맨뒤 배경

        sun=view.findViewById(R.id.sun);//이미지 버튼
        plant=view.findViewById(R.id.plant);
        Glide.with(this).load(R.raw.sun).into(sun);
        Glide.with(this).load(R.raw.plant).into(plant);

        weatherView = view.findViewById(R.id.weather_view);

        plant.setOnTouchListener(onBtnTouchListener);

        sun.setOnClickListener(new View.OnClickListener(){//태양 클릭 시
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View view){
                if(clickedSun==1) {//최대->중
                    main_back.setBackgroundColor(Color.parseColor("#A3A3A3"));
                    clickedSun++;
                }
                else if(clickedSun==2){//중->소
                    main_back.setBackgroundColor(Color.parseColor("#323232"));
                    clickedSun++;
                }
                else if(clickedSun==3){//소->꺼짐
                    main_back.setBackgroundColor(Color.parseColor("#1C1C1C"));
                    clickedSun++;
                }
                else if(clickedSun==4){//꺼짐->최대
                    main_back.setBackgroundColor(Color.WHITE);
                    clickedSun=1;
                }

            }


        });


        
        notice=view.findViewById(R.id.notice);//알림
        info1=view.findViewById(R.id.info1);//토양습도
        info2=view.findViewById(R.id.info2);//습도
        info3=view.findViewById(R.id.info3);//온도
        info4=view.findViewById(R.id.info4);//물통양
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

}

