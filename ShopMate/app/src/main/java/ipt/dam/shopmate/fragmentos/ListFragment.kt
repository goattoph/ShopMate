package ipt.dam.shopmate.fragmentos

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ipt.dam.shopmate.R
import ipt.dam.shopmate.adapter.ListsAdapter
import ipt.dam.shopmate.atividades.CreateListActivity
import ipt.dam.shopmate.atividades.ItemsActivity
import ipt.dam.shopmate.models.UsersList
import ipt.dam.shopmate.retrofit.RetrofitInitializer
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ListFragment : Fragment(R.layout.fragment_list) {
    // RecyclerView para mostrar as listas
    private lateinit var recyclerView: RecyclerView
    // Adapter que liga as listas ao RecyclerView
    private lateinit var listsAdapter: ListsAdapter
    private var lists: List<UsersList> = mutableListOf()

    // Metodo chamado quando o view do fragmento é criado
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Configura o  RecyclerView
        recyclerView = view.findViewById(R.id.recyclerViewLists)
        recyclerView.layoutManager = LinearLayoutManager(context)

        // Inicializa o adaptador
        listsAdapter = ListsAdapter(lists) { listId ->
            val intent = Intent(context, ItemsActivity::class.java)
            intent.putExtra("listId", listId)
            startActivity(intent)
        }
        recyclerView.adapter = listsAdapter

        // Carregar as listas do utilizador autenticado
        loadLists()

        // Botão para abrir a atividade de criar uma lista
        view.findViewById<View>(R.id.btnCreateList).setOnClickListener {
            val intent = Intent(context, CreateListActivity::class.java)
            startActivity(intent)
        }
    }

    // Função para atualizar as listas
    private fun loadLists() {
        RetrofitInitializer.usersService.getLists().enqueue(object : Callback<List<UsersList>> {
            override fun onResponse(call: Call<List<UsersList>>, response: Response<List<UsersList>>) {
                if (response.isSuccessful) {
                    lists = response.body() ?: mutableListOf()
                    // Ordenar as listas por número de id (mais recente maior id)
                    lists = lists.sortedByDescending { it.listId.toInt() }
                    // Atualiza o Adapter com as novas listas
                    listsAdapter = ListsAdapter(lists) { listId ->
                        // Ação quando se clica numa lista, navega para a atividade de mostrar itens
                        val intent = Intent(context, ItemsActivity::class.java)
                        intent.putExtra("listId", listId)
                        startActivity(intent)
                    }
                    recyclerView.adapter = listsAdapter
                } else {
                    Toast.makeText(context, "Erro ao carregar as listas", Toast.LENGTH_SHORT).show()
                }
            }
            // Metodo chamado em caso de erro
            override fun onFailure(call: Call<List<UsersList>>, t: Throwable) {
                Toast.makeText(context, "Erro ao conectar", Toast.LENGTH_SHORT).show()
            }
        })
    }

    // Metodo chamado sempre que o fragmento é retomado
    override fun onResume() {
        super.onResume()
        // Recarrega as listas
        loadLists()
    }
}