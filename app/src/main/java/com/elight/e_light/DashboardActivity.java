package com.elight.e_light;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.Arrays;

public class DashboardActivity extends AppCompatActivity {


    TextView allQuotes;
    TextView todaysQuote;
    TextView randomQuote;

    private int RC_SIGN_IN = 123;
    private String mUsername;
    private String email;
    private FirebaseAuth.AuthStateListener firebaseAuthStateListener;
    private FirebaseFirestore mFirestore;
    private String user_id;
    private FirebaseAuth mAuth;
    private ArrayList<Object> usersList;
    private boolean activateSubFabs;
    private FloatingActionButton fabSelect;
    private LinearLayout layoutFabContactUs;
    private LinearLayout layoutFabAboutUs;
    private LinearLayout layoutFabLogout;
    private boolean fabExpanded;
    private Dialog dialog;
    private FloatingActionButton customFab;
    private Window window;
    private WindowManager.LayoutParams param;
    private int resourceValues;
    private boolean isContactus;


    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        firebaseAuthStateListener = new FirebaseAuth.AuthStateListener() {


            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();

                if (user == null) {
                    email = user.getEmail();
                    user_id = user.getUid();
                    //user is signed in
                    sendToSub(email);

                } else {


                    startActivityForResult(
                            AuthUI.getInstance()
                                    .createSignInIntentBuilder()
                                    .setAvailableProviders(Arrays.asList(
                                            new AuthUI.IdpConfig.EmailBuilder().build(),
//                                            new AuthUI.IdpConfig.PhoneBuilder().build(),
//
//                                            new AuthUI.IdpConfig.FacebookBuilder().build(),
//                                            new AuthUI.IdpConfig.TwitterBuilder().build(),
                                            new AuthUI.IdpConfig.GoogleBuilder().build()
                                    ))
                                    .build(),
                            RC_SIGN_IN);
//                    sendToSub(email);
                }
                sendToSub(email);

            }
        };
    }
//        String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

//

                @Override
                protected void onCreate (Bundle savedInstanceState){
                    super.onCreate(savedInstanceState);
                    setContentView(R.layout.activity_dashboard);
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

                    allQuotes = (TextView) findViewById(R.id.all_quotes);
                    todaysQuote = (TextView) findViewById(R.id.random_quotes);
                    randomQuote = (TextView) findViewById(R.id.todays_quotes);
                    activateSubFabs = false;
                    fabSelect = (FloatingActionButton) this.findViewById(R.id.fabSelect);

                    layoutFabContactUs = (LinearLayout) this.findViewById(R.id.layoutFabContactUs);
                    layoutFabAboutUs = (LinearLayout) this.findViewById(R.id.layoutFabAboutUs);
                    layoutFabLogout = (LinearLayout) this.findViewById(R.id.layoutFabLogout);
//        shadowView = (View)findViewById(R.id.shadowView);


                    //When main Fab (Settings) is activateSubFabs, it expands if not expanded already.
                    //Collapses if main FAB was open already.
                    //This gives FAB (Settings) open/close behavior
                    fabSelect.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (fabExpanded == true) {
                                closeSubMenusFab();
                            } else {
                                CustomDialog();
//                    openSubMenusFab();


                            }
                        }
                    });

                    //Only main FAB is visible in the beginning
                    closeSubMenusFab();



                    mFirestore=FirebaseFirestore.getInstance();
                    firebaseAuthStateListener = new FirebaseAuth.AuthStateListener() {


                        @Override
                        public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                            FirebaseUser user = firebaseAuth.getCurrentUser();

                            if (user != null) {
                                email = user.getEmail();
                                user_id = user.getUid();
                                //user is signed in
                                sendToSub(email);

                            } else {


                                startActivityForResult(
                                        AuthUI.getInstance()
                                                .createSignInIntentBuilder()
                                                .setAvailableProviders(Arrays.asList(
                                                        new AuthUI.IdpConfig.EmailBuilder().build(),
//                                            new AuthUI.IdpConfig.PhoneBuilder().build(),
//
//                                            new AuthUI.IdpConfig.FacebookBuilder().build(),
//                                            new AuthUI.IdpConfig.TwitterBuilder().build(),
                                                        new AuthUI.IdpConfig.GoogleBuilder().build()
                                                ))
                                                .build(),
                                        RC_SIGN_IN);
//                    sendToSub(email);
                            }


                        }
                    };
