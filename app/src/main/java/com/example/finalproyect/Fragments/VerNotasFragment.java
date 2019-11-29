package com.example.finalproyect.Fragments;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.finalproyect.Clases.Nota;
import com.example.finalproyect.Daos.DAONotas;
import com.example.finalproyect.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class VerNotasFragment extends Fragment {

    private ListView listView;
    private ArrayList<Nota> notas;
    private ArrayAdapter<Nota> adapter;
    private DAONotas daoNotas;

    public VerNotasFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_ver_notas, container, false);

        listView = view.findViewById(R.id.lstTareas);

        String[] Notas1 = {""};

        notas= daoNotas.buscarporTitulo(Notas1);

        adapter = new ArrayAdapter<Nota>(view.getContext(),android.R.layout.simple_list_item_1,notas);

        listView.setAdapter(adapter);

        return view;
    }

}
