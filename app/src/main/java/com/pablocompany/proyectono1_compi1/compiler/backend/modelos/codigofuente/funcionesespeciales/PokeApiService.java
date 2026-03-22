package com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.funcionesespeciales;

import com.pablocompany.proyectono1_compi1.compiler.models.errores.ErrorAnalisis;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/*Clase delegada para poder implementar el request a la poke API para poder obtener los pokemon en un intervalo*/
public class PokeApiService {

    //URL de la API
    private static final String BASE_URL = "https://pokeapi.co/api/v2/";

    //Constructor para instanciar el service
    public PokeApiService(){
    }

    /*Metodo que permite retornar los nombres de los pokemon en un intervalo*/
    public List<String> getPokemones(int offset, int limit, List<ErrorAnalisis> listaErrores, int linea, int columna, String ambito){

        List<String> nombresPokemon = new ArrayList<>();

        OkHttpClient client = new OkHttpClient();
        String url = BASE_URL + "pokemon?offset=" + offset + "&limit=" + limit;

        System.out.println("\nurl: " + url);
        System.out.println("params recibidos offset: " + offset);
        System.out.println("params recibidos limit: " + limit);


        Request request = new Request.Builder()
                .url(url)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Error inesperado: " + response);


            String jsonData = response.body().string();
            JSONObject jsonObject = new JSONObject(jsonData);
            JSONArray results = jsonObject.getJSONArray("results");

            for (int i = 0; i < results.length(); i++) {
                JSONObject pokemon = results.getJSONObject(i);
                nombresPokemon.add(pokemon.getString("name"));
            }

        } catch (Exception e) {
            listaErrores.add(new ErrorAnalisis((ambito != null)? ambito:("who_is_that_pokemon()"), "Semantico",
                    "Error al conectar con PokeAPI: " + e.getMessage(), linea, columna));
            return null;
        }

        return nombresPokemon;
    }

}
