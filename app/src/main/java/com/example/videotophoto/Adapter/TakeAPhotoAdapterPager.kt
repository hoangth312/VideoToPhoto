package com.example.videotophoto.Adapter


import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.example.videotophoto.fragments.FragmentAutoShotting
import com.example.videotophoto.fragments.FragmentQuickCapture


class TakeAPhotoAdapterPager(fm: FragmentManager) : FragmentStatePagerAdapter(fm,) {
    private val count: Int? = 2
    override fun getCount(): Int {
       return count!!
    }

    override fun getItem(position: Int): Fragment {
      when(position){
          0->return FragmentQuickCapture.newInstance()
          1->return FragmentAutoShotting.newInstance()



      }
      return null!!
    }

}