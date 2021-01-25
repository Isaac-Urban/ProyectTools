package com.example.proyecttools;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

public class CategoryOne_Activity extends AppCompatActivity {

    public static final String CORRECT_ANSWER_i = "correct_answer";
    public static final String CURRENT_QUESTION_i = "current_question";
    public static final String ANSWER_IS_CORRECT_i = "RespuestaCorrecta";
    public static final String ANSWER_i = "answer";

    private int Tabla_resp_i[] = {R.id.rb_one, R.id.rb_two, R.id.rb_three,};                           //Array de opciones de respuesta
    private String[] all_questions;                                                                  //array que contiene las preguntas
    private RadioGroup Rg_resp_i;                                                                      //radiogroup de respuestas
    private Button bt_next_i;

    private TextView Tv_pregunta;
    private TextView tv_score;
    private TextView tv_maxscore;                                //objeto que guarda el score
    private TextView tv_vidas;
    private MediaPlayer mp_bad, mp_great;
    private int Vidas=0, Score_i=0;           //TODO: sin usar

    // Variables que debe guardar la app
    private boolean[] answer_is_correct;
    private int current_question;
    private int[] answer;
    private int correct_answer;

//TODO: Cuando la actividad lo necesite guardará los datos de estas variables para no afectar a la app
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(CORRECT_ANSWER_i, correct_answer);
        outState.putInt(CURRENT_QUESTION_i, current_question);
        outState.putBooleanArray(ANSWER_IS_CORRECT_i, answer_is_correct);
        outState.putIntArray(ANSWER_i, answer);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_one);

// referencias a campos en pantalla
        //alojamos nombre usuario gracias a la key
        String id_user_i = getIntent().getStringExtra("username");


        //objeto que guarda el nombre de usuario
        TextView tv_nombre = findViewById(R.id.Tv_tool_user);
        TextView tv_username = findViewById(R.id.tv_username);
        //nombre de usuario
        tv_nombre.setText(id_user_i);                                                                  //indicamos el lugar en dónde ponemos el texto
        tv_username.setText("Usuario: " + id_user_i);


        Tv_pregunta = findViewById(R.id.tv_quiz_questions);                                          //lugar donde se muestra la pregunta
        Rg_resp_i = findViewById(R.id.rg_answers);                                                     //grupo de opciones_r
        bt_next_i = findViewById(R.id.bt_next);                                                        //boton de avance
       //bt_prev = findViewById(R.id.bt_prev);                                                       //boton de regreso
        all_questions = getResources().getStringArray(R.array.Informatic_questions);                 //obtenemos array de strings de las preguntas
        tv_score = findViewById(R.id.tv_score);
        tv_maxscore = findViewById(R.id.tv_maxScore);
        tv_vidas = findViewById(R.id.tv_vidas);

//este if valida si la apiicacion se crea desde 0 o si tiene algo guardado que debe mostrar
        if (savedInstanceState == null) {
            startover();
        } else {
            Bundle state = savedInstanceState;
            correct_answer = state.getInt(CORRECT_ANSWER_i);
            current_question = state.getInt(CURRENT_QUESTION_i);
            answer_is_correct = state.getBooleanArray(ANSWER_IS_CORRECT_i);
            answer = state.getIntArray(ANSWER_i);
            ShowQuestion();
        }


//TODO: condicionales de los botones
        bt_next_i.setOnClickListener(v -> {
            CheckAnswer();
            if (current_question < all_questions.length - 1) {                                       //condicional para incrementar la pregunta actual, asi sabemos cuando debe cambiar de pregunta y cuando no
                current_question++;
                ShowQuestion();
                CheckResults();
            } else {
                Results();                                                                           //metodo que inicia cuando se esta en la ultima pregunta para checar resultados
            }
        });

