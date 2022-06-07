package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class DetalleSensorActivity extends AppCompatActivity {

    Button buttonInterrumpir, buttonVolver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_servidor);

        //Definición variables botones
        buttonInterrumpir = findViewById(R.id.buttonInterrumpir);
        buttonVolver = findViewById(R.id.buttonVolver);

        //Método que redirige a listado de servidores al dar click
        buttonVolver.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                Intent intent = new Intent();
                intent.setClass(getBaseContext(), ListadoServidoresActivity.class);
                startActivity(intent);
            }
        });
    }
}
