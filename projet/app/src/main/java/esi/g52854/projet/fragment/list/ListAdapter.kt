package esi.g52854.projet.fragment.list

import android.content.ContentValues.TAG
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import esi.g52854.projet.Communicator
import esi.g52854.projet.R
import esi.g52854.projet.Recette
import kotlinx.android.synthetic.main.custom_row.view.*
import kotlinx.android.synthetic.main.custom_row.view.difficulty
import kotlinx.android.synthetic.main.custom_row.view.time
import java.io.File

class ListAdapter: RecyclerView.Adapter<ListAdapter.MyViewHolder>() {
    private lateinit var recettesArray: MutableList<Recette>
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
            val mStorageReference : StorageReference = FirebaseStorage.getInstance().reference.child("images/"+currentItem.imageId)
            val localFile: File = File.createTempFile(currentItem.imageId,"jpg")
            mStorageReference.getFile(localFile).addOnSuccessListener {
                val bitmap : Bitmap = BitmapFactory.decodeFile(localFile.absolutePath)
                currentItem.imagePath = localFile.absolutePath
                holder.itemView.image_customRow.setImageBitmap(bitmap)
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
        recettesArray.take(7).forEach {
            db.collection(user)
                    .add(it)

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
    private fun setData(table :String){
             recettesArray = mutableListOf()

            db.collection(table).get()
                .addOnSuccessListener { result ->
                    takeRecipe(result)
                    if (table == "recette") {
                        recettesArray.shuffle()
                        addDB(recettesArray)
                    }
                    updateView(recettesArray)
                }
            }
    private fun takeRecipe(result: QuerySnapshot) {
        for (i in 1..7){
            var document = result.documents.random()
            val titre = document.data?.get("titre") as String
            val time = document.data!!["prepaduration"] as String
            val timeTotal = document.data!!["time"] as String
            val nbPerson = document.data!!["people"] as String
            val difficulty = document.data!!["difficulty"] as String

            val steps = document.data!!["steps"] as List<String>
            val ingredients = document.data!!["ingredients"] as List<String>
            val imageId = document.data!!["imageId"] as String
            recettesArray.add(
                    Recette(
                            document.id,
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
            result.documents.remove(document)
        }
        for (document in result.take(7)) {

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
}