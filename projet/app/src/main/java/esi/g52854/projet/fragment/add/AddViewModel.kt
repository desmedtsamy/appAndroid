package esi.g52854.projet.fragment.add

import android.content.Intent
import android.net.Uri
import android.widget.EditText
import android.widget.Spinner
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.util.*

class AddViewModel : ViewModel(){

    private var db : FirebaseFirestore = Firebase.firestore
    //image
     var imageId = "default.jpg"
     lateinit var filePath: Uri
     val imageRequest = 71

    fun saveRecipe(difficulty: String, typeStrings: Array<out String>, titre: String,
                   prepaTime: String, time: String, people: String, listSteps: MutableList<EditText>,
                   nbIngredients: Int, listIngredients: MutableList<EditText>,
                   listIngredientsTypes: MutableList<Spinner>,
                   listIngredientsQuantity: MutableList<EditText>): Boolean {


        if(imageId != "default.jpg"){
            uploadImage()
        }
        if(titre.isNotEmpty() || people.isNotEmpty()){
            val liststepsString = mutableListOf<String>()
            listSteps.forEach{
                if(it.text.toString().isNotEmpty())
                    liststepsString.add(it.text.toString())
            }
            val listIngredientsString = mutableListOf<String>()
            for (i in 0 until nbIngredients) {
                if(listIngredientsQuantity[i].text.toString().isNotEmpty() &&
                        listIngredients[i].text.toString().isNotEmpty()){

                    val quantity = listIngredientsQuantity[i].text.toString()
                    val type = typeStrings[listIngredientsTypes[i].selectedItemPosition]
                    val ingredient = listIngredients[i].text.toString()
                    listIngredientsString.add("$quantity $type $ingredient")
                }
            }
            val recette = hashMapOf(
                    "titre" to titre,
                    "prepaduration" to prepaTime,
                    "time" to time,
                    "difficulty" to difficulty,
                    "people" to people,
                    "steps" to liststepsString,
                    "ingredients" to listIngredientsString,
                    "imageId" to imageId
            )
            db.collection("recette")
                    .add(recette)
            return true
        }
        return false
    }
    fun chooseImage(addFragment: AddFragment) {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        addFragment.startActivityForResult(Intent.createChooser(intent, "Select Picture"), imageRequest)

        imageId = UUID.randomUUID().toString()
    }
    private fun uploadImage() {
        val storageReference: StorageReference
        val storage = FirebaseStorage.getInstance()
        storageReference = storage.reference
        val ref: StorageReference =
                storageReference.child("images/$imageId")
        ref.putFile(filePath)

    }
    fun time(time: String): String {
        if (time.isEmpty())
            return "0 minute"
        var tmp = time.toInt()
        var hour = 0
        while(tmp >= 60){
            hour++
            tmp -= 60
        }
        if(hour == 0)
            return "$tmp minutes"
        if(tmp < 10)
            return "$hour h 0$tmp"
        return "$hour h $tmp"

    }
}