////TODO: BASE DE DATOS QUE GUARDA EL SCORE Y EL NOMBRE
//        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "BD", null, 1);
//        SQLiteDatabase BD = admin.getWritableDatabase();                                              //poder crear la apertura en modo lectura de la BD
//        Cursor consulta = BD.rawQuery("select * from puntaje where score = (select max(score) from puntaje)", null); //tomamos los datos de la tabla puntaje para saber si ya hay maximo puntaje del jugador
//        if (consulta.moveToFirst()) {                                                                   //si encuentras algo dentro de la tabla
//            String tem_nombre = consulta.getString(0);
//            String tem_score = consulta.getString(1);
//            tv_maxscore.setText(tem_score);
//            BD.close();
//        } else {
//            BD.close();
//        }

        mp_bad = MediaPlayer.create(this, R.raw.bad);
        mp_great = MediaPlayer.create(this, R.raw.great);

    }       //fin do ONCREATE


    //TODO: Método para mostrar las preguntas
    private void ShowQuestion() {
        String pregunta = all_questions[current_question];                                           //usamos la primer pregunta sacandola del arreglo
        String[] Partes_Array = pregunta.split(";");                                          //partimos el string de las preguntas por cada punto y coma que especificamos en el string
        Rg_resp_i.clearCheck();                                                                        //Limpia la seleccion de la respuesta anterior
        Tv_pregunta.setText(Partes_Array[0]);                                                        // de donde sacamos las preguntas, refencia donde se mnmuestran las preguntas

        for (int i = 0; i < Tabla_resp_i.length; i++) {                                                //este ciclo recorre la tabla de los botones de las posibles respuestas
            RadioButton rb = findViewById(Tabla_resp_i[i]);                                            //se recorren todos los id y va a obtener el botón que toca
            String ans = Partes_Array[i + 1];                                                        //
            //CharAr(posición), caracter en la posicion == *
            if (ans.charAt(0) == '*') {                                                              //truco de la opción correcta
                correct_answer = i;
                ans = ans.substring(1);                                                              //pedimos el string a partir del caracter uno para no mostrar el *
            }
            rb.setText(ans);                                                                         //
            if (answer[current_question] == i) {                                                     //condicional para que cada vez que el usuario regrese a la pregunta anterior este marcada su selección
                rb.setChecked(true);
            }
        }
//        if (current_question == 0) {                                                                 //condiconal con la que ocultamos el boton prev en la primera pregunta
//            bt_prev.setVisibility(View.GONE);
//        } else {
//            bt_prev.setText(R.string.prev);
//            bt_prev.setVisibility(View.VISIBLE);
//        }

        if (current_question == all_questions.length - 1) {                                          //condicional que cambia el texto del boton cuando se muestra la ultima pregunta
            bt_next_i.setText(R.string.Finish);
        } else {
            bt_next_i.setText(R.string.Next);
        }
    }


//TODO: Método que nos ayuyda a quedarnos con el id de respuesta seleccionado
    private void CheckAnswer() {
        int id = Rg_resp_i.getCheckedRadioButtonId();
        int ans = -1;
        for (int i = 0; i < Tabla_resp_i.length; i++) {                                                //Bucle que recorre los id de respuestas
            if (Tabla_resp_i[i] == id) {                                                               //el radio group se comunica con la tabla de ids de respuestas y si el numero seleccionado coincide nos quedamos con el numero
                ans = i;                                                                             //el numero es el indice del arreglo que representa un radio button
            }
        }
        answer_is_correct[current_question] = (ans == correct_answer);                               //comparacion de booleano que nos ayuda a guardar que la pregunta sea correcta
        answer[current_question] = ans;
    }


//TODO: Método de checar resultados
    private void CheckResults() {                                                                    //condicional que almacena los resultados
        int Correctas = 0, Incorrectas = 0;

        //creamos el cuadro de alerta
        AlertDialog.Builder WIN = new AlertDialog.Builder(this);
        WIN.setIcon(R.drawable.ic_baseline_sentiment_very_satisfied_24);
        WIN.setTitle("¡FELICIDADES NIVEL COMPLETO!");
        String Alerta = String.format("Puntuación = 25");
        WIN.setMessage(Alerta);
        WIN.setCancelable(false);
        WIN.setNegativeButton(R.string.Start_Again, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startover();
            }
        });
        WIN.setPositiveButton(R.string.Finish, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                CategoryOne_Activity.this.finish();
            }
        });

        for  (int i = 0; i < all_questions.length; i++) {                                            //Respuestas, puntaje, vidas
            if (answer_is_correct[i]){                                                               //incrementamos valor de las respuestas
//                mp_great.start();
                //Toast.makeText(this, "Correcto", Toast.LENGTH_SHORT).show();
                Correctas+=1;
                tv_score.setText("Puntaje = " + Correctas + " / 25");
                Score_i = Correctas;                                                                 //actualizar registros en cada acierto
                if (Correctas == 24) {
                    WIN.create().show();
                }
//                break;
            }else{
                Incorrectas+=1;                                                                        //actualizar registros en cada fallo
//                mp_bad.start();
//                break;
            }
        }
}

