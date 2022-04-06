package com.projetdev;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.UUID;

import dmax.dialog.SpotsDialog;

public class profilefragment2 extends Fragment {
    private  FirebaseAuth firebaseAuth;
    private  FirebaseUser user1;
    private FirebaseDatabase firebaseDatabase;
    private  DatabaseReference databaseReference,mDatabase;
    private  StorageReference storageReference;
    private DatabaseReference DatabaseUsers;
    public AlertDialog dialog1;
    private static final int PICK_IMAGE_CODE = 1000;


    ImageView avatarEntIv;
    TextView emailEntTv;
    EditText addresse,PseudoEntTv;
    Button rinit,saveEnt;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.profilefragment2,container,false);
        firebaseAuth= FirebaseAuth.getInstance();
        user1=firebaseAuth.getCurrentUser();

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference= firebaseDatabase.getReference("Users");
        String userID = user1.getUid();




        avatarEntIv = view.findViewById(R.id.avatarEntIv);
        PseudoEntTv=view.findViewById(R.id.PseudoEntiD);
        emailEntTv=view.findViewById(R.id.EmailEntiD);
        saveEnt=(Button)view.findViewById(R.id.button_save2) ;
        addresse =(EditText)view.findViewById(R.id.Adresse);




        dialog1 = new SpotsDialog.Builder(getContext()).create();
        String uniqueIm1 = UUID.randomUUID().toString();

        storageReference= FirebaseStorage.getInstance().getReference(uniqueIm1);
        avatarEntIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent();
                intent.setType("image/");
                intent.setAction(intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Choisissez une image"),PICK_IMAGE_CODE);
            }
        });


        saveEnt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(),"Modifications enregistrés", Toast.LENGTH_SHORT).show();
                saedataEnt();

            }
       });


         rinit=(Button)view.findViewById(R.id.buttonRinit);
         rinit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              Intent intReset = new Intent(getActivity(), ResetActivity.class);
            startActivity(intReset);
            }
         });

        Query query= databaseReference.orderByChild("mail").equalTo(user1.getEmail());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds:dataSnapshot.getChildren()){
                    String PseudoEnt=""+ ds.child("pseudo").getValue();
                    String emailEnt=""+ ds.child("mail").getValue();
                    String imageEnt=""+ ds.child("image").getValue();
                    String addEnt=""+ ds.child("adresse").getValue();
                       String image= (String) ds.child("image").getValue();
                    if(!(image.equals(""))){
                        Picasso.get().load(image).into(avatarEntIv);
                    }

                    PseudoEntTv.setText(PseudoEnt);
                    emailEntTv.setText(emailEnt);
                    addresse.setText(addEnt);


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return view;

    }

    private void saedataEnt() {
        String adresse1 = addresse.getText().toString().trim();
        String PseudoEnt1=PseudoEntTv.getText().toString().trim();
        if(user1 != null){

            firebaseDatabase.getReference("Users").child(user1.getUid()).child("pseudo").setValue(PseudoEnt1);
            firebaseDatabase.getReference("Users").child(user1.getUid()).child("adresse").setValue(adresse1);}




    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if((requestCode == PICK_IMAGE_CODE) && (data != null) ){
            dialog1.show();
            UploadTask uploadTask =storageReference.putFile(data.getData());
            Task<Uri>task1= uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()){
                        Toast.makeText(getActivity(),"Erreur de téléchargement",Toast.LENGTH_SHORT).show();

                    }
                    return  storageReference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if(task.isSuccessful()){
                        String Url1 =task.getResult().toString().substring(0,task.getResult().toString().indexOf("&token"));
                        Log.d("DIRECTLINK",Url1);
                        Picasso.get().load(Url1).into(avatarEntIv);
                        firebaseDatabase.getReference("Users").child(user1.getUid()).child("image").setValue(Url1);
                        dialog1.dismiss();
                    }

                }
            });
        }
        else {
            FragmentManager fragmentManager =getFragmentManager();
            FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.view_pager2,profilefragment2.this);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }
    }
}