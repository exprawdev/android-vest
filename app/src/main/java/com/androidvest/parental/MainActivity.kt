package com.androidvest.parental

import android.app.Activity
import android.os.Bundle
import android.view.Gravity
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView

class MainActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val rootLayout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER
            setPadding(32, 32, 32, 32)
            setBackgroundColor(0xFF101820.toInt())
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        }

        val title = TextView(this).apply {
            text = "ANDROID VEST"
            textSize = 30f
            gravity = Gravity.CENTER
            setTextColor(0xFFFFFFFF.toInt())
        }

        val subtitle = TextView(this).apply {
            text = "Parental Control"
            textSize = 16f
            gravity = Gravity.CENTER
            setTextColor(0xFFB0BEC5.toInt())
        }

        rootLayout.addView(title)
        rootLayout.addView(subtitle)

        setContentView(rootLayout)
    }
}
