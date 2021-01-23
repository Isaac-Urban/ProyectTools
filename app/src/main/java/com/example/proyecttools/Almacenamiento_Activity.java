package com.example.proyecttools;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

public class Almacenamiento_Activity extends AppCompatActivity {

    public static final String CORRECT_ANSWER_A = "correct_answer";
    public static final String CURRENT_QUESTION_A = "current_question";
    public static final String ANSWER_IS_CORRECT_A = "RespuestaCorrecta";
    public static final String ANSWER_A = "answer";

    private int Tabla_resp_a[] = {R.id.rb_one_a, R.id.rb_two_a, R.id.rb_three_a,};                           //Array de opciones de respuesta
    private String[] all_questions_a;                                                                  //array que contiene las preguntas
    private RadioGroup Rg_resp_a;                                                                      //radiogroup de respuestas
    private Button bt_next_a;

    private TextView Tv_pregunta_a;
    private TextView tv_score_a;
    private TextView tv_maxscore_a;                                //objeto que guarda el score
    private TextView tv_vidas_a;
    private MediaPlayer mp_bad, mp_great;
    private int Vidas=0, Almacenamiento_score =0;           //TODO: sin usar

    // Variables que debe guardar la app
    private boolean[] answer_is_correct_a;
    private int current_question_a;
    private int[] answer_a;
    private int correct_answer_a;

//TODO: Cuando la actividad lo necesite guardará los datos de estas variables para no afectar a la app
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(CORRECT_ANSWER_A, correct_answer_a);
        outState.putInt(CURRENT_QUESTION_A, current_question_a);
        outState.putBooleanArray(ANSWER_IS_CORRECT_A, answer_is_correct_a);
        outState.putIntArray(ANSWER_A, answer_a);
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_almacenamiento_);

// referencias a campos en pantalla
        //alojamos nombre usuario gracias a la key
        String id_user_a = getIntent().getStringExtra("username");
        all_questions_a = getResources().getStringArray(R.array.Almacenamiento_questions);              //obtenemos array de strings de las preguntas

        //objeto que guarda el nombre de usuario
        TextView tv_nombre = findViewById(R.id.tv_user_one);
        TextView tv_username = findViewById(R.id.tv_username_one);
        Tv_pregunta_a = findViewById(R.id.tv_quiz_questions_one);                                       //lugar donde se muestra la pregunta
        Rg_resp_a = findViewById(R.id.rg_answers_one);                                                  //grupo de opciones_r
        bt_next_a = findViewById(R.id.bt_next_one);                                                     //boton de avance
        //bt_prev = findViewById(R.id.bt_prev);                                                       //boton de regreso
        tv_score_a = findViewById(R.id.al_score);
        tv_maxscore_a = findViewById(R.id.tv_maxScore_one);
        tv_vidas_a = findViewById(R.id.tv_vidas_one);


        if (savedInstanceState == null) {                                                               //este if valida si la apiicacion se crea desde 0 o si tiene algo guardado que debe mostrar
            startover();
        } else {
            Bundle state = savedInstanceState;
            correct_answer_a = state.getInt(CORRECT_ANSWER_A);
            current_question_a = state.getInt(CURRENT_QUESTION_A);
            answer_is_correct_a = state.getBooleanArray(ANSWER_IS_CORRECT_A);
            answer_a = state.getIntArray(ANSWER_A);
            ShowQuestion();
        }


//nombre de usuario

        tv_nombre.setText(id_user_a);                                                                  //indicamos el lugar en dónde ponemos el texto
        tv_username.setText("Usuario: " + id_user_a);


//TODO: condicionales de los botones
        bt_next_a.setOnClickListener(v -> {
            CheckAnswer();
            if (current_question_a < all_questions_a.length - 1) {                                       //condicional para incrementar la pregunta actual, asi sabemos cuando debe cambiar de pregunta y cuando no
                current_question_a++;
                ShowQuestion();
                CheckResults();
            } else {
                Results();                                                                           //metodo que inicia cuando se esta en la ultima pregunta para checar resultados
            }
        });

