package com.example.videotophoto.Adapter

import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.videotophoto.R
import kotlinx.android.synthetic.main.item_image_inside_adapter.view.*
import java.io.File


class ImagesInsideAdapter(private var data: ArrayList<File>) : RecyclerView.Adapter<ImagesInsideAdapter.ViewHolder>() {


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }

    fun setData(list: ArrayList<File>) {
        this.data = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent?.context).inflate(
                R.layout.item_image_inside_adapter,
                parent,
                false
        )
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val options = BitmapFactory.Options()
        options.outWidth = 100
        options.outHeight = 100
        val bitmap = BitmapFactory.decodeFile(data[position].toString(), options)
        holder.itemView.ivPhoto.setImageBitmap(bitmap)

    }

    override fun getItemCount(): Int {

        return data.size


    }


}


