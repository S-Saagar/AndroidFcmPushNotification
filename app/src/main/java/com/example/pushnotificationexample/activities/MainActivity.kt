package com.example.pushnotificationexample.activities

import android.os.Bundle
import com.example.pushnotificationexample.PushApp
import com.example.pushnotificationexample.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setToolBarData(toolBar = toolBar.id, title = getString(R.string.main_activity), showBackArrow = false, textBold = true)

        PushApp.instance.notificationHelper.initPrimaryChannel()

        PushApp.instance.notificationHelper.cancelAlarm()
    }
}