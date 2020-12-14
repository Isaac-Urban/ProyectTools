package com.example.proyecttools;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class ConceptosActivity extends AppCompatActivity {

    private TextView concept_user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conceptos);

//nombre de usuario
        concept_user = findViewById(R.id.tv_memory_userII);
        String id_user = getIntent().getStringExtra("id_user");                      //alojamos nombre usuario gracias a la key
        concept_user.setText(id_user);
    }

    //    metodo para el button atrás
    public void Home (View view){
        Intent atras = new Intent(this, CuerpoActivity.class);
        atras.putExtra("id_user", concept_user.getText().toString());
        startActivity(atras);

    }

}