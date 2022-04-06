package com.projetdev;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class EtudiantFunc1Activity extends AppCompatActivity {
    private DatabaseReference DatabaseUsers;
    ListView listViewStages;
    Button buttonBack;
    List<Stage> stageList;
    @Override
    public void onBackPressed() {

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_etudiant_func1);
        listViewStages =(ListView)findViewById(R.id.listViewEtudiant);
        DatabaseUsers = FirebaseDatabase.getInstance().getReference("Stages");
        stageList = new ArrayList<Stage>();
        listViewStages.setClickable(true);
        buttonBack=(Button)findViewById(R.id.buttonBack);
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(EtudiantFunc1Activity.this,HomeActivity.class));
            }
        });

        listViewStages.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                       Stage stage=(Stage) parent.getItemAtPosition(position);
                       String stageid=stage.uniqueId;
                       Intent int1 = new Intent(EtudiantFunc1Activity.this,Stage_Details.class);
                       int1.putExtra("id",stageid.toString());
                       startActivity(int1);



            }
        });




    }

    @Override
    protected void onStart() {
        super.onStart();
        DatabaseUsers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                stageList.clear();
                for(DataSnapshot stageSnapshot: dataSnapshot.getChildren()){
                        Stage stage =stageSnapshot.getValue(Stage.class);
                        stageList.add(stage);
                }
                StageList adapter = new StageList(EtudiantFunc1Activity.this,stageList);
                listViewStages.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
}
