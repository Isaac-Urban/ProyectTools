package com.example.proyecttools;

        import androidx.annotation.NonNull;
        import androidx.appcompat.app.AlertDialog;
        import androidx.appcompat.app.AppCompatActivity;
        import android.content.ContentValues;
        import android.content.DialogInterface;
        import android.content.Intent;
        import android.database.Cursor;
        import android.database.sqlite.SQLiteDatabase;
        import android.media.MediaPlayer;
        import android.os.Bundle;
        import android.widget.Button;
        import android.widget.RadioButton;
        import android.widget.RadioGroup;
        import android.widget.TextView;
        import android.widget.Toast;

        import java.time.Duration;

public class Redes_Activity extends AppCompatActivity {

    public static final String CORRECT_ANSWER = "correct_answer";
    public static final String CURRENT_QUESTION = "current_question";
    public static final String ANSWER_IS_CORRECT = "RespuestaCorrecta";
    public static final String ANSWER = "answer";

    private int Tabla_resp_r[] = {R.id.rb_one_r, R.id.rb_two_r, R.id.rb_three_r,};                           //Array de opciones de respuesta
    private String[] all_questions;                                                                  //array que contiene las preguntas
    private RadioGroup Rg_resp;                                                                      //radiogroup de respuestas
    private Button bt_next;

    private TextView Tv_pregunta;
    private TextView tv_score;
    private TextView tv_maxscore;                                //objeto que guarda el score
    private TextView tv_vidas;
    private MediaPlayer mp_bad, mp_great;
    private int Vidas=0, Score_r=0;           //TODO: sin usar
    private String id_user_r;                              //alojamos nombre usuario gracias a la key

    // Variables que debe guardar la app
    private boolean[] answer_is_correct;
    private int current_question;
    private int[] answer;
    private int correct_answer;

    //TODO: Cuando la actividad lo necesite guardará los datos de estas variables para no afectar a la app
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(CORRECT_ANSWER, correct_answer);
        outState.putInt(CURRENT_QUESTION, current_question);
        outState.putBooleanArray(ANSWER_IS_CORRECT, answer_is_correct);
        outState.putIntArray(ANSWER, answer);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_redes_);

// referencias a campos en pantalla

        id_user_r = getIntent().getStringExtra("username");
        all_questions = getResources().getStringArray(R.array.Redes_questions);              //obtenemos array de strings de las preguntas


        //objeto que guarda el nombre de usuario
        TextView tv_nombre = findViewById(R.id.Tv_tool_user);
        TextView tv_username = findViewById(R.id.tv_username_two);
        //nombre de usuario
        tv_nombre.setText(id_user_r);                                                                  //indicamos el lugar en dónde ponemos el texto
        tv_username.setText("Usuario: " + id_user_r);

        Tv_pregunta = findViewById(R.id.tv_quiz_questions_two);                                       //lugar donde se muestra la pregunta
        Rg_resp = findViewById(R.id.rg_answers_two);                                                  //grupo de opciones_r
        bt_next = findViewById(R.id.bt_next_two);                                                     //boton de avance
        //bt_prev = findViewById(R.id.bt_prev);                                                       //boton de regreso
        tv_score = findViewById(R.id.tv_score_two);
        tv_maxscore = findViewById(R.id.tv_maxScore_two);
        tv_vidas = findViewById(R.id.tv_vidas_two);


        if (savedInstanceState == null) {                                                               //este if valida si la apiicacion se crea desde 0 o si tiene algo guardado que debe mostrar
            startover();
        } else {
            Bundle state = savedInstanceState;
            correct_answer = state.getInt(CORRECT_ANSWER);
            current_question = state.getInt(CURRENT_QUESTION);
            answer_is_correct = state.getBooleanArray(ANSWER_IS_CORRECT);
            answer = state.getIntArray(ANSWER);
            ShowQuestion();
        }





