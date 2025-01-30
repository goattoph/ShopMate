package ipt.dam.shopmate.atividades


import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ipt.dam.shopmate.R
import ipt.dam.shopmate.adapter.ItemsAdapter
import ipt.dam.shopmate.models.Item
import ipt.dam.shopmate.retrofit.RetrofitInitializer
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

//usámos chatgpt no desenvolvimento da classe
class ItemsActivity : AppCompatActivity() {
    // RecyclerView para mostrar as listas
    private lateinit var recyclerView: RecyclerView

    // Adapter que liga as listas ao RecyclerView
    private lateinit var itemsAdapter: ItemsAdapter
    private var items: List<Item> = mutableListOf()

    // Metodo chamado quando a Atividade é criada
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Define o layout
        setContentView(R.layout.activity_items)

        // Configura o  RecyclerView
        recyclerView = findViewById(R.id.recyclerViewItems)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Passa a função deleteItem para o adapter
        itemsAdapter = ItemsAdapter(items, { itemId ->
            // Função de apagar um item
            deleteItem(itemId)
        })

        recyclerView.adapter = itemsAdapter

        // Ir buscar o listId através do Intent
        val listId = intent.getIntExtra("listId", -1)

        // Botão para abrir a atividade de criar um item
        val btnCreateItem = findViewById<Button>(R.id.btnCreateItem)
        btnCreateItem.setOnClickListener {
            val intent = Intent(this@ItemsActivity, CreateItemActivity::class.java)
            intent.putExtra("listId", listId)
            startActivity(intent)
        }

        val btnCancelItem = findViewById<Button>(R.id.btnBackItem)
        btnCancelItem.setOnClickListener {
            finish()
        }

        // Carregar os itens da lista
        loadItems()
    }

    // Função para atualizar os itens
    private fun loadItems() {
        // Usa o intent para ir buscar o id da lista
        val listId = intent.getIntExtra("listId", -1)
        // Verifica se o listId é válido ( -1 Valor padrão quando passado por Intent)
        if (listId != -1) {
            RetrofitInitializer.usersService.getItems(listId)
                .enqueue(object : Callback<List<Item>> {
                    // Metodo se a requisição for bem sucedida
                    override fun onResponse(
                        call: Call<List<Item>>,
                        response: Response<List<Item>>
                    ) {
                        if (response.isSuccessful) {
                            // Atualiza a lista de itens com os dados
                            items = response.body() ?: mutableListOf()
                            // Ordenar por ordem de última criação ( maior id )
                            items = items.sortedByDescending { it.itemId.toInt() }
                            itemsAdapter.updateItems(items)
                        } else {
                            Toast.makeText(
                                this@ItemsActivity,
                                "Erro ao carregar os itens",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                    // Metodo chamado em caso de erro
                    override fun onFailure(call: Call<List<Item>>, t: Throwable) {
                        Toast.makeText(this@ItemsActivity, "Erro ao conectar", Toast.LENGTH_SHORT)
                            .show()
                    }
                })
            // Caso o listId seja inválido
        } else {
            Toast.makeText(this, "ID da lista inválido", Toast.LENGTH_SHORT).show()
        }
    }

    // Função para eliminar um item
    private fun deleteItem(itemId: Int) {
        val context = this
        // Cria um AlertDialog para confirmar a exclusão
        val alertDialog = androidx.appcompat.app.AlertDialog.Builder(context)
            .setTitle("Confirmar exclusão!")
            .setMessage("Tem a certeza de que deseja apagar este item?")
            .setPositiveButton("Sim") { dialog, _ ->
                // Se sim, apaga o item especificado
                RetrofitInitializer.usersService.deleteItem(itemId)
                    .enqueue(object : Callback<Void> {
                        override fun onResponse(call: Call<Void>, response: Response<Void>) {
                            if (response.isSuccessful) {
                                Toast.makeText(
                                    context,
                                    "Item apagado com sucesso",
                                    Toast.LENGTH_SHORT
                                ).show()
                                // Atualiza os items após a exclusão
                                loadItems()
                            } else {
                                // Caso haja erro ao apagar o item
                                Toast.makeText(context, "Erro ao apagar o item", Toast.LENGTH_SHORT)
                                    .show()
                            }
                        }
                        // Em caso de falha na requisição
                        override fun onFailure(call: Call<Void>, t: Throwable) {
                            Toast.makeText(context, "Erro ao conectar", Toast.LENGTH_SHORT).show()
                        }
                    })
                // Fecha o dialog após a confirmação
                dialog.dismiss()
            }
            // Se não, fecha o dialog
            .setNegativeButton("Não") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
        alertDialog.show()
    }

    // Metodo para recarregar os items quando voltamos a esta atividade
    override fun onResume() {
        super.onResume()
        // Recarrega as listas
        loadItems()
    }
}
