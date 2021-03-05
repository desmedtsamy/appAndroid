package esi.g52854.projet

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<Button>(R.id.connexion_button).setOnClickListener {
            checkEmail(it)
        }
    }
    private fun checkEmail(view: View) {
        val editText = findViewById<EditText>(R.id.email_edit)
        val test = android.util.Patterns.EMAIL_ADDRESS.matcher(editText.text).matches();
        val message = if(test)  "email valide" else "erreur : email invalide"
        Toast.makeText(getApplicationContext(),message , Toast.LENGTH_SHORT).show();

        // Hide the keyboard.
        val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }
}