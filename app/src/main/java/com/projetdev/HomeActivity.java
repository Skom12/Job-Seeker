package com.projetdev;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class HomeActivity extends AppCompatActivity  {

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
        setContentView(R.layout.activity_home);
        bottomNav = findViewById(R.id.bottom_navigation_view);
        try {
            startingFragment= (Fragment) homefragment.class.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.view_pager,startingFragment).commit();
        bottomNav.setOnNavigationItemSelectedListener(navListener);
    }
    private BottomNavigationView.OnNavigationItemSelectedListener navListener =new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            selectedFragment = null;
            Class fragmentClass = null;
            if((menuItem.getItemId())== (R.id.nav_dc)){
                mAuth.getInstance().signOut();
                Toast.makeText(HomeActivity.this, "vous êtes déconnecté(e)s", Toast.LENGTH_SHORT).show();
                Intent inte= new Intent(HomeActivity.this ,MainActivity.class);
                startActivity(inte);
            }
            else{
                switch (menuItem.getItemId()){
                    case R.id.nav_profile:
                        fragmentClass= profilefragment1.class;
                        break;
                    case R.id.nav_home:
                        fragmentClass=homefragment.class;
                        break;
                }

                try {
                    selectedFragment = (Fragment) fragmentClass.newInstance();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.view_pager,selectedFragment).commit();
            }

            return true;
        }
    };

}
