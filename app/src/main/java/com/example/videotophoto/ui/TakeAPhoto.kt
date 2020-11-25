package com.example.videotophoto.ui

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.example.videotophoto.Adapter.TakeAPhotoAdapterPager
import com.example.videotophoto.R
import com.example.videotophoto.fragments.FragmentAutoShotting
import com.example.videotophoto.fragments.FragmentQuickCapture
import com.gigamole.navigationtabstrip.NavigationTabStrip
import kotlinx.android.synthetic.main.activity_take_a_photo.*

class TakeAPhoto : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_take_a_photo)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        setUp()
    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.toolbar, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.btnDone -> {
                val intent = Intent(this, GalleryActivity::class.java)
                this.startActivity(intent)
                finish()
                true

            }
            else -> super.onOptionsItemSelected(item)
        }


    }


    private fun setUp(){
      var adapterTakeaPhoto = TakeAPhotoAdapterPager(supportFragmentManager)
        viewpager_id.adapter = adapterTakeaPhoto
        navigation_id.setViewPager(viewpager_id)
        navigation_id.setTitles(resources.getString(R.string.quickcapture), resources.getString(R.string.automatic))
    }



}