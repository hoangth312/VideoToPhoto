package com.example.videotophoto.fragments

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import android.media.MediaPlayer
import android.net.Uri
import android.os.*
import android.text.Editable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.videotophoto.Adapter.QuickCaptureAdapter
import com.example.videotophoto.R
import com.example.videotophoto.ui.EditPhotoActivity
import it.sephiroth.android.library.rangeseekbar.RangeSeekBar
import it.sephiroth.android.library.rangeseekbar.RangeSeekBar.OnRangeSeekBarChangeListener
import kotlinx.android.synthetic.main.activity_set_time_dialog.view.*
import kotlinx.android.synthetic.main.fragment_auto_shotting.view.*
import kotlinx.android.synthetic.main.fragment_quickcapture.view.*
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.*
import kotlin.math.roundToLong

class FragmentAutoShotting : Fragment(), QuickCaptureAdapter.ViewHolder.Callback {
    var type = ""
    var quality = ""
    var videoUrl = ""
    var visibleItemCount = 0
    @Volatile
    var stopThread = false
    var retriever = MediaMetadataRetriever()
    var startTime: Long = 0
    var maxTime: Int = 0
    var minTime = 0
    var duration = 2000f
    var imageList = ArrayList<File>()
    var cutImageList = ArrayList<Bitmap>()
    var quickCaptureAdapter: QuickCaptureAdapter? = null
    var endWiths = ".jpg"
    var getPath = ""
    var asyn: Long = 0
    var a: Long = 0
    var cutTimeList = ArrayList<Bitmap>()

    private lateinit var viewOfLayout: View

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        viewOfLayout = inflater.inflate(R.layout.fragment_auto_shotting, container, false)

        return viewOfLayout

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadVideo()
        getDataVideo()
        startVideo()

