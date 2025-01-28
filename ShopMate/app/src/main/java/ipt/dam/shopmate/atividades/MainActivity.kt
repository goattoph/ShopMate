package ipt.dam.shopmate.atividades

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import ipt.dam.shopmate.R
import ipt.dam.shopmate.adapter.FragmentAdapter
import ipt.dam.shopmate.adapter.ListsAdapter
import ipt.dam.shopmate.retrofit.RetrofitInitializer
import ipt.dam.shopmate.retrofit.service.UsersPreferences
import ipt.dam.shopmate.models.UsersList
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    // Metodo chamado quando a Atividade é criada
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        // Define o layout
        setContentView(R.layout.activity_main)

        // Configurar o ViewPager2 com o adaptador
        val viewPager = findViewById<ViewPager2>(R.id.view_pager)
        val fragmentAdapter = FragmentAdapter(this)
        viewPager.adapter = fragmentAdapter

        // Associar o TabLayout ao ViewPager2
        val tabLayout = findViewById<TabLayout>(R.id.tab_layout)
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = fragmentAdapter.getTitle(position)
        }.attach()

        val logOutText = findViewById<TextView>(R.id.tvLogOut)
        logOutText.setOnClickListener {
            RetrofitInitializer.usersService.logOut().enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if (response.isSuccessful) {
                        val userPreferences = UsersPreferences(this@MainActivity)
                        userPreferences.clearPreferences()
                        val intent = Intent(this@MainActivity, LoginActivity::class.java)
                        startActivity(intent)
                        Toast.makeText(
                            this@MainActivity,
                            "Sair da conta realizado com sucesso",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        Toast.makeText(
                            this@MainActivity,
                            "Erro ao sair da conta: ${response.message()}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Toast.makeText(this@MainActivity, "Erro ao conectar", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }
}