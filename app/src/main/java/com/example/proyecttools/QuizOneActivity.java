package com.example.proyecttools;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class QuizOneActivity extends AppCompatActivity {

    private int ids_answers[] = {
            R.id.rb_one, R.id.rb_two, R.id.rb_three,
    };
    private TextView tv_user_memory;
    private int correct_answer;
    private int current_question;
    private String[] all_questions;
    private boolean[] correct_is_answer;
    private TextView text_question;
    private RadioGroup group;
    private Button bt_next;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_one);
//nombre de usuario
        tv_user_memory = findViewById(R.id.tv_user);
        String id_user = getIntent().getStringExtra("id_user");                      //alojamos nombre usuario gracias a la key
        tv_user_memory.setText(id_user);                                //indicamos el lugar en dónde ponemos el texto

// referencias a campos en pantalla
        text_question = (TextView) findViewById(R.id.tv_quiz_question_one);
        group = (RadioGroup) findViewById(R.id.rg_answers);
        bt_next = (Button) findViewById(R.id.button_check);

//obtenemos array de las preguntas
        all_questions = getResources().getStringArray(R.array.questions);
        correct_is_answer = new boolean[all_questions.length];
        current_question = 0;                                                                          //asignamos empezar en la pregunta 0
        ShowQuestion();                                                                                  //llamada a metodo show


//TODO: COSAS PENDIENTES POR HACER, hacer muchos pasos sin comprobar puede traaer problemas


        bt_next.setOnClickListener(v -> {
            int id = group.getCheckedRadioButtonId();
            int answer = -1;
            for (int i = 0; i < ids_answers.length; i++) {
                if (ids_answers[i] == id) {
                    answer = i;  //desmarca botones hijos

                }
            }
            correct_is_answer[current_question] = (answer == correct_answer);                            //guardamos que la pregunta sea correcta
            if(current_question < all_questions.length - 1){
                current_question++;
                ShowQuestion();
            }else{
                int correctas = 0, incorrectas= 0;
                for (boolean b : correct_is_answer){                                                 //el for pasa por todos los elementos y me los trae
                    if (b) correctas++;
                    else incorrectas++;
                }
                String result = String.format("Correctas = %d --  Incorrectas = %d", correctas, incorrectas);
                Toast.makeText(QuizOneActivity.this, result, Toast.LENGTH_SHORT).show();
            }
            for (int i = 0;i < correct_is_answer.length; i++){
                Log.i("ABC", String.format("Respuesta %d: %b", i, correct_is_answer[i]));
            }

        });

    }
    //metodo para mostrar las preguntas
    private void ShowQuestion() {

        String q = all_questions[current_question];                                                                 //usamos la primer pregunta sacandola del arreglo
        String[] parts = q.split(";");                                                        //partimos el string de las preguntas por cada punto y coma
        group.clearCheck();
        text_question.setText(parts[0]);                                                              // de donde sacamos las preguntas, refencia donde se mnmuestran las preguntas

        for (int i = 0; i < ids_answers.length; i++) {
            RadioButton rb = (RadioButton) findViewById(ids_answers[i]);
            //truco de la opción correcta
            String answer = parts[i+1];
            if (answer.charAt(0) == '*'){
                correct_answer = i;
                answer = answer.substring(1);                                                            //pedimos el string a partir del caracter uno para no mostrar el *
            }
            rb.setText(answer);                                                                   //
        }
        //DESDE String q0 = all_questions[0];   HASTA AQUÍ CAMBIA LA PREGUNTA
        if(current_question == all_questions.length - 1){
            bt_next.setText("Comprobar");
        }
    }

    //    metodo para el button atrás
    public void Home (View view){
        Intent atras = new Intent(this, CuerpoActivity.class);
        atras.putExtra("id_user", tv_user_memory.getText().toString());
        startActivity(atras);

    }
}