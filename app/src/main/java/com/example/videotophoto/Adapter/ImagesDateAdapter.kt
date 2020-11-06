package com.example.videotophoto.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.videotophoto.Model.ImagesDateModel
import com.example.videotophoto.R
import com.example.videotophoto.fragments.FragmentImages
import kotlinx.android.synthetic.main.item_images_date.view.*
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ImagesDateAdapter(private var date: ArrayList<ImagesDateModel>,var  context: Context) : RecyclerView.Adapter<ImagesDateAdapter.ViewHolder>() {

    var test = FragmentImages()
    var arrayFile = ArrayList<File>()
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        //  val name: TextView = itemView.findViewById(R.id.tv_date)

    }

    fun setData(list: ArrayList<ImagesDateModel>) {
        this.date = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent?.context).inflate(
                R.layout.item_images_date,
                parent,
                false
        )
        return ViewHolder(view)

    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var imageDate: ImagesDateModel = date[position]
        holder.itemView.tv_date.text = date[position].name



        holder.itemView.rvImageDate.layoutManager = GridLayoutManager(context, 3)
       //holder.itemView.rvImageDate.layoutManager



       var imageInsideAdapter =  ImagesInsideAdapter(arrayFile)
//        imageInsideAdapter.setData(imageDate.imgInSide)


        holder.itemView.rvImageDate.adapter = imageInsideAdapter

    }

    override fun getItemCount(): Int {
        return date.size

    }
    fun getDate(path: String): String? {
        val file = File(path)
        val lastModDate = Date(file.lastModified())
        val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy")
        return simpleDateFormat.format(lastModDate.time)



    }


    }
