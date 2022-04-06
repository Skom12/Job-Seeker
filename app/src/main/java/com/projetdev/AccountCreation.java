package com.projetdev;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AccountCreation extends AppCompatActivity {

    private  Spinner spinner;
      ArrayAdapter<CharSequence> adapter;
    private FirebaseAuth mAuth;
    private  EditText email2,password2,editTextNom,editTextPrenom,editTextPseudo;
    private  Button signUpBtn;
    private TextView textViewSignIn;
    private ProgressBar progressBar1;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_creation);

        spinner=(Spinner)findViewById(R.id.spinner);
        adapter=ArrayAdapter.createFromResource(AccountCreation.this , R.array.Domain,android.R.layout.simple_spinner_item);


        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(parent.getItemIdAtPosition(position)==1){
                    Toast.makeText(getBaseContext(),"Etudiant selectionné",Toast.LENGTH_LONG).show();}
                else if(parent.getItemIdAtPosition(position)==2) {
                    Toast.makeText(getBaseContext(),"Entreprise selectionnée",Toast.LENGTH_LONG).show();}


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        mAuth = FirebaseAuth.getInstance();


        editTextNom=(EditText)findViewById(R.id.editTextNom);
        editTextPrenom=(EditText)findViewById(R.id.editTextPrenom);
        editTextPseudo=(EditText)findViewById(R.id.editTextPseudo);
        email2=(EditText)findViewById(R.id.editTextMail2);
        password2=(EditText)findViewById(R.id.editTextPassword2);
        textViewSignIn=(TextView)findViewById(R.id.textViewSignIn);
        signUpBtn=(Button)findViewById(R.id.button);
        progressBar1 =(ProgressBar)findViewById(R.id.progressBar1);



        progressBar1.setVisibility(View.GONE);
        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String mail = email2.getText().toString();
                final String pass = password2.getText().toString();
                final String nom =editTextNom.getText().toString();
                final String prenom = editTextPrenom.getText().toString();
                final String pseudo = editTextPseudo.getText().toString();
                final String domain = spinner.getSelectedItem().toString();
                final String adresse="";
                final String image="";
                final String cv="";
                TextView errorText = (TextView)spinner.getSelectedView();



                if (mail.isEmpty()){
                    email2.setError("Veuillez saisir votre mail");
                    email2.requestFocus();
                }
                else if (pass.isEmpty()){
                    password2.setError("Veuillez saisir votre mot de passe");
                    password2.requestFocus();
                }
               else if (errorText.getText().toString().equals("Qui êtes vous?")){
                errorText.setError("Qui êtes vous?");
                errorText.setText("Qui êtes vous?");
               }
                else if (nom.isEmpty()){
                    editTextNom.setError("Veuillez saisir votre nom");
                    editTextNom.requestFocus();
                }
                else if (prenom.isEmpty()){
                    editTextPrenom.setError("Veuillez saisir votre prenom");
                    editTextPrenom.requestFocus();
                }
                else if (pseudo.isEmpty()){
                    editTextPseudo.setError("Veuillez saisir votre pseudo");
                    editTextPseudo.requestFocus();
                }
                else if ((mail.isEmpty()) && (pass.isEmpty()) && (nom.isEmpty()) && (prenom.isEmpty())&& (pseudo.isEmpty())&& (domain.isEmpty())){
                    Toast.makeText(AccountCreation.this, "Les cases sont vides",Toast.LENGTH_SHORT).show();
                }
                else if (!(mail.isEmpty()) && !(pass.isEmpty()) && !(nom.isEmpty()) && !(prenom.isEmpty())&& !(pseudo.isEmpty())&& !(domain.isEmpty())){
                   progressBar1.setVisibility(View.VISIBLE);
                    mAuth.createUserWithEmailAndPassword(mail,pass).addOnCompleteListener(AccountCreation.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    progressBar1.setVisibility(View.GONE);
                                    if(!(task.isSuccessful())){
                                        Toast.makeText(AccountCreation.this, "Création de compte échouée", Toast.LENGTH_SHORT).show();
                                    }
                                    else {
                                        FirebaseDatabase dataBaseUser =FirebaseDatabase.getInstance();
                                        String uid="";
                                        User user = new User(uid,nom,prenom,pseudo,mail,domain,adresse,image,cv);
                                        dataBaseUser.getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(user);
                                        FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("uid").setValue(FirebaseAuth.getInstance().getCurrentUser().getUid());
                                        if(domain.equals("Un Etudiant")){
                                            Toast.makeText(AccountCreation.this, "Création de compte réussite , Vous êtes connecté(e)s", Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(AccountCreation.this, HomeActivity.class));
                                        }
                                        else if (domain.equals("Une Entreprise")){
                                            Toast.makeText(AccountCreation.this, "Création de compte réussite , Vous êtes connecté(e)s", Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(AccountCreation.this, HomeActivityEntreprise.class));
                                        }

                                    }
                                }
                            }
                    );}
                else{
                    Toast.makeText(AccountCreation.this, "Erreur", Toast.LENGTH_SHORT).show();
                }

            }

        });
        textViewSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toSignUp= new Intent(AccountCreation.this, LoginActivity.class);
                startActivity(toSignUp);
            }
        });


    }

    }


