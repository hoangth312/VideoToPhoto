package com.example.videotophoto.fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Bitmap.CompressFormat
import android.media.MediaMetadataRetriever
import android.media.MediaPlayer.OnPreparedListener
import android.net.Uri
import android.os.*
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.videotophoto.Adapter.QuickCaptureAdapter
import com.example.videotophoto.R
import com.example.videotophoto.ui.EditPhotoActivity
import com.example.videotophoto.videotophoto
import kotlinx.android.synthetic.main.fragment_quickcapture.view.*
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.security.AccessController.getContext
import java.util.*


@Suppress("DEPRECATION")
class FragmentQuickCapture : Fragment(), QuickCaptureAdapter.ViewHolder.Callback {
    var videoUrl = ""
    var imageList = ArrayList<File>()
    var cutImageList = ArrayList<Bitmap>()

    @Volatile
    var stopthread = false
    var retriever = MediaMetadataRetriever()
    var type = ""
    var quality = ""
    var endWiths = ".jpg"
    var quickCaptureAdapter: QuickCaptureAdapter? = null
    var getPath = ""




    private lateinit var viewOfLayout: View
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        viewOfLayout = inflater.inflate(R.layout.fragment_quickcapture, container, false)

        if (imageList.isNotEmpty()) {
            for (i in imageList.indices) {
                if (imageList[i].absolutePath == getPath) {
                    imageList.removeAt(i)
                    if (cutImageList.isNotEmpty()) {
                    }
                    cutImageList.removeAt(i)
                    break
                }
            }
        }
        viewOfLayout.imv_cameraCutQuickCapture.setOnClickListener() { cutImagefromVideo() }
        return viewOfLayout


    }

    companion object {

        fun newInstance(): FragmentQuickCapture {
            return FragmentQuickCapture()
        }


    }

    override fun onResume() {
        super.onResume()

        loadVideo()
        getDataVideo(videoUrl)
        startVideo()
    }

    override fun onStart() {
        super.onStart()
        sharedPreferencesfromSetting()
    }

    override fun onPause() {
        super.onPause()
        stopthread = true
    }

    override fun onStop() {
        super.onStop()
        stopthread = true
        viewOfLayout.videoViewQuickCapture.pause()
        viewOfLayout.videoViewQuickCapture.suspend()
    }


    private fun sharedPreferencesfromSetting() {
        var sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        quality = sharedPreferences!!.getString("list_quality", "High").toString()
        type = sharedPreferences!!.getString("list_file_format ", "JPG").toString()
    }


    private fun loadVideo() {
        val intent = requireActivity().intent
        videoUrl = intent.getStringExtra("VIDEO PATH")!!
        var uri: Uri = Uri.parse(videoUrl)
        viewOfLayout.videoViewQuickCapture.setVideoURI(uri)
    }


    private fun getDataVideo(videoPath: String) {
        var videoFile = File(videoPath)
        videoFile = File(videoFile.path)
        val uri = Uri.parse(videoPath)
        retriever.setDataSource(context, uri)
        viewOfLayout.videoViewQuickCapture.setVideoURI(uri)
        viewOfLayout.videoViewQuickCapture.setOnPreparedListener(OnPreparedListener {
            val time = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
            val timeInMillisec = milliSecondsToTimer(time!!.toLong())
            viewOfLayout.tv_endQuickCapture.text = timeInMillisec

            viewOfLayout.seekBar_QuickCapture.max = viewOfLayout.videoViewQuickCapture.duration
        })

        var nameVideo = videoFile.name.replace(".mp4", "")
        viewOfLayout.tv_nameVideoQuickCapture.text = nameVideo


    }


    fun checkPlay(playing: Boolean) {
        if (playing) {
            viewOfLayout.imv_PlaySnapshot.setImageResource(R.drawable.ic_pause)
        } else {
            viewOfLayout.imv_PlaySnapshot.setImageResource(R.drawable.ic_play)
        }

    }

    @SuppressLint("ClickableViewAccessibility")
    fun startVideo() {

        viewOfLayout.run {
            imv_PlaySnapshot.setOnClickListener(View.OnClickListener {
                if (!videoViewQuickCapture.isPlaying) {
                    videoViewQuickCapture.start()
                    checkPlay(true)
                    val handler = Handler()
                    handler.postDelayed({ imv_PlaySnapshot.visibility = GONE }, 3000)
                } else {
                    videoViewQuickCapture.pause()
                    checkPlay(false)
                    viewOfLayout.imv_PlaySnapshot.visibility = VISIBLE
                }

            })
        }

        viewOfLayout.videoViewQuickCapture.setOnTouchListener { v, event ->
            viewOfLayout.imv_PlaySnapshot.visibility = VISIBLE
            false
        }


        viewOfLayout.seekBar_QuickCapture.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    viewOfLayout.videoViewQuickCapture.pause()
                    viewOfLayout.videoViewQuickCapture.seekTo(progress)
                    seekBar.progress = progress
                    checkPlay(false)
                    viewOfLayout.imv_PlaySnapshot.visibility = VISIBLE
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })
        Thread(Runnable {
            while (viewOfLayout.videoViewQuickCapture != null) {
                try {
                    if (viewOfLayout.videoViewQuickCapture.isPlaying) {
                        val msg = Message()
                        msg.arg1 = viewOfLayout.videoViewQuickCapture.currentPosition
                        handler.sendMessage(msg)
                        Thread.sleep(1)

                        if (msg.arg1 == viewOfLayout.videoViewQuickCapture.duration) {
                            viewOfLayout.videoViewQuickCapture.stopPlayback()
                            checkPlay(false)
                            viewOfLayout.videoViewQuickCapture.resume()
                        }
                    } else {
                        val msg = Message()
                        msg.arg1 = viewOfLayout.videoViewQuickCapture.currentPosition
                        handler.sendMessage(msg)
                        Thread.sleep(1)
                    }
                    if (stopthread) {
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

    private val handler: Handler = @SuppressLint("HandlerLeak")
    object : Handler() {
        override fun handleMessage(msg: Message) {
            viewOfLayout.tv_currentQuickCapture.text = milliSecondsToTimer(msg.arg1.toLong())
            viewOfLayout.seekBar_QuickCapture.progress = msg.arg1
        }
    }

    private fun cutImagefromVideo() {
        checkPlay(true)
        viewOfLayout.videoViewQuickCapture.pause()
        var handler = Handler()
        handler.postDelayed({ viewOfLayout.imv_PlaySnapshot.visibility = GONE }, 3000)
        val currentTime: Float = viewOfLayout.videoViewQuickCapture.getCurrentPosition() * 1000.toFloat()
        retriever = MediaMetadataRetriever()
        retriever.setDataSource(videoUrl)
        val brFrame: Bitmap? = retriever.getFrameAtTime(currentTime.toLong(), MediaMetadataRetriever.OPTION_CLOSEST)
        cutImageList.add(brFrame!!)
        try {
            createFileImage(brFrame)
        } catch (e: IOException) {
            e.printStackTrace()
        }
        loadRecyclerView(cutImageList, imageList)
        handler.postDelayed({ viewOfLayout.videoViewQuickCapture.start() }, 1000)
    }


    private fun createFileImage(bitmap: Bitmap) {
        val file = File(Environment.getExternalStorageDirectory().absoluteFile.toString() + "/FrameCaptured")
        if (!file.exists()) {
            file.mkdir()
        }
        val generator = Random()
        var n = 10000
        n = generator.nextInt(n)
        val fileName = "Image-" + n + endWiths
        var bitmapImage: CompressFormat
        if (type == "JPG") {
            endWiths = ".jpg"
            bitmapImage = CompressFormat.JPEG
        } else {
            endWiths = ".png"
            bitmapImage = CompressFormat.PNG
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
        viewOfLayout.rv_CutImageList_QuickCapture.layoutManager = linearLayoutManager
        viewOfLayout.rv_CutImageList_QuickCapture.adapter = quickCaptureAdapter
        quickCaptureAdapter!!.notifyDataSetChanged()
    }


    fun setQuality(): Int {
        if ("Best" == quality) {
            return 100
        }
        if ("Very High" == quality) {
            return 85
        }
        if ("High" == quality) {
            return 75
        }
        if ("Medium" == quality) {
            return 65
        }
        if ("Low" == quality) {
            return 50
        }
        return 75
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






