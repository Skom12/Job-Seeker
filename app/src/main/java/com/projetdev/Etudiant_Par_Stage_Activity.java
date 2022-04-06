package com.projetdev;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class Etudiant_Par_Stage_Activity extends AppCompatActivity {
    ListView EtudiantparStageListView;
    private DatabaseReference DatabaseUsers, DatabaseStage;
    FirebaseAuth mauth;
    List<User> etudiantList;

    String stageid;
    String test;
    List mData;
    Button btnRetour5;
    TextView empty1;


    @Override
    public void onBackPressed() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_etudiant__par__stage_);
        Intent intent = getIntent();
        stageid = intent.getStringExtra("id");
        EtudiantparStageListView = findViewById(R.id.EtudiantparStageListView);
        DatabaseStage = FirebaseDatabase.getInstance().getReference("Stages").child(stageid).child("userEtudList");
        DatabaseUsers = FirebaseDatabase.getInstance().getReference("Users");
        etudiantList = new ArrayList<User>();
        mData = new ArrayList<>();
        empty1=(TextView)findViewById(R.id.empty1);
        empty1.setText("Aucun étudiant n'a encore postulé pour ce stage.");
        EtudiantparStageListView.setEmptyView(empty1);
        btnRetour5=(Button)findViewById(R.id.btnRetour5);
        btnRetour5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Etudiant_Par_Stage_Activity.this,EntFunc1Activity.class));
            }
        });

        EtudiantparStageListView.setClickable(true);
        EtudiantparStageListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                User user=(User) parent.getItemAtPosition(position);
                String url=user.cv;
                if(!url.equals("")){
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    startActivity(i);

                }
                else{
                    Toast.makeText(Etudiant_Par_Stage_Activity.this, "L'étudiant n'a pas encore téléchargé son CV", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        DatabaseStage.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
              //  mData.clear();
                test="";
                etudiantList.clear();
                for(DataSnapshot data:dataSnapshot.getChildren()){
                   // mData.add(data);
                    test=data.getValue().toString();
                    if(!test.equals("test")){
                        DatabaseUsers.orderByChild("uid").equalTo(test).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                    User user = ds.getValue(User.class);
                                    etudiantList.add(user);

                                    EtudiantList adapter = new EtudiantList(Etudiant_Par_Stage_Activity.this, etudiantList);
                                    EtudiantparStageListView.setAdapter(adapter);
                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });






}
}



//               for (Map.Entry<String, Object> entry : etudiantMap.entrySet()) {
//                    Toast.makeText(Etudiant_Par_Stage_Activity.this, entry.getKey() + " = " + entry.getValue(), Toast.LENGTH_SHORT).show();
//                }



