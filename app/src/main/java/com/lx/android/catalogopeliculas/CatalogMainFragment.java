package com.lx.android.catalogopeliculas;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * A placeholder fragment containing a simple view.
 */
public class CatalogMainFragment extends Fragment {

    private GridView grid_pelis;
    private AdaptadorPelicula catalogoAdapter;

    public CatalogMainFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootview= inflater.inflate(R.layout.fragment_catalog_main, container, false);
        catalogoAdapter= new AdaptadorPelicula(getContext());
        grid_pelis= (GridView)rootview.findViewById(R.id.grid_peliculas);
        grid_pelis.setAdapter(catalogoAdapter);
        grid_pelis.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Pelicula peli= (Pelicula) parent.getItemAtPosition(position);

                //Crear un intent para abrir el detalle de la pelicula enviandole su id
                Intent intent = new Intent(getActivity(), DetallePelicula.class);
                intent.putExtra(Intent.EXTRA_TEXT, Integer.toString(position));
                startActivity(intent);
            }
        });

        return rootview;

    }

    public void actualizarCatalogo(){
        CatalogoAsync task= new CatalogoAsync();
        //SharedPreferences prefs= PreferenceManager.getDefaultSharedPreferences(getActivity());
        //String location= prefs.getString(getString(R.string.pref_location_key), getString(R.string.pref_location_default));
        task.execute();
    }

    @Override
    public void onStart() {
        super.onStart();
        actualizarCatalogo();
    }

    public class CatalogoAsync extends AsyncTask<Void, Void, JSONArray> {
        @Override
        protected JSONArray doInBackground(Void... params) {
            // These two need to be declared outside the try/catch
            // so that they can be closed in the finally block.
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String catalogoJson = null;
            //http://api.themoviedb.org/3/discover/movie?sort_by=popularity.desc&api_key=fa97ccb35dd2204df5f2c5a4259c8694
            //https://api.themoviedb.org/3/movie/550?api_key=fa97ccb35dd2204df5f2c5a4259c8694
            SharedPreferences prefs= PreferenceManager.getDefaultSharedPreferences(getActivity());

            String orden= prefs.getString(getString(R.string.pref_orden_key), getString(R.string.pref_orden_default));;
            String apiKey= "fa97ccb35dd2204df5f2c5a4259c8694";

            try {
                final String catalogo_base_url= "http://api.themoviedb.org/3/discover/movie?";
                final String ORDEN_PARAM= "sort_by";
                final String APIKEY_PARAM= "api_key";

                Uri biuldUri= Uri.parse(catalogo_base_url).buildUpon()
                        .appendQueryParameter(ORDEN_PARAM, orden)
                        .appendQueryParameter(APIKEY_PARAM, apiKey)
                        .build();

                URL url = new URL(biuldUri.toString());
                Log.v("Peticion internet", biuldUri.toString());

                // Create the request to OpenWeatherMap, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                catalogoJson= buffer.toString();
                Log.v("Respuesta internet", catalogoJson);
            }catch (Exception e){
                Log.e("CatalogoFragment", "Error ", e);
                // If the code didn't successfully get the weather data, there's no point in attemping
                // to parse it.
                return null;
            }finally{
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e("CatalogoFragment", "Error closing stream", e);
                    }
                }
            }

            try{
                //devolver una array de todas las peliculas por separado

                return obtenerPeliculasJson(catalogoJson);
            }catch (Exception e){
                Log.e("PronosticoFragment", e.getMessage(), e);
                e.printStackTrace();

            }

            return null;
        }

        @Override
        protected void onPostExecute(JSONArray pelisArray) {
            try {
                catalogoAdapter.clear();
                if(pelisArray==null){
                    return;
                }
                for(int i = 0; i < pelisArray.length(); i++) {
                    catalogoAdapter.addPelicula(new Pelicula(pelisArray.getJSONObject(i)));
                }

            }catch (JSONException e){
                Log.d("Error: ", e.toString());
            }
        }
    }

    private JSONArray obtenerPeliculasJson(String catalogoJson){
        // These are the names of the JSON objects that need to be extracted.
        final String OWM_RESULTS = "results";

        try {

            JSONObject forecastJson = new JSONObject(catalogoJson);
            JSONArray pelisArray = forecastJson.getJSONArray(OWM_RESULTS);
            Log.d("Lista peliculas:", pelisArray.toString());
            return pelisArray;

        }catch (JSONException e){

        }

        return null;
    }
}