//        bt_prev.setOnClickListener(v -> {
//            CheckAnswer();
//            if (current_question > 0) {                                                              //
//                current_question--;
//                ShowQuestion();
//            }
//        });

////TODO: PROCESOS NECESARIOS DE LA BASE DE DATOS QUE GUARDA EL SCORE Y EL NOMBRE
//        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "BD", null, 1);
//        SQLiteDatabase BD = admin.getWritableDatabase();                                              //poder crear la apertura en modo lectura de la BD
//        Cursor consulta = BD.rawQuery("select * from puntaje where score = (select max(score) from puntaje)", null); //tomamos los datos de la tabla puntaje para saber si ya hay maximo puntaje del jugador
//        if (consulta.moveToFirst()) {
//            String tem_nombre = consulta.getString(0);
//            String tem_score = consulta.getString(1);
//            tv_maxscore.setText("MaxPuntaje =  " + tem_score);
//            BD.close();
//        } else {
//            BD.close();
//        }


//      mp_bad = MediaPlayer.create(this, R.raw.bad);

    }       //fin do ONCREATE


    //TODO: Método para mostrar las preguntas
    private void ShowQuestion() {
        String pregunta = all_questions_a[current_question_a];                                           //usamos la primer pregunta sacandola del arreglo
        String[] Partes_Array = pregunta.split(";");                                          //partimos el string de las preguntas por cada punto y coma que especificamos en el string
        Rg_resp_a.clearCheck();                                                                        //Limpia la seleccion de la respuesta anterior
        Tv_pregunta_a.setText(Partes_Array[0]);                                                        // de donde sacamos las preguntas, refencia donde se mnmuestran las preguntas

        for (int i = 0; i < Tabla_resp_a.length; i++) {                                                //este ciclo recorre la tabla de los botones de las posibles respuestas
            RadioButton rb = findViewById(Tabla_resp_a[i]);                                            //se recorren todos los id y va a obtener el botón que toca
            String ans = Partes_Array[i + 1];                                                        //
            //CharAr(posición), caracter en la posicion == *
            if (ans.charAt(0) == '*') {                                                              //truco de la opción correcta
                correct_answer_a = i;
                ans = ans.substring(1);                                                              //pedimos el string a partir del caracter uno para no mostrar el *
            }
            rb.setText(ans);                                                                         //
            if (answer_a[current_question_a] == i) {                                                     //condicional para que cada vez que el usuario regrese a la pregunta anterior este marcada su selección
                rb.setChecked(true);
            }
        }
//        if (current_question == 0) {                                                                 //condiconal con la que ocultamos el boton prev en la primera pregunta
//            bt_prev.setVisibility(View.GONE);
//        } else {
//            bt_prev.setText(R.string.prev);
//            bt_prev.setVisibility(View.VISIBLE);
//        }

        if (current_question_a == all_questions_a.length - 1) {                                          //condicional que cambia el texto del boton cuando se muestra la ultima pregunta
            bt_next_a.setText(R.string.Finish);
        } else {
            bt_next_a.setText(R.string.Next);
        }
    }


    //TODO: Método que nos ayuyda a quedarnos con el id de respuesta seleccionado
    private void CheckAnswer() {
        int id = Rg_resp_a.getCheckedRadioButtonId();
        int ans = -1;
        for (int i = 0; i < Tabla_resp_a.length; i++) {                                                //Bucle que recorre los id de respuestas
            if (Tabla_resp_a[i] == id) {                                                               //el radio group se comunica con la tabla de ids de respuestas y si el numero seleccionado coincide nos quedamos con el numero
                ans = i;                                                                             //el numero es el indice del arreglo que representa un radio button
            }
        }
        answer_is_correct_a[current_question_a] = (ans == correct_answer_a);                               //comparacion de booleano que nos ayuda a guardar que la pregunta sea correcta
        answer_a[current_question_a] = ans;
    }


    //TODO: Método de checar resultados
    private void CheckResults() {                                                                    //condicional que almacena los resultados
        int Al_Correctas = 0, Incorrectas = 0;
//        mp_great = MediaPlayer.create(this, R.raw.great);

        AlertDialog.Builder WIN = new AlertDialog.Builder(this);                             //creamos el cuadro de alerta
        WIN.setIcon(R.drawable.ic_baseline_sentiment_very_satisfied_24);
        WIN.setTitle("¡FELICIDADES NIVEL COMPLETO!");
        String Alerta = String.format("Puntuación = %d", Almacenamiento_score);
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
                Almacenamiento_Activity.this.finish();
            }
        });
        WIN.setNeutralButton("Seguir Jugando", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        for  (int i = 0; i < all_questions_a.length; i++) {                                            //Respuestas, puntaje, vidas
            if (answer_is_correct_a[i]){                                                               //incrementamos valor de las respuestas
                //Toast.makeText(this, "Correcto", Toast.LENGTH_SHORT).show();
                Al_Correctas+=1;
                Almacenamiento_score = Al_Correctas;
                tv_score_a.setText("Puntaje= " + Al_Correctas + " / 50");
                                                                    //actualizar registros en cada acierto  R.string.Puntaje + Correctas + " / 50"
                if (Al_Correctas == 50) {
                    WIN.create().show();
                }

            }else if(!answer_is_correct_a[i]){
                Incorrectas+=1;                                                                        //actualizar registros en cada fallo

            }
        }
    }

    //TODO: Metodo que muestra resultados al final
    private void Results() {

        AlertDialog.Builder Builder = new AlertDialog.Builder(this);                     //Creamos el objeto builder para crear un cuadro de dialogo
        String username = getIntent().getStringExtra("username");
        Builder.setIcon(R.drawable.ic_baseline_sentiment_satisfied_24);
        Builder.setTitle(username + " obtuviste: ");
        String Message = String.format("Puntuación = %d / 50\n\n¿INTENTAR DE NUEVO?", Almacenamiento_score);
        Builder.setMessage(Message);
        Builder.setCancelable(false);
        Builder.setPositiveButton(R.string.Finish, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Almacenamiento_Activity.this.finish();
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
        answer_is_correct_a = new boolean[all_questions_a.length];
        answer_a = new int[all_questions_a.length];
        for (int i = 0; i < answer_a.length; i++){
            answer_a[i]= -1;
        }
        current_question_a = 0;
        ShowQuestion();
        tv_vidas_a.setText(R.string.vidas);
        tv_score_a.setText(R.string.puntuacion);
        tv_maxscore_a.setText(R.string.maximo_puntaje);
    }


//    //TODO: Método del score, alta de datos
//    public void Database(){
//        AdminSQLiteOpenHelper admnin = new AdminSQLiteOpenHelper(this, "BD", null, 1);
//        SQLiteDatabase BD = admnin.getWritableDatabase();                                                  //APERTURA EN MODO LECTURA Y ESCRITURA DE LA BASE DE DATOS
//
//        //Consulta para verificar si existen registros
//        Cursor Consulta = BD.rawQuery("select * from puntaje where score = (select max(score) from puntaje)", null);  //ve a la tabla puntaje y selecciona el puntaje max
//        if (Consulta.moveToFirst()){                                                                       //SI ENCUENTRA REGISTROS, permite saber si hubo respuesta de la orden
//            String tem_nombre = Consulta.getString(0);                                       //el nombre está en la columna 0 de la tabla puntaje
//            String tem_score = Consulta.getString(1);                                        //el score está en la columna 1 de la tabla puntaje
//
//            int best_score = Integer.parseInt(tem_score);
//            if (Score_a > best_score){                                                                     //veroifica que el score sea mayor de otros
//                ContentValues Modificacion = new ContentValues();
//                Modificacion.put("nombre", id_user_a);
//                Modificacion.put("score", Score_a);
//
//                BD.update("puntaje", Modificacion, "score" + best_score, null);
//            }
//            BD.close();
//        }else {                                                                                         // si no encuentra registros
//            ContentValues Insertar = new ContentValues();
//            Insertar.put("nombre", id_user_a);
//            Insertar.put("score", Score_a);
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