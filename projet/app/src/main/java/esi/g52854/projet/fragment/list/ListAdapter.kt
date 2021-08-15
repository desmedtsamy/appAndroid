package esi.g52854.projet.fragment.list

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import esi.g52854.projet.MainActivity
import esi.g52854.projet.R
import esi.g52854.projet.Recette
import kotlinx.android.synthetic.main.custom_row.view.*
import java.io.File

class ListAdapter(private val days: Array<String>, private val main : MainActivity, private val navController : NavController): RecyclerView.Adapter<ListAdapter.MyViewHolder>() {
    private var recipeList = emptyList<Recette>()

    class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        return MyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.custom_row, parent, false))
    }

    override fun getItemCount(): Int {

       return recipeList.size
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
            main.recette = currentItem
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

    fun setData(recipe: List<Recette>){
        this.recipeList = recipe
        notifyDataSetChanged()
    }
}