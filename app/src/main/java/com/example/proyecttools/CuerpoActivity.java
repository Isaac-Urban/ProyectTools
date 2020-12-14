package com.example.proyecttools;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.transition.Explode;
import android.transition.Fade;
import android.transition.Visibility;
import android.view.View;
//import android.widget.Button;
//import android.widget.EditText;
import android.view.animation.DecelerateInterpolator;
import android.widget.TextView;
import android.widget.Toolbar;

import com.example.proyecttools.R;

import static android.transition.Visibility.MODE_IN;

public class CuerpoActivity extends AppCompatActivity {

    private TextView tv_greeting;                              //objeto tv de saludo
    private TextView tv_memory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cuerpo);

        //transicion de entrada
        Fade transition = new Fade(Fade.IN);
        transition.setDuration(MainActivity.duration_transition);
        transition.setInterpolator(new DecelerateInterpolator());
        getWindow().setEnterTransition(transition);
        getWindow().setAllowEnterTransitionOverlap(false);



//        parte donde indicamos que ya aparezca el id_user
        tv_greeting = findViewById(R.id.tv_barra);
        tv_memory = findViewById(R.id.tv_memory_user);
        String id_user = getIntent().getStringExtra("id_user");                      //alojamos nombre usuario gracias a la key
        tv_greeting.setText("¡Bienvenido " + id_user + "!");                               //indicamos el lugar en dónde ponemos el texto
        tv_memory.setText(id_user);                               //indicamos el lugar en dónde ponemos el texto

    }

    //    metodo para el button nivel 1
    public void Level_One(View view) {
        Intent levelOne = new Intent(this, QuizOneActivity.class);
        levelOne.putExtra("id_user", tv_memory.getText().toString());
        startActivity(levelOne);
    }

    //    metodo para el button atrás
    public void Home(View view) {
        Intent atras = new Intent(this, MainActivity.class);
        finishAfterTransition();
        startActivity(atras);
    }

    //    metodo para el button web
    public void Web(View view) {
        Intent web = new Intent(this, ActivityWeb.class);
        startActivity(web);
    }

    //    metodo para el button conceptos
    public void Conceptos(View view) {
        Intent conceptos = new Intent(this, ConceptosActivity.class);
        conceptos.putExtra("id_user", tv_memory.getText().toString());
        startActivity(conceptos);
    }



}
