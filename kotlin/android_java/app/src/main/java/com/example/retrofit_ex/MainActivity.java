package com.example.retrofit_ex;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import java.io.InputStream;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    String name;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent=getIntent();
        name=intent.getStringExtra("name");
        Log.d("이름", name);


        ViewPager vp=findViewById(R.id.viewpager);
        VPAdapter adapter=new VPAdapter(getSupportFragmentManager());
        vp.setAdapter(adapter);
        // 연동
        TabLayout tab=findViewById(R.id.tab);
        tab.setupWithViewPager(vp);

        ArrayList<Integer> images=new ArrayList<>();
        images.add(R.drawable.profile);
        images.add(R.drawable.gallery);
        images.add(R.drawable.music);

        for(int i=0;i<3;i++) tab.getTabAt(i).setIcon(images.get(i));
    }
    public String returnName(){
        return name;
    }

}