package com.app.catalogouac
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.activity_home.view.*
import kotlinx.android.synthetic.main.item_producto.view.*

class ProductoAdapter(private val mContext:Context, private val listaProductos:List<Producto>):ArrayAdapter<Producto>(mContext,0,listaProductos) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val layout=LayoutInflater.from(mContext).inflate(R.layout.item_producto,parent,false)
        val producto=listaProductos[position]
        layout.nombreProductoTextView.text="nombre:  ${producto.nombre}"
        layout.precioProductoTextView.text="Precio: $${producto.precio}"
        layout.descripcionProductotextView.text=producto.descripcion
        layout.stockProductoTextView.text="Stock: ${producto.stock}"
        Glide.with(mContext)
            .load(producto.imagen)
            .into(layout.productoImageView)
        return layout
    }
}