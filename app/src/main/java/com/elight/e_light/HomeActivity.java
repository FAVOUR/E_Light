package com.elight.e_light;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AdditionalUserInfo;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.FirebaseUserMetadata;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import io.fabric.sdk.android.Fabric;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeActivity extends AppCompatActivity {
    FirebaseAuth mAuth;


    FirebaseAuth.AuthStateListener firebaseAuthStateListener;
    private int RC_SIGN_IN = 123;
    private String mUsername;
    private String email;
    private FirebaseFirestore mFirestore;

    private String user_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//   t
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_home);

        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();
        // Set the dimensions of the sign-in button.
//        SignInButton signInButton = findViewById(R.id.sign_in_button);
//        signInButton.setSize(SignInButton.SIZE_STANDARD);
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


//                    ititFireBaseUi();
                    startActivityForResult(
                            AuthUI.getInstance()

                                    .createSignInIntentBuilder()
                                    .setTheme(R.style.GreenTheme)
                                    .setLogo(R.drawable.logo)
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





                }


//
                }
            };


//
        };


//                    private void ititFireBaseUi() {
//                        AuthUI.getInstance()
//                                .signOut(getBaseContext())
//                                .addOnCompleteListener(new OnCompleteListener<Void>() {
//                                    public void onComplete(@NonNull Task<Void> task) {
//
//                                        // Choose authentication providers
//                                        List<AuthUI.IdpConfig> providers = Arrays.asList(
//                                                new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build(),
//                                                new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build()
//                                        );
//
//
//// Create and launch sign-in intent
//                                        startActivityForResult(
//                                                AuthUI.getInstance()
//                                                        .createSignInIntentBuilder()
//                                                        .setAvailableProviders(providers)
//                                                        .setLogo(R.drawable.logo)
//                                                        .build(),
//                                                RC_SIGN_IN);
//
//                               if (task.isSuccessful()) {
//                                        boolean isNew = task.getResult().a;
//                                        Log.d("MyTAG", "onComplete: " + (isNew ? "new user" : "old user"));
//                                    }
//                                    }
//                                });
//
//
//                    }

    private void onSignoutInitialize() {
    }

    private void onSignInInitialize(String username) {

        mUsername = username;
    }

    private void sendToSub(String email) {
        if(email.equals("simi@gmail.com")){
            
            Intent login= new Intent(HomeActivity.this,MasterDashboard.class);
            startActivity(login);
            finish();
        }
        else{Intent login= new Intent(HomeActivity.this,DashboardActivity.class);
            startActivity(login);
            finish();}


    }

    public boolean isNewSignUp(){
        FirebaseUserMetadata metadata = mAuth.getCurrentUser().getMetadata();
        return metadata.getCreationTimestamp() == metadata.getLastSignInTimestamp();
    }
    @Override
    public void onActivityResult(int requestCode,int resultCode,Intent data){
        super.onActivityResult(requestCode,resultCode,data);

        if(requestCode==RC_SIGN_IN){


          if( isNewSignUp())
          {
              Map<String, Object> userMap = new HashMap<>();
              userMap.put("name", "");
              userMap.put("token_id", "");
              mFirestore.collection("Users").add(userMap).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                  @Override
                  public void onSuccess(DocumentReference documentReference) {
//                      Toast.makeText(HomeActivity.this, "New user added ", Toast.LENGTH_SHORT).show();


                  }


              });
          }

            if(resultCode==RESULT_OK ) {

                }
                else {
//                    Toast.makeText(HomeActivity.this, "You are an existing user ", Toast.LENGTH_SHORT).show();

                };
            }


            else if (resultCode==RESULT_CANCELED){
                Toast.makeText(HomeActivity.this, "Login canceled ", Toast.LENGTH_SHORT).show();

                finish();

            }

        }




    @Override
    protected void onResume() {
        super.onResume();

        mAuth.addAuthStateListener(firebaseAuthStateListener);
    }


    @Override
    protected void onPause() {
        super.onPause();

        mAuth.removeAuthStateListener(firebaseAuthStateListener);
    }
}