package com.example.finalproyect.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.finalproyect.Clases.Nota;
import com.example.finalproyect.Daos.DAONotas;
import com.example.finalproyect.R;

public class ActualizarNotas extends AppCompatActivity {
    Nota nota;
    EditText etTitulo;
    EditText etDescripcion;
    Button btnActualizar;
    DAONotas dao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actualizar_notas);

        nota = (Nota)getIntent().getExtras().getSerializable("nota");

        etTitulo=(EditText)findViewById(R.id.etTitulo);
        etDescripcion=(EditText)findViewById(R.id.etDescripcion);
        btnActualizar=(Button)findViewById(R.id.btnActualizar);
         dao=new DAONotas(this);

        etTitulo.setText(nota.getTitulo());
        etDescripcion.setText(nota.getDescripcion());

        btnActualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              actualizar(view);

            }
        });

    }


    private void actualizar(View view){
        nota.setTitulo(etTitulo.getText().toString());
        nota.setDescripcion(etDescripcion.getText().toString());

        DAONotas dao = new DAONotas(this);


        dao.update(nota);
        finish();

    }

}
