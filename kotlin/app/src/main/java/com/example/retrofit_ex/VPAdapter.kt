package com.example.retrofit_ex

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import java.util.*

class VPAdapter(fm: FragmentManager?) : FragmentPagerAdapter(
    fm!!
) {
    private val items: ArrayList<Fragment>
    private val itext = ArrayList<String>()
    override fun getPageTitle(position: Int): CharSequence? {
        return itext[position]
    }

    override fun getItem(position: Int): Fragment {
        return items[position]
    }

    override fun getCount(): Int {
        return items.size
    }

    init {
        items = ArrayList()
        items.add(Fragment1())
        items.add(Fragment2())
        items.add(Fragment3())
        itext.add("프로필")
        itext.add("채팅")
        itext.add("커뮤니티")
    }
}