package com.example.finalproyect.Activity;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.finalproyect.BuildConfig;
import com.example.finalproyect.Clases.AlarmReceiver;
import com.example.finalproyect.Clases.Modelo;
import com.example.finalproyect.Clases.Nota;
import com.example.finalproyect.Clases.Recordatorio;
import com.example.finalproyect.Clases.RecordatorioAuxiliar;
import com.example.finalproyect.Clases.RecyclerViewAdapter;
import com.example.finalproyect.Clases.Ruta;
import com.example.finalproyect.Clases.Tarea;
import com.example.finalproyect.Daos.DAONotas;
import com.example.finalproyect.Daos.DaoRecordatorios;
import com.example.finalproyect.Daos.DaoRutas;
import com.example.finalproyect.Daos.DaoRutasNotas;
import com.example.finalproyect.Daos.DaoTareas;
import com.example.finalproyect.R;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class AgregarTareas extends AppCompatActivity implements View.OnClickListener {

    TextView tvTitulo;
    TextView tvDescripcion;
    EditText etTitulo;
    EditText etDescripcion;
    Button btnRecordatorio;
    Button btnPlusrecordatorios;
    Button btnGuardar;
    private String CHANNEL_ID="CANALID";

    Tarea tarea;
    private int day, month, year, hour, min;
    String m,d,fecha, hora, minutos, hr;

    final Calendar c = Calendar.getInstance();
    final Calendar calendarRecordatorios = Calendar.getInstance();
    ArrayList <RecordatorioAuxiliar> noSave = new ArrayList<>();
    private int dayRecordaotio, monthRecordatorio, yearRecordatorio, hourRecordatorio, minRecordatorio;
    String mRecordatorio,dRecordatorios,fechaRecordatorio, horaRecordatorio, minutosRecordatorio, hrRecordatorios;

    //Variables Multimedia
    private Button btnAudio, btnTomar, btnAdjuntar;

    String currentPhotoPath;

    int TOMAR_FOTO = 100;
    int SELEC_IMAGEN = 200;
    int TOMAR_VIDEO = 300;
    int SELEC_VIDEO = 400;

    //Variables para tomar foto
    String CARPETA_RAIZ = "MisFotos/";
    String CARPETA_IMAGENES = "imagenes";
    String RUTA_IMAGEN = CARPETA_RAIZ + CARPETA_IMAGENES;

    //Variables para grabar audio
    private MediaRecorder grabacion;
    private String archivoSalida = null;

    private final static String MY_PROVIDER = BuildConfig.APPLICATION_ID + ".providers.FileProvider";

    RecyclerViewAdapter recyclerViewAdapter;
    ArrayAdapter<Modelo> adapter;
    ArrayList<Modelo> listaModelos = new ArrayList<>();
    RecyclerView recyclerView;

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
        btnGuardar=(Button)findViewById(R.id.btnGuardarTareas);
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



        btnAudio = findViewById(R.id.btnAudio);
        btnTomar = findViewById(R.id.btnTomar);
        btnAdjuntar = findViewById(R.id.btnAdjuntar);
        recyclerView = findViewById(R.id.recycler);

        //Validación de permisos
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(AgregarTareas.this,
                    new String[]{Manifest.permission.CAMERA,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.RECORD_AUDIO}, 0);
        }

        btnGuardar.setOnClickListener(this);
        btnTomar.setOnClickListener(this);
        btnAudio.setOnClickListener(this);
        btnAdjuntar.setOnClickListener(this);

        adapter = new ArrayAdapter<Modelo>(this, android.R.layout.simple_list_item_1, listaModelos);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        recyclerViewAdapter = new RecyclerViewAdapter(this, listaModelos);


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

    public void guardarRecordatorios(int year, int month, int day, int hour, int min, String fecha, String hora){
        RecordatorioAuxiliar r = new RecordatorioAuxiliar(year, month, day, hour, min, fecha, hora);
        noSave.add(r);
    }


    public void lanzarNotificacion(View view,Tarea tarea){
        Intent intent = new Intent(
                "net.ivanvega.audioenandroidcurso.CAPTURARAUDIO"
        );
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);


        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this, CHANNEL_ID)
                        .setSmallIcon(android.R.drawable.ic_notification_overlay)
                        .setContentTitle("Realizar Tarea: "+ tarea.getTitulo())
                        .setContentText("Hacer: "+tarea.getDescripcion())
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                        // Set the intent that will fire when the user taps the notification
                        .setContentIntent(pendingIntent)
                        .setAutoCancel(true);



        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

