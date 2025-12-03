package com.example.tarea2;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class HistorialActivity extends AppCompatActivity {

    private TextView txtHistorial;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historial);

        txtHistorial = findViewById(R.id.txt_historial);

        DatabaseHelper db = new DatabaseHelper(this);
        Cursor cursor = db.obtenerPedidos();

        StringBuilder builder = new StringBuilder();

        while (cursor.moveToNext()) {
            builder.append("ID: ").append(cursor.getInt(0)).append("\n");
            builder.append("Nombre: ").append(cursor.getString(1)).append("\n");
            builder.append("Correo: ").append(cursor.getString(2)).append("\n");
            builder.append("Comida: ").append(cursor.getString(3)).append("\n");
            builder.append("Comentarios: ").append(cursor.getString(4)).append("\n");
            builder.append("Fecha: ").append(cursor.getString(5)).append("\n");
            builder.append("---------------------------\n");
        }

        txtHistorial.setText(builder.toString());
    }
}
