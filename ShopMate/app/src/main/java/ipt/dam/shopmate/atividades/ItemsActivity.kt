package ipt.dam.shopmate.atividades

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ipt.dam.shopmate.R
import ipt.dam.shopmate.adapter.ItemsAdapter
import ipt.dam.shopmate.models.Item
import ipt.dam.shopmate.retrofit.RetrofitInitializer
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
//import android.webkit.CookieManager

//usámos chatgpt no desenvolvimento da classe
class ItemsActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var itemsAdapter: ItemsAdapter
    private var items: List<Item> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_items)

        recyclerView = findViewById(R.id.recyclerViewItems)
        recyclerView.layoutManager = LinearLayoutManager(this)

        itemsAdapter = ItemsAdapter(items)
        recyclerView.adapter = itemsAdapter

        // Obter o listId
        val listId = intent.getIntExtra("listId", -1)

        if (listId != -1) {
            // Buscar os itens da lista
            RetrofitInitializer.usersService.getItems(listId).enqueue(object : Callback<List<Item>> {
                override fun onResponse(call: Call<List<Item>>, response: Response<List<Item>>) {
                    if (response.isSuccessful) {
                        items = response.body() ?: mutableListOf()
                        itemsAdapter.updateItems(items)
                    } else {
                        Toast.makeText(this@ItemsActivity, "Erro ao carregar os itens", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<List<Item>>, t: Throwable) {
                    Toast.makeText(this@ItemsActivity, "Erro ao conectar", Toast.LENGTH_SHORT).show()
                }
            })
        } else {
            Toast.makeText(this, "ID da lista inválido", Toast.LENGTH_SHORT).show()
        }
    }
}
