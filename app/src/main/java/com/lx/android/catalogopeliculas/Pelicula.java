package com.lx.android.catalogopeliculas;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Luis on 21/02/2016.
 */
public class Pelicula {

    private int id_pelicula;
    private String titulo;
    private String name_portada;
    private String sinopsis;
    private double calificacion;//mas rankeado
    private int popularidad;//mas popular
    private String fecha_lanzamiento;
    //lista de peliculas
    public static List<Pelicula> items_peliculas = new ArrayList<Pelicula>();

    //metodos
    public int getId_pelicula() {
        return id_pelicula;
    }

    public void setId_pelicula(int id_pelicula) {
        this.id_pelicula = id_pelicula;
    }

    public static void setItems_peliculas(List<Pelicula> items_peliculas) {
        Pelicula.items_peliculas = items_peliculas;
    }

    //Constructor para crear un nuevo objeto Pelicula con todos sus atributos
    public Pelicula(int id, String titulo, String name_portada, String sinopsis, int calificacion, int reproducciones, String fecha_lanzamiento){
        this.id_pelicula= id;
        this.titulo= titulo;
        this.name_portada= name_portada;
        this.sinopsis= sinopsis;
        this.calificacion = calificacion;
        this.popularidad = reproducciones;
        this.fecha_lanzamiento= fecha_lanzamiento;
    }

    public Pelicula(JSONObject peliculaJson){
        //
        try {
            this.id_pelicula= peliculaJson.getInt("id");
            this.titulo= peliculaJson.getString("original_title");
            this.name_portada= peliculaJson.getString("poster_path");
            this.sinopsis= peliculaJson.getString("overview");
            this.calificacion = formatearRating(peliculaJson.getDouble("vote_average"));
            Log.d("Pelicula", "Rating obtenido: "+peliculaJson.getDouble("vote_average"));
            Log.d("Pelicula", "Rating formateado: "+calificacion);
            this.popularidad = peliculaJson.getInt("popularity");
            this.fecha_lanzamiento= peliculaJson.getString("release_date");;
        }catch (JSONException  e){
            Log.d("Pelicula", "Error al crear objeto");
        }
    }

    public double formatearRating(double rating){
        //DecimalFormat decimalFormat= new DecimalFormat("#,#");
        //return (Double.parseDouble(decimalFormat.format(rating/2)));
        return Math.rint((rating/2)*10)/10;
    }



    public static void addPelicula(Pelicula peli){
        items_peliculas.add(peli);
    }


    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getName_portada() {
        return name_portada;
    }

    public void setName_portada(String name_portada) {
        this.name_portada = name_portada;
    }

    public static Pelicula[] getItems_peliculas() {
        //return items_peliculas;
        return null;
    }

    public static void setItems_peliculas(Pelicula[] items_peliculas) {
        //Pelicula.items_peliculas = items_peliculas;
    }

    public String getSinopsis() {
        return sinopsis;
    }

    public void setSinopsis(String sinopsis) {
        this.sinopsis = sinopsis;
    }

    public double getCalificacion() {
        return calificacion;
    }

    public void setCalificacion(double calificacion) {
        this.calificacion = calificacion;
    }

    public int getPopularidad() {
        return popularidad;
    }

    public void setPopularidad(int popularidad) {
        this.popularidad = popularidad;
    }

    public String getFecha_lanzamiento() {
        return fecha_lanzamiento;
    }

    public void setFecha_lanzamiento(String fecha_lanzamiento) {
        this.fecha_lanzamiento = fecha_lanzamiento;
    }

}
