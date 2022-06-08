package com.example.myapplication;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;


import androidx.appcompat.app.AppCompatActivity;



import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class IniciarSesionActivity extends AppCompatActivity implements Serializable{

    Button buttonIniciarSesion, buttonInvitado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iniciar_sesion);

        //Definición variables botones
        buttonIniciarSesion = findViewById(R.id.buttonIniciarSesion);
        buttonInvitado = findViewById(R.id.buttonInvitado);


        //Método que redirige a listado de servidores al dar click
        buttonIniciarSesion.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                new ServidorLocal().execute();
            }
        });

        //Método que redirige a listado de servidores al dar click
        buttonInvitado.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                new ServidorLocal().execute();
            }
        });


    }

    //Clase Asíncrona que hace petición a la API de servidores
    public class  ServidorLocal extends AsyncTask<String, Void, List<List<String>>> implements Serializable {

        @Override
        protected List<List<String>> doInBackground(String... params){
            List<List<String>> servidoresLocales = new ArrayList<>();

            String stringSearchHTTP = "http://192.168.100.132:9003/servidores-locales";
            String contentAsString = "";
            HttpURLConnection urlConnection = null;

            try {
                //crear conexión
                URL urlToRequest = new URL(stringSearchHTTP);
                urlConnection = (HttpURLConnection) urlToRequest.openConnection();
                urlConnection.setReadTimeout(20000);
                urlConnection.setConnectTimeout(30000);

                // manejo de errores
                int statusCode = urlConnection.getResponseCode();
                Log.d("MainActivity", "The response is: " + statusCode);

                // create JSON object from content
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                contentAsString=new Scanner(in).useDelimiter("\\A").next();
                System.out.println(contentAsString);
                JSONArray jsonArray =new JSONArray(contentAsString);

                for (int i = 0; i < jsonArray.length(); i++) {
                    List<String> servidorLocal = new ArrayList<>();
                    JSONObject jsonObject =jsonArray.getJSONObject(i);
                    JSONObject estadoJsonObject = jsonObject.getJSONObject("estado");
                    String _id = jsonObject.getString("_id");
                    String estado = estadoJsonObject.getString("estado");
                    servidorLocal.add(_id);
                    servidorLocal.add(estado);
                    servidoresLocales.add(servidorLocal);
                }

            }catch (Exception e) {
                e.printStackTrace();
            }
            finally { if (urlConnection != null) { urlConnection.disconnect(); } }
            return servidoresLocales;
        }

        //Método que se ejecuta una vez la atividad asíncrona ha finalizado
        @Override
        protected void onPostExecute(List<List<String>> servidoresLocales)
        {
            Intent intent = new Intent();
            intent.setClass(getBaseContext(), ListadoServidoresActivity.class);
            intent.putExtra("servidoresLocales",  (Serializable) servidoresLocales);
            startActivity(intent);

        }
    }
}
