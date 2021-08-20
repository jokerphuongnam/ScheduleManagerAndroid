package com.pnam.schedulemanager.ui.main

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class MainFragmentAdapter(
    internal val fragmentActivity: FragmentActivity,
    private val fragments: MutableList<Fragment> = mutableListOf()
) : FragmentStateAdapter(fragmentActivity) {

    /**
     * fragment add fragment and configure for this fragment by fragment will action 1 time
     * */
    fun <Frag : Fragment> addFragment(fragment: Frag, config: (Frag.() -> Unit)? = null) {
        fragments.add(fragment)
        config?.invoke(fragment)
    }

    override fun getItemCount(): Int = fragments.size

    override fun createFragment(position: Int): Fragment = fragments[position]
    fun getItem(position: Int): Fragment = fragments[position]
}