package com.example.videotophoto.ui


import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.example.videotophoto.Adapter.ListVideoAdapter
import com.example.videotophoto.R
import kotlinx.android.synthetic.main.activity_list_video.*
import java.io.File
import java.util.*

class ListVideoActivity : AppCompatActivity(), ListVideoAdapter.Callback{
    var lisVideoAdapter: ListVideoAdapter? = null
    var video = ArrayList<File>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_video)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        var intent = intent
        var pathFolder: String? = intent.getStringExtra("PATH")
        var file = File(pathFolder)
        file = File(file.path)
       video = getVideo(file)
        lisVideoAdapter = ListVideoAdapter(video, this)
        recyclerViewListVideo.layoutManager = GridLayoutManager(this, 3)
        recyclerViewListVideo.layoutManager
        recyclerViewListVideo.adapter = lisVideoAdapter
        recyclerViewListVideo.isNestedScrollingEnabled = false





    }

    fun getVideo(f: File): ArrayList<File> {
        val temp = ArrayList<File>()
        val files: Array<File> = f.listFiles()      
        for (f in files) {
            if (f.name.endsWith(".mp4")) {
                temp.add(f)
            }
        }
        return temp
    }

    override fun onSupportNavigateUp(): Boolean {
       onBackPressed()
        return true
    }


    override fun onClickItem(path: String?) {


        val intent = Intent(this, TakeAPhoto::class.java)
        intent.putExtra("VIDEO PATH", path)
        startActivity(intent)
    }


}