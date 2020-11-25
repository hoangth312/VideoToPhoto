package com.example.videotophoto.Adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.videotophoto.Model.ListSizeCropModel
import com.example.videotophoto.R
import kotlinx.android.synthetic.main.item_crop_size.view.*

class CropImageAdapter(private var sizeCropList: ArrayList<ListSizeCropModel>,
                       private var callback: Callback) : RecyclerView.Adapter<CropImageAdapter.ViewHolder>(){


    var selectedPosition = -1


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_crop_size, parent, false)
        return ViewHolder(view)
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if(selectedPosition==position)
            holder.itemView.tvSizeCropImage.setTextColor(Color.parseColor("#1D89DF"))
        else
            holder.itemView.tvSizeCropImage.setTextColor(Color.parseColor("#000000"));

        var size: ListSizeCropModel = sizeCropList[position]

        holder.itemView.tvSizeCropImage.text = sizeCropList[position].name
        holder.itemView.tvSizeCropImage.setOnClickListener{
            callback.onClickItem(position, size)
            selectedPosition=position;
            notifyDataSetChanged()
        }


    }


    override fun getItemCount(): Int {
    return  sizeCropList.size
    }
    interface Callback {


        fun onClickItem(position:Int, size:ListSizeCropModel )
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }




}