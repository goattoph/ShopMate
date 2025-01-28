package ipt.dam.shopmate.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import ipt.dam.shopmate.fragmentos.CreatorsFragment
import ipt.dam.shopmate.fragmentos.ListFragment


class FragmentAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {
    private val fragmentList = listOf(
        ListFragment(),       // Primeiro fragmento
        CreatorsFragment()    // Segundo fragmento
    )
    private val fragmentTitles = listOf("Listas", "Criadores")

    override fun getItemCount(): Int = fragmentList.size

    override fun createFragment(position: Int): Fragment = fragmentList[position]

    fun getTitle(position: Int): String = fragmentTitles[position]
}
