package com.projetdev;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.text.HtmlCompat;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class Stage_Details extends AppCompatActivity {
    TextView tvDetailType, tvDetailPar, tvDetailMail, tvDetailDomaine, tvDetailDescrip, tvDetailDuree;
    ImageView ivDetail;
    Button btnPostul,btnRetour;
    DatabaseReference DatabaseStageRef, DatabaseStage;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private String userID;
    List<String> result;
    protected String stageid;
    protected String test1;
    boolean k = false;
    TextView déjaPostul;

    @Override
    public void onBackPressed() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stage__details);
        tvDetailType = (TextView) findViewById(R.id.tvDetailType);
        tvDetailPar = (TextView) findViewById(R.id.tvDetailPar);
        tvDetailMail = (TextView) findViewById(R.id.tvDetailMail);
        tvDetailDomaine = (TextView) findViewById(R.id.tvDetailDomaine);
        tvDetailDuree = (TextView) findViewById(R.id.tvDetailDuree);
        tvDetailDescrip = (TextView) findViewById(R.id.tvDetailDescrip);
        ivDetail = (ImageView) findViewById(R.id.ivDetail);
        btnPostul = (Button) findViewById(R.id.btnPostul);
        déjaPostul=(TextView)findViewById(R.id.déjapostul);
        déjaPostul.setVisibility(View.GONE);
        btnRetour=(Button)findViewById(R.id.btnRetour);
        btnRetour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Stage_Details.this,EtudiantFunc1Activity.class));
            }
        });
        result = new ArrayList<>();
        Intent intent = getIntent();
        stageid = intent.getStringExtra("id");
        k = false;
        DatabaseStage = FirebaseDatabase.getInstance().getReference("Stages").child(stageid).child("userEtudList");
        DatabaseStage.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    test1 = data.getValue().toString();
                    if (test1.equals(userID)) {
                        k = true;
                    }
                }
                if (k == true) {
                    btnPostul.setOnClickListener(null);
                    btnPostul.setVisibility(View.GONE);
                    déjaPostul.setVisibility(View.VISIBLE);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        DatabaseStageRef = FirebaseDatabase.getInstance().getReference("Stages");
        Query query = DatabaseStageRef.orderByChild("uniqueId").equalTo(stageid);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String obtType = (String) ds.child("type").getValue();
                    String obtPar = (String) ds.child("nomEnt").getValue();
                    String obtMail = (String) ds.child("contact").getValue();
                    String obtadate = (String) ds.child("date").getValue();
                    String obtdurée = (String) ds.child("durée").getValue();
                    String obtDescrip = (String) ds.child("descrip").getValue();
                    String obtimage = (String) ds.child("imageEnt").getValue();
                    String obtDomaine = (String) ds.child("domaine").getValue();

                    tvDetailDomaine.setText("• Domaine de : " + obtDomaine);
                    tvDetailType.setText(obtType);
                    tvDetailPar.setText("• Proposé par : " + obtPar);
                    tvDetailMail.setText("• Adresse de contact : " + obtMail);
                    tvDetailDuree.setText("• Pour une durée de " + obtdurée + " commençant le : " + obtadate);
                    tvDetailDescrip.setMovementMethod(new ScrollingMovementMethod());
                    tvDetailDescrip.setText("• Description du stage :"+"\n" + obtDescrip);

                    if (!(obtimage.equals(""))) {
                        Picasso.get().load(obtimage).into(ivDetail);

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        userID = user.getUid();

        btnPostul.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                result.clear();

                //String stint = UUID.randomUUID().toString();
                result.add(userID);
                FirebaseDatabase.getInstance().getReference("Stages").child(stageid).child("userEtudList").push().setValue(userID);
                //startActivity(new Intent(Stage_Details.this, EtudiantFunc1Activity.class));

                    btnPostul.setOnClickListener(null);
                    Toast.makeText(Stage_Details.this, "Opération effectuée", Toast.LENGTH_SHORT).show();



            }
        });


    }


}


