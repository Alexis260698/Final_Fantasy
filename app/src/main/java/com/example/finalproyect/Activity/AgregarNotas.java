package com.example.finalproyect.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.finalproyect.Clases.Nota;
import com.example.finalproyect.Daos.DAONotas;
import com.example.finalproyect.Fragments.VerNotasFragment;
import com.example.finalproyect.R;

public class AgregarNotas extends AppCompatActivity {

    private Button btnGuardar;
    private EditText etTitulo;
    private EditText etDescripcion;

    Nota obj;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_notas);

        etTitulo = (EditText) findViewById(R.id.etTitulo);
        etDescripcion = (EditText) findViewById(R.id.etDescripcion);
        btnGuardar = findViewById(R.id.btnGuardar);

        final DAONotas dao = new DAONotas(this);

        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String titulo = ((EditText) findViewById(R.id.etTitulo)).getText().toString();
                String descripcion = ((EditText) findViewById(R.id.etDescripcion)).getText().toString();

                obj = new Nota(0,titulo,descripcion);
                dao.insert(obj);

                Toast.makeText(getApplicationContext(),"Nota Insertada",Toast.LENGTH_SHORT).show();
            }
        });
    }

}
