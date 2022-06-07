package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ListadoServidoresActivity extends AppCompatActivity implements Serializable {

    //Lista
    private ArrayList<String> data = new ArrayList<String>();
    List<List<String>> servidoresLocales;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listado_servidores);

        servidoresLocales = (List<List<String>>) getIntent().getSerializableExtra("servidoresLocales");
        Toast.makeText(getBaseContext(), servidoresLocales.get(0).get(0).toString(), Toast.LENGTH_SHORT).show();
        ListView listView = (ListView) findViewById(R.id.listview);
        generateListContent();
        listView.setAdapter(new MyListAdapter(this, R.layout.list_item, data));

    }

    private void generateListContent() {
        for(int i = 1; i < 11; i++) {
            data.add("Servidor " + i);
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
            if(convertView == null) {
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
                    Intent intent = new Intent();
                    intent.setClass(getBaseContext(), DetalleSensorActivity.class);
                    startActivity(intent);
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