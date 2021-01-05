package com.example.proyecttools;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.transition.Slide;
import android.transition.Transition;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
//import android.widget.ArrayAdapter;
//import android.widget.CheckBox;
import android.view.animation.DecelerateInterpolator;
import android.widget.EditText;
//import android.widget.Spinner;
//import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import java.time.Duration;

public class MainActivity extends AppCompatActivity {

    private EditText et1;                                                                           //variable nombre de usuario
    private Transition transition;
    public static final long duration_transition = 1500;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        et1 = findViewById(R.id.pt_user);                                     //objeto nombre de usuario            //conexion parte logica parte gráfica user

//metodo para guardar valores de usuario
        SharedPreferences Save_nombre = getSharedPreferences("datos", Context.MODE_PRIVATE);           //linea para recuperar lo que esta guardado
        et1.setText(Save_nombre.getString("name", ""));                                         //lo que se haya encontrado debe colocarse aquí
    }


    //Metodo del button vamos
    public void Vamos(View view) {

//metodo para la transicion
        transition = new Slide(Gravity.START);
        transition.setDuration(duration_transition);
        transition.setInterpolator(new DecelerateInterpolator());
        getWindow().setExitTransition(transition);

//        Méetodo para registrar nombre
        String registro_user = et1.getText().toString();

//  validación
        if (registro_user.length() == 0) {                                                                    //nos permite conocerel ancho de la cadena de caracteres
            Toast.makeText(this, "Debes ingresar un nombre", Toast.LENGTH_SHORT).show();
        } else if (registro_user.length() != 0) {
            Toast.makeText(this, "Validando registro", Toast.LENGTH_SHORT).show();
            Toast.makeText(this, "¡Listo!", Toast.LENGTH_SHORT).show();

            SharedPreferences preferences = getSharedPreferences("datos", Context.MODE_PRIVATE);       //
            SharedPreferences.Editor Obj_editor = preferences.edit();                                         //para poder editar el objeto para editarlo
            Obj_editor.putString("name", et1.getText().toString());                                           // indicamos al editor que colocaremos un objeto con referencia "name"
            Obj_editor.apply();        //Obj_editor.commit();                                                 //confirmar que queremos guardar la linea de arriba

            //        Método para pasar a segundo Activity
            //suprimes aviso
            @SuppressWarnings("Unchecked")
            Intent cuerpo = new Intent(this, CuerpoActivity.class);
            cuerpo.putExtra("id_user", et1.getText().toString());                                       // aqui pasamos el id_user a la otra activity
            startActivity(cuerpo, ActivityOptionsCompat.makeSceneTransitionAnimation(this).toBundle());
        }
    }


}