        viewOfLayout.imv_cameraCutTimeCapture.setOnClickListener{
            TimeCaptureAsynTask().execute()
        }
    }


    companion object {
        const val TWO = "Automatic"
        fun newInstance(): FragmentAutoShotting {
            return FragmentAutoShotting()


        }
    }


    override fun onStart() {
        super.onStart()
        sharedPreferencesfromSetting()
    }

    override fun onStop() {
        super.onStop()
        stopThread = true
    }


    private fun sharedPreferencesfromSetting() {
        var sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        quality = sharedPreferences!!.getString("list_quality", "High").toString()
        type = sharedPreferences!!.getString("list_file_format ", "JPG").toString()
    }


    private fun loadVideo() {
        val intent = requireActivity().intent
        videoUrl = intent.getStringExtra("VIDEO PATH")!!
        val uri = Uri.parse(videoUrl)
        viewOfLayout.videoViewTimeCapture.setVideoURI(uri)
        var videoFile = File(videoUrl)
        videoFile = File(videoFile.path)
        var nameVideo = videoFile.name.replace(".mp4", "")
        viewOfLayout.tv_nameVideoTimeCapture.text = nameVideo

    }

    private fun getDataVideo() {

        viewOfLayout.videoViewTimeCapture.setOnPreparedListener(MediaPlayer.OnPreparedListener {
            retriever.setDataSource(videoUrl)
            var time: String? = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)

            var durationMax = time!!.toInt()
            viewOfLayout.tv_endTimeCapture.text = milliSecondsToTimer(viewOfLayout.videoViewTimeCapture.duration.toLong())
            viewOfLayout.tv_currentTimeCapture.text = (milliSecondsToTimer(viewOfLayout.videoViewTimeCapture.currentPosition.toLong()))
            var currentTime = viewOfLayout.videoViewTimeCapture.currentPosition
            viewOfLayout.rangerSeekBarTimeCapture.max = viewOfLayout.videoViewTimeCapture.duration / 1000
            viewOfLayout.rangerSeekBarTimeCapture.setProgress(currentTime, durationMax)
            viewOfLayout.rangerSeekBarTimeCapture.isEnabled = true
            startTime = 0

            maxTime = (durationMax / 1000)

        })


        viewOfLayout.rangerSeekBarTimeCapture.setOnRangeSeekBarChangeListener(object : OnRangeSeekBarChangeListener {
            override fun onProgressChanged(rangeSeekBar: RangeSeekBar, minValue: Int, maxValue: Int, b: Boolean) {
                if (b) {
                    maxTime = maxValue
                    minTime = minValue
                    viewOfLayout.videoViewTimeCapture.seekTo(minValue * 1000)
                    viewOfLayout.tv_endTimeCapture.text = milliSecondsToTimer((maxValue * 1000).toLong())
                    viewOfLayout.tv_currentTimeCapture.text = (milliSecondsToTimer((minValue * 1000).toLong()))
                    startTime = (minValue + duration).roundToLong()

                }
            }

            override fun onStartTrackingTouch(rangeSeekBar: RangeSeekBar) {}
            override fun onStopTrackingTouch(rangeSeekBar: RangeSeekBar) {}
        })

    }

    @SuppressLint("ClickableViewAccessibility")
    fun startVideo() {
        viewOfLayout.edNumberSnap.setOnClickListener(View.OnClickListener {
            openDialogSetSeconds(0, viewOfLayout.videoViewTimeCapture.duration / 1000)
            // Toast.makeText(context, "123", Toast.LENGTH_SHORT).show()
        })

        viewOfLayout.run {
            imv_PlayTimeCapture.setOnClickListener(View.OnClickListener {
                if (!videoViewTimeCapture.isPlaying) {
                    videoViewTimeCapture.start()
                    checkPlay(true)
                    val handler = Handler()
                    handler.postDelayed({ imv_PlayTimeCapture.visibility = View.GONE }, 3000)
                } else {
                    videoViewTimeCapture.pause()
                    checkPlay(false)
                    viewOfLayout.imv_PlayTimeCapture.visibility = View.VISIBLE
                }

            })
        }

        viewOfLayout.videoViewTimeCapture.setOnTouchListener { v, event ->
            viewOfLayout.imv_PlayTimeCapture.visibility = View.VISIBLE
            false
        }



        Thread(Runnable {
            while (viewOfLayout.videoViewTimeCapture != null) {
                try {
                    if (viewOfLayout.videoViewTimeCapture.isPlaying) {
                        val msg = Message()
                        msg.arg1 = viewOfLayout.videoViewTimeCapture.currentPosition
                        handler.sendMessage(msg)
                        Thread.sleep(1)

                        if (msg.arg1 == viewOfLayout.videoViewTimeCapture.duration) {
                            viewOfLayout.videoViewTimeCapture.stopPlayback()
                            checkPlay(false)
                            viewOfLayout.videoViewTimeCapture.resume()
                        }
                    } else {
                        val msg = Message()
                        msg.arg1 = viewOfLayout.videoViewTimeCapture.currentPosition
                        handler.sendMessage(msg)
                        Thread.sleep(1)
                    }
                    if (stopThread) {
                        viewOfLayout.videoViewTimeCapture.pause()
                        return@Runnable
                    }

                } catch (ie: InterruptedException) {
                    Log.w("Video To Photo", "" + ie)
                }
                try {
                    Thread.sleep(1)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
            }
        }).start()

    }


    fun checkPlay(playing: Boolean) {
        if (playing) {
            viewOfLayout.imv_PlayTimeCapture.setImageResource(R.drawable.ic_pause)
        } else {
            viewOfLayout.imv_PlayTimeCapture.setImageResource(R.drawable.ic_play)
        }

    }

    private fun openDialogSetSeconds(min: Int, max: Int) {

        val builder = context?.let { AlertDialog.Builder(it) }
        val view = LayoutInflater.from(context).inflate(R.layout.activity_set_time_dialog, null)
        builder!!.setView(view)
                .setTitle(resources.getString(R.string.enterduration))
                .setNegativeButton("Cancel", DialogInterface.OnClickListener { dialog, which -> dialog.dismiss() })
                .setPositiveButton("OK", DialogInterface.OnClickListener { dialog, which ->

                    if (view.txtDuration.text.toString().isNotEmpty()) {
                        duration = (view.txtDuration.text.toString().toFloat() * 1000)
                        view.txtDuration.text = viewOfLayout.edNumberSnap.text as Editable?




                        if (duration / 1000 > (max - min)) {
                            val errorDialog = context?.let { AlertDialog.Builder(it) }
                            errorDialog!!.setTitle(resources.getString(R.string.error))
                                    .setMessage(resources.getString(R.string.snaperror))
                                    .setPositiveButton(resources.getString(R.string.ok), DialogInterface.OnClickListener { dialog, which -> dialog.dismiss() })
                            errorDialog.create()
                            errorDialog.show()

                        } else {
                            viewOfLayout.edNumberSnap.text = ((duration / 1000).toString())

                        }
                    } else {
                        val errorDialog = context?.let { AlertDialog.Builder(it) }
                        errorDialog!!.setTitle(resources.getString(R.string.error))
                                .setMessage(resources.getString(R.string.nullimesnap))
                                .setPositiveButton(resources.getString(R.string.ok), DialogInterface.OnClickListener { dialog, which -> dialog.dismiss() })
                        errorDialog.create()
                        errorDialog.show()
                    }

                }).create()
        builder.show()
    }


    private fun createFileImage(bitmap: Bitmap) {
        val file = File(Environment.getExternalStorageDirectory().absoluteFile.toString() + "/FrameCaptured")
        if (!file.exists()) {
            file.mkdir()
        }
        val generator = Random()
        var n = 10000
        n = generator.nextInt(n)
        val fileName = "Image-$n$endWiths"
        var bitmapImage: Bitmap.CompressFormat
        if (type == "JPG") {
            endWiths = ".jpg"
            bitmapImage = Bitmap.CompressFormat.JPEG
        } else {
            endWiths = ".png"
            bitmapImage = Bitmap.CompressFormat.PNG
        }
        val outFile = File(file, fileName)
        val fos = FileOutputStream(outFile)
        bitmap.compress(bitmapImage, setQuality(), fos)
        fos.flush()
        fos.close()
        imageList.add(outFile)
    }


    private fun loadRecyclerView(bitmaps: List<Bitmap>, files: List<File>) {
        quickCaptureAdapter = QuickCaptureAdapter(bitmaps, requireContext(), this, files)
        val linearLayoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        linearLayoutManager.reverseLayout = true
        linearLayoutManager.stackFromEnd = true
        viewOfLayout.rv_CutImageList_TimeCapture.layoutManager = linearLayoutManager
        viewOfLayout.rv_CutImageList_TimeCapture.adapter = quickCaptureAdapter
        quickCaptureAdapter!!.notifyDataSetChanged()
    }

    private fun setQuality(): Int {
        if ("Best" == quality) {
            return 100
        }
        if ("Very High".equals(quality)) {
            return 85
        }
        if ("High".equals(quality)) {
            return 75
        }
        if ("Medium".equals(quality)) {
            return 65
        }
        if ("Low".equals(quality)) {
            return 50
        }
        return 75
    }


   private inner class TimeCaptureAsynTask : AsyncTask<Void, ArrayList<Bitmap>, ArrayList<Bitmap>>()
    {
        lateinit var proressDialog: ProgressDialog

        override fun onPreExecute() {
            super.onPreExecute()
            proressDialog = ProgressDialog(context)
            proressDialog.setMessage(resources.getString(R.string.wait))
            proressDialog.setCancelable(false)
            proressDialog.show()


        }


        override fun doInBackground(vararg voids: Void?): ArrayList<Bitmap>? {
            asyn = startTime
            while (asyn <= maxTime *1000) {


                retriever.setDataSource(videoUrl)
                val bmFrame: Bitmap? = retriever.getFrameAtTime(asyn * 1000, MediaMetadataRetriever.OPTION_CLOSEST)
                try {
                    bmFrame?.let { createFileImage(it) }
                } catch (e: IOException) {
                    e.printStackTrace()
                }
                publishProgress()
                cutTimeList.add(bmFrame!!)
                visibleItemCount = cutTimeList.size
                if (isCancelled) {
                    a = (asyn + duration).toLong()
                    break
                }


                asyn += duration.toLong()

            }
            return cutTimeList;
        }




         override fun onPostExecute(bitmaps: ArrayList<Bitmap>) {

            loadRecyclerView(cutTimeList, imageList)
             proressDialog.dismiss()

            super.onPostExecute(bitmaps)
        }

        override fun onProgressUpdate(vararg values: ArrayList<Bitmap>?) {
            super.onProgressUpdate(*values)
            loadRecyclerView(cutTimeList, imageList)
        }

        }







    private val handler: Handler = @SuppressLint("HandlerLeak")
    object : Handler() {
        override fun handleMessage(msg: Message) {
            viewOfLayout.tv_currentTimeCapture.text = milliSecondsToTimer(msg.arg1.toLong())


        }
    }
    private fun milliSecondsToTimer(millisecond: Long): String {
        var finalTimerStirng = ""
        var secondsString = ""
        var minutesString = ""
        var hours: Int = ((millisecond / (1000 * 60 * 60)).toInt())
        var minutes: Int = ((((millisecond % (1000 * 60 * 60)) / (1000 * 60)).toInt()))
        var seconds: Int = ((((millisecond % (1000 * 60 * 60)) % (1000 * 60) / 1000).toInt()))
        if (hours < 10) {
            finalTimerStirng = "0$hours:"
        }
        if (minutes < 10) {
            minutesString = "0$minutes:"
        } else {
            minutesString = "$minutes:"
        }
        if (seconds < 10) {
            secondsString = "0$seconds"
        } else {
            secondsString = "$seconds"
        }
        if (hours < 10) {
            finalTimerStirng = "$minutesString$secondsString"

        } else {
            finalTimerStirng = "$finalTimerStirng$minutesString$secondsString"

        }
        return finalTimerStirng
    }

    override fun onClickItem(path: String?) {
        getPath = path.toString()
        val intent = Intent(context, EditPhotoActivity::class.java)
        intent.putExtra("PATH IMG", path)
        startActivity(intent)
    }


}