// notificationId is a unique int for each notification that you must define
        notificationManager.notify(1001, mBuilder.build());

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
                        daoRecordatorios.insert(recordatorio);
                        Log.i("Recordatorios", ""+recordatorio.getId());
            }
        }else{
        }
        finish();
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void crearNotificacion(int year, int month, int day, int hour, int min){
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        Intent notificationIntent = new Intent(this, AlarmReceiver.class);

        notificationIntent.putExtra("tarea", "Realizar la tarea "+tarea.getTitulo());

        PendingIntent broadcast = PendingIntent.getBroadcast(this, 100, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        //Calendar cal = Calendar.getInstance();

        c.set(year,month,day,hour,min,0);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), broadcast);

        //Toast.makeText(this, "Se creo la notificacion ", Toast.LENGTH_SHORT).show();
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

    @Override
    public void onClick(View view) {
        if (view == btnGuardar) {
            insertarTarea(view);
            insertRecordatorios(view);
            crearNotificacion(year,month,day,hour,min);
            lanzarNotificacion(view,tarea);
            btnRecordatorio.setEnabled(true);
        }

        if (view == btnAdjuntar) {
            dialogoAdjuntar();
        }

        if (view == btnTomar) {
            dialogoTomar();
        }

        if (view == btnAudio) {
            grabarAudio(view);
        }

    }

    private void insertarTarea(View view) {
        tarea = new Tarea(0, etTitulo.getText().toString(), etDescripcion.getText().toString(), fecha, hr);
        DaoTareas dao = new DaoTareas(this);

        switch (view.getId()){
            case R.id.btnGuardarTareas:
                dao.insert(tarea);
                Toast.makeText(this,"se agrego la tarea",Toast.LENGTH_SHORT).show();
        }

        //finish();
    }

    private void insertRutas(View view) {
        String[] Tareas1 = {""}; //para que me devuelva todas las tareas y yo tomar la ultima
        DaoTareas daoTareas = new DaoTareas(this);

        ArrayList<Integer> arrayIds = new ArrayList<>();
        arrayIds = daoTareas.buscarUltimoId(Tareas1); //El array que me gusrda todos los ids de las Tareas

        if(listaModelos !=null) {
            for (int i = 0; i < listaModelos.size(); i++) {
                Ruta ruta = new Ruta(0, listaModelos.get(i).data, listaModelos.get(i).type,arrayIds.get(arrayIds.size()-1));
                DaoRutas daoRutas = new DaoRutas(this);
                switch (view.getId()) {
                    case R.id.btnGuardarTareas:
                        daoRutas.insert(ruta);
                }
                Log.i("RUTAS", ""+ruta.getId() +" path= "+ruta.getRuta()+ "idTarea= "+ruta.getIdTarea());
            }
        }
       // finish();
    }
    //Choices
    private void dialogoTomar (){
        final CharSequence[] items = {getString(R.string.imagen), getString(R.string.video)};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.tituloAlert));
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if(item==0){
                    tomarImagen();
                }
                if(item==1){
                    tomarVideo();
                }
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void dialogoAdjuntar (){
        final CharSequence[] items = {getString(R.string.imagen), getString(R.string.video)};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.tituloAlert));
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if(item==0){
                    cargarImagen();
                }
                if(item==1){
                    cargarVideo();
                }
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    //Metodo para cargar Imagenes de la galeria
    private void cargarImagen(){
        Intent intentGaleria = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intentGaleria.setType("image/");
        startActivityForResult(intentGaleria.createChooser(intentGaleria,getString(R.string.actualizar)),SELEC_IMAGEN);
    }

    private void cargarVideo(){
        Intent intentGaleria = new Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
        intentGaleria.setType("video/");
        startActivityForResult(intentGaleria.createChooser(intentGaleria,getString(R.string.actualizar)),SELEC_VIDEO);
    }

    //Metodo para tomar video
    private void tomarVideo() {
        Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        if (takeVideoIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takeVideoIntent, TOMAR_VIDEO);
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    //Metodo para tomar imagenes
    private void tomarImagen() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File

            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        MY_PROVIDER,
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, TOMAR_FOTO);
            }
        }

    }

    //Metodo para grabar audio
    private void grabarAudio(View view) {
        if (grabacion == null){
            archivoSalida = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Grabacion.mp3";
            grabacion = new MediaRecorder();
            grabacion.setAudioSource(MediaRecorder.AudioSource.MIC);
            grabacion.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            grabacion.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
            grabacion.setOutputFile(archivoSalida);

            try{
                grabacion.prepare();
                grabacion.start();
            }catch (IOException e){

            }

            btnAudio.setText("Grabando...");
            Toast.makeText(getApplicationContext(), "Grabando...",Toast.LENGTH_SHORT).show();
        }else if(grabacion!=null){
            grabacion.stop();
            grabacion.release();
            grabacion = null;

            btnAudio.setText("Grabar");

            Modelo model = new Modelo(Modelo.AUDIO_TYPE, Uri.parse(archivoSalida));
            listaModelos.add(model);
            recyclerView.setAdapter(recyclerViewAdapter);

            Toast.makeText(getApplicationContext(), "Grabación finalizada",Toast.LENGTH_SHORT).show();
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        if(resultCode == RESULT_OK && requestCode == SELEC_IMAGEN) {
            Uri path =  data.getData();
            Modelo model = new Modelo(Modelo.IMAGE_TYPE,path);
            listaModelos.add(model);
            recyclerView.setAdapter(recyclerViewAdapter);
        } else if(resultCode == RESULT_OK && requestCode == TOMAR_FOTO) {
            Modelo model = new Modelo(Modelo.IMAGE_TYPE,Uri.parse(currentPhotoPath));
            listaModelos.add(model);
            recyclerView.setAdapter(recyclerViewAdapter);
        }else if (requestCode == TOMAR_VIDEO && resultCode == RESULT_OK) {
            Uri videoUri = data.getData();
            Modelo model = new Modelo(Modelo.VIDEO_TYPE, videoUri);
            listaModelos.add(model);
            recyclerView.setAdapter(recyclerViewAdapter);
        }else if (requestCode == SELEC_VIDEO && requestCode == RESULT_OK){
            Uri videoUri = data.getData();
            Modelo model = new Modelo(Modelo.VIDEO_TYPE, videoUri);
            listaModelos.add(model);
            recyclerView.setAdapter(recyclerViewAdapter);
        }
    }
}
