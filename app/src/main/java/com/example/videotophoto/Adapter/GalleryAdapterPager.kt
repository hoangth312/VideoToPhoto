package com.example.videotophoto.Adapter


import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.example.videotophoto.fragments.FragmentAutoShotting
import com.example.videotophoto.fragments.FragmentImages
import com.example.videotophoto.fragments.FragmentQuickCapture
import com.example.videotophoto.fragments.FragmentVideoSideShow


class GalleryAdapterPager(fm: FragmentManager) : FragmentStatePagerAdapter(fm,) {
    private val count: Int? = 2
    override fun getCount(): Int {
       return count!!
    }

    override fun getItem(position: Int): Fragment {
      when(position){
          0->return FragmentVideoSideShow.newInstance()
          1->return FragmentImages.newInstance()

      }
      return null!!
    }

}