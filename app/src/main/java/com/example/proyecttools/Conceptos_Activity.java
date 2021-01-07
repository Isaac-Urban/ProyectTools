package com.example.proyecttools;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class Conceptos_Activity extends AppCompatActivity {

    private TextView concept_user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conceptos);

//nombre de usuario
        concept_user = findViewById(R.id.tv_memory_userII);
        String id_user = getIntent().getStringExtra("username");                      //alojamos nombre usuario gracias a la key
        concept_user.setText(id_user);
    }

    //    metodo para el button atrás
    public void Home (View view){
        Intent atras = new Intent(this, Cuerpo_Activity.class);
        atras.putExtra("username", concept_user.getText().toString());
        startActivity(atras);

    }

//Indicamos que mediante el boton back del telefono tambien aplique la animacion
    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

}