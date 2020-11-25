package com.example.videotophoto.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.videotophoto.Adapter.GalleryAdapterPager
import com.example.videotophoto.R
import kotlinx.android.synthetic.main.activity_take_a_photo.*

class GalleryActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gallery)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        setUp()
    }




    private fun setUp(){
        var adaptergallery = GalleryAdapterPager(supportFragmentManager)
        viewpager_id.adapter = adaptergallery
        navigation_id.setViewPager(viewpager_id)
        navigation_id.setTitles( resources.getString(R.string.images),resources.getString(R.string.videos))
    }



}