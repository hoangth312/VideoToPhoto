package com.example.videotophoto.Adapter

import android.content.Context
import android.media.MediaMetadataRetriever
import android.media.MediaMetadataRetriever.METADATA_KEY_DURATION
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.VideoView
import androidx.recyclerview.widget.RecyclerView
import com.example.videotophoto.R
import java.io.File


class ListVideoAdapter(private val dataListVideo: ArrayList<File>, private val callback: Callback): RecyclerView.Adapter<ListVideoAdapter.ViewHolder>() {

    val retriever = MediaMetadataRetriever()
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val nameVideo: TextView = itemView.findViewById(R.id.tv_videorName)
        val thumbnail: VideoView = itemView.findViewById(R.id.vv_listVideoThum)
        val selectItemCount: TextView = itemView.findViewById(R.id.tv_selectedItemCount)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent?.context).inflate(
                R.layout.item_list_video_adapter,
                parent,
                false
        )

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val context: Context = holder.itemView.context
        holder.nameVideo?.text =(dataListVideo[position].name)

        val uri = Uri.parse(dataListVideo.get(position).absolutePath)
        holder.thumbnail?.setVideoURI(uri)
        holder.thumbnail?.seekTo(2000)



        retriever.setDataSource(context, uri)
        val time = retriever.extractMetadata(METADATA_KEY_DURATION)
         val timeInMillisec = milliSecondsToTimer(time!!.toLong())


        holder.selectItemCount.text = timeInMillisec



        holder.itemView.setOnClickListener {
            callback.onClickItem(dataListVideo[position].absolutePath)
        }

    }

    override fun getItemCount(): Int {
        return dataListVideo.size
    }
    interface Callback {
        fun onClickItem(path: String?)
    }

fun milliSecondsToTimer(millisecond: Long): String {
    var finalTimerStirng = ""
    var secondsString = ""
    var minutesString =""
    var hours: Int = ((millisecond / (1000*60*60)).toInt())
    var minutes: Int = ((((millisecond % (1000*60*60)) / (1000*60)).toInt()))
    var seconds: Int = ((((millisecond % (1000*60*60)) % (1000*60) / 1000).toInt()))
    if (hours < 10){
        finalTimerStirng = "0$hours:"
    }
    if (minutes<10){
        minutesString = "0$minutes:"
    }
    else{
        minutesString = "$minutes:"
    }
    if (seconds < 10 ){
        secondsString = "0$seconds"
    }
    else{
        secondsString ="$seconds"
    }
    if (hours < 10) {
        finalTimerStirng = "$minutesString$secondsString"

    }
   else {
        finalTimerStirng = "$finalTimerStirng$minutesString$secondsString"

    }
    return finalTimerStirng
    }



}






