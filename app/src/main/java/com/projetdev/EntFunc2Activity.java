package com.projetdev;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class EntFunc2Activity extends AppCompatActivity {

    private Button btnSubmit,btnRetour3;
    private EditText EtDomaine, EtDurée, EtDate, EtDescrip, EtMailContact;

    ArrayAdapter<CharSequence> adapterType;
    String usrId;

    Spinner spinnerType;
    List<String> userEtudList;
    private FirebaseAuth mAuth;
    @Override
    public void onBackPressed() {

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ent_func2);

        mAuth = FirebaseAuth.getInstance();


        usrId = mAuth.getCurrentUser().getUid();
        btnSubmit = (Button) findViewById(R.id.buttonSubmit);
        EtDomaine = (EditText) findViewById(R.id.EditTextType);
        EtDurée = (EditText) findViewById(R.id.EditTextDurée);
        EtDate = (EditText) findViewById(R.id.EditTextDate);
        EtDescrip = (EditText) findViewById(R.id.EditTextDescrip);
        EtMailContact = (EditText) findViewById(R.id.EditTextMailContact);
        EtDescrip.setMovementMethod(new ScrollingMovementMethod());


        btnRetour3=(Button)findViewById(R.id.btnRetour3);
        btnRetour3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(EntFunc2Activity.this,HomeActivityEntreprise.class));
            }
        });
        spinnerType = (Spinner) findViewById(R.id.SpinnerType);
        adapterType = ArrayAdapter.createFromResource(EntFunc2Activity.this, R.array.Type, android.R.layout.simple_spinner_item);
        adapterType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerType.setAdapter(adapterType);
        spinnerType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(parent.getItemIdAtPosition(position)>0){
                    Toast.makeText(getBaseContext(), parent.getSelectedItem().toString() + " selectionné", Toast.LENGTH_LONG).show();
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase.getInstance().getReference("Users").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        final String nomEnt = "" + dataSnapshot.child(usrId).child("pseudo").getValue();
                        final String imageEnt= (String) dataSnapshot.child(usrId).child("image").getValue();
                        final String type = spinnerType.getSelectedItem().toString().trim();
                        final String domaine = EtDomaine.getText().toString().trim();
                        final String durée = EtDurée.getText().toString().trim();
                        final String date = EtDate.getText().toString().trim();
                        final String descrip = EtDescrip.getText().toString().trim();
                        final String contact = EtMailContact.getText().toString().trim();
                        final String subUserId = usrId;
                        TextView errorText1 = (TextView)spinnerType.getSelectedView();

                        final String uniqueId = UUID.randomUUID().toString();

                        if (domaine.isEmpty()) {
                            EtDomaine.setError("Veuillez spécifier votre domaine");
                            EtDomaine.requestFocus();
                        } else if (date.isEmpty()) {
                            EtDate.setError("Veuillez saisir la date");
                            EtDate.requestFocus();
                        } else if (descrip.isEmpty()) {
                            EtDescrip.setError("Veuillez décrire le stage");
                            EtDescrip.requestFocus();
                        }else if (errorText1.getText().toString().equals("Veuillez choisir le type du stage")){
                            errorText1.setError("Veuillez choisir le type du stage");
                            errorText1.setText("Veuillez choisir le type du stage");
                        } else if (contact.isEmpty()) {
                            EtMailContact.setError("Veuillez saisir votre adresse mail de contact");
                            EtMailContact.requestFocus();
                        } else if (durée.isEmpty()) {
                            EtDurée.setError("Veuillez saisir la durée du stage");
                            EtDurée.requestFocus();
                        } else if ((domaine.isEmpty()) && (date.isEmpty()) && (descrip.isEmpty()) && (contact.isEmpty()) && (durée.isEmpty())) {
                            Toast.makeText(EntFunc2Activity.this, "Les cases sont vides", Toast.LENGTH_SHORT).show();
                        } else if (!(domaine.isEmpty()) && !(date.isEmpty()) && !(descrip.isEmpty()) && !(contact.isEmpty()) && !(durée.isEmpty())) {

                            Stage stage = new Stage(uniqueId,subUserId, nomEnt, type, domaine, descrip, date, durée, contact,imageEnt);
                            FirebaseDatabase.getInstance().getReference("Stages").child(uniqueId).setValue(stage);
                            FirebaseDatabase.getInstance().getReference("Stages").child(uniqueId).child("userEtudList").push().setValue("test");
                            Toast.makeText(EntFunc2Activity.this, "Offre de stage crée", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(EntFunc2Activity.this, HomeActivityEntreprise.class));

                        } else {
                            Toast.makeText(EntFunc2Activity.this, "Erreur", Toast.LENGTH_SHORT).show();

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }
        });
    }
}
