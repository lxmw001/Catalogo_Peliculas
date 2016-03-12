package com.lx.android.catalogopeliculas;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetallePeliculaFragment extends Fragment {

    private Pelicula pelicula;
    private View rootView;

    public DetallePeliculaFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView= inflater.inflate(R.layout.fragment_detalle_pelicula, container, false);
        Intent intent= getActivity().getIntent();
        if(intent!=null && intent.hasExtra(Intent.EXTRA_TEXT)){
            pelicula= Pelicula.items_peliculas.get(Integer.parseInt(intent.getStringExtra(Intent.EXTRA_TEXT)));
            llenarDetalles();
        }
        return rootView;
    }

    public void llenarDetalles(){
        TextView titulo= (TextView) rootView.findViewById(R.id.txtTitulo);
        TextView sinopsis= (TextView) rootView.findViewById(R.id.txtSinopsis);
        TextView lanzamiento= (TextView) rootView.findViewById(R.id.txtLanzamiento);
        //rating
        RatingBar rating= (RatingBar) rootView.findViewById(R.id.ratingPelicula);
        ImageView portada= (ImageView) rootView.findViewById(R.id.imgPoster);

        String url= "http://image.tmdb.org/t/p/w185//"+(pelicula.getName_portada());


        titulo.setText(pelicula.getTitulo());
        sinopsis.setText(pelicula.getSinopsis());
        lanzamiento.setText(pelicula.getFecha_lanzamiento());
        Log.d("Detalle Pelicula", "Rating value: " + rating.getRating());
        rating.setRating((float) pelicula.getCalificacion());
        Log.d("Detalle Pelicula", "Rating pelicula: " + rating.getRating());
        Picasso.with(getContext()).load(url).into(portada);
    }
}
