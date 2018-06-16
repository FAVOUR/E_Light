package com.elight.e_light;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SendActivity extends AppCompatActivity {

    private EditText mMessage;
    private FirebaseFirestore mFirestore;
    private FirebaseAuth mAuth;
    private Button mSendMessage;
    private TextView send_to;
    String mCurrentId;
    String userName;
    String user_id;
    private ImageButton mPhotoPickerButton;
    private FirebaseStorage mFirebaseStorage;
    private StorageReference mStorageReference;
    private ArrayList<Object> usersList;
    public int totalNotification;
    int totNotif;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send);
        mPhotoPickerButton = (ImageButton) findViewById(R.id.photoPickerButton);
        mMessage = (EditText) findViewById(R.id.message);
        mSendMessage = (Button) findViewById(R.id.button_send);
        mFirestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        mCurrentId = FirebaseAuth.getInstance().getUid();


        mFirebaseStorage = FirebaseStorage.getInstance();
        userName = getIntent().getStringExtra("Username");
//       user_id=mAuth.getUid();
        user_id = getIntent().getStringExtra("UserId");
//        send_to.setText("Message " + userName);

        mPhotoPickerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent imagePickerIntent = new Intent(Intent.ACTION_PICK);
                imagePickerIntent.setType("image/*");
                startActivityForResult(Intent.createChooser(imagePickerIntent, "Select Image"), 1);
            }
        });

        mMessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().trim().length() > 0) {
                    mSendMessage.setEnabled(true);
                } else {
                    mSendMessage.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });


        mSendMessage.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                sendQuotes(1);

//                String message = mMessage.getText().toString();
//                    Map<String, Object> notificationMessage = new HashMap<>();
//                    notificationMessage.put("message", message);
//                    notificationMessage.put("Priority", totNotif);
//                    notificationMessage.put("from", "Sam-E-Light");
////                    mFirestore.collection("Users/" + user_id + "/Notifications").add(notificationMessage)
//                    mFirestore.collection("Notifications").add(notificationMessage).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
//                        @Override
//                        public void onSuccess(DocumentReference documentReference) {
//                            Toast.makeText(SendActivity.this, "Message sent.", Toast.LENGTH_SHORT).show();
//                            mMessage.setText("");
//
//                        }
//                    }).addOnFailureListener(new OnFailureListener() {
//                        @Override
//                        public void onFailure(@NonNull Exception e) {
//                            Toast.makeText(SendActivity.this, "Error:" + e.getMessage(), Toast.LENGTH_SHORT).show();
//
//                        }
//                    });
            }
//            }
        });


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
                AlertDialog.Builder  builder = new AlertDialog.Builder(SendActivity.this);
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
                                        startActivity(new Intent(SendActivity.this, HomeActivity.class));

//                                        Toast.makeText(getApplicationContext(), "Pressed No button", Toast.LENGTH_LONG).show();
                                    }
                                });



                    }
                });
                AlertDialog alert= builder.create();
                alert.show();
//                AuthUI.getInstance().signOut(getBaseContext());
//                Map<String,Object> tokenMap= new HashMap<>();
//                tokenMap.put("token_id", FieldValue.delete());
//
//
//                mFirestore.collection("Users").document(mCurrentId).update(tokenMap).addOnSuccessListener(new OnSuccessListener<Void>() {
//                    @Override
//                    public void onSuccess(Void aVoid) {
//
////                        Intent loginOut= new Intent(SendActivity.this,HomeActivity.class);
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

    public void sendQuotes(int op) {

        mFirestore.collection("Notifications").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
//                    Toast.makeText(SendActivity.this, "List Retrieval failed." + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                if (task.isSuccessful()) {
                    totalNotification = 0;
                    for (DocumentSnapshot document : task.getResult()) {


                        totalNotification++;
                    }


//                    Toast.makeText(SendActivity.this, "All Notifications " + totalNotification, Toast.LENGTH_SHORT).show();

                    totalNotification++;
//                    Toast.makeText(SendActivity.this, "All Notifications++ " + totalNotification, Toast.LENGTH_SHORT).show();

                    String message = mMessage.getText().toString();
                    Map<String, Object> notificationMessage = new HashMap<>();
                    notificationMessage.put("message", message);
                    notificationMessage.put("Priority", totalNotification);
                    notificationMessage.put("from", "Sam-E-Light");
//                    mFirestore.collection("Users/" + user_id + "/Notifications").add(notificationMessage)
                    mFirestore.collection("Notifications").add(notificationMessage).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Toast.makeText(SendActivity.this, "Message sent.", Toast.LENGTH_SHORT).show();
                            mMessage.setText("");

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(SendActivity.this, "Error:" + e.getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    });

                }


            }

        });
    }
}