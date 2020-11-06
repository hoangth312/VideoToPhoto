package com.example.videotophoto.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView

import androidx.recyclerview.widget.RecyclerView
import com.example.videotophoto.Model.HomeModel
import com.example.videotophoto.R


class HomeAdapter(private val iconList: ArrayList<HomeModel>, private val cellClickListener: CellClickListener) : RecyclerView.Adapter<HomeAdapter.ViewHolder>() {


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

       val name: TextView = itemView.findViewById(R.id.tv_nameIcon)
       val iconHome: ImageView = itemView.findViewById(R.id.iv_folderVideoIcon)

            }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent?.context).inflate(
            R.layout.item_home_model,
            parent,
            false
        )
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model:HomeModel = iconList.get(position)
        holder.name?.text = iconList[position].name
        holder.iconHome?.setImageResource(iconList[position].img)

        holder.itemView.setOnClickListener {
            cellClickListener.onCellClickListener(position,model)
        }
    }

    override fun getItemCount(): Int {
        return iconList.size
    }
    interface CellClickListener {
        fun onCellClickListener(position:Int, model: HomeModel)
    }

}
