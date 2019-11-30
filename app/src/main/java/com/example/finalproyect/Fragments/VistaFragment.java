package com.example.finalproyect.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.finalproyect.Activity.multimedia;
import com.example.finalproyect.Clases.Nota;
import com.example.finalproyect.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class VistaFragment extends Fragment {

    private Button btnMultimedia;

    private Nota nota;



    public VistaFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_vista, container, false);

        btnMultimedia = view.findViewById(R.id.btnGuardar);



        return view;
    }

}
