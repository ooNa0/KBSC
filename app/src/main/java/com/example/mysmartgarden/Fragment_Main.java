package com.example.mysmartgarden;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
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
import androidx.appcompat.app.AlertDialog;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class Fragment_Main extends Fragment {

    int clickedSun;
    private boolean _isBtnDown;
    LinearLayout main_back;

    Singleton userSingleton = Singleton.getInstance();

    FirebaseFirestore db;

    ImageButton sun;//태양버튼
    ImageButton plant;// 식물버튼-> 물주기

    WeatherView weatherView;//뒤에 물 주는 배경

    TextView notice,info1,info2,info3,info4;

    // 값 xml 파일 값 settext를 위한 빌드업
    TextView plantName;
    TextView withday;

    List<User> list;

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

        list=new ArrayList<>();

        db = FirebaseFirestore.getInstance();

        withday=view.findViewById(R.id.day);// 함께한 일자
        plantName=view.findViewById(R.id.HelloPlantname);// 이름

        withday.setText("13"); // 이거 처음에 intro에서 입력한 날짜 타임스탬프로 찍어서 계산해주면 되려나
        plantName.setText(userSingleton.getName());

        /*
        DocumentReference docRef = db.collection("user").document("RWX5pwrnPCqHsxiLGPEe");//회원정보 불러오
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {//있으면
                        User user = document.toObject(User.class);
                        Log.d("TAG", "DocumentSnapshot data: " + document.getData());

                        name=user.getName();//일름받고 설정해주
                        long day = 0;
                        try {
                            day=dayCalculator(user.getDate());
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        withday.setText(name+"와 함께한지\n "+day+"일");
                        Log.d("TAG",name);

                    } else {
                        Log.d("TAG", "No such document");
                    }
                } else {
                    Log.d("TAG", "get failed with ", task.getException());
                }
            }
        });*/

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

    public long dayCalculator(String date1) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd"); //수정가능 // date1, date2 두 날짜를 parse()를 통해 Date형으로 변환.
        Date FirstDate = format.parse(date1); //지정한날(금연!!!!!!!!!!!!!!!!!!!! 시작날)
        long now =System.currentTimeMillis();
        Date SecondDate= new Date(now);//현재 날짜 // Date로 변환된 두 날짜를 계산한 뒤 그 리턴값으로 long type 변수를 초기화 하고 있다. // 연산결과 -950400000. long type 으로 return 된다.
        String getDay=format.format(SecondDate);
        Date mDate=format.parse(getDay);

        long calDate = mDate.getTime() - FirstDate.getTime();
        long lastCalDate = calDate/10; //연산 후에는 0이 하나 더 추가되어, 이렇게 10으로 나누어 준다.
        Log.d("칼데이트", String.valueOf(FirstDate));
        Log.d("칼데이트", String.valueOf(SecondDate));
        Log.d("칼데이트", String.valueOf(calDate));
        Log.d("칼데이트", String.valueOf(lastCalDate));
        return lastCalDate;


    }



}

