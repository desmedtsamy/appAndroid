package esi.g52854.projet.fragment.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import esi.g52854.projet.R
import esi.g52854.projet.database.User
import kotlinx.android.synthetic.main.custom_row.view.*
import java.text.SimpleDateFormat

class ListAdapter: RecyclerView.Adapter<ListAdapter.MyViewHolder>() {

    private var userList = emptyList<User>()

    class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.custom_row, parent, false))
    }

    override fun getItemCount(): Int {
       return userList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = userList[position]
        holder.itemView.id_txt.text = currentItem.userId.toString()
        holder.itemView.email_txt.text = currentItem.email
        holder.itemView.lastConnection_txt.text = SimpleDateFormat("dd-MMM-yyyy HH:mm")
                .format(currentItem.last_Connection).toString()
    }

    fun setData(user: List<User>){
        this.userList = user
        notifyDataSetChanged()
    }
}