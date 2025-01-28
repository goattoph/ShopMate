package ipt.dam.shopmate.fragmentos

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ipt.dam.shopmate.R
import ipt.dam.shopmate.adapter.ListsAdapter
import ipt.dam.shopmate.atividades.ItemsActivity
import ipt.dam.shopmate.models.UsersList
import ipt.dam.shopmate.retrofit.RetrofitInitializer
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ListFragment : Fragment(R.layout.fragment_list) {
    private lateinit var recyclerView: RecyclerView
    private lateinit var listsAdapter: ListsAdapter
    private var lists: List<UsersList> = mutableListOf()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.recyclerViewLists)
        recyclerView.layoutManager = LinearLayoutManager(context)

        listsAdapter = ListsAdapter(lists) { listId ->
            val intent = Intent(context, ItemsActivity::class.java)
            intent.putExtra("listId", listId)
            startActivity(intent)
        }
        recyclerView.adapter = listsAdapter
        recyclerView.adapter = listsAdapter

        // Recupera as listas do utilizador
        RetrofitInitializer.usersService.getLists().enqueue(object : Callback<List<UsersList>> {
            override fun onResponse(call: Call<List<UsersList>>, response: Response<List<UsersList>>) {
                if (response.isSuccessful) {
                    lists = response.body() ?: mutableListOf()
                    listsAdapter = ListsAdapter(lists) { listId ->
                        val intent = Intent(context, ItemsActivity::class.java)
                        intent.putExtra("listId", listId)
                        startActivity(intent)
                    }
                    recyclerView.adapter = listsAdapter
                    recyclerView.adapter = listsAdapter
                } else {
                    Toast.makeText(context, "Erro ao carregar as listas", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<UsersList>>, t: Throwable) {
                Toast.makeText(context, "Erro ao conectar", Toast.LENGTH_SHORT).show()
            }
        })
    }
}