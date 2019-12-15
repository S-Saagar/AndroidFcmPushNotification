package com.example.pushnotificationexample.activities

import android.graphics.Typeface
import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.pushnotificationexample.R
import kotlinx.android.synthetic.main.content_toolbar.view.*

open class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        window.setBackgroundDrawableResource(R.drawable.clean_background)
        super.onCreate(savedInstanceState)
    }

    protected fun setToolBarData(toolBar: Int, title: String?, subTitle: String? = "", showBackArrow: Boolean = true, textBold: Boolean = false) {
        val toolBarView = findViewById<Toolbar>(toolBar)
        if (title.isNullOrBlank()) {
            throw NullPointerException("Title is require")
        }
        if (textBold) {
            toolBarView.tvTitle.setTypeface(toolBarView.tvTitle.typeface, Typeface.BOLD)
        } else {
            toolBarView.tvTitle.setTypeface(toolBarView.tvTitle.typeface, Typeface.NORMAL)
        }
        toolBarView.tvTitle.text = title
        toolBarView.tvSubTitle.text = subTitle
        toolBarView.tvSubTitle.visibility = if (subTitle.isNullOrBlank()) {
            GONE
        } else {
            VISIBLE
        }
        toolBarView.ivBack.visibility = if (showBackArrow) {
            VISIBLE
        } else {
            GONE
        }
        toolBarView.ivBack.setOnClickListener {
            onBackPressed()
        }
    }
}