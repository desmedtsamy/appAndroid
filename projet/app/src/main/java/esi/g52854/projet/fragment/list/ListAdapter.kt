package esi.g52854.projet.fragment.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import esi.g52854.projet.Communicator
import esi.g52854.projet.R
import esi.g52854.projet.Recette
import kotlinx.android.synthetic.main.custom_row.view.*

class ListAdapter: RecyclerView.Adapter<ListAdapter.MyViewHolder>() {
    private lateinit var model: Communicator
    private var recipeList = emptyList<Recette>()
    private lateinit var navController : NavController
    private lateinit var days: Array<String>
    class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        return MyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.custom_row, parent, false))
    }

    override fun getItemCount(): Int {

       return recipeList.size
    }
    fun setNavController(navController : NavController){
        this.navController = navController
    }
    fun setModel(model : Communicator){
        this.model = model
    }
    fun setDays(days: Array<String>){
        this.days = days
    }
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = recipeList[position]
        holder.itemView.email_txt.text = currentItem.titre
        holder.itemView.difficulty.text = currentItem.difficulty
        holder.itemView.time.text = currentItem.time
        holder.itemView.tag = currentItem.id
        holder.itemView.id_txt.text = days[position%days.size]
        holder.itemView.setOnClickListener{
            model.setMsgCommunicator(currentItem)
            this.navController.navigate(R.id.detailFragment)
        }
    }

    fun setData(recipe: List<Recette>){
        this.recipeList = recipe
        notifyDataSetChanged()
    }
}