package com.example.mysmartgarden;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class PlantSpinerActivity extends AppCompatActivity {

    List<String> categories = new ArrayList<String>();

    //파이어스토어에 접근하기 위한 객체를 생성한다.
    private static FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_app_activity);



        //CollectionReference 는 파이어스토어의 컬렉션을 참조하는 객체다.
        CollectionReference productRef = db.collection("user");

        //get()을 통해서 해당 컬렉션의 정보를 가져온다.
        productRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(Task<QuerySnapshot> task) {
                //작업이 성공적으로 마쳤을때
                if (task.isSuccessful()) {
                    //컬렉션 아래에 있는 모든 정보를 가져온다.
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        //document.getData() or document.getId() 등등 여러 방법으로
                        //데이터를 가져올 수 있다.
                        Log.v("test", document.getData().toString());
                        Log.v("test", "yessss");

                    }
                    //그렇지 않을때
                } else {
                    Log.v("test", "nonono");
                }
            }
        });






        // Spinner 객체 생성
        final Spinner spiner = (Spinner) findViewById(R.id.spinner_plant);

        // 인자로 어댑처 생성
        final String[] string = getResources().getStringArray(R.array.plant_array);
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, string);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spiner.setAdapter(adapter);

        // Spinner 이벤트 리스너
        spiner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                Toast.makeText(getApplicationContext(), "Selected Country: "+ spiner.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }
}
