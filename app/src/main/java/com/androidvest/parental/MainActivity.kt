
package com.androidvest.parental

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Bundle
import android.graphics.Color
import android.content.res.ColorStateList
import android.view.Gravity
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView

class MainActivity : Activity() {

    private val backgroundColor = Color.rgb(15, 23, 42)
    private val cardColor = Color.rgb(30, 41, 59)
    private val primaryColor = Color.rgb(56, 189, 248)
    private val textColor = Color.WHITE
    private val secondaryColor = Color.rgb(148, 163, 184)

    private lateinit var root: LinearLayout

    private var locationStatusView: TextView? = null
    private var cameraStatusView: TextView? = null
    private var microphoneStatusView: TextView? = null

    companion object {
        private const val LOCATION_PERMISSION_REQUEST = 1001
        private const val CAMERA_PERMISSION_REQUEST = 1002
        private const val MICROPHONE_PERMISSION_REQUEST = 1003
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (applicationContext.packageName == "com.androidvest.child") {
            showChildSetup()
        } else {
            showDashboard()
        }
    }

    private fun createRoot(): ScrollView {
        val scrollView = ScrollView(this).apply {
            setBackgroundColor(backgroundColor)
            isFillViewport = true
        }

        root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(dp(20), dp(28), dp(20), dp(28))
        }

