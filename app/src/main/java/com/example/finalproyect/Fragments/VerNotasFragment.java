package com.example.finalproyect.Fragments;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.finalproyect.Clases.Nota;
import com.example.finalproyect.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class VerNotasFragment extends Fragment {

    private ListView listView;
    private ArrayList<Nota> notas;
    private ArrayAdapter<Nota> adapter;

    public VerNotasFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_ver_notas, container, false);

        listView = view.findViewById(R.id.lstNotas);

        List<String> notas = new ArrayList<String>();


        return view;
    }

}
