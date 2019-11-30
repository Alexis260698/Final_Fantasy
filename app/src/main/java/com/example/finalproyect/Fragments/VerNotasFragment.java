package com.example.finalproyect.Fragments;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.fragment.app.FragmentManager;

import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.finalproyect.Clases.Nota;
import com.example.finalproyect.Daos.DAONotas;
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
    private DAONotas daoNotas ;
    private FragmentActivity fa;

    private DataListener callback;

    public VerNotasFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        fa = (FragmentActivity) context;
        super.onAttach(context);

        try{
            callback = (DataListener) context;
        }catch (Exception e){
            throw new ClassCastException(context.toString()+"implementa DataListener prro");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_ver_notas, container, false);

        listView = view.findViewById(R.id.lstNotas);

        daoNotas = new DAONotas(view.getContext());

        notas = daoNotas.getAll();

        adapter = new ArrayAdapter(view.getContext(),android.R.layout.simple_list_item_1,notas);

        listView.setAdapter(adapter);

        registerForContextMenu(listView);

        return view;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;



        MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        listView = getActivity().findViewById(R.id.lstNotas);
        daoNotas = new DAONotas(getActivity());
        Nota nota = (Nota) listView.getItemAtPosition(info.position);

        switch (item.getItemId()){
            case R.id.ver_nota:
                callback.sendData(nota);
                return true;
            case R.id.editar:
                Intent intent = new Intent(getActivity(),AgregarNotasFragment.class);
                intent.putExtra("nota",nota);
                //(intent);

                return true;
            case R.id.borrar:
                daoNotas.eliminar(nota.getId());
                daoNotas = new DAONotas(getActivity());
                notas = daoNotas.getAll();
                adapter = new ArrayAdapter(getActivity(),android.R.layout.simple_list_item_1,notas);
                listView.setAdapter(adapter);

                return true;

            default:
                    return super.onContextItemSelected(item);

        }
    }

    public interface DataListener{
        void sendData(Nota nota);
    }

}
