package com.example.proyecttools;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
//import android.widget.Button;
//import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Cuerpo_Activity extends AppCompatActivity {

    //private TextView tv_greeting;                              //objeto tv de saludo
    private TextView tv_memory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cuerpo);

//        parte donde indicamos que ya aparezca el id_user

        tv_memory = findViewById(R.id.Tv_tool_user);
        String id_user = getIntent().getStringExtra("username");                                 //alojamos nombre usuario gracias a la key
        tv_memory.setText(id_user);                                                                   //indicamos el lugar en dónde ponemos el texto
//Lo de la barra


    }


    //    metodo para el button nivel 1
    public void Quiz_One(View view) {
        Intent levelOne = new Intent(this, CategoryOne_Activity.class);
        levelOne.putExtra("username", tv_memory.getText().toString());
        startActivity(levelOne);
    }

    //    metodo para el button conceptos
    public void Almacenamiento(View view) {
        Intent Almacenamiento = new Intent(this, Almacenamiento_Activity.class);
        Almacenamiento.putExtra("username", tv_memory.getText().toString());
        startActivity(Almacenamiento);
    }
    public void Redes(View view){
        Intent Redes = new Intent(this, Redes_Activity.class);
        Redes.putExtra("username", tv_memory.getText().toString());
        startActivity(Redes);
    }
    public void Procesador(View view){
        Intent Procesador = new Intent(this, Procesador_Activity.class);
        Procesador.putExtra("username", tv_memory.getText().toString());
        startActivity(Procesador);
    }

    //    metodo para el button atrás
    public void Home(View view) {
        Intent atras = new Intent(this, Main_Activity.class);
        startActivity(atras);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
    }

    //    metodo para el button web
    public void Web(View view) {
        Intent web = new Intent(this, Web_Activity.class);
        startActivity(web);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
    }

    public void Personales (View view){
        Toast.makeText(this, "AUN NO HAY PREGUNTAS ALMACENADAS", Toast.LENGTH_SHORT).show();
    }

    public void Modificar (View view){
        Toast.makeText(this, "Aun NO esta lista", Toast.LENGTH_SHORT).show();
    }

//Indicamos que mediante el boton back del telefono tambien aplique la animacion
    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }


}

