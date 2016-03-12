package com.lx.android.catalogopeliculas;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

/**
 * Created by Luis on 21/02/2016.
 */
public class AdaptadorPelicula extends BaseAdapter {

    private Context context;


    public AdaptadorPelicula(Context context){
        this.context= context;
    }

    @Override
    public int getCount() {
        return Pelicula.items_peliculas.size();
    }

    @Override
    public Pelicula getItem(int position) {
        return Pelicula.items_peliculas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return Pelicula.items_peliculas.get(position).getId_pelicula();
    }

    public void clear(){
        Pelicula.items_peliculas.clear();
        super.notifyDataSetChanged();
    }

    public void addPelicula(Pelicula pelicula){
        Pelicula.items_peliculas.add(pelicula);
        //arreglar para optimizar recursos
        super.notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Log.d("Elementos: ", Pelicula.items_peliculas.size()+", posicion: "+position);
        if(convertView == null){
            LayoutInflater inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView= inflater.inflate(R.layout.grid_item, parent, false);
        }
        ImageView portada= (ImageView) convertView.findViewById(R.id.portada_pelicula);
        String url= "http://image.tmdb.org/t/p/w185//"+(getItem(position)).getName_portada();
        //portada.setImageResource(Picasso.with().);
        Picasso.with(context).load(url).into(portada);

        return convertView;

    }
}
