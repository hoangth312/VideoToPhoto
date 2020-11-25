package com.example.videotophoto.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.videotophoto.R
import kotlinx.android.synthetic.main.item_emoji.view.*

class EmojiDialogAdapter(private var emojiList: ArrayList<String>,
                         private var callback: Callback) : RecyclerView.Adapter<EmojiDialogAdapter.ViewHolder>() {


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_emoji, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.txtEmoji.text = emojiList[position]

        holder.itemView.setOnClickListener() {
            callback.onClickItem(emojiList[position])
        }
    }

    override fun getItemCount(): Int {
        return emojiList.size
    }

    interface Callback {
        fun onClickItem(emoji: String?)
    }
}