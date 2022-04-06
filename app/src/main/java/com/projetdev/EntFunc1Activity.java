package com.projetdev;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class EntFunc1Activity extends AppCompatActivity {
    ListView listViewStages2;
    private DatabaseReference DatabaseUsers;
    FirebaseAuth mauth;
    List<Stage> Stageslist2;
    String userid;
    Button buttonBack2;
    TextView empty2;
    @Override
    public void onBackPressed() {

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ent_func1);
        DatabaseUsers = FirebaseDatabase.getInstance().getReference("Stages");
        listViewStages2=(ListView)findViewById(R.id.listViewStages2);
        Stageslist2 = new ArrayList<Stage>();
        buttonBack2=(Button)findViewById(R.id.buttonBack2);
        buttonBack2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(EntFunc1Activity.this,HomeActivityEntreprise.class));
            }
        });
        empty2=(TextView)findViewById(R.id.empty2);
        empty2.setText("Vous n'avez proposé aucun stage.");
        listViewStages2.setEmptyView(empty2);
        mauth=FirebaseAuth.getInstance();
       userid=mauth.getCurrentUser().getUid();


        listViewStages2.setClickable(true);
        listViewStages2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Stage stage=(Stage) parent.getItemAtPosition(position);
                String stageid=stage.uniqueId;
                Intent int2 = new Intent(EntFunc1Activity.this,Etudiant_Par_Stage_Activity.class);
                int2.putExtra("id",stageid.toString());
                startActivity(int2);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        DatabaseUsers.orderByChild("subUserId").equalTo(userid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Stageslist2.clear();

                for (DataSnapshot userSnapshot: dataSnapshot.getChildren()){
                    Stage stage = userSnapshot.getValue(Stage.class);
                    Stageslist2.add(stage);

                    StageList2 adapter = new StageList2(EntFunc1Activity.this, Stageslist2);
                    listViewStages2.setAdapter(adapter);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


}