package com.example.androidproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomnavigation.LabelVisibilityMode;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
Button logOut;
private Toolbar tb;
Fragment fg;
BottomNavigationView bottomNavigationView;
BottomNavigationView topNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        topNavigationView=findViewById(R.id.topNav);

        topNavigationView.setLabelVisibilityMode(LabelVisibilityMode.LABEL_VISIBILITY_LABELED);

        topNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                FragmentManager fm;
                FragmentTransaction ft;
                switch (item.getItemId()){
                    case  R.id.listDemande:
                        View f=findViewById(R.id.fragment_place);
                        f.setBackground(null);
                        fg=new listDemandeFragment();
                        fm=getSupportFragmentManager();
                         ft=fm.beginTransaction();
                        ft.replace(R.id.fragment_place,fg);
                        ft.commit();
                        break;
                    case  R.id.listDon:
                        f=findViewById(R.id.fragment_place);
                        f.setBackground(null);
                        fg=new listDonFragment();
                         fm=getSupportFragmentManager();
                         ft=fm.beginTransaction();
                        ft.replace(R.id.fragment_place,fg);
                        ft.commit();
                        break;
                    case  R.id.listCloseDon:
                         f=findViewById(R.id.fragment_place);
                        f.setBackground(null);
                        fg=new ListCloseDons();
                        fm=getSupportFragmentManager();
                        ft=fm.beginTransaction();
                        ft.replace(R.id.fragment_place,fg);
                        ft.commit();
                        break;
                    case  R.id.update:
                        f=findViewById(R.id.fragment_place);
                        f.setBackground(null);
                        fg=new updateCompte();
                        fm=getSupportFragmentManager();
                        ft=fm.beginTransaction();
                        ft.replace(R.id.fragment_place,fg);
                        ft.commit();
                        break;

                    case  R.id.logout:
                        FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(getApplicationContext(),login.class));
                        finish();
                        break;


                }
                return true;
            }
        });
bottomNavigationView=findViewById(R.id.bottomNav);
bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case  R.id.dons:
                View f=findViewById(R.id.fragment_place);
                f.setBackground(null);
                fg=new addDon();
                FragmentManager fm=getSupportFragmentManager();
                FragmentTransaction ft=fm.beginTransaction();
                ft.replace(R.id.fragment_place,fg);
                ft.commit();
                break;
            case  R.id.demandes:
                f=findViewById(R.id.fragment_place);
                f.setBackground(null);
                fg=new AddDelandeFragment();
                FragmentManager fm1=getSupportFragmentManager();
                FragmentTransaction ft1=fm1.beginTransaction();
                ft1.replace(R.id.fragment_place,fg);
                ft1.commit();
                break;
            case  R.id.listReservation:
                f=findViewById(R.id.fragment_place);
                f.setBackground(null);
                fg=new reservationFragment();
                fm=getSupportFragmentManager();
                ft=fm.beginTransaction();
                ft.replace(R.id.fragment_place,fg);
                ft.commit();
                break;


        }
        return true;

    }
});


 }



}
