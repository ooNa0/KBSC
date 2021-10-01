package com.example.mysmartgarden;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
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
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class Fragment_Observer extends Fragment {

    public String str=null;
    public CalendarView calendarView;
    public Button del_Btn,save_Btn,state_Btn;
    public TextView diaryTextView;
    public EditText contextEditText;
    private TextView state;

    private static FirebaseFirestore db = FirebaseFirestore.getInstance();

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
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstancdState) {
        super.onViewCreated(view, savedInstancdState);
//        calendarView = view.findViewById(R.id.calendarView);//달력
        diaryTextView = view.findViewById(R.id.diaryTextView);//날짜 보여주기
        save_Btn = view.findViewById(R.id.save_Btn);//저장버튼
        del_Btn = view.findViewById(R.id.del_Btn);//삭제버튼
        //cha_Btn = view.findViewById(R.id.cha_Btn);//수정버튼
        state_Btn = view.findViewById(R.id.stateButton);//상태 버튼
        contextEditText = view.findViewById(R.id.contextEditText);//내용버튼
        state = view.findViewById(R.id.plantState);

        MaterialCalendarView materialCalendarView = view.findViewById(R.id.calendarView);
        materialCalendarView.addDecorators(
                new SundayDecorator(),
                new SaturdayDecorator()
        );
        materialCalendarView.setSelectedDate(CalendarDay.today());
        //materialCalendarView.addDecorator(new EventDecorator(Color.GREEN, Collections.singleton(CalendarDay.today())));



//        CollectionReference productRef = db.collection("user").document(userSingleton.getDevice()).collection("observation");
//        HashSet<CalendarDay> set = new HashSet<CalendarDay>();
//        productRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                //작업이 성공적으로 마쳤을때
//                if (task.isSuccessful()) {
//                    //컬렉션 아래에 있는 모든 정보를 가져온다.
//                    for (QueryDocumentSnapshot document : task.getResult()) {
//                        //document.getData() or document.getId() 등등 여러 방법으로
//                        //데이터를 가져올 수 있다.
//                        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
//                        Date date = null;
//                        try {
//                            Log.v("date", document.getId());
//                            date = formatter.parse(document.getId());
//                            Log.v("date", date.toString());
//                            set.add(CalendarDay.from(date));
//
//
//                            //Log.v("date", "set ====" + set.toString());
//                            Log.v("date", "calendar ====" +  CalendarDay.from(date));
//                        } catch (ParseException e) {}
//                    }
//                    //materialCalendarView.addDecorator(new EventDecorator(set));
//                    //materialCalendarView.addDecorator(new EventDecorator(Collections.singleton(set)));
//                    //materialCalendarView.addDecorator(new EventDecorator(Collections.singleton(date)));
//
//                    //그렇지 않을때
//                } else {
//
//                }
//            }
//        });

        materialCalendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                diaryTextView.setVisibility(View.VISIBLE);
                save_Btn.setVisibility(View.VISIBLE);
                contextEditText.setVisibility(View.VISIBLE);
                state.setVisibility(View.VISIBLE);
                state_Btn.setVisibility(View.VISIBLE);

                Log.v("date", "-------" + date.toString());
                int year = date.getYear();
                int month = date.getMonth();
                int dayOfMonth = date.getDay();

                String dbname= String.format("%d - %d - %d",year,month+1,dayOfMonth);//다이어리 시간 보여주는 텍스트뷰 설정
                diaryTextView.setText(dbname);

                String doname=String.format("%d%d%d",year,month+1,dayOfMonth);//db 문서 이름 정하기
                Log.v("date", doname);

                DocumentReference docRef = db.collection("user").document(userSingleton.getDevice()).collection("observation").document(doname);//불러오기

                docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            Log.v("docu", document.toString());
                            if (document.exists()) {
                                Observation observation = document.toObject(Observation.class);
                                if(observation.getYear()==year && observation.getMonth()==month+1 && observation.getDay()==dayOfMonth){//있을때
                                    contextEditText.setText(observation.getInfo());
                                    //cha_Btn.setVisibility(View.VISIBLE);
                                    del_Btn.setVisibility(View.VISIBLE);
                                    save_Btn.setVisibility(View.VISIBLE);
                                    state_Btn.setText(observation.getState());
                                    existDay(year,month+1,dayOfMonth);
                                }
                                else{//없으
                                    contextEditText.setText("");
                                    //cha_Btn.setVisibility(View.INVISIBLE);
                                    del_Btn.setVisibility(View.INVISIBLE);
                                    save_Btn.setVisibility(View.VISIBLE);
                                    state_Btn.setText("✖");
                                    noneDay(year,month+1,dayOfMonth);
                                }

                            } else {//없으면
                                contextEditText.setText("");
                                //cha_Btn.setVisibility(View.INVISIBLE);
                                del_Btn.setVisibility(View.INVISIBLE);
                                save_Btn.setVisibility(View.VISIBLE);
                                state_Btn.setText("✖");
                                noneDay(year,month+1,dayOfMonth);
                            }
                        } else {
                            contextEditText.setText("");
                            //cha_Btn.setVisibility(View.INVISIBLE);
                            del_Btn.setVisibility(View.INVISIBLE);
                            save_Btn.setVisibility(View.VISIBLE);
                            state_Btn.setText("✖");
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
                                //info.setText("좋음");
                                info.setText("\uD83D\uDE00");
                                break;
                            case R.id.radio_button_soso:
                                //info.setText("중간");
                                info.setText("\uD83D\uDE36");
                                break;
                            case R.id.radio_button_bad:
                                //info.setText("나쁨");
                                info.setText("\uD83D\uDE1E");
                                break;
                            default:
                                info.setText("✖");
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
                        state_Btn.setText("✖");
                    }
                });
            }
        });

        save_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                save_Btn.setVisibility(View.VISIBLE);
                //cha_Btn.setVisibility(View.VISIBLE);
                del_Btn.setVisibility(View.VISIBLE);
                contextEditText.setVisibility(View.VISIBLE);
                state.setVisibility(View.VISIBLE);
                str=contextEditText.getText().toString();

                Map<String,Object> obser = new HashMap<>();
                obser.put("info",str);
                obser.put("year",nYear);
                obser.put("month",nMonth);
                obser.put("day",nDay);
                String state = state_Btn.getText().toString();
