package ipt.dam.shopmate.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ipt.dam.shopmate.R
import ipt.dam.shopmate.models.Item

class ItemsAdapter(private var items: List<Item>,
                   private val onDeleteClick: (Int) -> Unit,
                   private val onIsCheckedClick: (Int) -> Unit,
                   private val onEditClick: (Item) -> Unit) : RecyclerView.Adapter<ItemsAdapter.ItemViewHolder>() {

    // Metodo que cria e devolve o ViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        // Define o layout
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_row, parent, false)
        return ItemViewHolder(itemView)
    }

    // Metodo que vai associar os dados do item da lista ao ViewHolder na posição indicada
    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = items[position]
        // Atualiza os elementos com os dados do item
        holder.itemNameTextView.text = item.itemName // Nome
        Glide.with(holder.itemView.context)
            .load("https://my-favorite-things.azurewebsites.net/Imagens/${item.image}")
            .into(holder.itemImageView) // Imagem
        //holder.priceTextView.text = "Preço: ${item.price}€" // Preço
        holder.amountTextView.text = "Quantidade: ${item.amount}" // Quantidade
        holder.isCheckedTextView.isChecked = item.isChecked  // Se o item está ou não checked

        // Adiciona um clique na imageview de apagar que chama o onDeleteClick, passando-lhe o id do item
        holder.deleteButton.setOnClickListener {
            onDeleteClick(item.itemId)
        }

        // Adiciona um clique na checkbox que chama o onIsCheckedClick, passando-lhe o id do item
        holder.isCheckedTextView.setOnClickListener {
            onIsCheckedClick(item.itemId)
        }

        // Adiciona um clique imageview de editar que chama o onEditClick, passando-lhe o item completo
        holder.editButton.setOnClickListener {
            onEditClick(item)
        }
    }

    // Metodo que devolve o número de itens da lista
    override fun getItemCount(): Int = items.size

    // Metodo para atualizar a lista de itens, e atualizar a interface
    fun updateItems(newItems: List<Item>) {
        items = newItems
        notifyDataSetChanged()
    }

    // ViewHolder que tem as referências para as views de cada item
    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val itemNameTextView: TextView = itemView.findViewById(R.id.tvItemName) // Nome
        val itemImageView: ImageView = itemView.findViewById(R.id.ivListImage) // Imagem
        val amountTextView: TextView = itemView.findViewById(R.id.tvAmount) // Quantidade
        val isCheckedTextView : CheckBox = itemView.findViewById(R.id.tvChecked) // Se o item está ou não checked
        val deleteButton: ImageView = itemView.findViewById(R.id.btnDeleteItem) // ImageView de apagar um item
        val editButton: ImageView = itemView.findViewById(R.id.btnEditItem) // ImageView para editar um item
    }
}