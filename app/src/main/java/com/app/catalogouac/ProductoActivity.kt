package com.app.catalogouac

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.activity_producto.*

class ProductoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_producto)
        mostrarProducto()
    }
    private fun mostrarProducto(){
        title="Producto"
        val producto=intent.getSerializableExtra("producto") as Producto
        descripcionTextView.text=producto.descripcion
        precioTextView.text="Precio: ${producto.precio}"
        stockTextView.text="Stock : ${producto.stock}"
        nombreProductoTextView.text="Nombre : ${producto.nombre}"
        Glide.with(this /* context */)
            .load(producto.imagen)
            .into(productoImagenimageView)
    }
}
