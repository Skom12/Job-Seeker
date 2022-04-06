package com.projetdev;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.UUID;

public class StageList extends ArrayAdapter<Stage> {
 private Activity context;
 private List<Stage> stageList;


    TextView TVTYPE,TVPAR,TVDom;
    ImageView imageViewEntreprise;

 public StageList(Activity context, List<Stage> stageList){
     super(context,R.layout.list_stage_layout,stageList);
     this.context=context;
     this.stageList = stageList;
 }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();

        View ListViewItem = inflater.inflate(R.layout.list_stage_layout,null,true);

        TVTYPE=(TextView)ListViewItem.findViewById(R.id.TVTYPE);
        TVPAR=(TextView)ListViewItem.findViewById(R.id.TVPAR);

        TVDom=(TextView)ListViewItem.findViewById(R.id.TVDom);

        imageViewEntreprise=(ImageView)ListViewItem.findViewById(R.id.imageViewEntreprise);

        Stage stage = stageList.get(position);

        TVTYPE.setText(" "+stage.type);
        TVPAR.setText(" Proposé par : "+stage.nomEnt);
        TVDom.setText(" Domaine : "+stage.domaine);

        if((stage.imageEnt.equals(""))){

            Query query = FirebaseDatabase.getInstance().getReference("Users").orderByChild("uid").equalTo(stage.subUserId);
            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String image1 = ds.child("image").getValue().toString().trim();
                    FirebaseDatabase.getInstance().getReference("Stages").child(stage.uniqueId).child("imageEnt").setValue(image1);
                    stage.imageEnt=image1;
                    if(!(stage.imageEnt.equals(""))){
                        Picasso.get().load(image1).into(imageViewEntreprise);

                    }

                }}

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });}
        if(!(stage.imageEnt.equals(""))){
            Picasso.get().load(stage.imageEnt).into(imageViewEntreprise);

        }


        return ListViewItem;


    }

}
