package com.example.finalproyect.Fragments;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.finalproyect.Clases.Nota;
import com.example.finalproyect.Daos.DAONotas;
import com.example.finalproyect.R;

import javax.microedition.khronos.egl.EGLDisplay;

/**
 * A simple {@link Fragment} subclass.
 */
public class AgregarNotasFragment extends Fragment {

    private Button btnGuardar;
    private EditText etTitulo;
    private EditText etDescripcion;

    Nota obj;

    public AgregarNotasFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_agregar_notas,container,false);


        etTitulo = (EditText) view.findViewById(R.id.etTitulo);
        etDescripcion = (EditText) view.findViewById(R.id.etDescripcion);
        btnGuardar = view.findViewById(R.id.btnGuardar);

        final DAONotas dao = new DAONotas(view.getContext());

        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String titulo = ((EditText) view.findViewById(R.id.etTitulo)).getText().toString();
                String descripcion = ((EditText) view.findViewById(R.id.etDescripcion)).getText().toString();

                obj = new Nota(0,titulo,descripcion);
                dao.insert(obj);

                Toast.makeText(view.getContext(),"Nota Insertada",Toast.LENGTH_SHORT).show();
            }
        });

    return view;
    }

}
