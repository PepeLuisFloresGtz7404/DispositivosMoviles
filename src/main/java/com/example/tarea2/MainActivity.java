package com.example.tarea2;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private EditText inputNombre, inputCorreo, inputComentarios;
    private Spinner spinnerComida;
    private Button btnEnviar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Referencias a los elementos del layout
        inputNombre = findViewById(R.id.input_nombre);
        inputCorreo = findViewById(R.id.input_correo);
        inputComentarios = findViewById(R.id.input_comentarios);
        spinnerComida = findViewById(R.id.spinner_comida);
        btnEnviar = findViewById(R.id.btn_enviar);

        // Evento para enviar los datos
        btnEnviar.setOnClickListener(v -> {
            String nombre = inputNombre.getText().toString();
            String correo = inputCorreo.getText().toString();
            String comida = spinnerComida.getSelectedItem().toString();
            String comentarios = inputComentarios.getText().toString();

            // Creamos el intent para ir a la pantalla de pago
            Intent intent = new Intent(MainActivity.this, DetalleActivity.class);
            intent.putExtra("nombre", nombre);
            intent.putExtra("correo", correo);
            intent.putExtra("comida", comida);
            intent.putExtra("comentarios", comentarios);

            startActivity(intent);
        });
    }
}

