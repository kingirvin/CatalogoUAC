package com.app.catalogouac

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
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
            showHome(email,ProviderType.valueOf(provider))
        }

    }
    private fun setup(){
        registrarButton.setOnClickListener {
            if (emailEditText.text.isNotEmpty()&& passwordEditText.text.isNotEmpty()){
                FirebaseAuth.getInstance()
                    .createUserWithEmailAndPassword(emailEditText.text.toString(),passwordEditText.text.toString()).addOnCompleteListener {
                        if (it.isSuccessful){
                            showHome(it.result?.user?.email?:"",ProviderType.BASIC)
                        }else{
                            showAlert("Email ya registrado")
                        }
                    }
            }
        }
        accederButton.setOnClickListener {
            if (emailEditText.text.isNotEmpty()&& passwordEditText.text.isNotEmpty()){
                FirebaseAuth.getInstance()
                    .signInWithEmailAndPassword(emailEditText.text.toString(),passwordEditText.text.toString()).addOnCompleteListener {
                        if (it.isSuccessful){
                            showHome(it.result?.user?.email?:"",ProviderType.BASIC)
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode==GOOGLE_SIGN_IN){
            val task=GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val accont=task.getResult(ApiException::class.java)
                if (accont!=null){
                    val credencial=GoogleAuthProvider.getCredential(accont.idToken,null)
                    FirebaseAuth.getInstance().signInWithCredential(credencial).addOnCompleteListener {
                        if (it.isSuccessful){
                            showHome(accont.email?:"",ProviderType.GOOGLE)
                        }else{
                            showAlert("su email o contraseña no son correctos")
                        }
                    }
                }
            }catch (e:ApiException){
                showAlert("Error al inciciar sesión")
            }
        }
    }
    private fun showAlert(mensaje:String){
        val builder=AlertDialog.Builder(this)
        builder.setMessage(mensaje)
        builder.setPositiveButton("Aceptar",null)
        val dialog:AlertDialog=builder.create()
        dialog.show()
    }
    private fun showHome(email:String, provider:ProviderType){
        val homeIntent:Intent=Intent(this,HomeActivity::class.java).apply {
            putExtra("email",email)
            putExtra("provider",provider)
        }
        startActivity(homeIntent)
    }

}
