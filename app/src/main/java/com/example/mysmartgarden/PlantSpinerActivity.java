package com.example.mysmartgarden;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
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


    private DAO DAO = new DAO();
    Singleton userSingleton =Singleton.getInstance();
    List<String> categories = new ArrayList<String>();
    private EditText editText;
    private Spinner plant_spinner;
    private String plant_species;
    private String plant_name;
    private String plant_ip;
    private String DeviceID;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_app_activity);

        // Spinner 객체 생성
        final Spinner spiner = (Spinner) findViewById(R.id.ip_spinner_plant);

        // 인자로 어댑처 생성
        final String[] string = getResources().getStringArray(R.array.plant_array);
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, string);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spiner.setAdapter(adapter);

        // Spinner 이벤트 리스너
        spiner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                Toast.makeText(getApplicationContext(), "Selected : "+ spiner.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    //onclick event 메서드
    public void onclick(View view){

        DeviceID = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);

        plant_spinner = (Spinner)findViewById(R.id.ip_spinner_plant);
        plant_species = plant_spinner.getSelectedItem().toString();

        editText = (EditText)findViewById(R.id.input_plant_name);
        plant_name = editText.getText().toString();

        editText = (EditText)findViewById(R.id.input_ip);
        plant_ip = editText.getText().toString();

        // 데이터를 새로 입력받고, 문서로 데이터를 저장해준다.
        DTO DTO = new DTO(DeviceID,plant_name, plant_species, plant_ip);
        DAO.SaveUserID("user", DeviceID, plant_name, plant_species, plant_ip);

        intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.putExtra("name", plant_name);
        intent.putExtra("species", plant_species);
        intent.putExtra("ip", plant_ip);

        userSingleton.setDevice(DeviceID);
        userSingleton.setName(plant_name);
        userSingleton.setIp(plant_ip);
        userSingleton.setSpecies(plant_species);

        startActivity(intent);
        finish();
    }
}