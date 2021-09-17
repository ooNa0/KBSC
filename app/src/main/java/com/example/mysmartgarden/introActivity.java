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

        DeviceID = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);

        CollectionReference productRef = db.collection("user");
        //get()을 통해서 해당 컬렉션의 정보를 가져온다.
        productRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
<<<<<<< HEAD
            public void run() {
                // 처음에 이름이 비어있다면, 이름과 식물의 종을 정하는 페이지로 감
                //Intent intent = new Intent (getApplicationContext(), PlantSpinerActivity.class);
                //startActivity(intent);

                //인트로 실행 후 바로 MainActivity로 넘어감.
                Intent intent = new Intent (getApplicationContext(), MainActivity.class);
               startActivity(intent);
=======
            public void onComplete(Task<QuerySnapshot> task) {
                //작업이 성공적으로 마쳤을때
                isNewer = false;
                if (task.isSuccessful()) {
                    //컬렉션 아래에 있는 모든 정보를 가져온다.
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        if ((document.getId().toString()).equals(DeviceID)) {
                            isNewer = true;
                        }
                    }
                }

                // ture라면 DeviceID가 이미 있는 것, false라면 없는 것
                if (!isNewer) { // 만약에 디바이스가 처음 등록하는 것이라면?
                    // 처음에 이름이 비어있다면, 이름과 식물의 종을 정하는 페이지로 감
                    intent = new Intent(getApplicationContext(), PlantSpinerActivity.class);
                    // this -> Activity context
                    // getApplicationContext() -> Application context
                } else {
                    // 인트로 실행 후 바로 MainActivity로 넘어감.
                    intent = new Intent(getApplicationContext(), MainActivity.class);
                }
                intent.putExtra("state", "launch");
                startActivity(intent);
>>>>>>> 6a8d936ed730ab45620d424cb7c0ad1e889d4ed7
                finish();

            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}