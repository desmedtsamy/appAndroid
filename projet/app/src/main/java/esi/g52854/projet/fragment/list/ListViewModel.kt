package esi.g52854.projet.fragment.list

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import esi.g52854.projet.Communicator
import esi.g52854.projet.Recette

class ListViewModel : ViewModel(){

    var recettesArray: MutableList<Recette> = mutableListOf()
    private lateinit var db : FirebaseFirestore
    private lateinit var user : String
    private var firstTime : Boolean = true

    fun init(user: String){
        if(firstTime){
            firstTime = false
            db = Firebase.firestore
            this.user = user

            initRecettesArray()
            recettesArray.forEach() {
                Log.i("test42", it.titre)
            }

            Log.i("test42", "------------------")
        }
    }

    fun refresh(){

        db.collection(user)
                .get()
                .addOnSuccessListener { documents ->
                    for (document in documents) {
                        document.reference.delete()
                    }
                    initRecettesArray()
                }
    }
    private fun initRecettesArray(){
        db.collection(user).get().addOnSuccessListener { result ->
            if (result.isEmpty)
                setData("recette")
            else
                setData(user)
        }
    }
    private fun setData(table :String){
        recettesArray = mutableListOf()
        recettesArray.forEach(){
            Log.i("test42",it.titre)}

        Log.i("test42","------------------")
        db.collection(table).get()
                .addOnSuccessListener { result ->
                    if (table == "recette") {
                        docToRecettes( takeRecipe(result))
                        //recettesArray.shuffle()
                        addDB(recettesArray)
                    }else{
                        docToRecettes(result.documents)
                    }

                    result.documents.forEach(){
                        Log.i("test42",it.data?.get("titre").toString())}

                    Log.i("test42","------------------")
                }
    }
    private fun takeRecipe(result: QuerySnapshot): MutableList<DocumentSnapshot> {
        var documentArray:MutableList<DocumentSnapshot> = mutableListOf()
        for (i in 1..7){
            var document = result.documents.random()
            documentArray.add(document)
            result.documents.remove(document)
        }
        return documentArray
    }
    private fun docToRecettes(documents : MutableList<DocumentSnapshot>){
        documents.forEach{
            val titre = it.data?.get("titre") as String
            val time = it.data!!["prepaduration"] as String
            val timeTotal = it.data!!["time"] as String
            val nbPerson = it.data!!["people"] as String
            val difficulty = it.data!!["difficulty"] as String

            val steps = it.data!!["steps"] as List<String>
            val ingredients = it.data!!["ingredients"] as List<String>
            val imageId = it.data!!["imageId"] as String
            recettesArray.add(
                    Recette(
                            it.id,
                            titre,
                            difficulty,
                            nbPerson,
                            time,
                            timeTotal,
                            steps,
                            ingredients,
                            imageId,""
                    )
            )
        }

    }
    private fun addDB(recettesArray : MutableList<Recette>){
        recettesArray.take(7).forEach {
            db.collection(user)
                    .add(it)

        }
    }

}