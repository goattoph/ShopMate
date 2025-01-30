package ipt.dam.shopmate.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import ipt.dam.shopmate.fragmentos.CreatorsFragment
import ipt.dam.shopmate.fragmentos.ListFragment
import ipt.dam.shopmate.fragmentos.UserFragment


class FragmentAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {
    // Lista de fragmentos que vão ser exibidos no ViewPager2
    private val fragmentList = listOf(
        ListFragment(),       // Primeiro fragmento
        UserFragment(),       // Segundo fragmento
        CreatorsFragment()    // Terceiro fragmento
    )
    // Lista de títulos dos fragmentos
    private val fragmentTitles = listOf("Listas", "Perfil","Criadores")

    // Metodo que devolve o número de fragmentos a serem exibidos
    override fun getItemCount(): Int = fragmentList.size

    // Metodo que cria o fragmento consoante a posição indicada
    override fun createFragment(position: Int): Fragment = fragmentList[position]

    // Metodo que devolve o título correspondente ao fragmento consoante a posição
    fun getTitle(position: Int): String = fragmentTitles[position]
}
