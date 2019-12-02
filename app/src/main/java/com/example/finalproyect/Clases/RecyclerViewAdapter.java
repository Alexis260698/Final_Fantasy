package com.example.finalproyect.Clases;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalproyect.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter {
    ArrayList<Modelo> listaRutas;
    Context context;
    int total_types;
    MediaPlayer mPlayer;
    private boolean fabStateVolume = false;

    public RecyclerViewAdapter(Context context, ArrayList<Modelo> data) {
        //this.mInflater = LayoutInflater.from(context);
        this.listaRutas = data;
        this.context = context;
        total_types = data.size();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {

            case Modelo.IMAGE_TYPE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_img, parent, false);
                return new ViewHolderImagenes(view);
            case Modelo.AUDIO_TYPE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_audio, parent, false);
                return new ViewHolderAudio(view);
            case Modelo.VIDEO_TYPE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_video, parent, false);
                return new ViewHolderVideo(view);
        }
        return null;
    }

    @Override
    public int getItemViewType(int position) {

        switch (listaRutas.get(position).type) {
            case 0:
                return Modelo.IMAGE_TYPE;
            case 1:
                return Modelo.AUDIO_TYPE;
            case 2:
                return Modelo.VIDEO_TYPE;
            default:
                return -1;
        }

    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {
        final Modelo object = listaRutas.get(position);

        if (object != null) {

            switch (object.type) {

                case Modelo.IMAGE_TYPE:
                    ((ViewHolderImagenes) holder).myImageView.setImageURI(object.data);
                    break;
                case Modelo.AUDIO_TYPE:
                    ((ViewHolderAudio) holder).audio.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View view) {

                            if (fabStateVolume) {
                                if(mPlayer.isPlaying()) {
                                    mPlayer.stop();
                                }
                            } else {
                                mPlayer = new MediaPlayer();
                                mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                                try {
                                    mPlayer.setDataSource(context, object.data);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                try {
                                    mPlayer.prepare();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                mPlayer.start();
                                fabStateVolume = true;
                            }
                        }

                    });
                    break;
                case Modelo.VIDEO_TYPE:
                    ((ViewHolderVideo) holder).videoView.setVideoURI(object.data);
                    MediaController mediaController = new MediaController(context);
                    mediaController.setAnchorView(((ViewHolderVideo) holder).videoView);
                    ((ViewHolderVideo) holder).videoView.setMediaController(mediaController);
                    ((ViewHolderVideo) holder).videoView.seekTo(10);
                    break;
            }
        }
    }

    @Override
    public int getItemCount() {
        return listaRutas.size();
    }

    public static class ViewHolderImagenes extends RecyclerView.ViewHolder {
        ImageView myImageView;
        public ViewHolderImagenes(@NonNull View itemView) {
            super(itemView);
            myImageView = itemView.findViewById(R.id.imgAdaptador);
        }
    }

    public static class ViewHolderAudio extends RecyclerView.ViewHolder {
        FloatingActionButton audio;
        public ViewHolderAudio(@NonNull View itemView) {
            super(itemView);
            audio = itemView.findViewById(R.id.fabReproducir);
        }
    }

    public static class ViewHolderVideo extends RecyclerView.ViewHolder {
        VideoView videoView;
        public ViewHolderVideo(@NonNull View itemView) {
            super(itemView);
            videoView = itemView.findViewById(R.id.videoViewVista);
        }
    }
}
