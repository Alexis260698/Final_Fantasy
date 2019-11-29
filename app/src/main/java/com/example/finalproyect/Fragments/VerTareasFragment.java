package com.example.finalproyect.Fragments;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.finalproyect.Daos.DaoTareas;
import com.example.finalproyect.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class VerTareasFragment extends Fragment {

    private ListView lvTareas;
    private DaoTareas daoTareas;
    private ArrayList<Tarea> tareas;
    private ArrayAdapter<Tarea> adapter;


    public VerTareasFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_ver_tareas, container, false);

        lvTareas = view.findViewById(R.id.lstTareas);

        return view;
    }
    

}
