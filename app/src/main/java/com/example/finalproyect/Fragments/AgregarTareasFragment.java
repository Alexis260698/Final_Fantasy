package com.example.finalproyect.Fragments;


import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.finalproyect.Clases.AlarmReceiver;
import com.example.finalproyect.Clases.Recordatorio;
import com.example.finalproyect.Clases.RecordatorioAuxiliar;
import com.example.finalproyect.Clases.Tarea;
import com.example.finalproyect.Daos.DaoRecordatorios;
import com.example.finalproyect.Daos.DaoTareas;
import com.example.finalproyect.R;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 */
public class AgregarTareasFragment extends Fragment {

    TextView tvTitulo;
    TextView tvDescripcion;
    EditText etTitulo;
    EditText etDescripcion;
    Button btnRecordatorio;
    Button btnPlusrecordatorios;
    Button btnGuardar;


    Tarea tarea;
    private int day, month, year, hour, min;
    String m,d,fecha, hora, minutos, hr;


    final Calendar c = Calendar.getInstance();
    final Calendar calendarRecordatorios = Calendar.getInstance();
    ArrayList<Recordatorio> listaRecordatorios = new ArrayList<>();
    ArrayList <RecordatorioAuxiliar> noSave = new ArrayList<>();
    private int dayRecordaotio, monthRecordatorio, yearRecordatorio, hourRecordatorio, minRecordatorio;
    String mRecordatorio,dRecordatorios,fechaRecordatorio, horaRecordatorio, minutosRecordatorio, hrRecordatorios;



    public AgregarTareasFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_agregar_tareas, container, false);

        tvTitulo=(TextView) view.findViewById(R.id.tvTitulo);
        tvDescripcion=(TextView) view.findViewById(R.id.etTitulo);
        etTitulo=(EditText) view.findViewById(R.id.etTitulo);
        etDescripcion=(EditText) view.findViewById(R.id.etDescripcion);
        btnRecordatorio=(Button) view.findViewById(R.id.btnRecordatorio);
        btnPlusrecordatorios=(Button)view.findViewById(R.id.btnPlusrecordatorios);
        btnGuardar=(Button)view.findViewById(R.id.btnPlusrecordatorios);

        btnRecordatorio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirCalenadario(v);
            }
        });



        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insert(v);
                insertRecordatorios(v);
                //crearNotificacion(year,month,day,hour,min);
            }
        });


        return view;
    }

    public void onclick(View view){
        abrirCalenadario(view);
    }


    private void abrirCalenadario(View view) {
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);
        hour = c.get(Calendar.HOUR_OF_DAY);
        min = c.get(Calendar.MINUTE);


        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                if((month+1)<10){
                    m = "0"+""+(month+1);
                }else{
                    m = ""+(month+1);
                }
                if(dayOfMonth<10){
                    d = "0"+""+dayOfMonth;
                }else{
                    d= ""+dayOfMonth;
                }

                fecha= ""+year+"/"+m+"/"+d;

                abrirReloj();
            }
        } ,year,month,day);
        datePickerDialog.show();

    }


    private void abrirReloj() {
        hour = c.get(Calendar.HOUR_OF_DAY);
        min = c.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {

            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                if((hourOfDay+1)<10){
                    hora = "0"+""+(hourOfDay);
                }else{
                    hora = ""+(hourOfDay);
                }
                if(minute<10){
                    minutos = "0"+""+minute;
                }else{
                    minutos= ""+minute;
                }
                hr = hora+":"+minutos;
                min = minute;
                hour= hourOfDay;
                btnRecordatorio.setText(fecha+"  "+hr);


            }
        },hour,min,false);
        timePickerDialog.show();
    }


    private void abrirCalenadarioRecordatorio(View view) {
        System.out.println("entro a calendarioRecordatorio");
        yearRecordatorio = calendarRecordatorios.get(Calendar.YEAR);
        monthRecordatorio = calendarRecordatorios.get(Calendar.MONTH);
        dayRecordaotio = calendarRecordatorios.get(Calendar.DAY_OF_MONTH);
        hourRecordatorio = calendarRecordatorios.get(Calendar.HOUR_OF_DAY);
        minRecordatorio = calendarRecordatorios.get(Calendar.MINUTE);

        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                if((month+1)<10){
                    mRecordatorio = "0"+""+(month+1);
                }else{
                    mRecordatorio = ""+(month+1);
                }
                if(dayOfMonth<10){
                    dRecordatorios = "0"+""+dayOfMonth;
                }else{
                    dRecordatorios = ""+dayOfMonth;
                }

                fechaRecordatorio= ""+year+"/"+mRecordatorio+"/"+dRecordatorios;

                abrirRelojR();
            }
        } ,yearRecordatorio,monthRecordatorio,dayRecordaotio);
        datePickerDialog.show();

    }

    private void abrirRelojR() {
        hourRecordatorio = calendarRecordatorios.get(Calendar.HOUR_OF_DAY);
        minRecordatorio = calendarRecordatorios.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {

            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                if((hourOfDay+1)<10){
                    horaRecordatorio = "0"+""+(hourOfDay);
                }else{
                    horaRecordatorio = ""+(hourOfDay);
                }
                if(minute<10){
                    minutosRecordatorio = "0"+""+minute;
                }else{
                    minutosRecordatorio= ""+minute;
                }
                hrRecordatorios = horaRecordatorio+":"+minutosRecordatorio;
                minRecordatorio = minute;
                hourRecordatorio= hourOfDay;

                guardarRecordatorios(yearRecordatorio,monthRecordatorio,dayRecordaotio,hourRecordatorio,minRecordatorio, fechaRecordatorio, hrRecordatorios);
                }
                 },hourRecordatorio,minRecordatorio,false);
                 timePickerDialog.show();
    }

    public void guardarRecordatorios(int year, int month, int day, int hour, int min, String fecha, String hora){
        RecordatorioAuxiliar r = new RecordatorioAuxiliar(year, month, day, hour, min, fecha, hora);
        noSave.add(r);
    }


    private void insert(View view){
        System.out.println("entro a agregar tarea");
        tarea = new Tarea(0, etTitulo.getText().toString(), etDescripcion.getText().toString(), fecha, hr);

        DaoTareas dao = new DaoTareas(getContext());
                dao.insert(tarea);



    }

    public void insertRecordatorios(View view){
        String[] Tareas1 = {""}; //para que me devuelva todas las tareas y yo tomar la ultima
        DaoTareas daoTareas = new DaoTareas(getContext());

        ArrayList<Integer> arrayIds = new ArrayList<>();
        arrayIds = daoTareas.buscarUltimoId(Tareas1); //El array que me gusrda todos los ids de las Tareas

        if(noSave !=null) {
            for (int i = 0; i < noSave.size(); i++) {


                Recordatorio recordatorio = new Recordatorio(0, noSave.get(i).getFecha(), noSave.get(i).getHora(), arrayIds.get(arrayIds.size()-1));
                DaoRecordatorios daoRecordatorios = new DaoRecordatorios(getContext());

                switch (view.getId()) {
                    case R.id.btnGuardar:
                        daoRecordatorios.insert(recordatorio);
                        Log.i("Recordatorios", ""+recordatorio.getId());
                }
            }

        }else{

        }

    }



}
