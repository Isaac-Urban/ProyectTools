package com.example.proyecttools;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class Web_Activity extends AppCompatActivity {
    //    encapsulacion
    WebView wv1; //DADO A QUE NECESITAMOS UN ACESO NO RESTRINGIDO, NO USAMOS PRIVATE

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);

        wv1 = findViewById(R.id.wv_1); //conexión parte lógica a gráfica
        wv1.setWebViewClient(new WebViewClient()); //abre la pestaña del navegador en la misma app
        wv1.loadUrl("https://www.facebook.com/PC-Student-105972798130215/");
    }

//Indicamos que mediante el boton back del telefono tambien aplique la animacion
    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

}