package com.example.myapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import com.example.myapplication.requests.ServidorLocal;
import java.io.Serializable;

public class IniciarSesionActivity extends AppCompatActivity implements Serializable{

    Button buttonIniciarSesion, buttonInvitado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Con onCreate listo restauramos el theme de la app en lugar del launcher
        setTheme(R.style.Theme_MyApplication);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iniciar_sesion);

        //Definición variables botones
        buttonIniciarSesion = findViewById(R.id.buttonIniciarSesion);
        buttonInvitado = findViewById(R.id.buttonInvitado);


        //Método que redirige a listado de servidores al dar click
        buttonIniciarSesion.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                new ServidorLocal(IniciarSesionActivity.this).execute();
            }
        });

        //Método que redirige a listado de servidores al dar click
        buttonInvitado.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                new ServidorLocal(IniciarSesionActivity.this).execute();
            }
        });


    }
}