//                    var topic = "quotes"
                    FirebaseMessaging.getInstance().subscribeToTopic("quotes");
                }


    //closes FAB submenus
    private void closeSubMenusFab() {
        layoutFabContactUs.setVisibility(View.INVISIBLE);
        layoutFabAboutUs.setVisibility(View.INVISIBLE);
        layoutFabLogout.setVisibility(View.INVISIBLE);
//        shadowView.setVisibility(View.INVISIBLE);
        fabSelect.setImageResource(R.drawable.ic_settings_black_24dp);
        fabExpanded = false;
    }



    public void fabSetup() {
        //        setup fab button

        dialog = new Dialog(DashboardActivity.this);
        // it remove the dialog title
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

//        Change the image of the fab button
            window = dialog.getWindow();
            param = window.getAttributes();
//        if(!isContactus) {
////
//
//        }
//
//else {
//            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
//            lp.copyFrom(dialog.getWindow().getAttributes());
//            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
//            lp.height = WindowManager.LayoutParams.MATCH_PARENT;
//            dialog.show();
//            dialog.getWindow().setAttributes(lp);
//
//        }

        // set the background partial transparent
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));

        window.setGravity(Gravity.CENTER);
        if (R.layout.layout_fab_submenu==resourceValues) {
            // set the layout at right bottom<br />
            param.gravity = Gravity.BOTTOM | Gravity.RIGHT;

        }

        else{

            param.gravity = Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL; }

        // it dismiss the dialog when click outside the dialog frame<br />
        dialog.setCanceledOnTouchOutside(true);

    }

    public void CustomDialog() {

        resourceValues = R.layout.layout_fab_submenu;
        fabSetup();


        // set the laytout in the dialog
        dialog.setContentView(R.layout.layout_fab_submenu);

        customFab = (FloatingActionButton) dialog.findViewById(R.id.fabSelect);

        layoutFabContactUs = (LinearLayout) dialog.findViewById(R.id.layoutFabContactUs);

        layoutFabLogout = (LinearLayout) dialog.findViewById(R.id.layoutFabLogout);

        layoutFabAboutUs = (LinearLayout) dialog.findViewById(R.id.layoutFabAboutUs);


        customFab.setImageResource(R.drawable.ic_close_black_24dp);


        customFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activateSubFabs = false;
                resourceValues = R.id.fabSelect;
                dialog.dismiss();
            }
        });
        dialog.show();

        layoutFabContactUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(MasterDashboard.this, "I have been clicked \n Contact us" ,Toast.LENGTH_SHORT).show();

                activateSubFabs = true;

                resourceValues = R.id.layoutFabContactUs;
                confirm(resourceValues);
//                dialog.dismiss();
            }
        });

        layoutFabLogout.setOnClickListener(new View.OnClickListener() {

            AlertDialog.Builder builder;
            @Override
            public void onClick(View view) {
                activateSubFabs = true;

//                resourceValues = R.id.layoutFabLogout;
//
//                confirm(resourceValues);
                dialog.dismiss();
//
//                fabSetup();
//
//
//                // set the laytout in the dialog
//                dialog.setContentView(R.layout.logout);

                builder = new AlertDialog.Builder(DashboardActivity.this);
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
                                        startActivity(new Intent(DashboardActivity.this, HomeActivity.class));

//                                        Toast.makeText(getApplicationContext(), "Pressed No button", Toast.LENGTH_LONG).show();
                                    }
                                });



                    }
                });
                AlertDialog alert= builder.create();
                alert.show();
            }
        });

        layoutFabAboutUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                Toast.makeText(MasterDashboard.this, "I have been clicked \n About us " ,Toast.LENGTH_SHORT).show();

                activateSubFabs = true;

                resourceValues = R.id.layoutFabAboutUs;
                confirm(resourceValues);
