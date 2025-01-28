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
    // RecyclerView para mostrar os itens da lista
    private lateinit var recyclerView: RecyclerView
    // Adapter que liga os itens ao RecyclerView
    private lateinit var itemsAdapter: ItemsAdapter
    private var items: List<Item> = mutableListOf()

    // Metodo chamado quando a Atividade é criada
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Define o layout da Atividade
        setContentView(R.layout.activity_items)

        // Inicializa o RecyclerView
        recyclerView = findViewById(R.id.recyclerViewItems)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Inicializa e associa o Adapter ao RecyclerView
        itemsAdapter = ItemsAdapter(items)
        recyclerView.adapter = itemsAdapter

        // Obter o listId
        val listId = intent.getIntExtra("listId", -1)

        // Verifica se o listId é válido ( -1 Valor padrão quando passado por Intent)
        if (listId != -1) {
            // Buscar os itens da lista
            RetrofitInitializer.usersService.getItems(listId).enqueue(object : Callback<List<Item>> {
                // Metodo se a requisição for bem sucedida
                override fun onResponse(call: Call<List<Item>>, response: Response<List<Item>>) {
                    if (response.isSuccessful) {
                        // Atualiza a lista de itens com os dados
                        items = response.body() ?: mutableListOf()
                        itemsAdapter.updateItems(items)
                    } else {
                        Toast.makeText(this@ItemsActivity, "Erro ao carregar os itens", Toast.LENGTH_SHORT).show()
                    }
                }
                // Metodo chamado em caso de erro
                override fun onFailure(call: Call<List<Item>>, t: Throwable) {
                    Toast.makeText(this@ItemsActivity, "Erro ao conectar", Toast.LENGTH_SHORT).show()
                }
            })
            // Caso o listId seja inválido
        } else {
            Toast.makeText(this, "ID da lista inválido", Toast.LENGTH_SHORT).show()
        }
    }
}
