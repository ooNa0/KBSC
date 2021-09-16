package com.example.mysmartgarden;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DecimalFormat;

public class introActivity extends AppCompatActivity {

    private DAO DAO = new DAO();
    private static FirebaseFirestore db = FirebaseFirestore.getInstance();
    private boolean isNewer;
    private String DeviceID;
    private Intent intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.intro); //xml , java 소스 연결
        Handler handler = new Handler();
        DeviceID = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        //isNewer = DAO.isUserExist(DeviceID);

        CollectionReference productRef = db.collection("user");
        //get()을 통해서 해당 컬렉션의 정보를 가져온다.
        productRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(Task<QuerySnapshot> task) {
                //작업이 성공적으로 마쳤을때
                isNewer = false;
                if (task.isSuccessful()) {
                    //컬렉션 아래에 있는 모든 정보를 가져온다.
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Log.v("test", "===========!!!===========");
                        Log.v("test", DeviceID);
                        Log.v("test", document.getId());
                        if((document.getId().toString()).equals(DeviceID)){
                            // Log.v("test", document.getData().toString());
                            Log.v("test", "?!?!?!?!?!");
                            isNewer = true;
                            Log.v("test", "======================news???");
                            Log.v("test", String.valueOf(isNewer));
                        }
                    }
                } else { }
            }
        });
        Log.v("test", "======================news?");
        Log.v("test", String.valueOf(isNewer));

        handler.postDelayed(new Runnable(){
            @Override
            public void run() {
                //isNewer = DAO.isUserExist(DeviceID);
                Log.v("test", String.valueOf(isNewer));


                // ture라면 DeviceID가 이미 있는 것, false라면 없는 것
                if(!isNewer){ // 만약에 디바이스가 처음 등록하는 것이라면?
                    // 처음에 이름이 비어있다면, 이름과 식물의 종을 정하는 페이지로 감
                    Log.v("test", "is first");
                    intent = new Intent (getApplicationContext(), PlantSpinerActivity.class);
                }
                else{
                    // 인트로 실행 후 바로 MainActivity로 넘어감.
                    Log.v("test", "is not first");
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

/*
       CollectionReference productRef = db.collection("user");
            //get()을 통해서 해당 컬렉션의 정보를 가져온다.
            productRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(Task<QuerySnapshot> task) {
                //작업이 성공적으로 마쳤을때
                isNewer = false;
                if (task.isSuccessful()) {
                    //컬렉션 아래에 있는 모든 정보를 가져온다.
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        //document.getData() or document.getId() 등등 여러 방법으로
                        // 만약에 디바이스가 있다면, userExistFlag를 ture로

                        Log.v("test", "===========!!!===========");
                        Log.v("test", DeviceID);
                        Log.v("test", document.getId());

                        if((document.getId().toString()).equals(DeviceID)){
                           // Log.v("test", document.getData().toString());
                            Log.v("test", "?!?!?!?!?!");
                            isNewer = true;
                        }

                    }
                    //그렇지 않을때
                } else { }
            }
        });
        Log.v("test", "======================news?");
        Log.v("test", String.valueOf(isNewer));
 */