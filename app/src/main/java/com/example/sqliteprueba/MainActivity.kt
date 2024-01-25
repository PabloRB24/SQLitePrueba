package com.example.sqliteprueba

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var texto : EditText = findViewById(R.id.textoA)
        var articulo : Articulo = Articulo("Espada","Judas",1,2)
        var mochila = Mochila(20)
        mochila.addArticulo(articulo)

        mochila.getNumeroArt()

        mochila.getArticulos()

    }
}

class Mochila(private var pesoMochila: Int){
    val dbHelper = DatabaseHelper(this)
    fun getNumeroArt():Int{
        var numeroArt = dbHelper.getNumeroArticulos()
        return numeroArt
    }
    fun addArticulo(articulo: Articulo) {

        dbHelper.insertarArticulo(articulo)
    }
    fun findObjeto(nombre: String): Int {
        return dbHelper.findObjeto("Espada")
    }
    fun getArticulos(): ArrayList<Articulo> {
        return dbHelper.getArticulos()
    }

}