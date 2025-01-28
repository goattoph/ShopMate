package ipt.dam.shopmate.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ipt.dam.shopmate.R
import ipt.dam.shopmate.models.Item

class ItemsAdapter(private var items: List<Item>) : RecyclerView.Adapter<ItemsAdapter.ItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_row, parent, false)
        return ItemViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = items[position]
        holder.itemNameTextView.text = item.itemName
        Glide.with(holder.itemView.context)
            .load("https://my-favorite-things.azurewebsites.net/Imagens/${item.image}")
            .into(holder.itemImageView)
        holder.priceTextView.text = "Preço: ${item.price}€"
        holder.amountTextView.text = "Quantidade: ${item.amount}"
        holder.isCheckedTextView.text = "Feito: ${item.isChecked}"
    }

    override fun getItemCount(): Int = items.size

    fun updateItems(newItems: List<Item>) {
        items = newItems
        notifyDataSetChanged()
    }

    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val itemNameTextView: TextView = itemView.findViewById(R.id.tvItemName)
        val itemImageView: ImageView = itemView.findViewById(R.id.ivListImage)
        val priceTextView: TextView = itemView.findViewById(R.id.tvPrice)
        val amountTextView: TextView = itemView.findViewById(R.id.tvAmount)
        val isCheckedTextView : TextView = itemView.findViewById(R.id.tvChecked)
    }
}