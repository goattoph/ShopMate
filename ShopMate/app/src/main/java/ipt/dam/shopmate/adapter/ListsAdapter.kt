package ipt.dam.shopmate.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ipt.dam.shopmate.R
import ipt.dam.shopmate.models.UsersList

class ListsAdapter(
    private val lists: List<UsersList>,
    private val onListClick: (Int) -> Unit
) : RecyclerView.Adapter<ListsAdapter.ListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.activity_item_list, parent, false)
        return ListViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val list = lists[position]
        holder.listNameTextView.text = list.listName

        holder.itemView.setOnClickListener {
            onListClick(list.listId)
        }
    }

    override fun getItemCount(): Int = lists.size

    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val listNameTextView: TextView = itemView.findViewById(R.id.tvListName)
    }
}
