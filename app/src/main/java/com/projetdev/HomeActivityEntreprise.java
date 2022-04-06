package com.projetdev;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class HomeActivityEntreprise extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private BottomNavigationView bottomNav;
    private Fragment selectedFragment;
    private Fragment startingFragment;
    @Override
    public void onBackPressed() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_entreprise);
        bottomNav = findViewById(R.id.bottom_navigation_view2);


        try {
            startingFragment= (Fragment) homefragmentEnt.class.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.view_pager2,startingFragment).commit();

        bottomNav.setOnNavigationItemSelectedListener(navListener);
    }
    private BottomNavigationView.OnNavigationItemSelectedListener navListener =new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            selectedFragment = null;
            Class fragmentClass2 = null;
            if((menuItem.getItemId())== (R.id.nav_dc)){
                mAuth.getInstance().signOut();
                Toast.makeText(HomeActivityEntreprise.this, "vous êtes déconnecté(e)s", Toast.LENGTH_SHORT).show();
                Intent inte= new Intent(HomeActivityEntreprise.this ,MainActivity.class);
                startActivity(inte);
            }
            else{
                switch (menuItem.getItemId()){
                    case R.id.nav_profile:
                        fragmentClass2= profilefragment2.class;
                        break;
                    case R.id.nav_home:
                        fragmentClass2=homefragmentEnt.class;
                        break;
                }

                try {
                    selectedFragment = (Fragment) fragmentClass2.newInstance();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.view_pager2,selectedFragment).commit();
            }

            return true;
        }
    };

}