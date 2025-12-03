package com.example.tarea2;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ModificarPedidoActivity extends AppCompatActivity {

    private static final String TAG = "AppComida";
    private EditText inputNombre, inputCorreo, inputComentarios;
    private Spinner spinnerComida;
    private Button btnGuardar;
    private int pedidoId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modificar_pedido);

        inputNombre = findViewById(R.id.input_nombre_mod);
        inputCorreo = findViewById(R.id.input_correo_mod);
        inputComentarios = findViewById(R.id.input_comentarios_mod);
        spinnerComida = findViewById(R.id.spinner_comida_mod);
        btnGuardar = findViewById(R.id.btn_guardar_mod);

        String[] comidas = {"Pizza", "Hamburguesa", "Tacos", "Sushi", "Ensalada"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, comidas);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerComida.setAdapter(adapter);

        // Cargar el último pedido y rellenar campos
        DatabaseHelper db = new DatabaseHelper(this);
        Cursor cursor = db.obtenerUltimoPedido();

        if (cursor != null && cursor.moveToFirst()) {
            pedidoId = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
            String nombre = cursor.getString(cursor.getColumnIndexOrThrow("nombre"));
            String correo = cursor.getString(cursor.getColumnIndexOrThrow("correo"));
            String comida = cursor.getString(cursor.getColumnIndexOrThrow("comida"));
            String comentarios = cursor.getString(cursor.getColumnIndexOrThrow("comentarios"));

            inputNombre.setText(nombre);
            inputCorreo.setText(correo);
            inputComentarios.setText(comentarios);

            // Intentamos seleccionar el item del spinner que coincida con 'comida'
            int pos = adapter.getPosition(comida);
            if (pos >= 0) spinnerComida.setSelection(pos);

            cursor.close();
        } else {
            // No hay pedidos
            Toast.makeText(this, "No hay pedidos guardados para modificar", Toast.LENGTH_LONG).show();
            if (cursor != null) cursor.close();
            btnGuardar.setEnabled(false);
        }

        btnGuardar.setOnClickListener(v -> {
            if (pedidoId == -1) {
                Toast.makeText(this, "No hay un pedido para actualizar", Toast.LENGTH_SHORT).show();
                return;
            }

            String nuevoNombre = inputNombre.getText().toString().trim();
            String nuevoCorreo = inputCorreo.getText().toString().trim();
            String nuevaComida = spinnerComida.getSelectedItem().toString();
            String nuevosComentarios = inputComentarios.getText().toString().trim();

            // Fecha actualizada
            String fecha = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());

            boolean actualizado = db.actualizarPedido(pedidoId, nuevoNombre, nuevoCorreo, nuevaComida, nuevosComentarios, fecha);

            if (actualizado) {
                Log.d(TAG, "Pedido actualizado correctamente (id=" + pedidoId + ")");
                Toast.makeText(this, "Pedido actualizado", Toast.LENGTH_SHORT).show();
                finish(); // regresa al MainActivity o historial
            } else {
                Log.e(TAG, "Error al actualizar pedido (id=" + pedidoId + ")");
                Toast.makeText(this, "Error al actualizar", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
