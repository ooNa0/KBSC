package com.example.mysmartgarden;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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

import static android.content.Context.MODE_NO_LOCALIZED_COLLATORS;

public class Fragment_Observer extends Fragment {

    public String fname=null;
    public String str=null;
    public CalendarView calendarView;
    public Button cha_Btn,del_Btn,save_Btn;
    public TextView diaryTextView;
    public EditText contextEditText;

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
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstancdState){
        super.onViewCreated(view,savedInstancdState);
        calendarView=view.findViewById(R.id.calendarView);//달력
        diaryTextView=view.findViewById(R.id.diaryTextView);//날짜 보여주기
        save_Btn=view.findViewById(R.id.save_Btn);//저장버튼
        del_Btn=view.findViewById(R.id.del_Btn);//삭제버튼
        cha_Btn=view.findViewById(R.id.cha_Btn);//수정버튼

        contextEditText=view.findViewById(R.id.contextEditText);//내용버튼


        
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                diaryTextView.setVisibility(View.VISIBLE);
                save_Btn.setVisibility(View.VISIBLE);
                contextEditText.setVisibility(View.VISIBLE);

                String dbname= String.format("%d - %d - %d",year,month+1,dayOfMonth);
                diaryTextView.setText(dbname);

                String doname=String.format("%d%d%d",year,month+1,dayOfMonth);

                FirebaseFirestore db = FirebaseFirestore.getInstance();

                DocumentReference docRef = db.collection("observation").document(doname);
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
                                }
                                else{
                                    contextEditText.setText("");
                                    cha_Btn.setVisibility(View.INVISIBLE);
                                    del_Btn.setVisibility(View.INVISIBLE);
                                    save_Btn.setVisibility(View.VISIBLE);
                                    noneDay(year,month+1,dayOfMonth);
                                }

                            } else {
                                Log.d("TAG", "No such document");
                                Log.d("TAG", "get failed with ", task.getException());
                                contextEditText.setText("");
                                cha_Btn.setVisibility(View.INVISIBLE);
                                del_Btn.setVisibility(View.INVISIBLE);
                                save_Btn.setVisibility(View.VISIBLE);
                                noneDay(year,month+1,dayOfMonth);
                            }
                        } else {
                            Log.d("TAG", "get failed with ", task.getException());
                            contextEditText.setText("");
                            cha_Btn.setVisibility(View.INVISIBLE);
                            del_Btn.setVisibility(View.INVISIBLE);
                            save_Btn.setVisibility(View.VISIBLE);
                            noneDay(year,month+1,dayOfMonth);
                        }
                    }
                });

            }
        });

    }

    public void noneDay(int nYear,int nMonth, int nDay){


        save_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveDiary(fname);
                str=contextEditText.getText().toString();

                save_Btn.setVisibility(View.INVISIBLE);
                cha_Btn.setVisibility(View.VISIBLE);
                del_Btn.setVisibility(View.VISIBLE);
                contextEditText.setVisibility(View.INVISIBLE);

                Map<String,Object> obser = new HashMap<>();
                obser.put("info",str);
                obser.put("year",nYear);
                obser.put("month",nMonth);
                obser.put("day",nDay);
                obser.put("state","good");
                obser.put("name","NA0");

                FirebaseFirestore db = FirebaseFirestore.getInstance();

                db.collection("observation").document(""+nYear+nMonth+nDay).set(obser).addOnSuccessListener(new OnSuccessListener<Void>() { // 제대로 인서트 되면 함수의 주소를 가지고 있다가 넘겨줌.
                    @Override
                    public void onSuccess(Void aVoid) {
                        // 로그인 성공시 intent로 화면전환
                        Log.d("TAG", "save : onSuccess: 인서트 잘됨");
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



        cha_Btn.setOnClickListener(new View.OnClickListener() {//수정
            @Override
            public void onClick(View view) {
                contextEditText.setVisibility(View.VISIBLE);

                contextEditText.setText(str);

                save_Btn.setVisibility(View.VISIBLE);
                cha_Btn.setVisibility(View.INVISIBLE);
                del_Btn.setVisibility(View.INVISIBLE);

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
                removeDiary(fname);
            }
        });
    }

    /*public void  checkDay(int cYear, int cMonth, int cDay){
        fname=""+cYear+"-"+(cMonth+1)+""+"-"+cDay+".txt";//저장할 파일 이름설정
        FileInputStream fis=null;//FileStream fis 변수

        try{
            fis=getActivity().openFileInput(fname);

            byte[] fileData=new byte[fis.available()];
            fis.read(fileData);
            fis.close();

            str=new String(fileData);

            contextEditText.setVisibility(View.INVISIBLE);

            save_Btn.setVisibility(View.INVISIBLE);
            cha_Btn.setVisibility(View.VISIBLE);
            del_Btn.setVisibility(View.VISIBLE);




        }catch (Exception e){
            e.printStackTrace();
        }
    }*/

    @SuppressLint("WrongConstant")
    public void removeDiary(String readDay){
        FileOutputStream fos=null;

        try{
            fos=getActivity().openFileOutput(readDay,MODE_NO_LOCALIZED_COLLATORS);
            String content="";
            fos.write((content).getBytes());
            fos.close();

        }catch (Exception e){
            e.printStackTrace();
        }
    }
    @SuppressLint("WrongConstant")
    public void saveDiary( String readDay){
        FileOutputStream fos=null;

        try{
            fos=getActivity().openFileOutput(readDay,MODE_NO_LOCALIZED_COLLATORS);
            String content=contextEditText.getText().toString();
            fos.write((content).getBytes());
            fos.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }


}
