package com.projetdev;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    EditText editTextMail;
    EditText eTPassword;
    Button buttonSignIn;
    TextView textViewSignUp;
    TextView textViewReset;
    Button googleBtn;
    static final int GOOGLE_SIGN_IN = 123;
    private ProgressBar progressbar2;
    GoogleSignInClient mGoogleSignInClient;
    private String domain;
    private String uid;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        mAuth = FirebaseAuth.getInstance();



        editTextMail =(EditText) findViewById(R.id.editTextMail);
        eTPassword =(EditText) findViewById(R.id.eTPassword);
        buttonSignIn =(Button) findViewById(R.id.buttonSignin);
        textViewSignUp=(TextView) findViewById(R.id.textViewSignUp);
        progressbar2=(ProgressBar)findViewById(R.id.progressBar2);
        textViewReset=(TextView)findViewById(R.id.textViewReset);


        progressbar2.setVisibility(View.GONE);


        buttonSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String mail1 = editTextMail.getText().toString().trim();
                final String pass1 = eTPassword.getText().toString().trim();
                if (mail1.isEmpty()){
                    editTextMail.setError("Veuillez saisir votre mail");
                    editTextMail.requestFocus();
                }
                else if (pass1.isEmpty()){
                    eTPassword.setError("Veuillez saisir votre mot de passe");
                    eTPassword.requestFocus();
                }
                else if ((mail1.isEmpty()) && (pass1.isEmpty())){
                    Toast.makeText(LoginActivity.this, "Les cases sont vides",Toast.LENGTH_SHORT).show();
                }
                else if (!(mail1.isEmpty()) && !(pass1.isEmpty())){
                    progressbar2.setVisibility(View.VISIBLE);
                    mAuth.signInWithEmailAndPassword(mail1,pass1).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            progressbar2.setVisibility(View.GONE);

                            if(task.isSuccessful()){
                                FirebaseUser user = mAuth.getCurrentUser();
                                updateUI(user);
                            }
                            else {
                                Toast.makeText(LoginActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                            }
                        }
                    });
                }
                else{
                    progressbar2.setVisibility(View.GONE);
                    Toast.makeText(LoginActivity.this, "Erreur", Toast.LENGTH_SHORT).show();
                }

            }

        });
        textViewSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intSignUp = new Intent(LoginActivity.this, AccountCreation.class);
                startActivity(intSignUp);
            }
        });

        textViewReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intReset = new Intent(LoginActivity.this , ResetActivity.class);
                startActivity(intReset);
            }
        });
//        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//                .requestIdToken(getString(R.string.default_web_client_id))
//                .requestEmail()
//                .build();
//        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
//        googleBtn.setOnClickListener(v -> SignInGoogle());



    }

//    public void SignInGoogle() {
//        progressbar2.setVisibility(View.VISIBLE);
//        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
//        startActivityForResult(signInIntent, GOOGLE_SIGN_IN);
   // }
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (requestCode == GOOGLE_SIGN_IN) {
//            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
//            try {
//                GoogleSignInAccount account = task.getResult(ApiException.class);
//                if (account != null) firebaseAuthWithGoogle(account);
//            } catch (ApiException e) {
//                Log.w("TAG", "Google sign in failed", e);
//            }
//        }
//    }


  //  private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
    //        Log.d("TAG", "firebaseAuthWithGoogle:" + acct.getId());
    //
    //        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
    //        mAuth.signInWithCredential(credential)
    //                .addOnCompleteListener(this, task -> {
    //                    if (task.isSuccessful()) {
    //                        progressbar2.setVisibility(View.GONE);
    //
    //                        Log.d("TAG", "signInWithCredential:success");
    //
    //                        FirebaseUser user = mAuth.getCurrentUser();
    //
    //                        updateUI(user);
    //                    } else {
    //                        progressbar2.setVisibility(View.GONE);
    //
    //                        Log.w("TAG", "signInWithCredential:failure", task.getException());
    //
    //                        Toast.makeText(LoginActivity.this, "Authentication failed.",
    //                                Toast.LENGTH_SHORT).show();
    //
    //                    }
    //                });
    //}






    private void updateUI(FirebaseUser user) {
        if (user != null) {
            uid= user.getUid();
            DatabaseReference Ref =FirebaseDatabase.getInstance().getReference();
            DatabaseReference uidRef = Ref.child("Users").child(uid);
            ValueEventListener eventListener = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                   domain=dataSnapshot.child("domain").getValue(String.class);
                   if(domain.equals(("Un Etudiant"))){
                       Toast.makeText(LoginActivity.this, "Connexion réussite", Toast.LENGTH_SHORT).show();
                       Intent intToHome= new Intent(LoginActivity.this ,HomeActivity.class);
                       startActivity(intToHome);
                   }
                   else{
                       Toast.makeText(LoginActivity.this, "Connexion réussite", Toast.LENGTH_SHORT).show();
                       Intent intToHome= new Intent(LoginActivity.this ,HomeActivityEntreprise.class);
                       startActivity(intToHome);
                   }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {}
            };
            uidRef.addListenerForSingleValueEvent(eventListener);

        } else {
            Toast.makeText(LoginActivity.this, "Erreur", Toast.LENGTH_SHORT).show();
        }
    }
}





