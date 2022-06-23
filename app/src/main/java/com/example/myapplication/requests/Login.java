package com.example.myapplication.requests;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.example.myapplication.IniciarSesionActivity;
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

//Clase Asíncrona para inicio de sesión
public class Login extends AsyncTask<String, Void, String> implements Serializable {


    private String userId;
    private String password;
    private Context context;
    private IniciarSesionActivity activity;
    private int statusCode;

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public Login(String userId, String password, IniciarSesionActivity iniciarSesionActivity){
        this.userId = userId;
        this.password = password;
        this.context= iniciarSesionActivity;
        this.activity = iniciarSesionActivity;
    }

    @Override
    protected String doInBackground(String... params) {
        String stringSearchHTTP = "https://redsensors-servicio-consulta.pj87j18q4um.eu-gb.codeengine.appdomain.cloud/token";
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        StringBuilder response = new StringBuilder();
        String token = "";
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
            urlConnection.setUseCaches(false);
            urlConnection.setDoOutput(true);

            //JSON
            jsonObject.put("usuario", userId);
            jsonObject.put("contraseña", password);


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
            token = new JSONObject(String.valueOf(response)).getString("token");
            rd.close();
        }catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
        return token;
    }

    @Override
    protected void onPostExecute(String response) {
        SessionManager.setToken(response);
        SessionManager.setStatusCode(getStatusCode());
        //Si las credenciales son correctas redirige a ListadoServidoresActivity
        if(SessionManager.getStatusCode()==200){
            new ServidorLocal(activity).execute();
        }else{
            Toast.makeText(context, "Credenciales no pertenecen a ningún usuario", Toast.LENGTH_SHORT).show();
        }

    }
}
