package ipt.dam.shopmate.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ipt.dam.shopmate.R
import ipt.dam.shopmate.models.UsersList

class ListsAdapter( private val lists: List<UsersList>, private val onListClick: (Int) -> Unit) : RecyclerView.Adapter<ListsAdapter.ListViewHolder>() {

    // Metodo que cria o ViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        // Define o layout
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.activity_item_list, parent, false)
        return ListViewHolder(itemView)
    }

    // Metodo que vincula os dados na lista no ViewHolder
    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val list = lists[position]
        // Define o nome da lista no TextView
        holder.listNameTextView.text = list.listName

        // Adiciona um clique na lista, que chama o onListClick passando-lhe o id da lista
        holder.itemView.setOnClickListener {
            onListClick(list.listId)
        }
    }

    // Metodo que devolve o número de listas
    override fun getItemCount(): Int = lists.size

    // ViewHolder que tem as referências para as views de cada lista
    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val listNameTextView: TextView = itemView.findViewById(R.id.tvListName) // Nome da Lista
    }
}
