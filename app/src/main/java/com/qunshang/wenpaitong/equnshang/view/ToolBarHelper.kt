package com.qunshang.wenpaitong.equnshang.view

import android.app.Activity
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.qunshang.wenpaitong.R

class ToolBarHelper(var view : View) {

    init {

        view.findViewById<ImageView>(R.id.toolbar_back).setOnClickListener {
            val activity : Activity = view.context as Activity
            activity.finish()
        }
    }

    fun setTitle(title : String){
        view.findViewById<TextView>(R.id.toolbar_title).setText(title)
    }

}