package com.app.catalogouac

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_catalogo.*

class CatalogoActivity : AppCompatActivity() {
    private  val db= FirebaseFirestore.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_catalogo)
        leerDatosProducto()
    }
    private fun leerDatosProducto(){
        title="Catalogo de Productos"
        val listaProductos:MutableList<Producto> = mutableListOf()
        val listaProductos2:MutableList<Producto> = mutableListOf()
        db.collection("producto")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    Log.d("datos", "${document.id} => ${document.data["nombre"]}")
                    val descripcion=document.data["descripcion"].toString()
                    var productos=Producto(document.data["nombre"].toString(),document.data["precio"]
                        .toString(),"${descripcion.substring(0,2)}...",document.data["stock"]
                        .toString(),document.data["imagen"].toString())
                    listaProductos.add(productos)
                    productos=Producto(document.data["nombre"].toString(),document.data["precio"]
                        .toString(),descripcion,document.data["stock"]
                        .toString(),document.data["imagen"].toString())
                    listaProductos2.add(productos)
                }
                val adapter=ProductoAdapter(this,listaProductos)
                listaProductosListView.adapter=adapter
                listaProductosListView.setOnItemClickListener { parent, view, position, id ->
                    val intent=Intent(this,ProductoActivity::class.java)
                    intent.putExtra("producto",listaProductos2[position])
                    startActivity(intent)
                }
            }
            .addOnFailureListener { exception ->
                Log.w("datos", "Error getting documents.", exception)
            }
    }
}
