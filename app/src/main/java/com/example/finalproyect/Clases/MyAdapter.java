package com.example.finalproyect.Clases;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.example.finalproyect.R;

import java.util.List;

public class MyAdapter extends BaseAdapter {

    private Context context;
    private int layout;
    List<Modelo> rutas;

    public MyAdapter(Context context, int layout, List<Modelo> rutas){
        this.context = context;
        this.layout = layout;
        this.rutas = rutas;
    }

    @Override
    public int getCount() {
        return this.rutas.size();
    }

    @Override
    public Object getItem(int position) {
        return this.rutas.get(position);
    }

    @Override
    public long getItemId(int id) {
        return id;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        LayoutInflater layoutInflater = LayoutInflater.from(this.context);
        v = layoutInflater.inflate(R.layout.list_item_img,null);



        ImageView imageView = (ImageView) v.findViewById(R.id.imgAdaptador);
        //imageView.setImageBitmap();

        return null;
    }
}
