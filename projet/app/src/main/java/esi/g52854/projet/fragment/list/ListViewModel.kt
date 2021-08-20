package esi.g52854.projet.fragment.list

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import esi.g52854.projet.MainActivity
import esi.g52854.projet.Recette

class ListViewModel constructor(days: Array<String>,main: MainActivity, navController : NavController)
    : ViewModel(){

    private var recettesArray: MutableList<Recette> = mutableListOf()
    var adapter : ListAdapter = ListAdapter(days,main,navController)
    private var db : FirebaseFirestore = Firebase.firestore
    private val user : String = main.user

    init{
Log.i("test42",user)
        initRecettesArray()
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
        adapter.setData(recettesArray)
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
        db.collection(table).get()
                .addOnSuccessListener { result ->
                    if (table == "recette") {
                        docToRecettes( takeRecipe(result))
                        //recettesArray.shuffle()
                        addDB(recettesArray)
                    }else{
                        docToRecettes(result.documents)
                    }

                    adapter.setData(recettesArray)
                }
    }
    private fun takeRecipe(result: QuerySnapshot): MutableList<DocumentSnapshot> {
        val documentArray:MutableList<DocumentSnapshot> = mutableListOf()
        val array:MutableList<DocumentSnapshot> = result.documents

        array.shuffle()

        for (i in 1..7){
            documentArray.add(array.first())
            array.remove(array.first())
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