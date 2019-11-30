package com.example.finalproyect.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.finalproyect.Clases.Nota;
import com.example.finalproyect.Daos.DAONotas;
import com.example.finalproyect.R;

import java.io.IOException;

public class AgregarNotas extends AppCompatActivity {

    private Button btnGuardar;
    private EditText etTitulo;
    private EditText etDescripcion;

    private Button btnAudio, btnTomar, btnAdjuntar;

    ImageView imageView;
    VideoView videoView;
    Uri imagenUri;
    int TOMAR_FOTO = 100;
    int TOMAR_VIDEO = 300;
    int SELEC_IMAGEN = 200;

    String CARPETA_RAIZ = "MisFotos/";
    String CARPETA_IMAGENES = "imagenes";
    String RUTA_IMAGEN = CARPETA_RAIZ + CARPETA_IMAGENES;
    String path;

    private MediaRecorder grabacion;
    private String archivoSalida = null;



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

        //Validación de permisos
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getApplicationContext(),Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getApplicationContext(),Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(AgregarNotas.this,
                    new String[]{Manifest.permission.CAMERA,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.RECORD_AUDIO}, 0);
        }

        final DAONotas dao = new DAONotas(this);

        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String titulo = ((EditText) findViewById(R.id.etTitulo)).getText().toString();
                String descripcion = ((EditText) findViewById(R.id.etDescripcion)).getText().toString();

                obj = new Nota(0,titulo,descripcion);
                dao.insert(obj);

                Toast.makeText(getApplicationContext(),"Nota Insertada",Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        btnAudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                grabarAudio();
            }
        });

    }

    private void grabarAudio() {
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
            Toast.makeText(getApplicationContext(), "Grabación finalizada",Toast.LENGTH_SHORT).show();
        }
    }

}
