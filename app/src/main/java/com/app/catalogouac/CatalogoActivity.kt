package com.app.catalogouac

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_catalogo.*

class CatalogoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_catalogo)
        val arrayAdapter:ArrayAdapter<*>
        val producto=Producto("produc1","25","dgfsdgsgsdjh","20",R.drawable.image1)
        val producto1=Producto("produc2","225","dgfsd43433gsgsdjh","220",R.drawable.image1)
        val producto2=Producto("produc3","235","dgfsd55gsgsdjh","230",R.drawable.image1)
        val listaProducto= listOf(producto,producto1,producto2)
        val adapter=ProductoAdapter(this,listaProducto)
        listaProductosListView.adapter=adapter
        listaProductosListView.setOnItemClickListener { parent, view, position, id ->
            val intent=Intent(this,ProductoActivity::class.java)
            intent.putExtra("producto",listaProducto[position])
            startActivity(intent)
        }
/*
        val productos= arrayListOf("nombrebnd","precio","sdgfa","fdfgsghgjh")
        val lvDatos=findViewById<ListView>(R.id.listaProductosListView)
        arrayAdapter=ArrayAdapter(this,android.R.layout.simple_list_item_1,productos)
        lvDatos.adapter=arrayAdapter
        lvDatos.setOnItemClickListener(){parent, view, position, id ->
            Toast.makeText(this,parent.getItemAtPosition(position).toString(),Toast.LENGTH_LONG).show()
        }*/
    }
}
