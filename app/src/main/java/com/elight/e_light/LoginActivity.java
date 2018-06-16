package com.elight.e_light;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.HashMap;
import java.util.Map;

import io.fabric.sdk.android.Fabric;

public class LoginActivity extends AppCompatActivity {

    EditText memail;

    EditText mpassword;

    Button login;

    Button newAccount;

    private FirebaseAuth mAuth;

    private FirebaseFirestore mFirestore;
    private String email;

//    @Override
//    public void onStart() {
//        super.onStart();
//        // Check if user is signed in (non-null) and update UI accordingly.
//        FirebaseUser currentUser = mAuth.getCurrentUser();
//        if (currentUser == null){
//            sendToLogin();
//        }
//    }

    private void sendToLogin() {

        Intent login= new Intent(this,LoginActivity.class);
        startActivity(login);
        finish();}


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_login);

        memail=(EditText)findViewById(R.id.email);
        mpassword =(EditText)findViewById(R.id.password);
        login=(Button) findViewById(R.id.login);
        newAccount=(Button) findViewById(R.id.new_account);

        mAuth = FirebaseAuth.getInstance();
        mFirestore= FirebaseFirestore.getInstance();
    }

    public void login(View view) {
         email = memail.getText().toString();
        String password = mpassword.getText().toString();
        if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)){
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
//                                Log.d(TAG, "createUserWithEmail:success");

                                        String tokenId= FirebaseInstanceId.getInstance().getToken();
                                        String getCurrentId=mAuth.getCurrentUser().getUid();

                                        Map<String,Object> tokenMap= new HashMap <>();

                                        tokenMap.put("token_id",tokenId);

                                Toast.makeText(LoginActivity.this, "User id:  " + getCurrentId ,Toast.LENGTH_SHORT).show();


                                mFirestore.collection("Users").document(getCurrentId).update(tokenMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                sendToSub(email);

                                            }
                                        });



//
                                //                                updateUI(user);
                            } else {

                                // If sign in fails, display a message to the user.
//                                Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                Toast.makeText(LoginActivity.this, "Authentication failed."  + task.getException().getMessage(),Toast.LENGTH_SHORT).show();
//                                updateUI(null);
                            }

                            //
                        }
                    });
        }


}

    private void sendToSub(String email) {
       if(email.equals("simi@gmail.com")){
           Intent login= new Intent(LoginActivity.this,SendActivity.class);
           startActivity(login);
           finish();
       }
       else{Intent login= new Intent(LoginActivity.this,DashboardActivity.class);
           startActivity(login);
           finish();}


    }



    public void newAccount(View view) {
        Intent createAccount= new Intent(this,RegistrationActivity.class);
        startActivity(createAccount);
    }
}
