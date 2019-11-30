package com.example.finalproyect.Fragments;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.example.finalproyect.Activity.ActualizarNotas;
import com.example.finalproyect.Activity.ActualizarTareas;
import com.example.finalproyect.Activity.AgregarNotas;
import com.example.finalproyect.Clases.Nota;
import com.example.finalproyect.Clases.Tarea;
import com.example.finalproyect.Daos.DAONotas;
import com.example.finalproyect.Daos.DaoRecordatorios;
import com.example.finalproyect.Daos.DaoTareas;
import com.example.finalproyect.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;

/**
 * A simple {@link Fragment} subclass.
 */
public class VerNotasFragment extends Fragment {


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private ListView listView;
    private ArrayAdapter<Nota> adapter;
    private DAONotas daoNotas;
    private ArrayList<Nota> notas;

    private OnFragmentInteractionListener mListener;

    public VerNotasFragment() {
        // Required empty public constructor
    }

    public static VerNotasFragment newInstance(String param1, String param2) {
        VerNotasFragment fragment = new VerNotasFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_ver_notas, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        FloatingActionButton fab = (FloatingActionButton) getView().findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AgregarNotas.class);
                startActivity(intent);
            }
        });


        String[] Notas1 = {"", ""};

        daoNotas = new DAONotas(getActivity());

        notas = daoNotas.buscarporTitulo(Notas1);

        adapter = new ArrayAdapter<Nota>(getActivity(), android.R.layout.simple_list_item_1, notas);

        listView = (ListView) getActivity().findViewById(R.id.lstNotas);

        listView.setAdapter(adapter);

        registerForContextMenu(listView);

    }

    @Override
    public void onResume() {
        super.onResume();

        daoNotas = new DAONotas(getActivity());

        adapter = new ArrayAdapter<Nota>(getActivity(), android.R.layout.simple_list_item_1, notas);

        listView = (ListView) getActivity().findViewById(R.id.lstNotas);

        listView.setAdapter(adapter);

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View lv, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, lv, menuInfo);
        MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.context_menu, menu);
    }

    public boolean onContextItemSelected(MenuItem item) {
        daoNotas = new DAONotas(getActivity());

        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        listView = (ListView) getActivity().findViewById(R.id.lstNotas);

        Nota nota = (Nota) listView.getItemAtPosition(info.position);

        switch (item.getItemId()) {
            case R.id.eliminar:
                daoNotas.eliminar(nota.getId());

                daoNotas = new DAONotas(getActivity());

                adapter = new ArrayAdapter<Nota>(getActivity(), android.R.layout.simple_list_item_1, notas);

                listView = (ListView) getActivity().findViewById(R.id.lstNotas);

                listView.setAdapter(adapter);

                return true;
            case R.id.actualizar:
                Intent intent = new Intent(getActivity(), ActualizarNotas.class);
                intent.putExtra("Nota", nota);
                startActivity(intent);
                return true;
            default:
                return super.onContextItemSelected(item);
        }

    }


    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }



}
