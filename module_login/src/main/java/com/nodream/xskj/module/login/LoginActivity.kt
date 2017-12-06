package com.nodream.xskj.module.login

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.annotation.TargetApi
import android.content.pm.PackageManager
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.app.LoaderManager.LoaderCallbacks
import android.content.CursorLoader
import android.content.Loader
import android.database.Cursor
import android.net.Uri
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.provider.ContactsContract
import android.text.TextUtils
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.ArrayAdapter
import android.widget.TextView

import java.util.ArrayList
import android.Manifest.permission.READ_CONTACTS
import android.content.Context
import android.graphics.drawable.Drawable
import android.support.v4.content.ContextCompat
import android.support.v4.content.res.ResourcesCompat
import android.view.inputmethod.InputMethodManager
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.nodream.xskj.commonlib.base.BaseActivity
import com.nodream.xskj.commonlib.utils.ProgressDialogUtil
import com.nodream.xskj.commonlib.utils.ToastUtil
import com.nodream.xskj.module.R
import com.nodream.xskj.module.login.presenter.LoginPresenter
import com.orhanobut.logger.Logger

import kotlinx.android.synthetic.main.activity_login.*

/**
 * A login screen that offers login via email/password.
 */
@Route(path = "/login/loginactivity")
class LoginActivity : BaseActivity(), LoginContract.View{

    private var isActive: Boolean = true

//    private var mLoginPresenter: LoginContract.Presenter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        init()
        password.setOnEditorActionListener(TextView.OnEditorActionListener { _, id, _ ->
            if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                attemptLogin()
                return@OnEditorActionListener true
            }
            false
        })

        email_sign_in_button.setOnClickListener { attemptLogin() }
        login_to_register.setOnClickListener{
            ARouter.getInstance().build("/login/registeractivity").navigation()
        }
        login_back.setOnClickListener{
            finish()
        }
    }
    private fun init() {
//        LoginPresenter(this).start(this)
    }

    override fun isActive(): Boolean {
        return isActive
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        isActive = true
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        isActive = false
    }

    override fun getUserNameFormView(): String {
        return username.text.toString()
    }

    override fun getPassWordFormView(): String {
        return password.text.toString()
    }

    /**
     * 跳转到主页
     */
    override fun goToHomeView() {
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private fun attemptLogin() {
        key()
        // Reset errors.
        username.error = null
        password.error = null

        // Store values at the time of the login attempt.
        val usernameStr = username.text.toString()
        val passwordStr = password.text.toString()

        var cancel = false
        var focusView: View? = null

        // Check for a valid email address.
        if (TextUtils.isEmpty(usernameStr)) {
            username.error = getString(R.string.error_field_phone)
            focusView = username
            cancel = true
        }
        if (TextUtils.isEmpty(passwordStr)) {
            username.error = getString(R.string.error_field_password)
            focusView = username
            cancel = true
        }
        if (!TextUtils.isEmpty(passwordStr) && !isPasswordValid(passwordStr)) {
            password.error = getString(R.string.error_invalid_password)
            focusView = password
            cancel = true
        }
        if (cancel) {
            focusView?.requestFocus()
        } else {
//            ProgressDialogUtil.showProgressDialog(this)
            LoginPresenter(this,this).login()
        }
    }


    private fun isPasswordValid(password: String): Boolean {
        return password.length in 6..18
    }

    private fun key() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInput(0,InputMethodManager.HIDE_NOT_ALWAYS)
    }

//    /**
//     * Shows the progress UI and hides the login form.
//     */
//    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
//    fun showProgress(show: Boolean) {
//        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
//        // for very easy animations. If available, use these APIs to fade-in
//        // the progress spinner.
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
//            val shortAnimTime = resources.getInteger(android.R.integer.config_shortAnimTime).toLong()
//
//            login_form.visibility = if (show) View.GONE else View.VISIBLE
//            login_form.animate()
//                    .setDuration(shortAnimTime)
//                    .alpha((if (show) 0 else 1).toFloat())
//                    .setListener(object : AnimatorListenerAdapter() {
//                        override fun onAnimationEnd(animation: Animator) {
//                            login_form.visibility = if (show) View.GONE else View.VISIBLE
//                        }
//                    })
//
//            login_progress.visibility = if (show) View.VISIBLE else View.GONE
//            login_progress.animate()
//                    .setDuration(shortAnimTime)
//                    .alpha((if (show) 1 else 0).toFloat())
//                    .setListener(object : AnimatorListenerAdapter() {
//                        override fun onAnimationEnd(animation: Animator) {
//                            login_progress.visibility = if (show) View.VISIBLE else View.GONE
//                        }
//                    })
//        } else {
//            // The ViewPropertyAnimator APIs are not available, so simply show
//            // and hide the relevant UI components.
//            login_progress.visibility = if (show) View.VISIBLE else View.GONE
//            login_form.visibility = if (show) View.GONE else View.VISIBLE
//        }
//    }
}
