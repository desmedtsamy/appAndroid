package esi.g52854.projet.fragment.list

import android.app.Activity
import android.content.ContentValues.TAG
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import esi.g52854.projet.Communicator
import esi.g52854.projet.MainActivity
import esi.g52854.projet.R
import esi.g52854.projet.Recette
import kotlinx.android.synthetic.main.custom_row.view.*

class ListAdapter: RecyclerView.Adapter<ListAdapter.MyViewHolder>() {
    private lateinit var model: Communicator
    private var recipeList = emptyList<Recette>()
    private lateinit var navController : NavController
    private lateinit var days: Array<String>
    private lateinit var db : FirebaseFirestore
    private lateinit var user : String

    class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        return MyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.custom_row, parent, false))
    }

    override fun getItemCount(): Int {

       return recipeList.size
    }

    fun init(model : Communicator,navController : NavController,days: Array<String>,user: String){
        db = Firebase.firestore
        this.model = model
        this.navController = navController
        this.days = days
        this.user = user

        initRecettesArray()
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = recipeList[position]
        holder.itemView.repas_txt.text = currentItem.titre
        holder.itemView.difficulty.text = currentItem.difficulty
        holder.itemView.time.text = currentItem.time
        holder.itemView.tag = currentItem.id
        holder.itemView.persons.text = currentItem.people
        holder.itemView.day_txt.text = days[position%days.size]
        holder.itemView.setOnClickListener{
            model.setMsgCommunicator(currentItem)
            this.navController.navigate(R.id.detailFragment)
        }
    }

    private fun setData(recipe: List<Recette>){
        this.recipeList = recipe
        notifyDataSetChanged()
    }

    private fun updateView(recettesArray : MutableList<Recette>){
        setData(recettesArray)

    }
    private fun addDB(recettesArray : MutableList<Recette>){
        Log.i("test42","ajout dans la db "+recettesArray.size)
        recettesArray.take(7).forEach {
            db.collection(user)
                    .add(it)

        }
    }
    fun refresh(){


        db.collection(user)
                .get()
                .addOnSuccessListener { documents ->

                    Log.i("db", documents.size().toString())
                    for (document in documents) {
                        document.reference.delete()
                    }
                    initRecettesArray()
                }
    }
    private fun setData(table :String){
        Log.i("gnee",user.toString())
            val recettesArray = mutableListOf<Recette>()

            db.collection(table).get()
                .addOnSuccessListener { result ->
                    for (document in result.take(7)) {
                        val titre = document.data["titre"] as String
                        val time = document.data["prepaduration"] as String
                        val timeTotal = document.data["time"] as String
                        val nbPerson = document.data["people"] as String
                        val difficulty = document.data["difficulty"] as String

                        val steps = document.data["steps"] as List<String>
                        val ingredients = document.data["ingredients"] as List<String>
                        Log.i("db", titre)
                        recettesArray.add(
                            Recette(
                                document.id,
                                titre,
                                difficulty,
                                nbPerson,
                                time,
                                timeTotal,
                                steps,
                                ingredients
                            )
                        )
                    }
                    if (table == "recette") {
                        recettesArray.shuffle()
                        addDB(recettesArray)
                    }
                    updateView(recettesArray)
                }
                .addOnFailureListener { exception ->
                    Log.d(TAG, "Error getting documents: ", exception)
                }
            }
    private fun initRecettesArray(){
        db.collection(user).get().addOnSuccessListener { result ->
            Log.i("db", result.size().toString())
            if (result.isEmpty)
                setData("recette")
            else
                setData(user)
        }
    }
}