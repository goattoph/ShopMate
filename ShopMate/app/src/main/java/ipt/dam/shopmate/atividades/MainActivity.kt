package ipt.dam.shopmate.atividades

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import ipt.dam.shopmate.R
import ipt.dam.shopmate.adapter.FragmentAdapter

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
    }
}