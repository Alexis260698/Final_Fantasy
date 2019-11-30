package com.example.finalproyect.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;

import com.example.finalproyect.Clases.Nota;
import com.example.finalproyect.Fragments.VerNotasFragment;
import com.example.finalproyect.Fragments.VistaFragment;
import com.example.finalproyect.R;

public class intermedio extends FragmentActivity implements VerNotasFragment.DataListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intermedio);

    }

    @Override
    public void sendData(Nota nota) {
        VistaFragment vistaFragment = (VistaFragment) getSupportFragmentManager().findFragmentById(R.id.)
    }
}
