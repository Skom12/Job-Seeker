package com.projetdev;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.zip.Inflater;

public class EtudiantList extends ArrayAdapter {
    private Activity context;
    private List<User> etudiantList;

    TextView TVNom, TVMAIL, TVCV;
    ImageView imageViewEtudiant;

    public EtudiantList(Activity context, List<User> etudiantList) {
        super(context, R.layout.list_etudiant_layout, etudiantList);
        this.context = context;
        this.etudiantList = etudiantList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();

        View ListViewItem = inflater.inflate(R.layout.list_etudiant_layout, null, true);
        User usr = etudiantList.get(position);

        TVNom = (TextView) ListViewItem.findViewById(R.id.TVNom);
        TVMAIL = (TextView) ListViewItem.findViewById(R.id.TVMAIL);
        TVCV = (TextView) ListViewItem.findViewById(R.id.TVCV);
        imageViewEtudiant = (ImageView) ListViewItem.findViewById(R.id.imageViewEtudiant);

        TVNom.setText(" "+usr.nom+" "+usr.prenom);
        TVMAIL.setText(" "+usr.mail);



        if(!(usr.image.equals(""))){
            Picasso.get().load(usr.image).into(imageViewEtudiant);
        }


        return ListViewItem;
    }
}
