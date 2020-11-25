package com.example.videotophoto.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.videotophoto.Model.ImagesInsideModel
import com.example.videotophoto.R
import kotlinx.android.synthetic.main.item_image_inside_adapter.view.*
import java.io.File


class ImagesInsideAdapter(
    var context: Context,
    var list: MutableList<File>,
    private val cellClickListener: CellClickListener
) : RecyclerView.Adapter<ImagesInsideAdapter.ViewHolder>() {

    private var data = ArrayList<ImagesInsideModel>()
    var isLongPressed: Boolean = false

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }

    fun setData(list: ArrayList<ImagesInsideModel>) {
        this.data = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.item_image_inside_adapter,
            parent,
            false
        )
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var images = list[position]

        Glide.with(context).load(images)
            .override(100, 100)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .skipMemoryCache(true)
            .into(holder.itemView.ivPhoto)
      //  Glide.with(context).load(images).override(100,100).into(holder.itemView.ivPhoto)
//        val options = BitmapFactory.Options()
//        options.outWidth = 5
//        options.outHeight = 5
//
//        val bitmap = BitmapFactory.decodeFile(list[position].toString(), options)
//      holder.itemView.ivPhoto.setImageBitmap(Bitmap.createScaledBitmap(bitmap, 100, 100, false))

        holder.itemView.setOnClickListener {
                    if (isLongPressed){
                        if (holder.itemView.checkBox.isChecked){
                            holder.itemView.checkBox.isChecked = false
                            cellClickListener.onDisChoose(images)
                        }else{
                            holder.itemView.checkBox.isChecked = true
                            cellClickListener.onChoose(images)
                        }



                    }else cellClickListener.onClickItem(images)



        }


        holder.itemView.setOnLongClickListener {

            cellClickListener.onChoose(images)


            holder.itemView.checkBox.isChecked = true

            showCheckbox()

            true
        }



        if (isLongPressed) {
            holder.itemView.checkBox.visibility = View.VISIBLE
        } else {
            holder.itemView.checkBox.visibility = View.GONE
        }


    }
    fun showCheckbox() {
        isLongPressed = true
        notifyDataSetChanged() // Required for update
    }

    override fun getItemCount(): Int {

        return list.size


    }





    interface CellClickListener {
        fun onClickItem(file: File)
        fun onDisChoose(file: File?)
        fun onChoose(file: File)

    }
}


