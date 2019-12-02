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
import android.widget.ListView;

import com.example.finalproyect.Activity.ActualizarTareas;
import com.example.finalproyect.Activity.AgregarTareas;
import com.example.finalproyect.Activity.VerMultimedia;
import com.example.finalproyect.Clases.Tarea;
import com.example.finalproyect.Daos.DaoTareas;
import com.example.finalproyect.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class VerTareasFragment extends Fragment {

    private ListView lvTareas;
    private DaoTareas daoTareas;
    private ArrayList<Tarea> tareas;
    private ArrayAdapter<Tarea> adapter;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public VerTareasFragment() {

    }

    public static VerTareasFragment newInstance(String param1, String param2) {
        VerTareasFragment fragment = new VerTareasFragment();
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
        View view=inflater.inflate(R.layout.fragment_ver_tareas, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle saveInstanceState){
        FloatingActionButton fab = (FloatingActionButton) getView().findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AgregarTareas.class);
                startActivity(intent);
            }
        });


        String[] Tareas1 = {""};

        daoTareas = new DaoTareas(getActivity());

        tareas = daoTareas.buscarporTitulo(Tareas1);

        adapter = new ArrayAdapter<Tarea>(getActivity(), android.R.layout.simple_list_item_1, tareas);

        lvTareas = (ListView) getActivity().findViewById(R.id.lstTareas);

        lvTareas.setAdapter(adapter);

        registerForContextMenu(lvTareas);
    }


    @Override
    public void onResume() {
        super.onResume();
        String[] Tarea1 = {""};

        daoTareas = new DaoTareas(getActivity());
        tareas = daoTareas.buscarporTitulo(Tarea1);
        adapter = new  ArrayAdapter<Tarea>(getActivity(), android.R.layout.simple_list_item_1, tareas);
        lvTareas = (ListView) getActivity().findViewById(R.id.lstTareas);
        lvTareas.setAdapter(adapter);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View lv, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, lv, menuInfo);
        MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.context_menu, menu);
    }

    public boolean onContextItemSelected(MenuItem item) {
        daoTareas = new DaoTareas(getActivity());

        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        lvTareas = (ListView) getActivity().findViewById(R.id.lstTareas);

        Tarea tarea = (Tarea) lvTareas.getItemAtPosition(info.position);

        switch (item.getItemId()) {
            case R.id.ver:
                Intent intentVer = new Intent(getActivity(), VerMultimedia.class);
                intentVer.putExtra("tarea", tarea);
                startActivity(intentVer);
                return true;
            case R.id.borrar:
                daoTareas.eliminar(tarea.getId());
                daoTareas = new DaoTareas(getActivity());
                adapter = new ArrayAdapter<Tarea>(getActivity(), android.R.layout.simple_list_item_1, tareas);
                lvTareas = (ListView) getActivity().findViewById(R.id.lstTareas);
                lvTareas.setAdapter(adapter);
                return true;
            case R.id.editar:
                Intent intent = new Intent(getActivity(), ActualizarTareas.class);
                intent.putExtra("tarea", tarea);
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
