package com.example.videotophoto.Adapter

import android.content.Context
import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.videotophoto.R
import java.io.File

class QuickCaptureAdapter(private val listImageList: List<Bitmap>,
                          val context: Context,
                          private val callback: ViewHolder.Callback,
                          private val imageListCut: List<File>)
                        : RecyclerView.Adapter<QuickCaptureAdapter.ViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent?.context).inflate(
                R.layout.item_quick_capture,
                parent,
                false
        )
        return  ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.imgCut.setImageBitmap(listImageList[position])
        holder.itemView.setOnClickListener(View.OnClickListener {
            callback.onClickItem(imageListCut[position].absolutePath) })
    }

    override fun getItemCount(): Int {
        return listImageList.size
    }




    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


           var imgCut:ImageView = itemView.findViewById(R.id.imgCut)

        interface Callback {
            fun onClickItem(path: String?)
        }
    }
}