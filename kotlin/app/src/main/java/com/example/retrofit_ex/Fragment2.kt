package com.example.retrofit_ex

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment

class Fragment2 : Fragment() {
    var name: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.activity_chat_main, container, false)
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED
        ) ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
            10
        )
        name = (activity as MainActivity?)!!.returnName()
        Log.d("이름", name!!)
        view.findViewById<View>(R.id.enterBtn)
            .setOnClickListener { v: View? ->
                val intent = Intent(activity, ChatActivity::class.java)
                intent.putExtra("name", name)
                startActivity(intent)
            }
        return view
    }
}