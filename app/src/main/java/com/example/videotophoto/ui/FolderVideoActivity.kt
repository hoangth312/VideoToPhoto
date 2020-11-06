package com.example.videotophoto.ui

import android.content.Intent
import android.os.Bundle
import android.os.Environment
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.example.videotophoto.Adapter.FolderVideoAdapter
import com.example.videotophoto.R
import kotlinx.android.synthetic.main.activity_select_video.*
import java.io.File


class FolderVideoActivity : AppCompatActivity(), FolderVideoAdapter.CellClickListener {

    var videoPath: MutableList<File> = mutableListOf()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_video)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        setUpView()

    }

    private fun setUpView() {
        var myVideo = ListDir(Environment.getExternalStorageDirectory())
        var videoPathList = videoPath.toSet().toList()

        var fordelAdapter = FolderVideoAdapter(videoPathList as MutableList<File>, this)


        recyclerViewFolderVideo.layoutManager = GridLayoutManager(this, 2)
        recyclerViewFolderVideo.layoutManager
        recyclerViewFolderVideo.adapter = fordelAdapter
    }


    fun ListDir(f: File): ArrayList<File> {
        val temp = ArrayList<File>()
        val files = f.listFiles()
        for (file in files) {
            if (file.isDirectory) {
                temp.addAll(ListDir(file)!!)
            }
            if (file.name.endsWith(".mp4")) {
                videoPath.add(File(file.parent))
                temp.add(file)
            }
        }
        return temp
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }


    override fun onClickItem(path: String) {
        val intent = Intent(this, ListVideoActivity::class.java)
        intent.putExtra("PATH", path)
        startActivity(intent)
    }

}
























