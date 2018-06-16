package com.elight.e_light;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;

public class RegistrationActivity extends AppCompatActivity {

   private EditText mname;

    private EditText memail;

    private EditText mpassword;

    private Button newAccountBtn;

    ProgressBar registrationProgressBar;





    private FirebaseAuth mAuth;
    public FirebaseFirestore mFirestore;
    private StorageReference mStorageRef;
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        mAuth = FirebaseAuth.getInstance();
        mFirestore=FirebaseFirestore.getInstance();
//        mStorageRef = FirebaseStorage.getInstance().getReference();

//        ok=(TextView)findViewById(R.id.messageView);
        mname=(EditText) findViewById(R.id.user_name);
        memail=(EditText)findViewById(R.id.email);
        mpassword=(EditText)findViewById(R.id.password);
        newAccountBtn =(Button) findViewById(R.id.new_account);
        registrationProgressBar =(ProgressBar) findViewById(R.id.progressBar2);
        registrationProgressBar.setVisibility(View.INVISIBLE);


        newAccountBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                registrationProgressBar.setVisibility(View.VISIBLE);
                final String name = mname.getText().toString();
                String  password = mpassword.getText().toString();
                   email = memail.getText().toString();
                if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(password) && !TextUtils.isEmpty(email)) {
                    mAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
//                                Log.d(TAG, "createUserWithEmail:success");

//                                    String user_id = mAuth.getCurrentUser().getUid();
//                                    StorageReference userProfile=

                                        final String user_id = mAuth.getCurrentUser().getUid();

                                                String tokenId= FirebaseInstanceId.getInstance().getToken();
                                        Map<String, Object> userMap = new HashMap<>();
                                        userMap.put("name", name);
                                        userMap.put("token_id",tokenId);
                                        mFirestore.collection("Users").document(user_id).set(userMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Toast.makeText(RegistrationActivity.this, "Authentication successful.", Toast.LENGTH_SHORT).show();
                                                registrationProgressBar.setVisibility(View.INVISIBLE);
                                                sendToMainActivity(email);

                                            }
                                        });



//                                updateUI(user);
                                    } else {
//                                    ok.append(uname + password + email);
                                        // If sign in fails, display a message to the user.
//                                Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                        registrationProgressBar.setVisibility(View.INVISIBLE);
                                        Toast.makeText(RegistrationActivity.this, "Authentication failed." + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
//                                updateUI(null);

                                    }

                                    // ...
                                }
                            });
                }

            }
        });
    }




//        Intent login= new Intent(this,SubscriberActivity.class);
//        startActivity(login);
//        finish();


    private void sendToMainActivity(String email) {

        if(email.equals("simi@gmail.com")){
            Intent login= new Intent(RegistrationActivity.this,SendActivity.class);
            startActivity(login);
            finish();
        }
        else{Intent login= new Intent(RegistrationActivity.this,QuotesActivity.class);
            startActivity(login);
            finish();}
       
    }
}
