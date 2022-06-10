package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class ListadoServidoresActivity extends AppCompatActivity implements Serializable {

    private static final DecimalFormat df = new DecimalFormat("00.00");
    private ArrayList<String> nombresServidoresLocales = new ArrayList<>();
    private ArrayList<String> estadoservidoresLocales = new ArrayList<>();
    List<List<String>> servidoresLocales;
    String servidorSeleccionado;
    String estadoServidorSeleccionado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listado_servidores);

        servidoresLocales = (List<List<String>>) getIntent().getSerializableExtra("servidoresLocales");
        ListView listView = (ListView) findViewById(R.id.listview);
        generateListContent();
        listView.setAdapter(new MyListAdapter(this, R.layout.list_item, nombresServidoresLocales));
    }

    private void generateListContent() {
        for (int i = 0; i < servidoresLocales.size(); i++) {
            nombresServidoresLocales.add(servidoresLocales.get(i).get(0));
            estadoservidoresLocales.add(servidoresLocales.get(i).get(1));
        }
    }

    private class MyListAdapter extends ArrayAdapter<String> {
        private int layout;
        private List<String> mObjects;

        private MyListAdapter(Context context, int resource, List<String> objects) {
            super(context, resource, objects);
            mObjects = objects;
            layout = resource;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            servidorSeleccionado = nombresServidoresLocales.get(position);
            estadoServidorSeleccionado = estadoservidoresLocales.get(position);

            ViewHolder mainViewholder = null;
            if (convertView == null) {
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(layout, parent, false);
                ViewHolder viewHolder = new ViewHolder();
                viewHolder.title = (TextView) convertView.findViewById(R.id.list_item_text);
                viewHolder.button = (Button) convertView.findViewById(R.id.list_item_btn);
                convertView.setTag(viewHolder);
            }
            mainViewholder = (ViewHolder) convertView.getTag();

            mainViewholder.button.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    new DetalleServidor().execute();

                }
            });

            mainViewholder.title.setText(getItem(position));

            return convertView;
        }
    }

    public class ViewHolder {
        TextView title;
        Button button;
    }

    //Clase Asíncrona que hace petición a la API de detalle servidores
    public class DetalleServidor extends AsyncTask<String, Void, String> implements Serializable {

        @Override
        protected String doInBackground(String... params) {
            String temperaturaSensor="";
            String stringSearchHTTP = "http://192.168.100.132:9003/servidores-locales/" + servidorSeleccionado + "/sensores";
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
            intent.setClass(getBaseContext(), DetalleSensorActivity.class);
            intent.putExtra("temperaturaServidor", temperaturaSensor);
            intent.putExtra("servidorSeleccionado", servidorSeleccionado);
            intent.putExtra("estadoServidor",estadoServidorSeleccionado);

            startActivity(intent);
        }
    }
}