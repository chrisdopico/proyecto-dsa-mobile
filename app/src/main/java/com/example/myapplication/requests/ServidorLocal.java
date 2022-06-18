package com.example.myapplication.requests;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.example.myapplication.IniciarSesionActivity;
import com.example.myapplication.ListadoServidoresActivity;

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

public class  ServidorLocal extends AsyncTask<String, Void, List<List<String>>> implements Serializable {

        Context context;

        public ServidorLocal(IniciarSesionActivity iniciarSesionActivity){
            super();
            this.context = iniciarSesionActivity;
        }


        @Override
        protected List<List<String>> doInBackground(String... params){
            List<List<String>> servidoresLocales = new ArrayList<>();

            String stringSearchHTTP = "https://redsensors-servicio-consulta.eu-gb.cf.appdomain.cloud/servidores-locales";
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
            intent.setClass(context, ListadoServidoresActivity.class);
            intent.putExtra("servidoresLocales",  (Serializable) servidoresLocales);
            context.startActivity(intent);

        }
}


