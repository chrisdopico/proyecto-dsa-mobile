package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class DetalleSensorActivity extends AppCompatActivity {

    Button buttonInterrumpir, buttonVolver;
    String servidorSeleccionado, estadoServidor, temperaturaServidor;
    TextView tituloServidor, textViewValorEstado, textViewValorTemperatura;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_servidor);


        //Recuperar servidorSeleccionado
        temperaturaServidor = getIntent().getStringExtra("temperaturaServidor");
        servidorSeleccionado = getIntent().getStringExtra("servidorSeleccionado");
        estadoServidor = getIntent().getStringExtra("estadoServidor");


        //Definición variables botones
        buttonInterrumpir = findViewById(R.id.buttonInterrumpir);
        buttonVolver = findViewById(R.id.buttonVolver);

        //EditText
        tituloServidor = findViewById(R.id.tituloServidor);
        textViewValorEstado = findViewById(R.id.textViewValorEstado);
        textViewValorTemperatura = findViewById(R.id.textViewValorTemperatura);

        tituloServidor.setText(servidorSeleccionado);
        textViewValorEstado.setText(estadoServidor);
        textViewValorTemperatura.setText(temperaturaServidor);

        //Método que redirige a listado de servidores al dar click
        buttonVolver.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                DetalleSensorActivity.this.onBackPressed();
            }
        });
    }
}
