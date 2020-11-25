package com.example.videotophoto.ui

import android.Manifest
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.SeekBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.videotophoto.Adapter.CropImageAdapter
import com.example.videotophoto.Model.ListSizeCropModel
import com.example.videotophoto.R
import com.example.videotophoto.fragments.FragmentEmojiDiglog
import com.example.videotophoto.fragments.FragmentTextEditDialog
import com.skydoves.colorpickerview.ColorPickerDialog
import com.skydoves.colorpickerview.listeners.ColorEnvelopeListener
import ja.burhanrashid52.photoeditor.OnSaveBitmap
import ja.burhanrashid52.photoeditor.PhotoEditor
import ja.burhanrashid52.photoeditor.PhotoEditor.OnSaveListener
import ja.burhanrashid52.photoeditor.TextStyleBuilder
import kotlinx.android.synthetic.main.activity_edit_photo.*
import java.io.File
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.attribute.BasicFileAttributes
import java.nio.file.attribute.FileTime
import java.text.DecimalFormat


class EditPhotoActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener, CropImageAdapter.Callback {
    var photoEditor: PhotoEditor? = null
    var path: String? = ""
    var test = 0
    var edit: Boolean = false

    private var bitmap: Bitmap? = null
    var listSize = ArrayList<ListSizeCropModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_photo)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        nav_view_edit_photo.itemIconTintList = null
        nav_view_tab_edit.itemIconTintList = null
        undoAndRedo()
        showImages()
        bottomNaviClick()
        bottomNaviEdit()
        saveFileAndExit()

    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    override fun onBackPressed() {

        val builder = AlertDialog.Builder(this)
        builder.setTitle(R.string.Exit).setMessage(R.string.ExitQuestion)
                .setNegativeButton(R.string.cancel, DialogInterface.OnClickListener() { dialog, _ -> dialog.dismiss() })
                .setPositiveButton(R.string.ok, DialogInterface.OnClickListener { dialog, _ ->
                    super@EditPhotoActivity.onBackPressed()
                    dialog.dismiss()
                    finish()
                })
        builder.show()
    }

    companion object {
        const val FILE_PROVIDER_AUTHORITY = "com.example.videotophoto.fileprovider"
    }


    private fun undoAndRedo() {
        ivUndo.setOnClickListener(View.OnClickListener { photoEditor!!.undo() })
        ivRendo.setOnClickListener(View.OnClickListener { photoEditor!!.redo() })
    }


    private fun showImages() {
        var intent = intent
        path = intent.getStringExtra("PATH IMG")

        val uri = Uri.parse(path)
        val file = File(path)
        bitmap = BitmapFactory.decodeFile(file.absolutePath)
        photoEditorView.source.setImageBitmap(bitmap)
        cropImageView.setImageBitmap(bitmap)
        zoomImage.setImageBitmap(bitmap)

//        Glide.with(this).load(uri).into(zoomImage)

        photoEditor = PhotoEditor.Builder(this, photoEditorView)
                .setPinchTextScalable(true)
                .build()

    }


    private fun saveFileAndExit() {
        ivSave.setOnClickListener(View.OnClickListener {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                Toast.makeText(this, "abc", Toast.LENGTH_LONG).show()
            }
            path?.let { it1 ->
                photoEditor!!.saveAsFile(it1, object : OnSaveListener {
                    override fun onSuccess(imagePath: String) {
                        val bitmap = BitmapFactory.decodeFile(path)
                        photoEditorView.source.setImageBitmap(bitmap)

//                          val intent = Intent(this@EditPhotoActivity, GalleryActivity::class.java)
//                        intent.putExtra("PATH SUCCES", path)
//                        startActivity(intent)
                finish()
                    }

                    override fun onFailure(exception: Exception) {
                        Toast.makeText(this@EditPhotoActivity, "Fail", Toast.LENGTH_LONG).show()
                    }
                })
            }

        })
        ivCancelEdit.setOnClickListener {backEdit() }

    }


    private fun bottomNaviClick() {
        nav_view_edit_photo.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_Share -> {
                    shareImage()
                }
                R.id.navigation_Edit -> {

                    editPhoto()
                }
                R.id.navigation_Info -> {
                    metadataImagesDialog()
                }
                R.id.navigation_Delete -> {
                    deleteImage()
                }
            }
            true
        }
    }

    private fun bottomNaviEdit() {
        nav_view_tab_edit.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_backEdit -> {

                    backEdit()
                }
                R.id.navigation_cropImage -> {
                    cropImage()

                }
                R.id.navigation_editText -> {

                    editText()
                }
                R.id.navigation_sticker -> {
                    sticker()
                }
                R.id.navigation_paint -> {

                    paitn()
                }
            }
            true
        }
    }

    private fun metadataImagesDialog() {
        try {
            val file = File(path)
            val size = file.length() / (1024 * 1024).toDouble()
            var decimalFormat = DecimalFormat("#.00")
            var endwith = ""
            var filesize = ""
            var attr: BasicFileAttributes? = null
            var creationTime: FileTime? = null

            val bitmap = BitmapFactory.decodeFile(file.absolutePath)
            if (size > 1024) {
                decimalFormat = DecimalFormat("#.00")
                filesize = decimalFormat.format(size)
                endwith = " MB"
            } else {
                filesize = size.toString()
                endwith = " Kb"
            }
            val dialog = AlertDialog.Builder(this)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                attr = Files.readAttributes(Paths.get(path), BasicFileAttributes::class.java)
                creationTime = attr.creationTime()
                dialog.setTitle(R.string.Details).setMessage("""${resources.getString(R.string.FileName)}:
                    ${file.name}
                    ${resources.getString(R.string.FileSize)}:
                    $filesize$endwith
                    ${resources.getString(R.string.Resolution)}:
                    ${bitmap.height} x ${bitmap.width}
                    ${resources.getString(R.string.Date)}:
                    ${attr.creationTime()}
                    ${resources.getString(+R.string.Path)}:
                    $path""")
                        .setPositiveButton(R.string.ok, DialogInterface.OnClickListener { dialog, _ -> dialog.dismiss() })
                        .create()
            } else {
                dialog.setTitle(R.string.Details).setMessage("""${resources.getString(R.string.FileName)}:
                    ${file.name}
                    ${resources.getString(R.string.FileSize)}:
                    $filesize$endwith
                    ${resources.getString(R.string.Resolution)}:
                    ${bitmap.width} x ${bitmap.height}
                    ${resources.getString(R.string.Path)}:
                    
                    $path""")
                        .setPositiveButton(R.string.ok, DialogInterface.OnClickListener { dialog, _ -> dialog.dismiss() })
                        .create()

            }
            dialog.show()
        } catch (e: IOException) {
            e.printStackTrace();
        }


    }


    private fun deleteImage() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(R.string.delete).setMessage(R.string.DeleteQuestion).setNegativeButton(R.string.cancel,
                DialogInterface.OnClickListener { dialog, _ -> dialog.dismiss() }).setPositiveButton(R.string.ok,
                DialogInterface.OnClickListener { dialog, _ ->
                    val file1 = File(path)
                    file1.delete()
                    dialog.dismiss()

                    super@EditPhotoActivity.onBackPressed()

                })
        builder.show()
    }

    private fun buidFileProviderUri(uri: Uri): Uri? {
        return FileProvider.getUriForFile(this, FILE_PROVIDER_AUTHORITY, File(uri.path))
    }

    private fun shareImage() {
        val uri = Uri.parse(path)
        val intent1 = Intent(Intent.ACTION_SEND)
        intent1.type = "image/*"
        intent1.putExtra(Intent.EXTRA_STREAM, buidFileProviderUri(uri))
        startActivity(Intent.createChooser(intent1, getString(R.string.title_share)))
    }


    private fun editPhoto() {
        lnUndoAndRedo.visibility = View.VISIBLE
        nav_view_edit_photo.visibility = View.INVISIBLE
        nav_view_tab_edit.visibility = View.VISIBLE
        // cropImage.visibility = View.VISIBLE
        //zoomImage.visibility = View.GONE

    }

    private fun backEdit() {
        nav_view_tab_edit.visibility = View.INVISIBLE
        nav_view_edit_photo.visibility = View.VISIBLE
        lnUndoAndRedo.visibility = View.GONE
        zoomImage.visibility = View.VISIBLE
        cropImageView.visibility = View.GONE

        lnTickCropImage.visibility = View.GONE


    }


    private fun sticker() {
        photoEditorView.visibility = View.VISIBLE
        zoomImage.visibility = View.GONE
        cropImageView.visibility = View.GONE
        lnUndoAndRedo.visibility = View.VISIBLE
        lnTickCropImage.visibility = View.INVISIBLE
        var emojiDialog = FragmentEmojiDiglog(this, photoEditor!!)
        emojiDialog.show(supportFragmentManager, "emojidialog")


    }


    private fun editText() {
        zoomImage.visibility = View.GONE
        cropImageView.visibility = View.GONE
        photoEditorView.visibility = View.VISIBLE
        lnUndoAndRedo.visibility = View.VISIBLE
        lnTickCropImage.visibility = View.INVISIBLE
        val editTextFragments = FragmentTextEditDialog(this, photoEditor!!)
        editTextFragments.show(supportFragmentManager, "editextdialog")


        editTextFragments.setOnTextEditorListener(object : FragmentTextEditDialog.TextEditor {
            override fun onDone(inputText: String?, colorCode: Int) {
                val styleBuilder = TextStyleBuilder()
                styleBuilder.withTextColor(colorCode)
                photoEditor!!.addText(inputText, styleBuilder)

            }
        })
    }


    private fun paitn() {

        fun setDefaultPaint() {
            sbSize.progress = 50
            sbOpacity.progress = 99
            photoEditor!!.brushSize = 50F
            photoEditor!!.setOpacity(99)
            zoomImage.visibility = View.GONE
            cropImageView.visibility = View.GONE
            photoEditorView.visibility = View.VISIBLE
            nav_view_tab_edit.visibility = View.INVISIBLE
            lnPaint.visibility = View.VISIBLE
            lnUndoAndRedo.visibility = View.VISIBLE
            lnTickCropImage.visibility = View.INVISIBLE
            photoEditor!!.setBrushDrawingMode(true);
            lnCropSize.visibility = View.INVISIBLE
        }

        fun clickBtBack() {
            lnPaint.visibility = View.INVISIBLE
            nav_view_tab_edit.visibility = View.VISIBLE
        }


        fun setSpinner() {
            var textSize = resources.getStringArray(R.array.paint)
            var adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, textSize)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerPaint.adapter = adapter
            spinnerPaint.onItemSelectedListener = this
        }

        fun setSizeAndOpacitySB() {
            sbSize.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(seekBar: SeekBar?, progress: Int, b: Boolean) {
                    if (toggleEraser.isChecked) {
                        photoEditor!!.setBrushEraserSize(progress.toFloat())
                        photoEditor!!.setBrushDrawingMode(false)
                        photoEditor!!.brushEraser()
                        tvSBSize.text = progress.toString()

                    } else {
                        tvSBSize.text = progress.toString()
                        photoEditor!!.brushSize = progress.toFloat()
                    }


                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {}
                override fun onStopTrackingTouch(seekBar: SeekBar?) {}
            })
            sbOpacity.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(seekBar: SeekBar?, progress: Int, b: Boolean) {
                    tvSBOpacity.text = progress.toString()
                    photoEditor!!.setOpacity(progress)
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {}
                override fun onStopTrackingTouch(seekBar: SeekBar?) {}
            })

        }


        fun checkEraser() {
            if (toggleEraser.isChecked) {
                Toast.makeText(this, "true", Toast.LENGTH_SHORT).show()
                photoEditor!!.setBrushDrawingMode(false)
                photoEditor!!.brushEraser()
                spinnerPaint.setSelection(0)
                spinnerPaint.isEnabled = false

            } else {
                photoEditor!!.setBrushDrawingMode(true)
                spinnerPaint.isEnabled = true
            }

        }




        setDefaultPaint()
        btBackFromPaint.setOnClickListener { clickBtBack() }
        setSpinner()
        setSizeAndOpacitySB()

        colorBroad.setOnClickListener { seletcColorText() }
        toggleEraser.setOnClickListener { checkEraser() }

    }


    private fun seletcColorText() {
        ColorPickerDialog.Builder(this)
                .setTitle("ColorPicker Dialog")
                .setPreferenceName("MyColorPickerDialog")
                .setPositiveButton(getString(R.string.ok),
                        ColorEnvelopeListener { envelope, _ ->
                            photoEditor!!.brushColor = envelope.color
                            // itemView.addTextEditText.setHintTextColor(envelope.color)

                        })
                .setNegativeButton(getString(R.string.cancel)
                ) { dialogInterface, _ -> dialogInterface.dismiss() }
                .attachAlphaSlideBar(true) // the default value is true.
                .attachBrightnessSlideBar(true) // the default value is true.
                .setBottomSpace(12) // set a bottom space between the last slidebar and buttons.
                .show()

    }


    private fun cropImage() {


        fun setDefaultPaint() {
            lnUndoAndRedo.visibility = View.INVISIBLE
            lnTickCropImage.visibility = View.VISIBLE
            zoomImage.visibility = View.GONE
            cropImageView.visibility = View.VISIBLE
            lnCropSize.visibility = View.VISIBLE
            nav_view_tab_edit.visibility = View.INVISIBLE
          

        }

        fun setRvCropSize() {

            var adapterCropImage = CropImageAdapter(listSize, this)
            val linearLayoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
            rvCropSize.layoutManager = linearLayoutManager
            rvCropSize.adapter = adapterCropImage
            listSize.add(ListSizeCropModel("1:1  "))
            listSize.add(ListSizeCropModel("4:3  "))
            listSize.add(ListSizeCropModel("3:4  "))
            listSize.add(ListSizeCropModel("5:7  "))
            listSize.add(ListSizeCropModel("7:5  "))
            listSize.add(ListSizeCropModel("2:3  "))
            listSize.add(ListSizeCropModel("3:2  "))
            listSize.add(ListSizeCropModel("3:5  "))
            listSize.add(ListSizeCropModel("5:3  "))
            listSize.add(ListSizeCropModel("9:16  "))
            listSize.add(ListSizeCropModel("16:9  "))
        }

        ivCancelCrop.setOnClickListener {
            lnUndoAndRedo.visibility = View.VISIBLE
            lnTickCropImage.visibility = View.INVISIBLE
            zoomImage.visibility = View.VISIBLE
            cropImageView.visibility = View.GONE
            nav_view_tab_edit.visibility = View.VISIBLE
        }


     fun setUp(){
         if (edit) {
             Log.e("EDIT", edit.toString() + "")
             photoEditor!!.saveAsBitmap(object : OnSaveBitmap {
                 override fun onBitmapReady(saveBitmap: Bitmap) {
                     zoomImage.setImageBitmap(saveBitmap)
                     photoEditorView.source.setImageBitmap(saveBitmap)
                     cropImageView.setImageBitmap(saveBitmap)
                 }

                 override fun onFailure(e: java.lang.Exception) {}
             })
         } else {
             Log.e("EDIT", edit.toString() + " dsds")
         }
     }


        fun cropImage() {
           // setAspectRatioXY(test)

            val croped: Bitmap = cropImageView.croppedImage
            zoomImage.setImageBitmap(croped)
            photoEditorView.source.setImageBitmap(croped)
            cropImageView.setImageBitmap(croped)
            nav_view_tab_edit.visibility = View.VISIBLE
            lnSize.visibility = View.INVISIBLE

        }





        setDefaultPaint()
        setRvCropSize()
        setUp()
        ivTickCropImage.setOnClickListener {cropImage()}
       setAspectRatioXY(test)
    }


    private fun setAspectRatioXY(position: Int){
        test = position
     when (position) {

         0 -> cropImageView.setAspectRatio(1, 1)
         1 -> cropImageView.setAspectRatio(4, 3)
         2 -> cropImageView.setAspectRatio(3, 4)
         3 -> cropImageView.setAspectRatio(5, 7)
         4 -> cropImageView.setAspectRatio(7, 5)
         5 -> cropImageView.setAspectRatio(2, 3)
         6 -> cropImageView.setAspectRatio(3, 2)
         7 -> cropImageView.setAspectRatio(3, 5)
         8 -> cropImageView.setAspectRatio(5, 3)
         9 -> cropImageView.setAspectRatio(9, 16)
         10 -> cropImageView.setAspectRatio(16, 9)

         else -> Toast.makeText(this, "test", Toast.LENGTH_SHORT).show()
     }



    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        if (parent!!.id == R.id.spinnerPaint) {
            var valueFromSpinner = parent.getItemAtPosition(position).toString()
            when (valueFromSpinner) {
                "Size" -> {
                    lnSize.visibility = View.VISIBLE
                    lnOpacity.visibility = View.INVISIBLE
                }

                "Opacity" -> {
                    lnSize.visibility = View.INVISIBLE
                    lnOpacity.visibility = View.VISIBLE
                }
            }
        }
    }


    override fun onNothingSelected(parent: AdapterView<*>?) {

    }

    override fun onClickItem(position: Int, size: ListSizeCropModel) {
        setAspectRatioXY(position)


    }


}