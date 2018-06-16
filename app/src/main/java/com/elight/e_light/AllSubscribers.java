package com.elight.e_light;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class AllSubscribers extends AppCompatActivity {

    private FirebaseFirestore mFirestore;
    private ArrayList <String> usersList;
    private TextView total_subscribers;
    private int  totalSubscribers=0;


    @Override
    public void onStart() {
        super.onStart();

        mFirestore.collection("Users").addSnapshotListener(this,new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                if(e != null){
                    Toast.makeText( AllSubscribers.this, "List Retrieval failed." +  e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                usersList= new ArrayList<>();
                for(DocumentChange doc:documentSnapshots.getDocumentChanges()){
                    if(doc.getType() == DocumentChange.Type.ADDED);
                    String userId=doc.getDocument().getId();

//
//                    Users users = doc.getDocument().toObject(Users.class).withId(userId);
//                    if (currentUser == userId){
//                        usersList.clear();
//                    }
                    usersList.add(userId);
//                    usersList.notifyAll();

//                     String name = doc.getDocument().getString("name");
                }
                totalSubscribers =usersList.size();
//                Toast.makeText( AllSubscribers.this,"Tot" + totalNotification, Toast.LENGTH_SHORT).show();
                total_subscribers.setText(String.valueOf(totalSubscribers));
            }
        });
//


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_subscribers);
        total_subscribers =(TextView)findViewById(R.id.total_subscribers);
        mFirestore=FirebaseFirestore.getInstance();




        mFirestore.collection("Users").addSnapshotListener(this,new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                if(e != null){
                    Toast.makeText( AllSubscribers.this, "List Retrieval failed." +  e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                usersList= new ArrayList<>();
                for(DocumentChange doc:documentSnapshots.getDocumentChanges()){
                    if(doc.getType() == DocumentChange.Type.ADDED);
                    String userId=doc.getDocument().getId();

//
//                    Users users = doc.getDocument().toObject(Users.class).withId(userId);
//                    if (currentUser == userId){
//                        usersList.clear();
//                    }
                    usersList.add(userId);
//                    usersList.notifyAll();

//                     String name = doc.getDocument().getString("name");
                }
                totalSubscribers =usersList.size();
//                Toast.makeText( AllSubscribers.this,"Tot" + totalNotification, Toast.LENGTH_SHORT).show();
              total_subscribers.setText(String.valueOf(totalSubscribers));
            }
        });
//
//        Toast.makeText( AllSubscribers.this,"Total subscribers: " + totalNotification, Toast.LENGTH_SHORT).show();

    }
}
