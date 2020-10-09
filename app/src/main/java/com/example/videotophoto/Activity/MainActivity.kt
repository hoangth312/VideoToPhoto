package com.example.videotophoto.Activity

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.example.videotophoto.Adapter.HomeAdapter
import com.example.videotophoto.Model.HomeModel
import com.example.videotophoto.R
import com.example.videotophoto.fragments.CustomDialogAbout
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(), HomeAdapter.CellClickListener {
    val dataList = ArrayList<HomeModel>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var navView: BottomNavigationView = findViewById(R.id.nav_view)
        navView.itemIconTintList = null
        setUpView()
        Icon()





        navView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_Share -> {
                    navShare()
                }
                R.id.navigation_RateUS -> {
                    navRateUs()
                }
                R.id.navigation_About -> {
                    navAbout()
                }
            }
            true
        }


    }


    private fun setUpView() {
        recyclerView.layoutManager = GridLayoutManager(this, 2)
        recyclerView.layoutManager
        val homeAPT = HomeAdapter(dataList, this)
        recyclerView.adapter = homeAPT
    }

    private fun Icon() {
        dataList.add(HomeModel("Select Video", R.drawable.photography))
        dataList.add(HomeModel("Gallery", R.drawable.gallery))
        dataList.add(HomeModel("Sideshow Maker", R.drawable.sideshow))
        dataList.add(HomeModel("Setting", R.drawable.settings))
    }


    private fun navShare() {
        intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_SUBJECT, "Subject Her")
        intent.putExtra(Intent.EXTRA_TEXT, "text")
        startActivity(Intent.createChooser(intent, "Share"))
    }


    private fun navRateUs() {
        val uri = Uri.parse("market://details?id=com.android.chrome")
        val uriSnug = Uri.parse("https://play.google.com/store/apps/details?id=com.android.chrome")
        try {
            val rateApp = Intent(Intent.ACTION_VIEW, uri)
            startActivity(rateApp)
        } catch (e: ActivityNotFoundException) {
            val rateApp2 = Intent(Intent.ACTION_VIEW, uriSnug)
            startActivity(rateApp2)
        }
    }

    private fun navAbout() {
        var dialog = CustomDialogAbout()
        dialog.show(supportFragmentManager, "customDialog")
    }

    private fun openDetailActivity(position: Int) = when(position){
        0 -> {
            intent = Intent(this, SelectVideoActivity::class.java)
            startActivity(intent)
        }
        3 -> {
            intent = Intent(this, SettingActivity::class.java)
            startActivity(intent)
        }
        else ->   Toast.makeText(this, "Cell clicked", Toast.LENGTH_SHORT).show()

    }





    override fun onCellClickListener(position: Int, model: HomeModel) {
        openDetailActivity(position)
    }

}



