package com.example.videotophoto.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.videotophoto.Model.ImagesDateModel
import com.example.videotophoto.R
import kotlinx.android.synthetic.main.item_images_date.view.*
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ImagesDateAdapter(
    private var date: ArrayList<ImagesDateModel>,
    var context: Context,
    private var fileList: MutableList<File>,
    var cellClickListener: ImagesInsideAdapter.CellClickListener
) : RecyclerView.Adapter<ImagesDateAdapter.ViewHolder>() {
    var sp = SimpleDateFormat("dd-MM-yyyy")

    var dateOfImagesConvert = ""

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


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
        var dateModel = date[position]
        var listImages = ArrayList<File>()


        holder.itemView.tv_date.text = date[position].name
        val linearLayoutManager = GridLayoutManager(context, 3)
        for (i in 0 until fileList.size) {
            if (checkDate(fileList[i]) == date[position].name) {
                listImages.add(fileList[i])
            }
        }

        holder.itemView.rvImageDate.layoutManager = linearLayoutManager
        var imageInsideAdapter = ImagesInsideAdapter(context, listImages, cellClickListener)
        imageInsideAdapter.setData(dateModel.imgInSide)
        holder.itemView.rvImageDate.adapter = imageInsideAdapter


    }

    override fun getItemCount(): Int {
        return date.size

    }

    private fun checkDate(file: File): String {
        var dateOfImages = file.lastModified()
        var dates = Date(dateOfImages)
        dateOfImagesConvert = sp.format(dates)
        return dateOfImagesConvert
    }


}


