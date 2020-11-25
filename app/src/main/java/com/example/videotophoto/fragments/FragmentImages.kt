package com.example.videotophoto.fragments

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.*
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.videotophoto.Adapter.ImagesDateAdapter
import com.example.videotophoto.Adapter.ImagesInsideAdapter
import com.example.videotophoto.BuildConfig
import com.example.videotophoto.Model.ImagesDateModel
import com.example.videotophoto.Model.ImagesInsideModel
import com.example.videotophoto.R
import com.example.videotophoto.ui.EditPhotoActivity
import kotlinx.android.synthetic.main.fragment_images.view.*
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class FragmentImages : Fragment(), ImagesInsideAdapter.CellClickListener {
    var sp = SimpleDateFormat("dd-MM-yyyy")
    private lateinit var v: View
    var dateFormat: MutableList<String> = mutableListOf()
    var imageList: MutableList<File> = mutableListOf()
    private val dataList = ArrayList<ImagesDateModel>()
    private val imageInside = ArrayList<ImagesInsideModel>()
    private var isDetele = ArrayList<File>()
    var dateOfImagesConvert = ""
    var date: Date? = null

    private var imageDateAdapter: ImagesDateAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        v = inflater.inflate(R.layout.fragment_images, container, false)

        return v
    }

    companion object {

        fun newInstance(): FragmentImages {
            return FragmentImages()
        }
    }

    override fun onResume() {
        super.onResume()
        dataList.clear()
        readImage()
        loadRecyclerView()
        setUp()


    }


    private fun loadRecyclerView() {

        imageDateAdapter = context?.let { ImagesDateAdapter(dataList, it, imageList, this) }
        val linearLayoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        v.rvDate.layoutManager = linearLayoutManager
        v.rvDate.adapter = imageDateAdapter
        imageDateAdapter!!.notifyDataSetChanged()


    }

    private fun setUp() {


        for (i in 0 until dateFormat.size) {

            dataList.add(ImagesDateModel(dateFormat[i], imageInside))

        }
    }


    fun readImage() {
        imageList.clear()
        val file = File(Environment.getExternalStorageDirectory().absolutePath + "/FrameCaptured")
        if (!file.exists()) {
            file.mkdirs()
        }
        val files = file.listFiles()
        Arrays.sort(files) { o1, o2 -> o2.lastModified().compareTo(o1.lastModified()) }


        for (f in files) {

            if (f.name.endsWith(".jpg") || f.name.endsWith(".png")) {
                imageList.add(f)

                var dateOfImages = f.lastModified()
                date = Date(dateOfImages)
                dateOfImagesConvert = sp.format(date!!.time)

                // sp.format(Date(f.lastModified()).time)
                dateFormat.add(dateOfImagesConvert)
                val newItems = dateFormat
                newItems.addAll(dateFormat)
                dateFormat = newItems.toSet().toMutableList()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        super.onCreate(savedInstanceState)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menuDeleteImage -> {
                dialogDelete()
            }



        }
        return super.onOptionsItemSelected(item)
    }

    private fun dialogDelete() {
        val builder = AlertDialog.Builder(context)
        //builder.setIcon(R.drawable.icon_warning)
        builder.setTitle(getString(R.string.delete))
        builder.setMessage("Delete " + isDetele.size + " image")
        builder.setNegativeButton(
            getString(R.string.cancel)
        ) { dialogInterface, i -> dialogInterface.dismiss() }
        builder.setPositiveButton(
            getString(R.string.ok)
        ) { dialogInterface, i ->
            for (file in isDetele) {
                if (file.delete()) {
                } else {
                }
                imageDateAdapter!!.notifyDataSetChanged()
            }
            isDetele.clear()
            dialogInterface.dismiss()

        }
        builder.show()
    }



    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_gallery_image, menu)


    }

    override fun onClickItem(file: File) {
        val intent = Intent(context, EditPhotoActivity::class.java)
        intent.putExtra("PATH IMG", file.toString())

        startActivity(intent)

    }

    override fun onDisChoose(file: File?) {
        if (isDetele.size > 0) {
            isDetele.remove(file)

        }
    }


    override fun onChoose(file: File) {
        if (isDetele.contains(file)) {
            return
        }
        isDetele.add(file)
        Log.e("CHECKCALL", isDetele.size.toString() + "")
    }


}





