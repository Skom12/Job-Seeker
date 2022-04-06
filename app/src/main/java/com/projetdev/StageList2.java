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

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;
import java.util.zip.Inflater;

public class StageList2 extends ArrayAdapter {
    private Activity context;
    private List<Stage> stageList2;
    private DatabaseReference DatabaseStage;

    TextView TvDispoType,TvDispoDomaine,TvDispoDurée,TvDispoDate;
    ImageView imageViewClose;

    public StageList2(Activity context,List<Stage> stageList2){
        super(context,R.layout.list_stage_2_layout,stageList2);
        this.context=context;
        this.stageList2=stageList2;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater Inflater= context.getLayoutInflater();

        View ListViewItem2= Inflater.inflate(R.layout.list_stage_2_layout,null,true);

        TvDispoType=(TextView)ListViewItem2.findViewById(R.id.TvDispType);
        TvDispoDomaine=(TextView)ListViewItem2.findViewById(R.id.TvDispDomaine);
        TvDispoDurée=(TextView)ListViewItem2.findViewById(R.id.TvDispDurée);
        TvDispoDate=(TextView)ListViewItem2.findViewById(R.id.TvDispDate);
        imageViewClose=(ImageView)ListViewItem2.findViewById(R.id.imageViewClose);
        Stage stage = stageList2.get(position);

        TvDispoType.setText(stage.type);
        TvDispoDomaine.setText("Dans le domaine de : "+stage.domaine);
        TvDispoDurée.setText("Durée : "+stage.durée);
        TvDispoDate.setText("Commence le : "+stage.date);

        DatabaseStage = FirebaseDatabase.getInstance().getReference("Stages");
        imageViewClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseStage.child(stage.uniqueId).removeValue();
            }
        });

        return ListViewItem2;

    }
}
