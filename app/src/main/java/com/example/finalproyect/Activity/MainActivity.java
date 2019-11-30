package com.example.finalproyect.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.example.finalproyect.Fragments.AgregarNotasFragment;
import com.example.finalproyect.Fragments.AgregarTareasFragment;
import com.example.finalproyect.Fragments.BienvenidaFragment;
import com.example.finalproyect.Fragments.VerNotasFragment;
import com.example.finalproyect.Fragments.VerTareasFragment;
import com.example.finalproyect.R;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setToolbar();
        setFragmentByDefault();

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.navview);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                boolean fragmentTransaction = false;
                Fragment  fragment = null;

                switch (menuItem.getItemId()){
                    case R.id.menu_ver_notas:
                        fragment = new VerNotasFragment();
                        fragmentTransaction = true;
                        break;
                    case R.id.menu_ver_tareas:
                        fragment = new VerTareasFragment();
                        fragmentTransaction = true;
                        break;
                }

                if (fragmentTransaction){
                    getSupportFragmentManager().beginTransaction().replace(R.id.content_frame,fragment).commit();
                    menuItem.setChecked(true);
                    getSupportActionBar().setTitle(menuItem.getTitle());
                    drawerLayout.closeDrawers();
                }

                return false;
            }
        });


    }

    private void setFragmentByDefault(){
        getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, new BienvenidaFragment()).commit();
    }

    private void setToolbar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_home);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                //abrir menu lateral
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
