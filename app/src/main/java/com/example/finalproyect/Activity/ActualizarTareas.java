package com.example.finalproyect.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.finalproyect.Clases.Tarea;
import com.example.finalproyect.Daos.DaoTareas;
import com.example.finalproyect.R;

import java.util.Calendar;

public class ActualizarTareas extends AppCompatActivity {

    Tarea tarea;
    EditText etTitulo;
    EditText etDescripcion;
    Button btnRecordatorio;
    Button btnGuardar;
    DaoTareas dao;
    private int day, month, year, hour, min;
    String m,d,fecha, hora, minutos, hr;
    final Calendar c = Calendar.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actualizar_tareas);

        tarea = (Tarea)getIntent().getExtras().getSerializable("tarea");

        etTitulo=(EditText)findViewById(R.id.etTitulo);
        etDescripcion=(EditText)findViewById(R.id.etDescripcion);
        btnRecordatorio=(Button)findViewById(R.id.btnRecordatorio);
        btnGuardar=(Button)findViewById(R.id.btnGuardar);

        etTitulo.setText(tarea.getTitulo());
        etDescripcion.setText(tarea.getDescripcion());
        btnRecordatorio.setText(tarea.getFecha()+"  "+tarea.getHora());
        dao = new DaoTareas(this);


        btnRecordatorio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirCalenadario(v);
            }
        });

        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tarea.setTitulo(etTitulo.getText().toString());
                tarea.setDescripcion(etDescripcion.getText().toString());
                tarea.setFecha(fecha);
                tarea.setHora(hr);


                dao.update(tarea);
                finish();

            }
        });

    }

    private void abrirCalenadario(View view) {
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);
        hour = c.get(Calendar.HOUR_OF_DAY);
        min = c.get(Calendar.MINUTE);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
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
                //efecha.setText(dayOfMonth+"/"+(month + 1)+"/"+year);
                //btnFecha.setText(year+"/"+m+"/"+d);
                fecha= ""+year+"/"+m+"/"+d;

                abrirReloj();
            }
        } ,year,month,day);
        datePickerDialog.show();

    }

    private void abrirReloj() {
        hour = c.get(Calendar.HOUR_OF_DAY);
        min = c.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {

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

}
