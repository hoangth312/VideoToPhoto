package com.example.videotophoto.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

import com.example.videotophoto.R
import java.io.File


class FolderVideoAdapter(private val data: MutableList<File>,
                         private val cellClickListener: CellClickListener)
    : RecyclerView.Adapter<FolderVideoAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        val nameFolder: TextView = itemView.findViewById<TextView>(R.id.tv_folderName)


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent?.context).inflate(
                R.layout.item_folder_video_adapter,
                parent,
                false
        )
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val file = File(data[position].toString()).absoluteFile
       
        holder.nameFolder?.text =(file.name + "(" + countFiles(file) + ")")
     

        holder.itemView.setOnClickListener {
            cellClickListener.onClickItem(data[position].absolutePath)
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }
    interface CellClickListener {
        fun onClickItem(path: String)
    }

    private fun countFiles(file: File): Int {
        val list = java.util.ArrayList<File>()
        val f = file.listFiles()
        for (files in f) {
            if (files.name.endsWith(".mp4")) {
                list.add(files)
            }
        }
        return list.size
    }



}