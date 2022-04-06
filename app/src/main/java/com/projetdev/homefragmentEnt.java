package com.projetdev;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class homefragmentEnt extends Fragment {
    Button btnEntF1,btnEntF2;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.homeentreprisefragment, container,false);
        btnEntF1=(Button) view.findViewById(R.id.buttonEntFunc1);
        btnEntF2=(Button)view.findViewById(R.id.buttonEntFunc2);


        btnEntF1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(),EntFunc1Activity.class));
            }
        });
        btnEntF2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(),EntFunc2Activity.class));
            }
        });
        return view;
    }
}
