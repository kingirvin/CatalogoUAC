package com.app.catalogouac

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_home.*




import java.io.File
import java.util.*

class HomeActivity : AppCompatActivity() {
    //imagen
    private val SELECT_ACTIVITY=50
    private var imageUri: Uri?=null
    private  val db=FirebaseFirestore.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val JPG:Int=0
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
        imagenProductoImageView.setOnClickListener {
            ImageController.selectPhotoFromGallery(this,SELECT_ACTIVITY)
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when{
            requestCode==SELECT_ACTIVITY && resultCode == Activity.RESULT_OK->{
                imageUri=data!!.data
                imagenProductoImageView.setImageURI(imageUri)
            }
        }
    }
    private fun setup(){
        title="Nuevo Producto"
        guardarButton.setOnClickListener {
            uploadImageToFirebaseStorage()
        }
        buscarButton.setOnClickListener {
            val producto=productoEditText.text.toString()
            db.collection("producto").document(producto).get().addOnSuccessListener {
                descripcionEditText.setText(it.get("descripcion")as String?)
                precioEditText.setText(it.get("precio")as String?)
                stockEditText.setText(it.get("stock")as String?)
                Glide.with(this /* context */)
                    .load(it.get("imagen").toString())
                    .into(imagenProductoImageView)
                //Log.d("datos", "${document.id} => ${document.data}")
            }
        }
    }
    private fun uploadImageToFirebaseStorage(){
        if(imageUri==null) return
        val fileName=UUID.randomUUID().toString()
        val ref=FirebaseStorage.getInstance().getReference("images/$fileName")
        ref.putFile(imageUri!!)
            .addOnSuccessListener {
            ref.downloadUrl.addOnSuccessListener {
                Log.d("registe imagen","imagen localizado:$it")
                guardarProducto(it.toString())
                productoEditText.text=null
                descripcionEditText.text= null
                precioEditText.text=null
                stockEditText.text=null
            }
        }
    }
    private fun guardarProducto(imageFile:String){
        db.collection("producto").document(productoEditText.text.toString()).set(
            hashMapOf("nombre" to productoEditText.text.toString(),
                "descripcion" to descripcionEditText.text.toString(),
                "precio" to precioEditText.text.toString(),
                "imagen" to imageFile,
                "stock" to stockEditText.text.toString())
        )
    }
}