//TODO: Metodo que muestra resultados al final
private void Results() {

            AlertDialog.Builder Builder = new AlertDialog.Builder(this);                     //Creamos el objeto builder para crear un cuadro de dialogo
            String username = getIntent().getStringExtra("username");
                Builder.setIcon(R.drawable.ic_baseline_sentiment_satisfied_24);
                Builder.setTitle(username + " obtuviste: ");
                String Message = String.format("Puntuación = %d / 25\n\n¿INTENTAR DE NUEVO?", Score_i);
                Builder.setMessage(Message);
                Builder.setCancelable(false);
                Builder.setPositiveButton(R.string.Finish, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    CategoryOne_Activity.this.finish();
                }
            });
                Builder.setNegativeButton(R.string.Start_Again, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                        startover();                                                                         //borrar respuestas y regresar a la pregunta 1
                }
                });
            Builder.create().show();
}


//TODO: Metodo para empezar desde 0
    private void startover(){
        answer_is_correct = new boolean[all_questions.length];
        answer = new int[all_questions.length];
        for (int i = 0; i < answer.length; i++){
            answer[i]= -1;
        }
        current_question = 0;
        ShowQuestion();
        tv_vidas.setText("Vidas = 3");
        tv_score.setText("Puntaje = 0 / 25");
        tv_maxscore.setText("MáxPuntaje = 0");
    }


////TODO: Método del score, alta de datos
//    public void Database(){
//    AdminSQLiteOpenHelper Admin = new AdminSQLiteOpenHelper(this, "BD", null, 1);
//    SQLiteDatabase BD = Admin.getWritableDatabase();                                                  //APERTURA EN MODO LECTURA Y ESCRITURA DE LA BASE DE DATOS
//
//        //Consulta para verificar si existen registros
//        Cursor Consulta = BD.rawQuery("select * from puntaje where score = (select max(score) from puntaje)", null);  //ve a la tabla puntaje y selecciona el puntaje max
//        if (Consulta.moveToFirst()){                                                                       //SI ENCUENTRA REGISTROS, permite saber si hubo respuesta de la orden
//            String tem_nombre = Consulta.getString(0);                                       //el nombre está en la columna 0 de la tabla puntaje
//            String tem_score = Consulta.getString(1);                                        //el score está en la columna 1 de la tabla puntaje
//
//            int best_score = Integer.parseInt(tem_score);
//            if (Score > best_score){                                                                      //veroifica que el score sea mayor de otros
//                ContentValues Modificacion = new ContentValues();
//                Modificacion.put("nombre", id_user);
//                Modificacion.put("score", Score);
//                BD.update("puntaje", Modificacion, "score" + best_score, null);
//            }
//            BD.close();
//        }else {                                                                                         // si no encuentra registros
//            ContentValues Insertar = new ContentValues();
//            Insertar.put("nombre", id_user);
//            Insertar.put("score", Score);
//            BD.insert("puntaje", null, Insertar);
//            BD.close();
//        }
//    }


//Indicamos que mediante el boton back del telefono tambien aplique la animacion
        @Override
        public void finish () {
            super.finish();
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        }

    }


////TODO: Metodo de los sonidos, score y vidas
//    private void Comprobar(){
//        int id = Rg_resp.getCheckedRadioButtonId();
//        int ans = -1;
//        for (int i = 0; i < Tabla_resp.length; i++) {                                                //Bucle que recorre los id de respuestas
//            if (Tabla_resp[i] == id) {                                                               //el radio group se comunica con la tabla de ids de respuestas y si el numero seleccionado coincide nos quedamos con el numero
//                ans = i;                                                                             //el numero es el indice del arreglo que representa un radio button
//            }
//            if (ans == correct_answer) {
//                mp_great.start();
//                Score++;
//                tv_score.setText("Puntaje = " + Score);
//            } else {
//                mp_bad.start();
//                Vidas--;
//                if (Vidas == 3){
//                    iv_vidas.setImageResource(R.mipmap.tres_vidas);
//                }else if (Vidas == 2) {
//                    Toast.makeText(this, "Te quedan 2 vidas", Toast.LENGTH_SHORT).show();
//                    iv_vidas.setImageResource(R.mipmap.dos_vidas);
//                }else if (Vidas == 1) {
//                    Toast.makeText(this, "Te queda 1 vida", Toast.LENGTH_SHORT).show();
//                    iv_vidas.setImageResource(R.mipmap.una_vida);
//                }else if (Vidas == 0){
//                    Toast.makeText(this, "No te quedan vidas", Toast.LENGTH_SHORT).show();
//                    Intent menu = new Intent(this, Cuerpo_Activity.class);
//                    startActivity(menu);
//                    //finish();
//
//                }
//            }
//        }
//    }

