package com.example.tarea2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.database.Cursor;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "AppComida";
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private NavigationView navigationView;

    private EditText inputNombre, inputCorreo, inputComentarios;
    private Spinner spinnerComida;
    private Button btnEnviar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Configurar Toolbar como Action Bar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Drawer
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);
        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // Escucha de las opciones del menú lateral
        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_modificar) {
                Log.d(TAG, "Modificar pedido seleccionado");
                Intent intent = new Intent(MainActivity.this, ModificarPedidoActivity.class);
                startActivity(intent);

            } else if (id == R.id.nav_historial) {
                Log.d(TAG, "Historial de pedidos seleccionado");
                Intent intent = new Intent(MainActivity.this, HistorialActivity.class);
                startActivity(intent);

            } else if (id == R.id.nav_cancelar) {
                Log.d(TAG, "Cancelar pedido seleccionado");
                mostrarDialogoCancelar();

            } else if (id == R.id.nav_ayuda) {
                Log.d(TAG, "Ayuda seleccionada");
                mostrarDialogoAyuda();
            }

            drawerLayout.closeDrawers();
            return true;
        });


        // Referencias del formulario
        inputNombre = findViewById(R.id.input_nombre);
        inputCorreo = findViewById(R.id.input_correo);
        inputComentarios = findViewById(R.id.input_comentarios);
        spinnerComida = findViewById(R.id.spinner_comida);
        btnEnviar = findViewById(R.id.btn_enviar);

        // Enviar pedido
        btnEnviar.setOnClickListener(v -> {
            String nombre = inputNombre.getText().toString();
            String correo = inputCorreo.getText().toString();
            String comida = spinnerComida.getSelectedItem().toString();
            String comentarios = inputComentarios.getText().toString();

            // Guardar en SQLite
            DatabaseHelper db = new DatabaseHelper(MainActivity.this);
            String fecha = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date());


            boolean guardado = db.insertarPedido(nombre, correo, comida, comentarios, fecha);

            if (guardado) {
                Log.d(TAG, "Pedido guardado correctamente en SQLite");
            } else {
                Log.e(TAG, "Error al guardar pedido en SQLite");
            }

            // Mandar a detalle después de guardar
            Intent intent = new Intent(MainActivity.this, DetalleActivity.class);
            intent.putExtra("nombre", nombre);
            intent.putExtra("correo", correo);
            intent.putExtra("comida", comida);
            intent.putExtra("comentarios", comentarios);
            startActivity(intent);
        });

    }

    // Menú superior (3 puntitos)
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    // Menú superior (3 puntitos)
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_refresh) {
            Log.d(TAG, "Reenviar pedido seleccionado");
            reenviarUltimoPedido();
            return true;
        } else if (id == R.id.action_settings) {
            Log.d(TAG, "Configuración seleccionada");
            Toast.makeText(this, "Configuración", Toast.LENGTH_SHORT).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    private void mostrarDialogoCancelar() {
        new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("Cancelar pedido")
                .setMessage("¿Seguro que deseas cancelar el último pedido guardado?")
                .setPositiveButton("Sí", (dialog, which) -> cancelarUltimoPedido())
                .setNegativeButton("No", null)
                .show();
    }
    private void cancelarUltimoPedido() {

        Log.d("SQLITE", "Buscando último pedido...");

        DatabaseHelper db = new DatabaseHelper(MainActivity.this);
        Cursor cursor = db.obtenerUltimoPedido();

        if (cursor != null && cursor.moveToFirst()) {

            int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
            Log.d("SQLITE", "Último pedido ID: " + id);

            cursor.close();

            boolean eliminado = db.eliminarPedido(id);

            if (eliminado) {
                Log.d("SQLITE", "Pedido borrado correctamente");
                Toast.makeText(this, "Pedido cancelado correctamente", Toast.LENGTH_SHORT).show();
            } else {
                Log.e("SQLITE", "Error al borrar pedido");
                Toast.makeText(this, "Error al cancelar pedido", Toast.LENGTH_SHORT).show();
            }

        } else {
            Log.e("SQLITE", "Cursor vacío, no hay pedidos");
            Toast.makeText(this, "No hay pedidos para cancelar", Toast.LENGTH_SHORT).show();
        }
    }

    private void mostrarDialogoAyuda() {
        new AlertDialog.Builder(this)
                .setTitle("Ayuda")
                .setMessage("Aplicación de pedidos de comida rápida.\n\n" +
                        "• Llena el formulario para hacer tu pedido\n" +
                        "• Puedes ver el historial de pedidos\n" +
                        "• Puedes modificar el último pedido\n" +
                        "• Puedes cancelar el último pedido\n\n" +
                        "Para más información contacta a soporte.")
                .setPositiveButton("Entendido", null)
                .show();
    }

    private void reenviarUltimoPedido() {
        DatabaseHelper db = new DatabaseHelper(this);
        Cursor cursor = db.obtenerUltimoPedido();

        if (cursor != null && cursor.moveToFirst()) {
            String nombre = cursor.getString(cursor.getColumnIndexOrThrow("nombre"));
            String correo = cursor.getString(cursor.getColumnIndexOrThrow("correo"));
            String comida = cursor.getString(cursor.getColumnIndexOrThrow("comida"));
            String comentarios = cursor.getString(cursor.getColumnIndexOrThrow("comentarios"));

            // Rellenar los campos del formulario
            inputNombre.setText(nombre);
            inputCorreo.setText(correo);
            inputComentarios.setText(comentarios);

            // Seleccionar la comida en el spinner
            int position = -1;
            for (int i = 0; i < spinnerComida.getCount(); i++) {
                if (spinnerComida.getItemAtPosition(i).toString().equals(comida)) {
                    position = i;
                    break;
                }
            }
            if (position >= 0) {
                spinnerComida.setSelection(position);
            }

            Toast.makeText(this, "Pedido cargado para reenviar", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "Pedido recargado en el formulario");
            cursor.close();
        } else {
            Toast.makeText(this, "No hay pedidos para reenviar", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "No hay pedidos en la base de datos");
            if (cursor != null) cursor.close();
        }
    }
}
