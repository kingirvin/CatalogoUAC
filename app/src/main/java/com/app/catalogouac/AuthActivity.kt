package com.app.catalogouac

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_auth.*

class AuthActivity : AppCompatActivity() {
    private val GOOGLE_SIGN_IN=100
    private val imageUri:Uri?=null
    private lateinit var firebaseAnalytics: FirebaseAnalytics
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)
        firebaseAnalytics = Firebase.analytics
        val analytics:FirebaseAnalytics=FirebaseAnalytics.getInstance(this)
        val bundle=Bundle()
        bundle.putString("message","integracion de firebase completa")
        analytics.logEvent("InitScreen",bundle)
        //setup
        setup()
        session()
        }

    override fun onStart() {
        super.onStart()
        authLayout.visibility=View.VISIBLE
    }
    private  fun session(){
        val prefs: SharedPreferences=getSharedPreferences(getString(R.string.prefs_file),
            Context.MODE_PRIVATE)
        val email:String?=prefs.getString("email",null)
        val provider:String?=prefs.getString("provider",null)
        if (email!=null&& provider!=null){
            authLayout.visibility= View.INVISIBLE
            showHome()
        }

    }
    private fun setup(){
        accederButton.setOnClickListener {
            if (emailEditText.text.isNotEmpty()&& passwordEditText.text.isNotEmpty()){
                FirebaseAuth.getInstance()
                    .signInWithEmailAndPassword(emailEditText.text.toString(),passwordEditText.text.toString()).addOnCompleteListener {
                        if (it.isSuccessful){
                            showHome()
                        }else{
                            showAlert("su email o contraseña no son correctos")
                        }
                    }
            }
        }
        catalogoButton.setOnClickListener {
            val homeIntent:Intent=Intent(this,CatalogoActivity::class.java)
            startActivity(homeIntent)
        }

    }
    private fun showAlert(mensaje:String){
        val builder=AlertDialog.Builder(this)
        builder.setMessage(mensaje)
        builder.setPositiveButton("Aceptar",null)
        val dialog:AlertDialog=builder.create()
        dialog.show()
    }
    private fun showHome(){
        val homeIntent:Intent=Intent(this,HomeActivity::class.java)
        startActivity(homeIntent)
    }

}
