package com.example.tarea2;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class DetalleActivity extends AppCompatActivity {

    private TextView bannerDetalle, monto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle);

        bannerDetalle = findViewById(R.id.banner_detalle);
        monto = findViewById(R.id.monto);

        // Recibir los datos enviados desde MainActivity
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String nombre = extras.getString("nombre");
            String correo = extras.getString("correo");
            String comida = extras.getString("comida");
            String comentarios = extras.getString("comentarios");

            // Personalizar el texto del banner y monto
            bannerDetalle.setText("Pago de " + comida);
            monto.setText("$ 199.00");

            // Ejemplo: mostrar los datos en consola (solo para comprobar)
            System.out.println("Pedido de " + nombre + " (" + correo + "): " + comida + " - " + comentarios);
        }
    }
}

