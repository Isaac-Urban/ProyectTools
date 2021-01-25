package com.example.proyecttools;

import androidx.annotation.DrawableRes;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.transition.Slide;
import android.transition.Transition;
import android.view.Gravity;
import android.view.View;
//import android.widget.ArrayAdapter;
//import android.widget.CheckBox;
import android.view.animation.DecelerateInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
//import android.widget.Spinner;
//import android.widget.TextView;
import android.widget.TextView;
import android.widget.Toast;

public class Main_Activity extends AppCompatActivity {

    private EditText et1;                                                                           //variable nombre de usuario

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
        String username = et1.getText().toString();                                                    //  Metodo para registrar nombre
                                                                                                        //  validación
        if (username.length() == 0) {                                                                    //nos permite conocerel ancho de la cadena de caracteres
            Toast.makeText(this, "Debes ingresar un nombre", Toast.LENGTH_SHORT).show();
            et1.requestFocus();                                                                          //hacemos que se abra el teclado automaticamente cuando no se ha escrito un nombre
            InputMethodManager IMM = (InputMethodManager)getSystemService(this.INPUT_METHOD_SERVICE);
            IMM.showSoftInput(et1, InputMethodManager.SHOW_IMPLICIT);

        } else if (username.length() != 0) {

            Toast.makeText(this, "Validando registro", Toast.LENGTH_SHORT).show();
            Toast.makeText(this, "¡Listo!", Toast.LENGTH_SHORT).show();

            SharedPreferences preferences = getSharedPreferences("datos", Context.MODE_PRIVATE);       //
            SharedPreferences.Editor Obj_editor = preferences.edit();                                         //para poder editar el objeto para editarlo
            Obj_editor.putString("name", et1.getText().toString());                                           // indicamos al editor que colocaremos un objeto con referencia "name"
            Obj_editor.apply();        //Obj_editor.commit();                                                 //confirmar que queremos guardar la linea de arriba

            //Método para pasar a segundo Activity
            @SuppressWarnings("Unchecked")                                                             //suprimes aviso
            Intent cuerpo = new Intent(this, Cuerpo_Activity.class);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {                             // Check if we're running on Android 5.0 or higher
                cuerpo.putExtra("username", et1.getText().toString());                                       // aqui pasamos el id_user a la otra activity
                startActivity(cuerpo);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }else {
                cuerpo.putExtra("username", et1.getText().toString());                                       // aqui pasamos el id_user a la otra activity
                startActivity(cuerpo);
            }
            }
    }



    @Override
    public void onBackPressed() {
        AlertDialog.Builder Alerta = new AlertDialog.Builder(this);
        Alerta.setTitle("¿Seguro que deseas salir?");
        Alerta.setIcon(R.drawable.ic_baseline_warning_24);
        Alerta.setCancelable(false);
        Alerta.setPositiveButton(R.string.Salir, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Main_Activity.this.finish();
            }
        });
        Alerta.setNegativeButton(R.string.Regresar, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        Alerta.create().show();
    }
}
