package com.elight.e_light;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class NotificationActivity extends AppCompatActivity {

    TextView messageView;
    TextView authorsView;
    TextView  hashtag;
    String datamessage;
    String  dataFrom;
    String authorsMessage;
    String  authorsName;
    private FirebaseFirestore mFireStore;
    List<Quotes>todaysQuote;
    int toatalQuotes;
    Quotes quotes;
    private String datamessagesFBase;
    private String dataFromsFBase;
    private int goal;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        mFireStore= FirebaseFirestore.getInstance();
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build();
        mFireStore.setFirestoreSettings(settings);

        todaysQuote = new ArrayList<>();

        messageView = (TextView)findViewById(R.id.notification_message);
        authorsView = (TextView)findViewById(R.id.notification_from);
        hashtag = (TextView)findViewById(R.id.hashtag);


        datamessage= getIntent().getStringExtra("message");
        dataFrom=getIntent().getStringExtra("authors_name");


         authorsMessage= getIntent().getStringExtra("authors__message");
        authorsName=getIntent().getStringExtra("authors__name");



        if (authorsMessage == null && authorsName == null  && datamessage == null ){

            Toast.makeText(NotificationActivity.this,"everything is empty ", Toast.LENGTH_SHORT).show();

            SharedPreferences preferences = getSharedPreferences("StoreNotification",MODE_PRIVATE);

            datamessagesFBase = preferences.getString("message","");
             dataFromsFBase = preferences.getString("authors_name","");

            if (datamessagesFBase != null && dataFromsFBase != null){





            Toast.makeText(NotificationActivity.this, "Fixing the issue ", Toast.LENGTH_SHORT).show();

                mFireStore.collection("Notifications")
                        .whereGreaterThanOrEqualTo("Priority", 0)
                        .get()
                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                    quotes = documentSnapshot.toObject(Quotes.class);

                                    todaysQuote.add(quotes);


                                }
                                toatalQuotes= todaysQuote.size();

                                Toast.makeText(NotificationActivity.this, "Total quotes: " + toatalQuotes , Toast.LENGTH_SHORT).show();

                                toatalQuotes--;

                                datamessagesFBase= todaysQuote.get(toatalQuotes).getMessage();
                                dataFromsFBase=todaysQuote.get(toatalQuotes).getFrom();


                                SharedPreferences preferences = getSharedPreferences("StoreNotification",MODE_PRIVATE);
                                SharedPreferences.Editor editor = preferences.edit();
                                editor.putString("message", datamessagesFBase);
                                editor.putString("authors_name", dataFromsFBase);
                                editor.apply();

                                Toast.makeText(NotificationActivity.this, "Done adding to sharedPreferences", Toast.LENGTH_SHORT).show();

                                Toast.makeText(NotificationActivity.this, "Today's quote number \n  " +  toatalQuotes + "  message: \n " + datamessagesFBase + "name: "+ dataFromsFBase, Toast.LENGTH_SHORT).show();


                                messageView.setText(datamessagesFBase);
                                authorsView.setText(dataFromsFBase);
                                hashtag.setVisibility(View.VISIBLE);
                            }

                        });


               
//

////
//                Toast.makeText(NotificationActivity.this, "Today's quote number \n" +  toatalQuotes + "message: \n " + datamessage + "name: "+ dataFroms, Toast.LENGTH_SHORT).show();


            }

            else{
                Toast.makeText(NotificationActivity.this, "Guess it ok ", Toast.LENGTH_SHORT).show();

              messageView.setText(datamessagesFBase);
                authorsView.setText(dataFromsFBase);
                hashtag.setVisibility(View.VISIBLE);

            }
//
//                String from_id=todaysQuote.get(toatalQuotes).getFrom();
//                holder.quotesList.setText(todaysQuote.get(toatalQuotes).getMessage());



            }


        else if(authorsMessage != null && authorsName != null   && datamessage == null){
//            Toast.makeText(NotificationActivity.this, "Today's message Empty...  " + "message: " + authorsMessage + "name: "+ authorsName, Toast.LENGTH_SHORT).show();

            messageView.setText( authorsMessage);
            authorsView.setText( authorsName);
            hashtag.setVisibility(View.VISIBLE);
        }

       else{

//            Toast.makeText(NotificationActivity.this, "Not from shared preference...  " + "message: " + datamessage + "name: "+ dataFrom, Toast.LENGTH_SHORT).show();
            SharedPreferences preferences = getSharedPreferences("StoreNotification",MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("message", datamessage);
            editor.putString("authors_name", dataFrom);
            editor.apply();
//            Toast.makeText(NotificationActivity.this, "Done Saving...  " + "message: " + datamessage + "name: "+ dataFrom, Toast.LENGTH_SHORT).show();

            messageView.setText( datamessage);
            authorsView.setText( dataFrom);
            hashtag.setVisibility(View.VISIBLE);
        }

        goal =toatalQuotes;

    }








//    @Override
//    public void onRestoreInstanceState(Bundle savedInstanceState) {
//        super.onRestoreInstanceState(savedInstanceState);
//        // Restore UI state from the savedInstanceState.
//        // This bundle has also been passed to onCreate.
//        datamessage = savedInstanceState.getString("message");
//        dataFrom = savedInstanceState.getString("authors_name");
//
//        messageView = (TextView)findViewById(R.id.notification_message);
//        authorsView = (TextView)findViewById(R.id.notification_from);
//        messageView.setText( datamessage);
//        authorsView.setText( dataFrom);
//    }
}
