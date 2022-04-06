package com.projetdev;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ResetActivity extends AppCompatActivity {
    private FirebaseAuth mauth;
    EditText editTextPassReset;
    Button buttonReset;
    private ProgressBar progressBar3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset);

        editTextPassReset = (EditText)findViewById(R.id.editTextPassReset);
        buttonReset=(Button)findViewById(R.id.buttonReset);
        progressBar3=(ProgressBar)findViewById(R.id.progressBar3);

        mauth = FirebaseAuth.getInstance();

        progressBar3.setVisibility(View.GONE);

        buttonReset.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                progressBar3.setVisibility(View.VISIBLE);
                mauth.sendPasswordResetEmail(editTextPassReset.getText().toString().trim())
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        progressBar3.setVisibility(View.GONE);
                        if(task.isSuccessful()){
                            Toast.makeText(ResetActivity.this,"Mot de passe envoyé à votre adresse mail",Toast.LENGTH_LONG ).show();
                        }
                        else{
                            Toast.makeText(ResetActivity.this,task.getException().getMessage(),Toast.LENGTH_LONG ).show();
                        }

                    }
                });
            }
        });



    }
}
