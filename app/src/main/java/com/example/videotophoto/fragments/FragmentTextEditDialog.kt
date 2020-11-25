package com.example.videotophoto.fragments

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.videotophoto.R
import com.skydoves.colorpickerview.ColorPickerDialog
import com.skydoves.colorpickerview.listeners.ColorEnvelopeListener
import ja.burhanrashid52.photoeditor.PhotoEditor
import kotlinx.android.synthetic.main.fragment_text_edit_dialog.view.*


class FragmentTextEditDialog(var getContext: Context, var photoEditor: PhotoEditor) : DialogFragment() {

    lateinit var itemView: View
    lateinit var  textEditor: TextEditor
    private var colorText: Int = Color.parseColor("#000000")




    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        itemView = inflater.inflate(R.layout.fragment_text_edit_dialog, container, false)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))




        itemView.colorBroad.setOnClickListener { seletcColorText() }

        itemView.alignment.setOnClickListener { alignment() }

        itemView.tvDone.setOnClickListener {

             textEditor.onDone(itemView.addTextEditText.text.toString(), colorText)

            dismiss()

        }



        return itemView


    }


    private fun seletcColorText() {
        ColorPickerDialog.Builder(context)
                .setTitle("ColorPicker Dialog")
                .setPreferenceName("MyColorPickerDialog")
                .setPositiveButton(getString(R.string.ok),
                        ColorEnvelopeListener { envelope, fromUser ->

                            itemView.addTextEditText.setTextColor(envelope.color)
                           // itemView.addTextEditText.setHintTextColor(envelope.color)
                            colorText = envelope.color
                        })
                .setNegativeButton(getString(R.string.cancel)
                ) { dialogInterface, i -> dialogInterface.dismiss() }
                .attachAlphaSlideBar(true) // the default value is true.
                .attachBrightnessSlideBar(true) // the default value is true.
                .setBottomSpace(12) // set a bottom space between the last slidebar and buttons.
                .show()

    }

    private fun alignment() {

        when (itemView.alignment.tag.toString()) {

            "1" -> {
                itemView.alignment.tag = 2
                itemView.alignment.setImageResource(R.drawable.left_align)
                itemView.addTextEditText.gravity = Gravity.START
            }

            "2" -> {
                itemView.alignment.tag = 3
                itemView.alignment.setImageResource(R.drawable.right_align)
                itemView.addTextEditText.gravity = Gravity.END
            }


            "3" -> {
                itemView.alignment.tag = 1
                itemView.alignment.setImageResource(R.drawable.center_align)
                itemView.addTextEditText.gravity = Gravity.CENTER
            }
        }
    }

    fun setOnTextEditorListener(textEditor1: TextEditor) {
        textEditor = textEditor1
    }

    interface TextEditor {
        fun onDone(inputText: String?, colorCode: Int)

    }



}