//TODO: condicionales de los botones
        bt_next.setOnClickListener(v -> {
            CheckAnswer();
            if (current_question < all_questions.length - 1) {                                       //condicional para incrementar la pregunta actual, asi sabemos cuando debe cambiar de pregunta y cuando no
                current_question++;
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


      mp_bad = MediaPlayer.create(this, R.raw.bad);
      mp_great = MediaPlayer.create(this, R.raw.great);

    }       //fin do ONCREATE


    //TODO: Método para mostrar las preguntas
    private void ShowQuestion() {
        String pregunta = all_questions[current_question];                                           //usamos la primer pregunta sacandola del arreglo
        String[] Partes_Array = pregunta.split(";");                                          //partimos el string de las preguntas por cada punto y coma que especificamos en el string
        Rg_resp.clearCheck();                                                                        //Limpia la seleccion de la respuesta anterior
        Tv_pregunta.setText(Partes_Array[0]);                                                        // de donde sacamos las preguntas, refencia donde se mnmuestran las preguntas

        for (int i = 0; i < Tabla_resp_r.length; i++) {                                                //este ciclo recorre la tabla de los botones de las posibles respuestas
            RadioButton rb = findViewById(Tabla_resp_r[i]);                                            //se recorren todos los id y va a obtener el botón que toca
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
            bt_next.setText(R.string.Finish);
        } else {
            bt_next.setText(R.string.Next);
        }
    }


    //TODO: Método que nos ayuyda a quedarnos con el id de respuesta seleccionado
    private void CheckAnswer() {
        int id = Rg_resp.getCheckedRadioButtonId();
        int ans = -1;
        for (int i = 0; i < Tabla_resp_r.length; i++) {                                                //Bucle que recorre los id de respuestas
            if (Tabla_resp_r[i] == id) {                                                               //el radio group se comunica con la tabla de ids de respuestas y si el numero seleccionado coincide nos quedamos con el numero
                ans = i;                                                                             //el numero es el indice del arreglo que representa un radio button
            }
        }
        answer_is_correct[current_question] = (ans == correct_answer);                               //comparacion de booleano que nos ayuda a guardar que la pregunta sea correcta
        answer[current_question] = ans;
    }


    //TODO: Método de checar resultados
    private void CheckResults() {                                                                    //condicional que almacena los resultados
        int Correctas = 0, Incorrectas = 0;

        AlertDialog.Builder WIN = new AlertDialog.Builder(this);                             //creamos el cuadro de alerta
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
                Redes_Activity.this.finish();
            }
        });

        for  (int i = 0; i < all_questions.length; i++) {                                            //Respuestas, puntaje, vidas
            if (answer_is_correct[i]){                                                               //incrementamos valor de las respuestas
                //Toast.makeText(this, "Correcto", Toast.LENGTH_SHORT).show();
                Correctas+=1;
                tv_score.setText("Puntaje= "+ Correctas + " / 25");
                Score_r = Correctas;                                                                 //actualizar registros en cada acierto
                if (Correctas == 24) {
                    WIN.create().show();
                }

            }else{
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
        String Message = String.format("Puntuación = %d / 25\n\n¿INTENTAR DE NUEVO?", Score_r);
        Builder.setMessage(Message);
        Builder.setCancelable(false);
        Builder.setPositiveButton(R.string.Finish, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Redes_Activity.this.finish();
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
//            if (Score_r > best_score){                                                                     //veroifica que el score sea mayor de otros
//                ContentValues Modificacion = new ContentValues();
//                Modificacion.put("nombre", id_user_r);
//                Modificacion.put("score", Score_r);
//
//                BD.update("puntaje", Modificacion, "score" + best_score, null);
//            }
//            BD.close();
//        }else {                                                                                         // si no encuentra registros
//            ContentValues Insertar = new ContentValues();
//            Insertar.put("nombre", id_user_r);
//            Insertar.put("score", Score_r);
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