
package com.androidvest.parental

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView

class AuthActivity : Activity() {

    private val authManager = FirebaseAuthManager()

    private val backgroundColor = Color.rgb(15, 23, 42)
    private val cardColor = Color.rgb(30, 41, 59)
    private val primaryColor = Color.rgb(56, 189, 248)
    private val textColor = Color.WHITE
    private val secondaryColor = Color.rgb(148, 163, 184)

    private lateinit var root: LinearLayout
    private lateinit var emailInput: EditText
    private lateinit var passwordInput: EditText
    private lateinit var messageView: TextView
    private lateinit var progressBar: ProgressBar
    private lateinit var submitButton: Button
    private lateinit var modeButton: Button

    private var isRegisterMode = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (packageName == "com.androidvest.child") {
            authenticateChild()
            return
        }

        val currentUser = authManager.getCurrentUser()

        if (currentUser != null && !currentUser.isAnonymous) {
            openDashboard()
            return
        }

        if (currentUser != null && currentUser.isAnonymous) {
            authManager.signOut()
        }

        showAuthScreen()
    }

    private fun authenticateChild() {
        showLoadingScreen()

        authManager.signInChildAnonymously { result ->
            runOnUiThread {
                result.onSuccess {
                    openDashboard()
                }.onFailure { error ->
                    showErrorScreen(
                        error.message ?: "Child authentication failed."
                    )
                }
            }
        }
    }

    private fun showLoadingScreen() {
        root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER
            setPadding(32, 32, 32, 32)
            setBackgroundColor(backgroundColor)
        }

        val title = TextView(this).apply {
            text = "ANDROID VEST CHILD"
            textSize = 23f
            gravity = Gravity.CENTER
            setTextColor(textColor)
        }

        val progress = ProgressBar(this)

        root.addView(title)
        root.addView(progress)

        setContentView(root)
    }

    private fun showErrorScreen(message: String) {
        root.removeAllViews()

        val title = TextView(this).apply {
            text = "CHILD CONNECTION ERROR"
            textSize = 20f
            gravity = Gravity.CENTER
            setTextColor(textColor)
        }

        val description = TextView(this).apply {
            text = message
            textSize = 14f
            gravity = Gravity.CENTER
            setTextColor(secondaryColor)
        }

        val retryButton = Button(this).apply {
            text = "Try Again"
            setOnClickListener {
                authenticateChild()
            }
        }

        root.addView(title)
        root.addView(description)
        root.addView(retryButton)
    }

    private fun showAuthScreen() {
        root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER
            setPadding(24, 32, 24, 32)
            setBackgroundColor(backgroundColor)
        }

        val scrollView = android.widget.ScrollView(this).apply {
            isFillViewport = true
            addView(root)
        }

        val title = TextView(this).apply {
            text = "ANDROID VEST"
            textSize = 28f
            gravity = Gravity.CENTER
            setTextColor(textColor)
            setTypeface(null, android.graphics.Typeface.BOLD)
        }

        val subtitle = TextView(this).apply {
            text = "Parent Account"
            textSize = 15f
            gravity = Gravity.CENTER
            setTextColor(secondaryColor)
        }

        emailInput = createInput(
            "Email address",
            android.text.InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
        )

        passwordInput = createInput(
            "Password",
            android.text.InputType.TYPE_CLASS_TEXT or
                android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD
        )

        messageView = TextView(this).apply {
            textSize = 13f
            setTextColor(secondaryColor)
            gravity = Gravity.CENTER
        }

        progressBar = ProgressBar(this).apply {
            visibility = View.GONE
        }

        submitButton = createButton("Sign In") {
            submitAuthentication()
        }

        modeButton = createButton("Create New Account") {
            isRegisterMode = !isRegisterMode
            updateMode()
        }

        root.addView(title)
        root.addView(subtitle, params(0, 16))
        root.addView(emailInput, params(0, 28))
        root.addView(passwordInput, params(0, 12))
        root.addView(messageView, params(0, 12))
        root.addView(progressBar, params(0, 8))
        root.addView(submitButton, params(0, 16))
        root.addView(modeButton, params(0, 8))

        setContentView(scrollView)
        updateMode()
    }

    private fun createInput(
        hintText: String,
        inputType: Int
    ): EditText {
        return EditText(this).apply {
            hint = hintText
            this.inputType = inputType
            textSize = 15f
            setTextColor(textColor)
            setHintTextColor(secondaryColor)
            setPadding(16, 12, 16, 12)
            setBackgroundColor(cardColor)
        }
    }

    private fun createButton(
        title: String,
        action: () -> Unit
    ): Button {
        return Button(this).apply {
            text = title
            isAllCaps = false
            setTextColor(Color.WHITE)
            setBackgroundColor(primaryColor)
            setOnClickListener {
                action()
            }
        }
    }

    private fun updateMode() {
        if (isRegisterMode) {
            submitButton.text = "Create Account"
            modeButton.text = "Already have an account? Sign In"
        } else {
            submitButton.text = "Sign In"
            modeButton.text = "Create New Account"
        }

        messageView.text = ""
    }

    private fun submitAuthentication() {
        val email = emailInput.text.toString().trim()
        val password = passwordInput.text.toString()

        if (email.isEmpty()) {
            messageView.text = "Please enter your email address."
            return
        }

        if (password.length < 6) {
            messageView.text = "Password must contain at least 6 characters."
            return
        }

        setLoading(true)
        messageView.text = ""

        val callback: (Result<com.google.firebase.auth.FirebaseUser>) -> Unit =
            { result ->
                runOnUiThread {
                    setLoading(false)

                    result.onSuccess {
                        openDashboard()
                    }.onFailure { error ->
                        messageView.text =
                            error.message ?: "Authentication failed."
                    }
                }
            }

        if (isRegisterMode) {
            authManager.registerParent(email, password, callback)
        } else {
            authManager.signInParent(email, password, callback)
        }
    }

    private fun setLoading(loading: Boolean) {
        progressBar.visibility =
            if (loading) View.VISIBLE else View.GONE

        submitButton.isEnabled = !loading
        modeButton.isEnabled = !loading
    }

    private fun openDashboard() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun params(
        top: Int,
        bottom: Int
    ): LinearLayout.LayoutParams {
        return LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        ).apply {
            topMargin = top
            bottomMargin = bottom
        }
    }
}
