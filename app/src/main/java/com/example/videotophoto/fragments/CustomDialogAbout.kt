package com.example.videotophoto.fragments

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.fragment.app.DialogFragment

import com.example.videotophoto.R
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.dialog_custom_about.view.*



class CustomDialogAbout : DialogFragment() {

    override fun onDestroyView() {
        super.onDestroyView()
        this.clearFindViewByIdCache()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view: View = inflater.inflate(R.layout.dialog_custom_about, container, false)


        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
        view.text2?.setOnClickListener {openWeb()}
        view.text4?.setOnClickListener{openPolicy()}
        return view
    }

private fun openWeb(){
    val uri = Uri.parse("https://hdpsolutions.com/")
    val authorName = Intent(Intent.ACTION_VIEW, uri)
    startActivity(authorName)
}
    private fun openPolicy(){
        val uri = Uri.parse("http://hdpsolution.com/privacy_policy/CameraApps/PrivacyPolicyCameraApps.htm")
        val policy = Intent(Intent.ACTION_VIEW, uri)
        startActivity(policy)
    }

}




