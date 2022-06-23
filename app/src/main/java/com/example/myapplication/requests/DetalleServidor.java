package com.example.myapplication.requests;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.example.myapplication.DetalleSensorActivity;
import com.example.myapplication.ListadoServidoresActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.Scanner;

//Clase Asíncrona que hace petición a la API de detalle servidores
public class DetalleServidor extends AsyncTask<String, Void, String> implements Serializable {

    String servidorSeleccionado;
    String estadoServidorSeleccionado;
    Context context;
    private static final DecimalFormat df = new DecimalFormat("00.00");
    public DetalleServidor(String servidorSeleccionado, ListadoServidoresActivity listadoServidoresActivity){
        super();
        this.servidorSeleccionado = servidorSeleccionado;
        this.context = listadoServidoresActivity;
    }
    @Override
    protected String doInBackground(String... params) {
        String temperaturaSensor="";
        String stringSearchHTTP = "https://redsensors-servicio-consulta.pj87j18q4um.eu-gb.codeengine.appdomain.cloud/servidores-locales/" + servidorSeleccionado + "/sensores";
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
            contentAsString = new Scanner(in).useDelimiter("\\A").next();
            System.out.println(contentAsString);
            JSONArray jsonArray = new JSONObject(contentAsString).getJSONArray("sensores");
            estadoServidorSeleccionado = new JSONObject(contentAsString).getJSONObject("estado").getString("estado");

            for (int i = 0; i < jsonArray.length(); i++) {
                String tipoSensor = jsonArray.getJSONObject(i).getString("type");
                Double valorSensor = jsonArray.getJSONObject(i).getJSONObject("lectura").getDouble("valor");
                if (tipoSensor.equals("temperatura")) {
                    temperaturaSensor = df.format(valorSensor) + "ºC";
                    break;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }

        return temperaturaSensor;
    }

    //Método que se ejecuta una vez la atividad asíncrona ha finalizado
    @Override
    protected void onPostExecute(String temperaturaSensor) {
        Intent intent = new Intent();
        intent.setClass(context, DetalleSensorActivity.class);
        intent.putExtra("temperaturaServidor", temperaturaSensor);
        intent.putExtra("servidorSeleccionado", servidorSeleccionado);
        intent.putExtra("estadoServidor",estadoServidorSeleccionado);

        context.startActivity(intent);
    }
}
