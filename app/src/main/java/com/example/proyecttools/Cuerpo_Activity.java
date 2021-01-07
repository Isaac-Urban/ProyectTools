package com.example.proyecttools;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.transition.Fade;
import android.view.View;
//import android.widget.Button;
//import android.widget.EditText;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Cuerpo_Activity extends AppCompatActivity {

    //private TextView tv_greeting;                              //objeto tv de saludo
    private TextView tv_memory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cuerpo);

//        parte donde indicamos que ya aparezca el id_user
        //tv_greeting = findViewById(R.id.tv_barra);
        tv_memory = findViewById(R.id.tv_memory_user);
        String id_user = getIntent().getStringExtra("username");                                 //alojamos nombre usuario gracias a la key
        tv_memory.setText(id_user);                                                                   //indicamos el lugar en dónde ponemos el texto

    }


    //    metodo para el button nivel 1
    public void Quiz_One(View view) {
        Intent levelOne = new Intent(this, CategoryOne_Activity.class);
        levelOne.putExtra("username", tv_memory.getText().toString());
        startActivity(levelOne);
    }

    //    metodo para el button conceptos
    public void Conceptos(View view) {
        Intent conceptos = new Intent(this, Conceptos_Activity.class);
        conceptos.putExtra("username", tv_memory.getText().toString());
        startActivity(conceptos);
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

    public void Overflow(View view) {
        Intent cuerpo = new Intent(this, Cuerpo_Activity.class);
        cuerpo.putExtra("username", tv_memory.getText().toString());                                 // aqui pasamos el id_user a la otra activity
        startActivity(cuerpo);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
    }

//Indicamos que mediante el boton back del telefono tambien aplique la animacion
    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }


}

