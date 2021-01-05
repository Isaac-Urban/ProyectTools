package com.example.proyecttools;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

public class QuizOneActivity extends AppCompatActivity {

    public static final String CORRECT_ANSWER = "correct_answer";
    public static final String CURRENT_QUESTION = "current_question";
    public static final String RESPUESTA_CORRECTA = "RespuestaCorrecta";
    public static final String ANSWER = "answer";

    private int Tabla_resp[] = {R.id.rb_one, R.id.rb_two, R.id.rb_three, };             //Array de opciones de respuesta
    private TextView Tv_nombre;                                                   //objeto que guarda el nombre de usuario
    private TextView Tv_pregunta;
    private RadioGroup Rg_resp;
    private Button bt_next, bt_prev;
    private String[] all_questions;

// Variables que debe guardar la app
    private boolean[] Array_resp_correcta;
    private int Pregunta_actual;
    private int[] answer;
    private int Int_resp_correcta;

//TODO: Cuando la actividad lo necesite guardará los datos de estas variables para no afectar a la app
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(CORRECT_ANSWER, Int_resp_correcta);
        outState.putInt(CURRENT_QUESTION, Pregunta_actual);
        outState.putBooleanArray(RESPUESTA_CORRECTA, Array_resp_correcta);
        outState.putIntArray(ANSWER, answer);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_one);

//nombre de usuario
        Tv_nombre = findViewById(R.id.tv_user);
        String id_user = getIntent().getStringExtra("id_user");                               //alojamos nombre usuario gracias a la key
        Tv_nombre.setText(id_user);                                                                 //indicamos el lugar en dónde ponemos el texto

// referencias a campos en pantalla
        Tv_pregunta = findViewById(R.id.tv_quiz_question_one);                                       //lugar donde se muestra la pregunta
        Rg_resp = findViewById(R.id.rg_answers);                                                     //grupo de opciones_r
        bt_next = findViewById(R.id.bt_next);                                                        //boton de avance
        bt_prev = findViewById(R.id.bt_prev);                                                        //boton de regreso
        all_questions = getResources().getStringArray(R.array.questions);                            //obtenemos array de strings de las preguntas

//este if valida si la apiicacion se crea desde 0 o si tiene algo guardado que debe mostrar
        if (savedInstanceState == null){
            StartOver();
        }else {
            Bundle state = savedInstanceState;
            Int_resp_correcta = state.getInt(CORRECT_ANSWER);
            Pregunta_actual = state.getInt(CURRENT_QUESTION);
            Array_resp_correcta = state.getBooleanArray(RESPUESTA_CORRECTA);
            answer = state.getIntArray(ANSWER);
            ShowQuestion();
        }



//TODO: condicionales de los botones
        bt_next.setOnClickListener(v -> {
            int id = Rg_resp.getCheckedRadioButtonId();
            int ans_next = -1;
            for (int i = 0; i < Tabla_resp.length; i++) {
                if (Tabla_resp[i] == id) {
                    ans_next = i;  //desmarca botones hijos
                }
            }
            Array_resp_correcta[Pregunta_actual] = (ans_next == Int_resp_correcta);                    //comparacion de booleano que nos ayuda a guardar que la pregunta sea correcta
            if(Pregunta_actual < all_questions.length - 1){                                          //condicional para incrementar la pregunta actual, asi sabemos cuando debe cambiar de pregunta y cuando no
                Pregunta_actual++;
                ShowQuestion();
            }else{
                CheckResults();                                                                      //metodo que inicia cuando se esta en la ultima pregunta para checar resultados
            }
        });

        bt_prev.setOnClickListener(v -> {
        CheckAnswer();
        if (Pregunta_actual > 0){                                                                    //
            Pregunta_actual--;
            ShowQuestion();
        }
        });

    }


 //TODO: Método para mostrar las preguntas
    private void ShowQuestion() {

        String pregunta = all_questions[Pregunta_actual];                                             //usamos la primer pregunta sacandola del arreglo
        String[] Partes_Array = pregunta.split(";");                                           //partimos el string de las preguntas por cada punto y coma que especificamos en el string
        Rg_resp.clearCheck();                                                                         //Limpia la seleccion de la respuesta anterior
        Tv_pregunta.setText(Partes_Array[0]);                                                         // de donde sacamos las preguntas, refencia donde se mnmuestran las preguntas

        for (int i = 0; i < Tabla_resp.length; i++) {
            RadioButton rb = findViewById(Tabla_resp[i]);
            String ans = Partes_Array[i+1];                                                          //
            //CharAr(posición), caracter en la posicion
            if (ans.charAt(0) == '*'){                                                               //truco de la opción correcta
                Int_resp_correcta = i;
                ans = ans.substring(1);                                                              //pedimos el string a partir del caracter uno para no mostrar el *
            }
            rb.setText(ans);                                                                         //
            if (answer[Pregunta_actual] == i){                                                       //condicional para que cada vez que el usuario regrese a la pregunta anterior este marcada su selección
                rb.setChecked(true);
            }
        }
        if(Pregunta_actual == 0){                                                                   //condiconal con la que ocultamos el boton prev en la primera pregunta
            bt_prev.setVisibility(View.GONE);
            }else{
            bt_prev.setText(R.string.prev);
            bt_prev.setVisibility(View.VISIBLE);
        }

        if(Pregunta_actual == all_questions.length - 1){                                            //condicional que cambia el texto del boton cuando se muestra la ultima pregunta
            bt_next.setText(R.string.Finish);
        }else{
            bt_next.setText(R.string.Next);
        }
    }

//TODO: Método de regreso a pregunta anterior
    private void CheckAnswer() {
        int id = Rg_resp.getCheckedRadioButtonId();
        int ans = -1;
        for (int i = 0; i < Tabla_resp.length; i++){
            if (Tabla_resp[i] == id){
                ans = i;
            }
        }
        Array_resp_correcta[Pregunta_actual] = (ans == Int_resp_correcta);
        answer[Pregunta_actual] = ans;
    }

//TODO: Método de checar resultados
    private void CheckResults() {
        int Correctas = 0, Incorrectas= 0;                                                             //condicional que almacena los resultados
        for (int i=0; i < all_questions.length; i++){                                                 //el for pasa por todos los elementos y me los trae
            if (Array_resp_correcta[i]) Correctas++;
            else if (answer[i] == -1){
                Incorrectas++;
            }
        }
        //nombre de usuario


        AlertDialog.Builder Builder = new AlertDialog.Builder(this);                         //Creamos el objeto builder para crear un cuadro de dialogo
        String id_user = getIntent().getStringExtra("id_user");
        Builder.setTitle("id_user" + id_user + "obtuviste:");
        String Message = String.format("Correctas = %d\nIncorrectas = %d\n\n    ¿Intentar de nuevo?   ",Correctas, Incorrectas);
        Builder.setMessage(Message);
        Builder.setCancelable(false);
        Builder.setPositiveButton(R.string.Finish, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        Builder.setNegativeButton(R.string.Start_Again, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                StartOver();                                                    //borrar respuestas y regresar a la pregunta 1
            }
        });
        Builder.create().show();
    }


//TODO: Método para empezar de nuevo
    private void StartOver() {
        Array_resp_correcta = new boolean[all_questions.length];
        answer = new int[all_questions.length];
        for (int i=0; i < answer.length; i++){
            answer[i] = -1;
        }
        Pregunta_actual = 0;                                                                          //asignamos empezar en la pregunta 0
        ShowQuestion();                                                                                  //llamada a metodo show
    }



}

