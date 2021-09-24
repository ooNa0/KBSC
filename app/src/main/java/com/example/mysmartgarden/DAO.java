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

    public void SaveUserID(String collectionName, String newDocumentName, String name, String species, String ip, String entry){
        DTO userData = new DTO(newDocumentName,name, species, ip, entry);
        DocumentReference newUser = db.collection(collectionName).document(newDocumentName);
        newUser.set(userData);
    }
}