package com.example.videotophoto.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.videotophoto.R


class FragmentVideoSideShow : Fragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_video_side_show, container, false)
    }

    companion object {

        fun newInstance(): FragmentVideoSideShow {
            return FragmentVideoSideShow()
        }
    }
}
