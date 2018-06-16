package com.elight.e_light;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class QuotesActivity extends AppCompatActivity {


    RecyclerView allQuotesRV;
    QuotesAdapter qoutesAdapter;
    List<Quotes> mQuotesList;
    private FirebaseFirestore mFireStore;
    private String currentUserId;
    private String priority;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quotes);
        mFireStore= FirebaseFirestore.getInstance();
        mFireStore= FirebaseFirestore.getInstance();
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build();
        mFireStore.setFirestoreSettings(settings);

        currentUserId= FirebaseAuth.getInstance().getCurrentUser().getUid();
        mQuotesList =new ArrayList<>();
//        if(mQuotesList==null){
//          progressBar.setVisibility(View.VISIBLE);
        allQuotesRV =(RecyclerView)findViewById(R.id.rvquotes);
        qoutesAdapter=new QuotesAdapter(this, mQuotesList);


        allQuotesRV.setHasFixedSize(true);
        allQuotesRV.setLayoutManager(new LinearLayoutManager(this));
        allQuotesRV.setAdapter(qoutesAdapter);
        priority = getIntent().getStringExtra("priority");

//        }
//
//        else{


//            Toast.makeText(QuotesActivity.this, "Userid :" + currentUserId , Toast.LENGTH_SHORT).show();
//            Toast.makeText(QuotesActivity.this, "RecivedData" , Toast.LENGTH_SHORT).show();

            int number=  mQuotesList.size();




//            Toast.makeText(QuotesActivity.this, "Visibility gone"  , Toast.LENGTH_SHORT).show();


//        document(currentUserId).collection("Notifications").

        if (priority.equals("0")) {
            mFireStore.collection("Notifications").addSnapshotListener(this, new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                    if (e != null) {
                        Toast.makeText(QuotesActivity.this, "List Retrieval failed." + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    for (DocumentChange doc : documentSnapshots.getDocumentChanges()) {
//                     if(doc.getType() == DocumentChange.Type.ADDED);
                        Quotes quotes = doc.getDocument().toObject(Quotes.class);
//                    Toast.makeText(QuotesActivity.this, "Extracted all data " , Toast.LENGTH_SHORT).show();

                        mQuotesList.add(quotes);
                        int number = mQuotesList.size();
//                    Toast.makeText(QuotesActivity.this, "added Quotes " + number , Toast.LENGTH_SHORT).show();
                        qoutesAdapter.notifyDataSetChanged();

//                     String name = doc.getDocument().getString("name");
                    }
                }
            });
        }

        else{
            //Since it is random quotes (priority=0) then query with the priority
            mFireStore.collection("Notifications")
                    .whereGreaterThanOrEqualTo("Priority", 0)
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                Quotes quotes = documentSnapshot.toObject(Quotes.class);

                                mQuotesList.add(quotes);

                                qoutesAdapter.notifyDataSetChanged();
                            }
                        }
                    });


        }

//        }



    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_catalog.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_catalog, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {


            // Respond to a click on the "Insert dummy data" menu option
            case R.id.logout:
                AlertDialog.Builder  builder = new AlertDialog.Builder(QuotesActivity.this);
                builder.setTitle("Confirm Logout");
                builder.setMessage("Are you sure you want to Logout?");
//                builder.setIcon(R.drawable.logo);

                builder.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
//                        Toast.makeText(getApplicationContext(), "Pressed Yes button", Toast.LENGTH_LONG).show();

                    }
                });
                builder.setNegativeButton("Logout", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        AuthUI.getInstance()
                                .signOut(getBaseContext())
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    public void onComplete(@NonNull Task<Void> task) {
                                        // user is now signed out
                                        startActivity(new Intent(QuotesActivity.this, HomeActivity.class));

//                                        Toast.makeText(getApplicationContext(), "Pressed No button", Toast.LENGTH_LONG).show();
                                    }
                                });



                    }
                });
                AlertDialog alert= builder.create();
                alert.show();
//                Map<String,Object> tokenMap= new HashMap<>();
//                tokenMap.put("token_id", FieldValue.delete());
//
//                mFireStore.collection("Users").document(currentUserId).update(tokenMap).addOnSuccessListener(new OnSuccessListener<Void>() {
//                    @Override
//                    public void onSuccess(Void aVoid) {
////                        AuthUI.getInstance().signOut(getBaseContext());
////                        Intent loginOut= new Intent(QuotesActivity.this,HomeActivity.class);
////                        startActivity(loginOut);
////                        finish();
//
//
//                    }
//                });

                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
