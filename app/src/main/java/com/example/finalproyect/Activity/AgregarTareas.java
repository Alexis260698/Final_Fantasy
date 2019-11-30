package com.example.finalproyect.Activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.finalproyect.Clases.AlarmReceiver;
import com.example.finalproyect.Clases.Recordatorio;
import com.example.finalproyect.Clases.RecordatorioAuxiliar;
import com.example.finalproyect.Clases.Tarea;
import com.example.finalproyect.Daos.DaoRecordatorios;
import com.example.finalproyect.Daos.DaoTareas;
import com.example.finalproyect.R;

import java.util.ArrayList;
import java.util.Calendar;

public class AgregarTareas extends AppCompatActivity {

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
    ArrayList <RecordatorioAuxiliar> noSave = new ArrayList<>();
    private int dayRecordaotio, monthRecordatorio, yearRecordatorio, hourRecordatorio, minRecordatorio;
    String mRecordatorio,dRecordatorios,fechaRecordatorio, horaRecordatorio, minutosRecordatorio, hrRecordatorios;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_tareas);


        tvTitulo=(TextView) findViewById(R.id.tvTitulo);
        tvDescripcion=(TextView) findViewById(R.id.etTitulo);
        etTitulo=(EditText) findViewById(R.id.etTitulo);
        etDescripcion=(EditText) findViewById(R.id.etDescripcion);
        btnRecordatorio=(Button) findViewById(R.id.btnRecordatorio);
        btnPlusrecordatorios=(Button)findViewById(R.id.btnPlusrecordatorios);
        btnGuardar=(Button)findViewById(R.id.btnGuardar);

        btnRecordatorio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirCalenadario(v);
            }
        });


        btnPlusrecordatorios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirCalenadarioRecordatorio(view);
            }
        });

        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insert(view);
                crearNotificacion(year,month,day,hour,min);
                insertRecordatorios(view);
                btnRecordatorio.setEnabled(true);

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
                btnRecordatorio.setEnabled(false);


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

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
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

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {

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

        tarea = new Tarea(0, etTitulo.getText().toString(), etDescripcion.getText().toString(), fecha, hr);

        DaoTareas dao = new DaoTareas(this);
       Toast toast1=
               Toast.makeText(this,"se agrego la tarea",Toast.LENGTH_SHORT);
       toast1.show();
        dao.insert(tarea);



    }



    public void insertRecordatorios(View view){
        String[] Tareas1 = {""}; //para que me devuelva todas las tareas y yo tomar la ultima
        DaoTareas daoTareas = new DaoTareas(this);

        ArrayList<Integer> arrayIds = new ArrayList<>();
        arrayIds = daoTareas.buscarUltimoId(Tareas1); //El array que me gusrda todos los ids de las Tareas

        if(noSave !=null) {
            for (int i = 0; i < noSave.size(); i++) {

                crearNotiRecordatorio(noSave.get(i).getYear(), noSave.get(i).getMonth(), noSave.get(i).getDay(),noSave.get(i).getHour(), noSave.get(i).getMin());

                Recordatorio recordatorio = new Recordatorio(0, noSave.get(i).getFecha(), noSave.get(i).getHora(), arrayIds.get(arrayIds.size()-1));
                DaoRecordatorios daoRecordatorios = new DaoRecordatorios(this);

                switch (view.getId()) {
                    case R.id.btnGuardar:
                        daoRecordatorios.insert(recordatorio);
                        Log.i("Recordatorios", ""+recordatorio.getId());
                }
            }

        }else{

        }
        finish();
    }

    public void crearNotiRecordatorio(int year, int month, int day, int hour, int min){
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent notificationIntent = new Intent(this, AlarmReceiver.class);
        notificationIntent.putExtra("tarea", "Recordatorio de la tarea "+tarea.getTitulo());
        PendingIntent broadcast = PendingIntent.getBroadcast(this, 200, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        calendarRecordatorios.set(year,month,day,hour,min,0);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendarRecordatorios.getTimeInMillis(), broadcast);

        Log.i("Recordatorios", "Hora "+hour+":"+min);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void crearNotificacion(int year, int month, int day, int hour, int min){
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        Intent notificationIntent = new Intent(this, AlarmReceiver.class);

        notificationIntent.putExtra("tarea", "Realizar la tarea "+tarea.getTitulo());

        PendingIntent broadcast = PendingIntent.getBroadcast(this, 100, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);



        c.set(year,month,day,hour,min,0);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), broadcast);

        Toast.makeText(this, "Se creo la notificacion ", Toast.LENGTH_SHORT).show();
    }

}
