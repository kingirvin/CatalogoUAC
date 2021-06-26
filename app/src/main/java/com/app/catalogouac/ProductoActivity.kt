package com.app.catalogouac

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_producto.*

class ProductoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_producto)
        val producto=intent.getSerializableExtra("producto") as Producto
        descripcionTextView.text=producto.descripcion
        precioTextView.text=producto.precio
        stockTextView.text=producto.stock
        nombreProductoTextView.text=producto.nombre
    }
}