//                dialog.dismiss();
            }
        });
    }


    public void confirm(int resourceValues) {
        if (activateSubFabs) {
            switch (resourceValues) {

                case R.id.layoutFabContactUs:
                    dialog.dismiss();
                    isContactus=true;
                    fabSetup();
                    dialog.setContentView(R.layout.contact_us);
//                    getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
//                    LinearLayout view = (LinearLayout) dialog.findViewById(R.id.central);
//                    view.setGravity(Gravity.CENTER);

                    Button sendMail = (Button) dialog.findViewById(R.id.contact_email);
                    sendMail.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            isContactus=false;
                            Intent emailIntent = new Intent(Intent.ACTION_SEND);
                            emailIntent.setData(Uri.parse("mailto:"));
                            emailIntent.setType("text/plain");
                            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Re:The Enlightenment Center App");
                            emailIntent.putExtra(Intent.EXTRA_TEXT, "Hello, There");
                            emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"TheelightCenter@gmail.com"});
//                            emailIntent.setType("message/rfc822");
                            Intent mailer = Intent.createChooser(emailIntent, "Send to ");
                            startActivity(mailer);
                            dialog.dismiss();
                        }
                    });


//                          activateSubFabs =false;
                    dialog.show();

                    break;
                case R.id.layoutFabAboutUs:
                    dialog.dismiss();
                    fabSetup();
                    dialog.setContentView(R.layout.about_us);
//                    LinearLayout view1 = (LinearLayout) dialog.findViewById(R.id.central);
//                    view1.setGravity(Gravity.CENTER);

                    Button dialogButton = (Button) dialog.findViewById(R.id.btn_dialog);
                    dialogButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
//                    Toast.makeText(this, "pop up! \n About us " ,Toast.LENGTH_SHORT).show();

                    dialog.show();
                    break;
                case R.id.layoutFabLogout:


            }
        }
    }




    private void sendToSub(String email) {
        if(email.equals("simi@gmail.com")){
            Intent login= new Intent(DashboardActivity.this,SendActivity.class);
            startActivity(login);
            finish();
        }
//        else{Intent login= new Intent(HomeActivity.this,DashboardActivity.class);
//            startActivity(login);
//            finish();}


    }
    public void all_Quotes(View view) {

        Intent intent = new Intent(this,QuotesActivity.class);
        intent.putExtra("priority","1");
        startActivity(intent);
//        mFirestore.collection("Notifications").addSnapshotListener(this,new EventListener<QuerySnapshot>() {
//            public int totalSubscribers;
//
//            @Override
//            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
//                if(e != null){
//                    Toast.makeText( DashboardActivity.this, "List Retrieval failed." +  e.getMessage(), Toast.LENGTH_SHORT).show();
//                }
//                usersList= new ArrayList<>();
//                for(DocumentChange doc:documentSnapshots.getDocumentChanges()){
//                    if(doc.getType() == DocumentChange.Type.ADDED);
//                    String userId=doc.getDocument().getId();
//
////
////                    Users users = doc.getDocument().toObject(Users.class).withId(userId);
////                    if (currentUser == userId){
////                        usersList.clear();
////                    }
//                    usersList.add(userId);
////                    usersList.notifyAll();
//
////                     String name = doc.getDocument().getString("name");
//                }
//                totalSubscribers =usersList.size();
//                Toast.makeText( DashboardActivity.this,"All Notifications " + totalSubscribers, Toast.LENGTH_SHORT).show();
//
//            }
//        });

    }

    public void random_quotes(View view) {
        Intent randomQuotes = new Intent(DashboardActivity.this, QuotesActivity.class);
        randomQuotes.putExtra("priority","0");
        startActivity(randomQuotes);
    }

    public void todays_quote(View view) {

        Intent todaysQuote = new Intent(DashboardActivity.this, NotificationActivity.class);
        startActivity(todaysQuote);

    }
}
