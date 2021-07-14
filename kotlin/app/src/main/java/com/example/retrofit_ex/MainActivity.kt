package com.example.retrofit_ex

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import java.util.*

class MainActivity : AppCompatActivity() {
    var name: String? = null
    var email: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val intent = intent
        name = intent.getStringExtra("name")
        email = intent.getStringExtra("email")
        Log.d("이름", name!!)
        val vp = findViewById<ViewPager>(R.id.viewpager)
        val adapter = VPAdapter(supportFragmentManager)
        vp.adapter = adapter
        // 연동
        val tab = findViewById<TabLayout>(R.id.tab)
        tab.setupWithViewPager(vp)
        val images = ArrayList<Int>()
        images.add(R.drawable.profile)
        images.add(R.drawable.gallery)
        images.add(R.drawable.music)
        for (i in 0..2) tab.getTabAt(i)!!.setIcon(images[i])
    }

    fun returnName(): String? {
        return name
    }

    fun returnEmail(): String? {
        return email
    }
}