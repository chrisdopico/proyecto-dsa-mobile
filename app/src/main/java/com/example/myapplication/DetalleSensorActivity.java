package com.example.myapplication;


import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.requests.InterrumpirServidor;
import com.example.myapplication.session.SessionManager;

public class DetalleSensorActivity extends AppCompatActivity {

    public static Button buttonInterrumpir;
    Button buttonVolver;
    String servidorSeleccionado, estadoServidor, temperaturaServidor;
    TextView tituloServidor, textViewValorEstado, textViewValorTemperatura;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_servidor);

        //Recuperar servidorSeleccionado
        temperaturaServidor = getIntent().getStringExtra("temperaturaServidor");
        servidorSeleccionado = getIntent().getStringExtra("servidorSeleccionado");
        Toast.makeText(getBaseContext(), servidorSeleccionado, Toast.LENGTH_SHORT).show();
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

        //Ocultar botón interrumpir si se entra como invitado
        if(IniciarSesionActivity.mostrarBotonInterrumpir==false){
            buttonInterrumpir.setVisibility(View.GONE);
        }


        //Método que redirige a listado de servidores al dar click
        buttonVolver.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                DetalleSensorActivity.this.onBackPressed();
            }
        });


        //Método que interrumpe o activa el servidor
        buttonInterrumpir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new InterrumpirServidor(estadoServidor, servidorSeleccionado, DetalleSensorActivity.this).execute();
            }
        });
    }
}
