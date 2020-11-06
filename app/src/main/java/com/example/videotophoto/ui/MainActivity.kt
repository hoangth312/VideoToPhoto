package com.example.videotophoto.ui

import android.Manifest
import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.example.videotophoto.Adapter.HomeAdapter
import com.example.videotophoto.Model.HomeModel
import com.example.videotophoto.R
import com.example.videotophoto.fragments.CustomDialogAbout
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.dialog_select_video.*


class MainActivity : AppCompatActivity(), HomeAdapter.CellClickListener {
    val dataList = ArrayList<HomeModel>()


    companion object {
        const val ONE = "QuickCapture"
        const val PERMISSION_CODE_GALLLERY = 1
        const val REQUEST_CODE_PICK_CAPTURE = 2
        private const val MY_PERMISSION_REQUEST = 100
        private const val VIDEO_CAPTURE = 101

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var navView: BottomNavigationView = findViewById(R.id.nav_view)
        navView.itemIconTintList = null
        checkPermissions()
        setUpView()
        Icon()





        navView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_Share -> {
                    navShare()
                }
                R.id.navigation_RateUS -> {
                    navRateUs()
                }
                R.id.navigation_About -> {
                    navAbout()
                }
            }
            true
        }


    }


    private fun setUpView() {
        recyclerView.layoutManager = GridLayoutManager(this, 2)
        recyclerView.layoutManager
        val homeAPT = HomeAdapter(dataList, this)
        recyclerView.adapter = homeAPT
    }

    private fun Icon() {
        dataList.add(HomeModel(resources.getString(R.string.select_video), R.drawable.photography))
        dataList.add(HomeModel(resources.getString(R.string.gallery), R.drawable.gallery))
        dataList.add(HomeModel(resources.getString(R.string.sideshow_maker), R.drawable.sideshow))
        dataList.add(HomeModel(resources.getString(R.string.setting), R.drawable.settings))
    }


    private fun navShare() {
        intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_SUBJECT, "Subject Her")
        intent.putExtra(Intent.EXTRA_TEXT, "text")
        startActivity(Intent.createChooser(intent, "Share"))
    }


    private fun navRateUs() {
        val uri = Uri.parse("market://details?id=com.android.chrome")
        val uriSnug = Uri.parse("https://play.google.com/store/apps/details?id=com.android.chrome")
        try {
            val rateApp = Intent(Intent.ACTION_VIEW, uri)
            startActivity(rateApp)
        } catch (e: ActivityNotFoundException) {
            val rateApp2 = Intent(Intent.ACTION_VIEW, uriSnug)
            startActivity(rateApp2)
        }
    }

    private fun navAbout() {
        var dialogAbout = CustomDialogAbout()
        dialogAbout.show(supportFragmentManager, "customDialog")
    }

    private fun openDetailActivity(position: Int) = when (position) {

        0 -> {
            dialogSelectVideo()
//            intent = Intent(this, SelectVideoActivity::class.java)
//            startActivity(intent)
        }
        1 -> {
            intent = Intent(this, GalleryActivity::class.java)
            startActivity(intent)
        }
        3 -> {
            intent = Intent(this, SettingActivity::class.java)
            startActivity(intent)
        }
        else -> Toast.makeText(this, "Cell clicked", Toast.LENGTH_SHORT).show()

    }

    private fun dialogSelectVideo() {
        var dialogSelectVideo = Dialog(this)
        // dialogSelectVideo.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialogSelectVideo.setContentView(R.layout.dialog_select_video)
        dialogSelectVideo.show()

        dialogSelectVideo.lnNewVideo.setOnClickListener {
            val intent = Intent(MediaStore.ACTION_VIDEO_CAPTURE)
            startActivityForResult(intent, VIDEO_CAPTURE)
            dialogSelectVideo.cancel()

        }

        dialogSelectVideo.lnLibrary.setOnClickListener {
            dialogSelectVideo.cancel()
            val intent = Intent(this, FolderVideoActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NO_ANIMATION
            startActivity(intent)
        }

    }


    override fun onCellClickListener(position: Int, model: HomeModel) {
        openDetailActivity(position)
    }

    //  private fun checkPermission() {
//        var params: Array<String>? = null
//        var writeExternal: String = Manifest.permission.WRITE_EXTERNAL_STORAGE
//        var readExternal: String = Manifest.permission.READ_EXTERNAL_STORAGE
//        var cameraPermission = Manifest.permission.CAMERA
//
//        val hasWriteESPermission = ActivityCompat.checkSelfPermission(this, writeExternal)
//        val hasReadESPermission = ActivityCompat.checkSelfPermission(this, readExternal)
//        val hasCameraPermission = ActivityCompat.checkSelfPermission(this, cameraPermission)
//
//        var permissions = ArrayList<String>()
//
//        if (hasWriteESPermission != PackageManager.PERMISSION_GRANTED)
//            permissions.add(writeExternal)
//        if (hasReadESPermission != PackageManager.PERMISSION_GRANTED)
//            permissions.add(readExternal)
//        if (hasCameraPermission != PackageManager.PERMISSION_GRANTED)
//            permissions.add(cameraPermission)
//
//        if (!permissions.isEmpty()) {
//            params = permissions.toTypedArray()
//        }
//        if (params != null && params.size > 0) {
//            ActivityCompat.requestPermissions(this, params, MY_PERMISSION_REQUEST)
//        } else {
//        }
//    }

    private fun checkPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED || checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                val permission = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                requestPermissions(permission, PERMISSION_CODE_GALLLERY)
            }
        }


    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String?>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_CODE_GALLLERY) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            } else {
                Toast.makeText(this, "Permission denied...", Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_PICK_CAPTURE && resultCode == RESULT_OK && data != null) {
            val intent = Intent(this, FolderVideoActivity::class.java)
            startActivity(intent)
        }
    }


}





