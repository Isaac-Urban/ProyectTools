package com.example.proyecttools;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class Imagenes_Activity extends AppCompatActivity {

    private ImageView Imagen;
    private MediaPlayer mp_bad, mp_great;
    String numero[] = {"uno", "dos", "tres", "cuatro", "cinco", "seis", "siete", "ocho", "nueve", "diez", "once"};
    int score, resultado, vidas;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imagenes_);

//Referiencias a campos en pantalla
        Imagen = findViewById(R.id.iv_imagenes);


        ImagenAleatoria();

    }


//TODO: recuerda poner el nombre de numeros a las imaggenes
    public void ImagenAleatoria(){
        if(score <= 9){
            resultado= (int)(Math.random()*10);

            if(resultado <= 10){
                for(int i = 0; i<numero.length; i++){
                    int id = getResources().getIdentifier(numero[i], "drawable", getPackageName());
                    if(resultado == i){
                        Imagen.setImageResource(id);
                    }
                }
            }else{
                ImagenAleatoria();
            }
        }else{
            //aqui definiras lo que se hará una vez se cumpla la condicion del score
        }
    }

}