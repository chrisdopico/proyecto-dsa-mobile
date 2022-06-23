package com.example.myapplication.requests;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.example.myapplication.DetalleSensorActivity;
import com.example.myapplication.session.SessionManager;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;

public class InterrumpirServidor extends AsyncTask<String, Void, String> implements Serializable {

    String servidorSeleccionado;
    private int statusCode;
    String token = SessionManager.getToken();
    String estadoServidor;
    String mensaje;
    Context context;

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public InterrumpirServidor(String estadoServidor, String servidorSeleccionado, DetalleSensorActivity detalleSensorActivity){
        this.estadoServidor=estadoServidor;
        this.servidorSeleccionado = servidorSeleccionado;
        this.context = detalleSensorActivity;
    }

    @Override
    protected String doInBackground(String... params) {
        String stringSearchHTTP = "https://redsensors-servicio-consulta.pj87j18q4um.eu-gb.codeengine.appdomain.cloud/servidores-locales/"+servidorSeleccionado+"/interrumpir";
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        StringBuilder response = new StringBuilder();
        JSONObject jsonObject = new JSONObject();
        try {
            //crear conexión
            URL urlToRequest = new URL(stringSearchHTTP);
            urlConnection = (HttpURLConnection) urlToRequest.openConnection();
            urlConnection.setReadTimeout(20000);
            urlConnection.setConnectTimeout(30000);
            urlConnection.setRequestMethod("POST");

            //headers
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setRequestProperty("Accept", "application/json");
            urlConnection.setRequestProperty("Authorization", "Bearer "+token);

            urlConnection.setUseCaches(false);
            urlConnection.setDoOutput(true);

            //JSON

            jsonObject.put("interrumpir", getEstadoInterrumpirServidor(estadoServidor));

            //Enviar request
            OutputStream outputStream = urlConnection.getOutputStream();
            byte[] input = jsonObject.toString().getBytes("utf-8");
            outputStream.write(input,0,input.length);

            try {
                inputStream = urlConnection.getInputStream();
                HttpURLConnection httpConn = (HttpURLConnection) urlConnection;
                int statusCode = httpConn.getResponseCode();
                setStatusCode(statusCode);
            } catch (IOException ioe) {
                if (urlConnection instanceof HttpURLConnection) {
                    HttpURLConnection httpConn = (HttpURLConnection) urlConnection;
                    int statusCode = httpConn.getResponseCode();
                    setStatusCode(statusCode);
                    if (statusCode != 200) {
                        inputStream = httpConn.getErrorStream();
                    }
                }
            }

            //Obtener respuesta
            BufferedReader rd = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = rd.readLine()) != null) {
                response.append(line);
                response.append('\r');
            }
            mensaje = new JSONObject(String.valueOf(response)).getString("mensaje");
            rd.close();
        }catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
        return mensaje;
    }
    @Override
    protected void onPostExecute(String mensaje) {
        if (getStatusCode()==200){
            Toast.makeText(context.getApplicationContext(), mensaje, Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(context.getApplicationContext(), "Token no válido", Toast.LENGTH_SHORT).show();
        }


    }

    public boolean getEstadoInterrumpirServidor(String estadoServidor){
        boolean estadoInterrumpirServidor;
        if(estadoServidor.equals("interrumpido")){
            estadoInterrumpirServidor = false;
        }else{
            estadoInterrumpirServidor = true;
        }

        return estadoInterrumpirServidor;
    }

}
