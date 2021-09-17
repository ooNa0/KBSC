package com.example.mysmartgarden;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class DAO {

    //파이어스토어에 접근하기 위한 객체를 생성한다.
    private static FirebaseFirestore db = FirebaseFirestore.getInstance();
    private boolean userExistFlag;

    public DAO(){ }

    public void SaveUserID(String collectionName, String newDocumentName, String name, String species, String ip){
        DTO userData = new DTO(name, species, ip);
        DocumentReference newUser = db.collection(collectionName).document(newDocumentName);
        newUser.set(userData);
    }

    public void getAllUser(){
        //CollectionReference 는 파이어스토어의 컬렉션을 참조하는 객체다.
        CollectionReference productRef = db.collection("user");

        boolean flag = false;

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
    }
}