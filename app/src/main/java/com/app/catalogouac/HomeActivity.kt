package com.app.catalogouac

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_home.*

enum class ProviderType{
    BASIC,
    GOOGLE
}
class HomeActivity : AppCompatActivity() {
    private  val db=FirebaseFirestore.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        //setup
        val bundle:Bundle?=intent.extras
        val email:String?=bundle?.getString("email")
        val provider:String?=bundle?.getString("provider")
        setup()
        //GUARDAR DATOS
        val prefs:SharedPreferences.Editor=getSharedPreferences(getString(R.string.prefs_file),Context.MODE_PRIVATE).edit()
        prefs.putString("email",email)
        prefs.putString("provider",provider)
        prefs.apply()
    }
    private fun setup(){
        title="Nuevo Producto"
        val builder= AlertDialog.Builder(this)
        builder.setMessage("mensaje")
        builder.setPositiveButton("Aceptar",null)
        val dialog: AlertDialog =builder.create()
        dialog.show()
        guardarButton.setOnClickListener {
            db.collection("producto").document(productoEditText.text.toString()).set(
                hashMapOf("nombre" to productoEditText.text.toString(),
                "descripcion" to descripcionEditText.text.toString(),
                "precio" to precioEditText.text.toString(),
                "stock" to stockEditText.text.toString())
            )
        }
        buscarButton.setOnClickListener {
            val producto=productoEditText.text.toString()
            db.collection("producto").document(producto).get().addOnSuccessListener {
                descripcionEditText.setText(it.get("descripcion")as String?)
                precioEditText.setText(it.get("precio")as String?)
                stockEditText.setText(it.get("stock")as String?)
            }
        }
    }
}