//                if(state=="상태"){
//                    obser.put("state","모름");
//                }
//                else{
//                    obser.put("state",state);
//                }

                //String doname=String.format("%04d%02d%02d",nYear,nMonth,nDay);
                obser.put("name",userSingleton.getName());

                FirebaseFirestore db = FirebaseFirestore.getInstance();
                db.collection("user").document(userSingleton.getDevice()).collection("observation").document(""+nYear+nMonth+nDay).set(obser).addOnSuccessListener(new OnSuccessListener<Void>() { // 제대로 인서트 되면 함수의 주소를 가지고 있다가 넘겨줌.
                    @Override
                    public void onSuccess(Void aVoid) {
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
                                info.setText("좋음☺");
                                break;
                            case R.id.radio_button_soso:
                                info.setText("중간\uD83D\uDE10");
                                break;
                            case R.id.radio_button_bad:
                                info.setText("나쁨☹");
                                break;
                            default:
                                info.setText("✖");
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
                        state_Btn.setText("✖");
                    }
                });
            }
        });

        del_Btn.setOnClickListener(new View.OnClickListener() {//삭제
            @Override
            public void onClick(View view) {

                contextEditText.setText("");
                contextEditText.setVisibility(View.VISIBLE);
                state.setVisibility(View.VISIBLE);
                save_Btn.setVisibility(View.VISIBLE);
                del_Btn.setVisibility(View.INVISIBLE);

                FirebaseFirestore db = FirebaseFirestore.getInstance();

                db.collection("user").document(userSingleton.getDevice()).collection("observation").document(""+eYear+eMonth+eDay)
                        .delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d("TAG", "DocumentSnapshot successfully deleted!");
                                state_Btn.setText("✖");
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
