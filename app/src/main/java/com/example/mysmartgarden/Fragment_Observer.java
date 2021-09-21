package com.example.mysmartgarden;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



public class Fragment_Observer extends Fragment {


    public String str=null;
    public CalendarView calendarView;
    public Button cha_Btn,del_Btn,save_Btn,state_Btn;
    public TextView diaryTextView;
    public EditText contextEditText;
    Singleton userSingleton = Singleton.getInstance();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    // Inflate the view for the fragment based on layout XML
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.obser_fragment,container,false);
        return view;
    }
    @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstancdState){
        super.onViewCreated(view,savedInstancdState);
        calendarView=view.findViewById(R.id.calendarView);//달력
        diaryTextView=view.findViewById(R.id.diaryTextView);//날짜 보여주기
        save_Btn=view.findViewById(R.id.save_Btn);//저장버튼
        del_Btn=view.findViewById(R.id.del_Btn);//삭제버튼
        cha_Btn=view.findViewById(R.id.cha_Btn);//수정버튼
        state_Btn=view.findViewById(R.id.stateButton);//상태 버튼
        contextEditText=view.findViewById(R.id.contextEditText);//내용버튼


        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                diaryTextView.setVisibility(View.VISIBLE);
                save_Btn.setVisibility(View.VISIBLE);
                contextEditText.setVisibility(View.VISIBLE);
                state_Btn.setVisibility(View.VISIBLE);

                String dbname= String.format("%d - %d - %d",year,month+1,dayOfMonth);//다이어리 시간 보여주는 텍스트뷰 설정
                diaryTextView.setText(dbname);

                String doname=String.format("%d%d%d",year,month+1,dayOfMonth);//db 문서 이름 정하기

                FirebaseFirestore db = FirebaseFirestore.getInstance();

                DocumentReference docRef = db.collection("user").document(userSingleton.getDevice()).collection("observation").document(doname);//불러오기
                docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                Observation observation = document.toObject(Observation.class);
                                Log.d("TAG", "DocumentSnapshot data: " + document.getData());

                                Log.d("TAG", String.valueOf(observation));

                                if(observation.getYear()==year && observation.getMonth()==month+1 && observation.getDay()==dayOfMonth){//있을때
                                    contextEditText.setText(observation.getInfo());
                                    cha_Btn.setVisibility(View.VISIBLE);
                                    del_Btn.setVisibility(View.VISIBLE);
                                    save_Btn.setVisibility(View.INVISIBLE);
                                    state_Btn.setText(observation.getState());
                                    existDay(year,month+1,dayOfMonth);
                                }
                                else{//없으
                                    contextEditText.setText("");
                                    cha_Btn.setVisibility(View.INVISIBLE);
                                    del_Btn.setVisibility(View.INVISIBLE);
                                    save_Btn.setVisibility(View.VISIBLE);
                                    state_Btn.setText("상태");
                                    noneDay(year,month+1,dayOfMonth);
                                }

                            } else {//없으면
                                Log.d("TAG", "No such document");
                                Log.d("TAG", "get failed with ", task.getException());
                                contextEditText.setText("");
                                cha_Btn.setVisibility(View.INVISIBLE);
                                del_Btn.setVisibility(View.INVISIBLE);
                                save_Btn.setVisibility(View.VISIBLE);
                                state_Btn.setText("상태");
                                noneDay(year,month+1,dayOfMonth);
                            }
                        } else {
                            Log.d("TAG", "get failed with ", task.getException());
                            contextEditText.setText("");
                            cha_Btn.setVisibility(View.INVISIBLE);
                            del_Btn.setVisibility(View.INVISIBLE);
                            save_Btn.setVisibility(View.VISIBLE);
                            state_Btn.setText("상태");
                            noneDay(year,month+1,dayOfMonth);
                        }
                    }
                });

            }
        });

    }

    public void noneDay(int nYear,int nMonth, int nDay){
        state_Btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
               Dialog dialog = new Dialog(view.getContext());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // 타이틀 제거
                dialog.setContentView(R.layout.dialog);             // xml 레이아웃 파일과 연결

                dialog.show();
                TextView info =dialog.findViewById(R.id.info);

                RadioGroup radio = dialog.findViewById(R.id.radio);
                radio.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener(){

                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        switch(checkedId){
                            case R.id.radio_button_good:
                                info.setText("좋음");
                                break;
                            case R.id.radio_button_soso:
                                info.setText("중간");
                                break;
                            case R.id.radio_button_bad:
                                info.setText("나쁨");
                                break;
                            default:
                                info.setText("모름");
                                break;

                        }
                    }
                });
                Button decide=dialog.findViewById(R.id.decide);
                decide.setOnClickListener(new View.OnClickListener(){

                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        String str=info.getText().toString();
                        state_Btn.setText(str);
                    }
                });
                Button cancle = dialog.findViewById(R.id.cancle);
                cancle.setOnClickListener(new View.OnClickListener(){


                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        state_Btn.setText("상태");
                    }
                });



            }
        });

        save_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                save_Btn.setVisibility(View.INVISIBLE);
                cha_Btn.setVisibility(View.VISIBLE);
                del_Btn.setVisibility(View.VISIBLE);
                contextEditText.setVisibility(View.VISIBLE);
                str=contextEditText.getText().toString();

                Map<String,Object> obser = new HashMap<>();
                obser.put("info",str);
                obser.put("year",nYear);
                obser.put("month",nMonth);
                obser.put("day",nDay);
                String state = state_Btn.getText().toString();
                if(state=="상태"){
                    obser.put("state","모름");
                }
                else{
                    obser.put("state",state);
                }

                obser.put("name",userSingleton.getName());

                FirebaseFirestore db = FirebaseFirestore.getInstance();

                db.collection("user").document(userSingleton.getDevice()).collection("observation").document(""+nYear+nMonth+nDay).set(obser).addOnSuccessListener(new OnSuccessListener<Void>() { // 제대로 인서트 되면 함수의 주소를 가지고 있다가 넘겨줌.
                    @Override
                    public void onSuccess(Void aVoid) {
                        // 로그인 성공시 intent로 화면전환
                        Log.d("TAG", "save : onSuccess: 인서트 잘됨");
                        Toast.makeText(view.getContext(),"저장 완료.",Toast.LENGTH_SHORT).show();
                    }
                })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d("TAG", "onFailure: " + e.getMessage());
                            }
                        });



            }
        });
    }

    public void existDay(int eYear, int eMonth, int eDay){

        state_Btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Dialog dialog = new Dialog(view.getContext());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // 타이틀 제거
                dialog.setContentView(R.layout.dialog);             // xml 레이아웃 파일과 연결

                dialog.show();
                TextView info =dialog.findViewById(R.id.info);

                RadioGroup radio = dialog.findViewById(R.id.radio);
                radio.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener(){

                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        switch(checkedId){
                            case R.id.radio_button_good:
                                info.setText("좋음");
                                break;
                            case R.id.radio_button_soso:
                                info.setText("중간");
                                break;
                            case R.id.radio_button_bad:
                                info.setText("나쁨");
                                break;
                            default:
                                info.setText("모름");
                                break;

                        }
                    }
                });
                Button decide=dialog.findViewById(R.id.decide);
                decide.setOnClickListener(new View.OnClickListener(){

                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        String str=info.getText().toString();
                        state_Btn.setText(str);
                    }
                });
                Button cancle = dialog.findViewById(R.id.cancle);
                cancle.setOnClickListener(new View.OnClickListener(){


                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        state_Btn.setText("상태");
                    }
                });



            }
        });

        cha_Btn.setOnClickListener(new View.OnClickListener() {//수정
            @Override
            public void onClick(View view) {
                contextEditText.setVisibility(View.VISIBLE);

                str=contextEditText.getText().toString();

                save_Btn.setVisibility(View.INVISIBLE);
                cha_Btn.setVisibility(View.VISIBLE);
                del_Btn.setVisibility(View.VISIBLE);

                Map<String,Object> obser = new HashMap<>();
                obser.put("info",str);
                obser.put("year",eYear);
                obser.put("month",eMonth);
                obser.put("day",eDay);

                String state = state_Btn.getText().toString();

                if(state=="상태"){
                    obser.put("state","모름");
                }
                else{
                    obser.put("state",state);
                }
                obser.put("name",userSingleton.getName());

                FirebaseFirestore db = FirebaseFirestore.getInstance();

                db.collection("user").document(userSingleton.getDevice()).collection("observation").document(""+eYear+eMonth+eDay).set(obser).addOnSuccessListener(new OnSuccessListener<Void>() {

                    public void onSuccess(Void aVoid) {
                        // 로그인 성공시 intent로 화면전환
                        Log.d("TAG", "save : onSuccess: 인서트 잘됨");

                        Toast.makeText(view.getContext(),"수정 완료.",Toast.LENGTH_SHORT).show();
                    }
                })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d("TAG", "onFailure: " + e.getMessage());
                            }
                        });


            }

        });
        del_Btn.setOnClickListener(new View.OnClickListener() {//삭제
            @Override
            public void onClick(View view) {

                contextEditText.setText("");
                contextEditText.setVisibility(View.VISIBLE);
                save_Btn.setVisibility(View.VISIBLE);
                cha_Btn.setVisibility(View.INVISIBLE);
                del_Btn.setVisibility(View.INVISIBLE);

                FirebaseFirestore db = FirebaseFirestore.getInstance();

                db.collection("user").document(userSingleton.getDevice()).collection("observation").document(""+eYear+eMonth+eDay)
                        .delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d("TAG", "DocumentSnapshot successfully deleted!");
                                state_Btn.setText("상태");
                                Toast.makeText(view.getContext(),"삭제 완료.",Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w("TAG", "Error deleting document", e);
                            }
                        });

            }
        });
    }




}
