package com.example.sqliteprueba

import android.annotation.SuppressLint
import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.os.Parcel
import android.os.Parcelable

class DatabaseHelper(context: Any): SQLiteOpenHelper(context, DATABASE,null,DATABASE_VERSION) {

companion object{
    private const val DATABASE_VERSION = 1
    private const val DATABASE = "Articulos.db"
    private const val TABLA_ARTICULOS = "articulos"
    private const val KEY_ID = "_id"
    private const val COLUMN_TIPO_ARTICULO = "tipoArticulo"
    private const val COLUMN_NOMBRE = "nombre"
    private const val COLUMN_PESO = "peso"
    private const val COLUMN_PRECIO = "precio"
}

    override fun onCreate(db: SQLiteDatabase) {
        val createTable = "CREATE TABLE $TABLA_ARTICULOS ($KEY_ID INTEGER PRIMARY KEY,"+
                "$COLUMN_TIPO_ARTICULO TEXT, $COLUMN_NOMBRE TEXT, $COLUMN_PESO INTEGER,"+
                " $COLUMN_PRECIO TEXT)"

        db.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLA_ARTICULOS")
        onCreate(db)
    }

    fun insertarArticulo(articulo: Articulo){
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_TIPO_ARTICULO, articulo.getTipoArticulo())
            put(COLUMN_NOMBRE,articulo.getNombre())
            put(COLUMN_PESO, articulo.getPeso())
            put(COLUMN_PRECIO,articulo.getPrecio())
        }
        db.insert(TABLA_ARTICULOS,null,values)
        db.close()

    }

    @SuppressLint("Range")
    fun getArticulos(): ArrayList<Articulo> {
        val articulos = ArrayList<Articulo>()
        val selectQuery = "SELECT * FROM $TABLA_ARTICULOS"
        val db = this.readableDatabase
        val cursor = db.rawQuery(selectQuery,null)
        if (cursor.moveToFirst()){
            do {
                val id = cursor.getInt(cursor.getColumnIndex(KEY_ID))
                val nombre = cursor.getString(cursor.getColumnIndex(COLUMN_NOMBRE))
                val tipoArticulo = cursor.getString(cursor.getColumnIndex(COLUMN_TIPO_ARTICULO))
                val peso = cursor.getInt(cursor.getColumnIndex(COLUMN_PESO))
                val precio = cursor.getInt(cursor.getColumnIndex(COLUMN_PRECIO))
                articulos.add(Articulo(tipoArticulo,nombre,peso,precio))
            }while (cursor.moveToFirst())
        }
        cursor.close()
        db.close()

        return articulos
    }

    fun getNumeroArticulos():Int{
        val selectQuery = "SELECT * FROM $TABLA_ARTICULOS"
        val db = this.readableDatabase
        val cursor = db.rawQuery(selectQuery,null)
        var num = 0
        if (cursor.moveToFirst()){
            do {
                num++
            }while (cursor.moveToFirst())
        }
        return num
    }

    @SuppressLint("Range")
    fun findObjeto(nombreB: String):Int{
        val selectQuery = "SELECT * FROM $TABLA_ARTICULOS"
        val db = this.readableDatabase
        val cursor = db.rawQuery(selectQuery,null)
        var num = -1

        if (cursor.moveToFirst()){
            do {
                val id = cursor.getInt(cursor.getColumnIndex(KEY_ID))
                val nombre = cursor.getString(cursor.getColumnIndex(COLUMN_NOMBRE))
                if (nombreB == nombre ) {
                    num = id
                }

            }while (cursor.moveToFirst()|| num != -1)
        }
        cursor.close()
        db.close()

        return num
    }


}


class Articulo(private var tipoArticulo: String, private var nombre: String, private var peso: Int, private var precio: Int):Parcelable {


    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readInt(),
        parcel.readInt()
    ) {
    }

    fun getPeso(): Int {
        return peso
    }
    fun getNombre(): String {
        return nombre
    }
    fun getTipoArticulo(): String {
        return tipoArticulo
    }
    fun getPrecio(): Int {
        return precio
    }

    override fun toString(): String {
        return "[Tipo Artículo:$tipoArticulo, Nombre:$nombre, Peso:$peso]"
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(tipoArticulo)
        parcel.writeString(nombre)
        parcel.writeInt(peso)
        parcel.writeInt(precio)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Articulo> {
        override fun createFromParcel(parcel: Parcel): Articulo {
            return Articulo(parcel)
        }

        override fun newArray(size: Int): Array<Articulo?> {
            return arrayOfNulls(size)
        }
    }
}
