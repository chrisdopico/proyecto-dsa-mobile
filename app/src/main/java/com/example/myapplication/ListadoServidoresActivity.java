package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.example.myapplication.requests.DetalleServidor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;



public class ListadoServidoresActivity extends AppCompatActivity implements Serializable {

    private ArrayList<String> nombresServidoresLocales = new ArrayList<>();
    private ArrayList<String> estadoservidoresLocales = new ArrayList<>();
    List<List<String>> servidoresLocales;
    String servidorSeleccionado;
    String estadoServidorSeleccionado;
    String mensajeToken;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listado_servidores);

        servidoresLocales = (List<List<String>>) getIntent().getSerializableExtra("servidoresLocales");
        try {
            mensajeToken = getIntent().getStringExtra("mensajeToken");
        }catch (Exception e){

        }
        ListView listView = (ListView) findViewById(R.id.listview);
        generateListContent();
        listView.setAdapter(new MyListAdapter(this, R.layout.list_item, nombresServidoresLocales));
        //Toast.makeText(getBaseContext(), mensajeToken, Toast.LENGTH_SHORT).show();
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
                    servidorSeleccionado = nombresServidoresLocales.get(position);
                    estadoServidorSeleccionado = estadoservidoresLocales.get(position);
                    //Toast.makeText(getContext(), "Servidor" + servidorSeleccionado +" "+"position: "+position, Toast.LENGTH_SHORT).show();
                    new DetalleServidor(servidorSeleccionado,estadoServidorSeleccionado,
                            ListadoServidoresActivity.this).execute();
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

}