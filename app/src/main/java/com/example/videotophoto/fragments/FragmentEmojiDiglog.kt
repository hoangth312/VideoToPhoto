package com.example.videotophoto.fragments

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.videotophoto.Adapter.EmojiDialogAdapter
import com.example.videotophoto.R
import ja.burhanrashid52.photoeditor.PhotoEditor
import kotlinx.android.synthetic.main.fragment_emoji_diglog.view.*
import java.util.*


class FragmentEmojiDiglog(var getContext: Context, var photoEditor: PhotoEditor) : DialogFragment() , EmojiDialogAdapter.Callback{


    var emojiL = ArrayList<String>()
    var emojiAdapter: EmojiDialogAdapter? = null

    private lateinit var v: View


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        v = inflater.inflate(R.layout.fragment_emoji_diglog, container, false)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        setUp()
        return v
    }


    fun setUp(){
        emojiL = PhotoEditor.getEmojis(context)
        emojiAdapter = EmojiDialogAdapter(emojiL, this)
        val layoutManager = GridLayoutManager(context, 5)

        v.rvEmoji.adapter = emojiAdapter
        v.rvEmoji.layoutManager = layoutManager
    }



    override fun onClickItem(emoji: String?) {
        Toast.makeText(context, emoji + "", Toast.LENGTH_LONG).show()
        photoEditor.addEmoji(emoji)
        dismiss()
    }


}