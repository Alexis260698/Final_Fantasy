package com.example.finalproyect.Activity;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaRecorder;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.finalproyect.BuildConfig;
import com.example.finalproyect.Clases.Modelo;
import com.example.finalproyect.Clases.Nota;
import com.example.finalproyect.Clases.RecyclerViewAdapter;
import com.example.finalproyect.Clases.Ruta;
import com.example.finalproyect.Daos.DAONotas;
import com.example.finalproyect.Daos.DaoRutasNotas;
import com.example.finalproyect.R;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AgregarNotas extends AppCompatActivity implements View.OnClickListener {

    private Button btnGuardar;
    private EditText etTitulo, etDescripcion;

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

    DAONotas dao;
    Nota obj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_notas);

        etTitulo = (EditText) findViewById(R.id.etTitulo);
        etDescripcion = (EditText) findViewById(R.id.etDescripcion);
        btnGuardar = findViewById(R.id.btnGuardar);
        btnAudio = findViewById(R.id.btnAudio);
        btnTomar = findViewById(R.id.btnTomar);
        btnAdjuntar = findViewById(R.id.btnAdjuntar);
        recyclerView = findViewById(R.id.recycler);

        //Validación de permisos
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(AgregarNotas.this,
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

    public void onClick(View view) {
        if (view == btnGuardar) {
            insertarNota(view);
            insertRutas(view);
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

    //Inserta Nota en la BD
    private void insertarNota(View view) {
        dao = new DAONotas(this);

        String titulo = ((EditText) findViewById(R.id.etTitulo)).getText().toString();
        String descripcion = ((EditText) findViewById(R.id.etDescripcion)).getText().toString();

        obj = new Nota(0, titulo, descripcion);

        switch (view.getId()) {
            case R.id.btnGuardar:
                dao.insert(obj);
        }


        finish();
    }

    private void insertRutas(View view) {
        String[] Notas1 = {""};
        DAONotas daoNotas = new DAONotas(this);

        ArrayList<Integer> arrayIds = new ArrayList<>();

        arrayIds = daoNotas.buscarUltimoId(Notas1);

        if (listaModelos != null) {
            for (int i = 0; i < listaModelos.size(); i++) {

                Ruta ruta = new Ruta(0, listaModelos.get(i).data, listaModelos.get(i).type, arrayIds.get(arrayIds.size() - 1));
                DaoRutasNotas daoRutasNotas = new DaoRutasNotas(this);

                switch (view.getId()) {
                    case R.id.btnGuardar:
                        daoRutasNotas.insert(ruta);
                        Log.i("RUTAS", "" + ruta.getId() + " path= " + ruta.getRuta() + "idNota= " + ruta.getIdTarea());
                        //finish();
                }
                Log.i("RUTAS", "" + ruta.getId() + " path= " + ruta.getRuta() + "idNota= " + ruta.getIdTarea());
            }
        }
        finish();
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