        scrollView.addView(root)
        return scrollView
    }

    // PARENT APPLICATION

    private fun showDashboard() {
        val scrollView = createRoot()

        addTitle("ANDROID VEST")
        addSubtitle("Parental Control Dashboard")

        addActionButton("SETTINGS") {
            showSettings()
        }

        addSectionTitle("DEVICE STATUS")

        addCard(
            "Child Device",
            "Not connected",
            "Connect a child device to begin."
        )

        addSectionTitle("ACTIVITY OVERVIEW")

        addCard(
            "Screen Time",
            "Not available",
            "Usage information will appear after setup."
        )

        addCard(
            "Location",
            "Not available",
            "Location requires device setup and permission."
        )

        addCard(
            "Application Activity",
            "Not available",
            "Installed app activity will appear here."
        )

        addSectionTitle("PARENTAL CONTROLS")

        addCard(
            "Usage Limits",
            "Not configured",
            "Set daily screen time limits."
        )

        addCard(
            "App Management",
            "Not configured",
            "Manage allowed applications."
        )

        addCard(
            "Safety Settings",
            "Not configured",
            "Configure family safety preferences."
        )

        addFooter()
        setContentView(scrollView)
    }

    private fun showSettings() {
        val scrollView = createRoot()

        addTitle("SETTINGS")
        addSubtitle("Android Vest Configuration")

        addSectionTitle("ACCOUNT")

        addCard(
            "Parent Account",
            "Not configured",
            "Parent account information and account preferences."
        )

        addActionButton("Parent Account Settings") {
            showInformation(
                "Parent Account",
                "Account configuration will be available in a future update."
            )
        }

        addSectionTitle("CHILD DEVICE")

        addCard(
            "Device Connection",
            "Not connected",
            "Pair a child device using a secure pairing process."
        )

        addActionButton("Device Pairing") {
            showInformation(
                "Device Pairing",
                "Secure device pairing will be implemented in the next stage."
            )
        }

        addSectionTitle("SECURITY AND PRIVACY")

        addCard(
            "Permissions",
            "Not configured",
            "Device permissions will be requested only when required."
        )

        addActionButton("Permission Information") {
            showInformation(
                "Permissions",
                "Android Vest will explain each permission before requesting it."
            )
        }

        addCard(
            "Privacy",
            "Family safety",
            "Monitoring features must be visible and use appropriate consent."
        )

        addActionButton("Privacy Information") {
            showInformation(
                "Privacy",
                "Personal data should be protected and used only for family safety."
            )
        }

        addSectionTitle("APPLICATION")

        addCard(
            "Application Version",
            "Version 1.0",
            "Android Vest Parental Control."
        )

        addActionButton("Back to Dashboard") {
            showDashboard()
        }

        addFooter()
        setContentView(scrollView)
    }

    private fun showInformation(
        title: String,
        message: String
    ) {
        val scrollView = createRoot()

        addTitle(title)
        addSubtitle(message)

        addActionButton("Back to Settings") {
            showSettings()
        }

        setContentView(scrollView)
    }

    // CHILD APPLICATION

    private fun showChildSetup() {
        val scrollView = createRoot()

        locationStatusView = null
        cameraStatusView = null
        microphoneStatusView = null

        addTitle("ANDROID VEST CHILD")
        addSubtitle("Device Setup & Permissions")

        addSectionTitle("DEVICE PERMISSIONS")

        addLocationPermissionCard()
        addCameraPermissionCard()
        addMicrophonePermissionCard()

        addCard(
            "Screen Sharing",
            "Not configured",
            "Screen sharing requires confirmation through the Android system."
        )

        addSectionTitle("PRIVACY AND SECURITY")

        addCard(
            "Permission Information",
            "User controlled",
            "You can review or revoke permissions in Android Settings."
        )

        addCard(
            "Device Status",
            "Setup required",
            "Complete the required setup before connecting to the parent device."
        )

        addFooter()
        setContentView(scrollView)
    }

    // LOCATION PERMISSION

    private fun addLocationPermissionCard() {
        val card = createPermissionCard(
            "Location Access",
            "Allow location sharing after reviewing the Android permission request."
        )

        val statusView = createStatusView()
        locationStatusView = statusView

        val permissionButton = createPermissionButton(
            "Allow Location Access"
        ) {
            requestLocationPermission()
        }

        card.addView(statusView, marginParams(top = 8))
        card.addView(permissionButton, marginParams(top = 12))

        root.addView(card, marginParams(bottom = 12))

        updateLocationStatus()
    }

    private fun requestLocationPermission() {
        val fineGranted = checkSelfPermission(
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        val coarseGranted = checkSelfPermission(
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        if (fineGranted || coarseGranted) {
            updateLocationStatus()
            return
        }

        requestPermissions(
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ),
            LOCATION_PERMISSION_REQUEST
        )
    }

    private fun updateLocationStatus() {
        val fineGranted = checkSelfPermission(
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        val coarseGranted = checkSelfPermission(
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        val status = when {
            fineGranted -> "Allowed - Precise location"
            coarseGranted -> "Allowed - Approximate location"
            else -> "Not granted"
        }

        locationStatusView?.text = status
    }

    // CAMERA PERMISSION

    private fun addCameraPermissionCard() {
        val card = createPermissionCard(
            "Camera Access",
            "Allow camera permission for user-visible camera features."
        )

        val statusView = createStatusView()
        cameraStatusView = statusView

        val permissionButton = createPermissionButton(
            "Allow Camera Access"
        ) {
            requestCameraPermission()
        }

        card.addView(statusView, marginParams(top = 8))
        card.addView(permissionButton, marginParams(top = 12))

        root.addView(card, marginParams(bottom = 12))

        updateCameraStatus()
    }

    private fun requestCameraPermission() {
        val cameraGranted = checkSelfPermission(
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED

        if (cameraGranted) {
            updateCameraStatus()
            return
        }

        requestPermissions(
            arrayOf(Manifest.permission.CAMERA),
            CAMERA_PERMISSION_REQUEST
        )
    }

    private fun updateCameraStatus() {
        val cameraGranted = checkSelfPermission(
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED

        cameraStatusView?.text = if (cameraGranted) {
            "Allowed"
        } else {
            "Not granted"
        }
    }

    // MICROPHONE PERMISSION

    private fun addMicrophonePermissionCard() {
        val card = createPermissionCard(
            "Microphone Access",
            "Allow microphone permission for user-visible audio features."
        )

        val statusView = createStatusView()
        microphoneStatusView = statusView

        val permissionButton = createPermissionButton(
            "Allow Microphone Access"
        ) {
            requestMicrophonePermission()
        }

        card.addView(statusView, marginParams(top = 8))
        card.addView(permissionButton, marginParams(top = 12))

        root.addView(card, marginParams(bottom = 12))

        updateMicrophoneStatus()
    }

    private fun requestMicrophonePermission() {
        val microphoneGranted = checkSelfPermission(
            Manifest.permission.RECORD_AUDIO
        ) == PackageManager.PERMISSION_GRANTED

        if (microphoneGranted) {
            updateMicrophoneStatus()
            return
        }

        requestPermissions(
            arrayOf(Manifest.permission.RECORD_AUDIO),
            MICROPHONE_PERMISSION_REQUEST
        )
    }

    private fun updateMicrophoneStatus() {
        val microphoneGranted = checkSelfPermission(
            Manifest.permission.RECORD_AUDIO
        ) == PackageManager.PERMISSION_GRANTED

        microphoneStatusView?.text = if (microphoneGranted) {
            "Allowed"
        } else {
            "Not granted"
        }
    }

    // PERMISSION CALLBACK

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(
            requestCode,
            permissions,
            grantResults
        )

        when (requestCode) {
            LOCATION_PERMISSION_REQUEST -> updateLocationStatus()
            CAMERA_PERMISSION_REQUEST -> updateCameraStatus()
            MICROPHONE_PERMISSION_REQUEST -> updateMicrophoneStatus()
        }
    }

    // SHARED UI COMPONENTS

    private fun createPermissionCard(
        title: String,
        description: String
    ): LinearLayout {
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

        val descriptionView = TextView(this).apply {
            text = description
            textSize = 12f
            setTextColor(secondaryColor)
        }

        card.addView(titleView)
        card.addView(descriptionView, marginParams(top = 6))

        return card
    }

    private fun createStatusView(): TextView {
        return TextView(this).apply {
            textSize = 14f
            setTextColor(primaryColor)
        }
    }

    private fun createPermissionButton(
        title: String,
        action: () -> Unit
    ): Button {
        return Button(this).apply {
            text = title
            textSize = 14f
            isAllCaps = false
            setTextColor(Color.WHITE)
            backgroundTintList = ColorStateList.valueOf(primaryColor)

            setOnClickListener {
                action()
            }
        }
    }

    private fun addTitle(title: String) {
        val view = TextView(this).apply {
            text = title
            textSize = 27f
            setTextColor(textColor)
            setTypeface(null, android.graphics.Typeface.BOLD)
        }

        root.addView(view)
    }

    private fun addSubtitle(subtitle: String) {
        val view = TextView(this).apply {
            text = subtitle
            textSize = 14f
            setTextColor(secondaryColor)
        }

        root.addView(view, marginParams(top = 6, bottom = 20))
    }

    private fun addSectionTitle(title: String) {
        val view = TextView(this).apply {
            text = title
            textSize = 13f
            setTextColor(primaryColor)
            setTypeface(null, android.graphics.Typeface.BOLD)
        }

        root.addView(view, marginParams(top = 18, bottom = 10))
    }

    private fun addCard(
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

        root.addView(card, marginParams(bottom = 12))
    }

    private fun addActionButton(
        title: String,
        action: () -> Unit
    ) {
        val button = Button(this).apply {
            text = title
            textSize = 14f
            isAllCaps = false
            setTextColor(Color.WHITE)
            backgroundTintList = ColorStateList.valueOf(primaryColor)

            setOnClickListener {
                action()
            }
        }

        root.addView(button, marginParams(top = 6, bottom = 12))
    }

    private fun addFooter() {
        val footer = TextView(this).apply {
            text = "ANDROID VEST • VERSION 1.0"
            textSize = 11f
            gravity = Gravity.CENTER
            setTextColor(secondaryColor)
        }

        root.addView(footer, marginParams(top = 24))
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
