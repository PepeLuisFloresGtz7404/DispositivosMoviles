package com.example.tarea2;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.ContentValues;
import android.database.Cursor;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "pedidos.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_PEDIDOS = "pedidos";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_PEDIDOS + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "nombre TEXT, " +
                "correo TEXT, " +
                "comida TEXT, " +
                "comentarios TEXT, " +
                "fecha TEXT)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PEDIDOS);
        onCreate(db);
    }

    // INSERTAR PEDIDO
    public boolean insertarPedido(String nombre, String correo, String comida, String comentarios, String fecha) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues valores = new ContentValues();

        valores.put("nombre", nombre);
        valores.put("correo", correo);
        valores.put("comida", comida);
        valores.put("comentarios", comentarios);
        valores.put("fecha", fecha);

        long resultado = db.insert(TABLE_PEDIDOS, null, valores);
        return resultado != -1;
    }

    // CONSULTAR TODOS LOS PEDIDOS
    public Cursor obtenerPedidos() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_PEDIDOS + " ORDER BY id DESC", null);
    }

    // Devuelve un Cursor con el último pedido (id más alto), o null si no hay
    public Cursor obtenerUltimoPedido() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_PEDIDOS + " ORDER BY id DESC LIMIT 1", null);
    }

    // Actualiza un pedido por id. Retorna true si se actualizó al menos una fila.
    public boolean actualizarPedido(int id, String nombre, String correo, String comida, String comentarios, String fecha) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues valores = new ContentValues();

        valores.put("nombre", nombre);
        valores.put("correo", correo);
        valores.put("comida", comida);
        valores.put("comentarios", comentarios);
        valores.put("fecha", fecha);

        int filas = db.update(TABLE_PEDIDOS, valores, "id = ?", new String[]{ String.valueOf(id) });
        return filas > 0;
    }

    // Eliminar un pedido por ID
    public boolean eliminarPedido(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int filas = db.delete(TABLE_PEDIDOS, "id = ?", new String[]{String.valueOf(id)});
        return filas > 0;
    }


}

