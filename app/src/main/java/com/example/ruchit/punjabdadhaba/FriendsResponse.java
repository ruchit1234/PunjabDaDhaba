package com.example.ruchit.punjabdadhaba;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.IgnoreExtraProperties;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

@IgnoreExtraProperties
public class FriendsResponse {
     FirebaseFirestore db;
    public String getTable1() {
        return Table1;
    }

    public void setTable1(String table1) {
        Table1 = table1;
    }

    private String Table1;


    public FriendsResponse() {

    }

    public FriendsResponse(String Table1) {
        this.Table1 = Table1;
    }


}