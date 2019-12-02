package com.example.finalproyect.Activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.finalproyect.Clases.Modelo;
import com.example.finalproyect.Clases.Nota;
import com.example.finalproyect.Clases.RecyclerViewAdapter;
import com.example.finalproyect.Clases.Ruta;
import com.example.finalproyect.Clases.Tarea;
import com.example.finalproyect.Daos.DaoRutas;
import com.example.finalproyect.Daos.DaoRutasNotas;
import com.example.finalproyect.R;

import java.util.ArrayList;

public class VerMultimedia extends AppCompatActivity {

    RecyclerViewAdapter recyclerViewAdapter;
    ArrayAdapter<Modelo> adapter;
    ArrayList<Modelo> listaModelos = new ArrayList<>();
    RecyclerView recyclerView;
    Nota nota;
    Tarea tarea;
    DaoRutasNotas daoRutasNotas;
    DaoRutas daoRutas;
    ArrayList<Ruta> rutas = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_multimedia);

        recyclerView = findViewById(R.id.recyclerMultimedia);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        daoRutasNotas = new DaoRutasNotas(this);
        daoRutas = new DaoRutas(this);

        nota = (Nota) getIntent().getExtras().getSerializable("nota");
        tarea = (Tarea) getIntent().getExtras().getSerializable("tarea");

        if(nota != null){
            String[] id = {String.valueOf(nota.getId())};
            rutas = daoRutasNotas.buscarObjeto(id);
        }else if(tarea != null){
            String[] id = {String.valueOf(tarea.getId())};
            rutas = daoRutas.buscarObjeto(id);
        }

        int i;
        for (i=0; i<rutas.size(); i++){
            Toast.makeText(this,"entro",Toast.LENGTH_SHORT).show();
            if (rutas.get(i).getTipo()==0){
                Modelo model = new Modelo(Modelo.IMAGE_TYPE,rutas.get(i).getRuta());
                listaModelos.add(model);
            }else if(rutas.get(i).getTipo()==1){
                Modelo model = new Modelo(Modelo.AUDIO_TYPE, rutas.get(i).getRuta());
                listaModelos.add(model);
            }else if(rutas.get(i).getTipo()==2){
                Modelo model = new Modelo(Modelo.VIDEO_TYPE, rutas.get(i).getRuta());
                listaModelos.add(model);
            }
        }

        recyclerViewAdapter = new RecyclerViewAdapter(this, listaModelos);
        recyclerView.setAdapter(recyclerViewAdapter);

        //Toast.makeText(this,String.valueOf(rutas.size()),Toast.LENGTH_SHORT).show();
    }
}
