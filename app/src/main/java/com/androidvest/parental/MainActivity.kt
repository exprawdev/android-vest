
package com.androidvest.parental

import android.app.Activity
import android.os.Bundle
import android.graphics.Color
import android.view.Gravity
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView

class MainActivity : Activity() {

    private val backgroundColor = Color.rgb(15, 23, 42)
    private val cardColor = Color.rgb(30, 41, 59)
    private val primaryColor = Color.rgb(56, 189, 248)
    private val textColor = Color.WHITE
    private val secondaryColor = Color.rgb(148, 163, 184)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val scrollView = ScrollView(this).apply {
            setBackgroundColor(backgroundColor)
            isFillViewport = true
        }

        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(dp(20), dp(28), dp(20), dp(28))
        }

        val header = TextView(this).apply {
            text = "ANDROID VEST"
            textSize = 27f
            setTextColor(textColor)
            setTypeface(null, android.graphics.Typeface.BOLD)
        }

        root.addView(header)

        val subtitle = TextView(this).apply {
            text = "Parental Control Dashboard"
            textSize = 14f
            setTextColor(secondaryColor)
        }

        root.addView(subtitle, marginParams(top = 6, bottom = 24))

        addSectionTitle(root, "DEVICE STATUS")

        addCard(
            root,
            "Child Device",
            "Not connected",
            "Connect a child device to begin."
        )

        addSectionTitle(root, "ACTIVITY OVERVIEW")

        addCard(
            root,
            "Screen Time",
            "Not available",
            "Usage information will appear after setup."
        )

        addCard(
            root,
            "Location",
            "Not available",
            "Location requires device setup and permission."
        )

        addCard(
            root,
            "Application Activity",
            "Not available",
            "Installed app activity will appear here."
        )

        addSectionTitle(root, "PARENTAL CONTROLS")

        addCard(
            root,
            "Usage Limits",
            "Not configured",
            "Set daily screen time limits."
        )

        addCard(
            root,
            "App Management",
            "Not configured",
            "Manage allowed applications."
        )

        addCard(
            root,
            "Safety Settings",
            "Not configured",
            "Configure family safety preferences."
        )

        val footer = TextView(this).apply {
            text = "ANDROID VEST • VERSION 1.0"
            textSize = 11f
            gravity = Gravity.CENTER
            setTextColor(secondaryColor)
        }

        root.addView(footer, marginParams(top = 24))

        scrollView.addView(root)
        setContentView(scrollView)
    }

    private fun addSectionTitle(
        root: LinearLayout,
        title: String
    ) {
        val view = TextView(this).apply {
            text = title
            textSize = 13f
            setTextColor(primaryColor)
            setTypeface(null, android.graphics.Typeface.BOLD)
        }

        root.addView(view, marginParams(top = 18, bottom = 10))
    }

    private fun addCard(
        root: LinearLayout,
        title: String,
        status: String,
        description: String
    ) {
        val card = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(dp(16), dp(16), dp(16), dp(16))
            setBackgroundColor(cardColor)
        }

        val titleView = TextView(this).apply {
            text = title
            textSize = 17f
            setTextColor(textColor)
            setTypeface(null, android.graphics.Typeface.BOLD)
        }

        val statusView = TextView(this).apply {
            text = status
            textSize = 14f
            setTextColor(primaryColor)
        }

        val descriptionView = TextView(this).apply {
            text = description
            textSize = 12f
            setTextColor(secondaryColor)
        }

        card.addView(titleView)
        card.addView(statusView, marginParams(top = 8))
        card.addView(descriptionView, marginParams(top = 6))

        root.addView(
            card,
            marginParams(top = 0, bottom = 12)
        )
    }

    private fun marginParams(
        top: Int = 0,
        bottom: Int = 0
    ): LinearLayout.LayoutParams {
        return LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        ).apply {
            topMargin = dp(top)
            bottomMargin = dp(bottom)
        }
    }

    private fun dp(value: Int): Int {
        return (value * resources.displayMetrics.density).toInt()
    }
}
