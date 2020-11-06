package com.example.videotophoto.fragments

import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.videotophoto.Adapter.ImagesDateAdapter
import com.example.videotophoto.Model.ImagesDateModel
import com.example.videotophoto.Model.ImagesInsideModel
import com.example.videotophoto.R
import kotlinx.android.synthetic.main.fragment_images.view.*
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class FragmentImages : Fragment() {
    var sp = SimpleDateFormat("dd-MM-yyyy")
    private lateinit var v: View
     var dateFormat:MutableList<String> = mutableListOf()
    var imageList: MutableList<File> = mutableListOf()
    private val dataList = ArrayList<ImagesDateModel>()
    private val imageInside = ArrayList<ImagesInsideModel>()
    private var imageListInside: MutableList<File> = mutableListOf()
    var dateOfImagesConvert = ""
    var date: Date? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        v = inflater.inflate(R.layout.fragment_images, container, false)
        return v
    }

    companion object {

        fun newInstance(): FragmentImages {
            return FragmentImages()
        }
    }

    override fun onResume() {
        super.onResume()
        readImage()

        setUp()
        loadRecyclerView()


    }


    private fun loadRecyclerView() {
        val imageDateAdapter = context?.let { ImagesDateAdapter(dataList, it) }
        val linearLayoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        v.rvDate.layoutManager = linearLayoutManager
        v.rvDate.adapter = imageDateAdapter
        imageDateAdapter!!.notifyDataSetChanged()
    }

private fun setUp(){
        for (i in 0 until dateFormat.size){
            dataList.add(ImagesDateModel(dateFormat[i], imageInside))




        }



      //imageInside.add(ImagesInsideModel()


}


    fun readImage() {
       // imageList.clear()
        val file = File(Environment.getExternalStorageDirectory().absolutePath + "/FrameCaptured")
        if (!file.exists()) {
            file.mkdirs()
        }
        val files = file.listFiles()
        Arrays.sort(files) { o1, o2 -> o2.lastModified().compareTo(o1.lastModified()) }


        for (f in files) {

            if (f.name.endsWith(".jpg") || f.name.endsWith(".png")) {
                imageList.add(f)

                var dateOfImages = f.lastModified()
                 date = Date(dateOfImages)
                dateOfImagesConvert = sp.format(date!!.time)



                dateFormat.add(dateOfImagesConvert)
                val newItems = dateFormat
                newItems.addAll(dateFormat)
                dateFormat = newItems.toSet().toMutableList()







                }


            }


        }

    }





