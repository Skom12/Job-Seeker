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
import android.widget.ProgressBar;
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

import java.util.UUID;

import dmax.dialog.SpotsDialog;


public final class profilefragment1 extends Fragment {
    private FirebaseAuth firebaseAuth;
    private  FirebaseUser user;
    private  FirebaseDatabase firebaseDatabase;
    private  DatabaseReference databaseReference;
    private StorageReference storageReference;
    private static final int PICK_IMAGE_CODE = 1000;
    private static final int PICK_CV_CODE = 2000;



    ImageView avatarIv;
    TextView emailTv, info;
    EditText PseudoTv;


    Button rinit, Buttonsave,buttonUpload;
    public AlertDialog dialog;

    ProgressBar progressBarPic1;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.profilefragment1, container, false);

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Users");




        avatarIv = view.findViewById(R.id.avatarIv);
        PseudoTv = view.findViewById(R.id.PseudoiD);
        emailTv = view.findViewById(R.id.EmailiD);
        info = view.findViewById(R.id.info);


        progressBarPic1=view.findViewById(R.id.progressbar_pic1);



        Buttonsave = (Button) view.findViewById(R.id.button_save);
        Buttonsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(getActivity(), "Modifications enregistrés", Toast.LENGTH_SHORT).show();
                saveUserInformation();
            }
        });


        rinit = (Button) view.findViewById(R.id.buttonRinit);
        rinit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intReset = new Intent(getActivity(), ResetActivity.class);
                startActivity(intReset);
            }
        });

        dialog = new SpotsDialog.Builder(getContext()).create();
        String uniqueIm = UUID.randomUUID().toString();
        storageReference=FirebaseStorage.getInstance().getReference(uniqueIm);

        buttonUpload=(Button) view.findViewById(R.id.buttonUpload);
        buttonUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent();
                intent.setType("Doc/");
                intent.setAction(intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Choisissez Votre CV"),PICK_CV_CODE);

            }
        });

        avatarIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent =new Intent();
                intent.setType("image/");
                intent.setAction(intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Choisissez une image"),PICK_IMAGE_CODE);
            }
        });



        Query query = databaseReference.orderByChild("mail").equalTo(user.getEmail());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String name = "" + ds.child("nom").getValue();
                    String Pname = "" + ds.child("prenom").getValue();
                    String Pseudo = "" + ds.child("pseudo").getValue();
                    String email = "" + ds.child("mail").getValue();
                    String infor = name + " " + Pname;
                     String image= (String) ds.child("image").getValue();//
                    if(!(image.equals(""))){
                        Picasso.get().load(image).into(avatarIv);
                    }

                    PseudoTv.setText(Pseudo);
                    emailTv.setText(email);
                    info.setText(infor);


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        return view;

    }

    public void saveUserInformation() {
        String Pseudo1 = PseudoTv.getText().toString().trim();
        if(user != null){

            firebaseDatabase.getReference("Users").child(user.getUid()).child("pseudo").setValue(Pseudo1);


        }}

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if((requestCode == PICK_IMAGE_CODE)&& (data != null) ){
            dialog.show();
            UploadTask uploadTask =storageReference.putFile(data.getData());
            Task<Uri>task= uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
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
                        String Url =task.getResult().toString().substring(0,task.getResult().toString().indexOf("&token"));
                        Log.d("DIRECTLINK",Url);
                        Picasso.get().load(Url).into(avatarIv);
                        firebaseDatabase.getReference("Users").child(user.getUid()).child("image").setValue(Url);

                        dialog.dismiss();
                    }
                }
            });
        }
        else {
            FragmentManager fragmentManager =getFragmentManager();
            FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.view_pager,profilefragment1.this);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }

        if((requestCode == PICK_CV_CODE)&& (data != null) ){
            dialog.show();
            UploadTask uploadTask =storageReference.putFile(data.getData());
            Task<Uri>task= uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
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
                        firebaseDatabase.getReference("Users").child(user.getUid()).child("cv").setValue(Url1);
                        dialog.dismiss();
                    }
                }
            });
        }
        else {
            FragmentManager fragmentManager =getFragmentManager();
            FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.view_pager,profilefragment1.this);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }
   }
}