package studios.devs.mobi.qrstoringhelper.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.facebook.*
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.jakewharton.rxbinding2.view.RxView
import dagger.android.AndroidInjection
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_launch.*
import studios.devs.mobi.qrstoringhelper.MainApplication.Companion.QR_STORING_HELPER_LOCAL_STORE
import studios.devs.mobi.qrstoringhelper.R
import studios.devs.mobi.qrstoringhelper.databinding.ActivityLaunchBinding
import studios.devs.mobi.qrstoringhelper.extensions.addTo
import studios.devs.mobi.qrstoringhelper.model.ButtonAction
import studios.devs.mobi.qrstoringhelper.model.OfflineUserProfile
import studios.devs.mobi.qrstoringhelper.model.Screen
import studios.devs.mobi.qrstoringhelper.viewmodels.LaunchViewModel
import studios.devs.mobi.qrstoringhelper.viewmodels.LaunchViewModelInput
import studios.devs.mobi.qrstoringhelper.viewmodels.LaunchViewModelInputOutput
import studios.devs.mobi.qrstoringhelper.viewmodels.LaunchViewModelOutput
import java.util.*
import javax.inject.Inject


class LaunchActivity : BaseActivity() {

    private val RC_SIGN_IN = 1

    private lateinit var facebookLoginCallback: CallbackManager
    private lateinit var mGoogleSignInClient: GoogleSignInClient

    lateinit var binding: ActivityLaunchBinding

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel: LaunchViewModelInputOutput by lazy {
        ViewModelProvider(this, viewModelFactory)
            .get(LaunchViewModel::class.java)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_launch)
        setupFacebookAuth()
        setupGoogleAuth()
    }

    @SuppressLint("HardwareIds")
    private fun createOfflineUserProfile(googleAccount: GoogleSignInAccount? = null, facebookAccount: Profile? = null) {
        googleAccount?.let { return }
        facebookAccount?.let { return }

        val editor = getSharedPreferences(QR_STORING_HELPER_LOCAL_STORE, MODE_PRIVATE).edit()

        var username = Settings.Secure.getString(contentResolver, "bluetooth_name")
        if (username.isNullOrBlank()){ username = Build.MANUFACTURER + Build.MODEL }

        editor.putString(OfflineUserProfile.USER_NAME, username)
        editor.putLong(OfflineUserProfile.USER_ID, username.hashCode().toLong())
        editor.apply()
    }

    private fun fetchOfflineUserProfile(): OfflineUserProfile? {
        val defaultValue = "Unknown"
        val prefs = getSharedPreferences(QR_STORING_HELPER_LOCAL_STORE, MODE_PRIVATE)
        val name = prefs.getString("accountName", defaultValue) //"No name defined" is the default value.
        val id = prefs.getLong("accountId", 0L) //0 is the default value.
        return if (name != "Unknown" && id != 0L) {
            OfflineUserProfile(id, name ?: "")
        } else {
            null
        }
    }

    private fun setupGoogleAuth() {
        val gso =
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build()

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
    }

    override fun onStart() {
        super.onStart()
        viewModel
            .bind(this)
            .addTo(compositeDisposable)

        val googleAccount = GoogleSignIn.getLastSignedInAccount(this)
        val facebookAccount = Profile.getCurrentProfile()
        val offlineAccount = fetchOfflineUserProfile()

        if(googleAccount != null || facebookAccount != null || offlineAccount != null) {
            startActivityByNameWithParamsAndFinish(Screen.Main(null))
        }
    }

    private fun setupFacebookAuth() {
        facebookLoginCallback = CallbackManager.Factory.create()
        facebook_login_button.setPermissions(listOf("public_profile", "email", "public_profile email"))

        LoginManager.getInstance().registerCallback(facebookLoginCallback,
            object : FacebookCallback<LoginResult?> {
                override fun onSuccess(loginResult: LoginResult?) {
                    Log.e("VAY", "Success")
                    // App code
                }

                override fun onCancel() {
                    Log.e("VAY", "Success")
                    // App code
                }

                override fun onError(exception: FacebookException) {
                    Log.e("VAY", "Success")
                    // App code
                }
            })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        facebookLoginCallback.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val task =
                GoogleSignIn.getSignedInAccountFromIntent(data)

            try {
                val account : GoogleSignInAccount? = task.getResult(ApiException::class.java)

//                val intent = Intent(this@SignInActivity, UserProfile::class.java)
//                startActivity(intent)

            } catch (e: ApiException) {
                // The ApiException status code indicates the detailed failure reason.
                // Please refer to the GoogleSignInStatusCodes class reference for more information.
                Log.e("TAG", "signInResult:failed code=" + e.statusCode)
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    fun openGoogleLogin(){
//        val intent = mGoogleSignInClient.signInIntent
//        startActivityForResult(intent, RC_SIGN_IN)
    }

    fun openOfflineLogin() {
        showWarning(
            message = getString(R.string.offline_version_message),
            positiveButton = ButtonAction(getString(R.string.proceed)) {
                createOfflineUserProfile()
                startActivityByNameWithParamsAndFinish(Screen.Main(null))
            },
            negativeButton = ButtonAction(getString(R.string.close)) {}
        )
    }
}

private fun LaunchViewModelInputOutput.bind(activity: LaunchActivity): List<Disposable> {
    return listOf(
        output.bind(activity.binding),
        output.bind(activity),
        input.bind(activity.binding),
        activity.binding.configureWith(activity)
    ).flatten()
}

private fun ActivityLaunchBinding.configureWith(activity: LaunchActivity): List<Disposable> {
    return listOf(
        RxView.clicks(googleLoginButton).subscribe { activity.openGoogleLogin() },
        RxView.clicks(offlineVersionBtn).subscribe { activity.openOfflineLogin() }
    )
}

private fun LaunchViewModelInput.bind(binding: ActivityLaunchBinding): List<Disposable> {
    return listOf(

    )
}

private fun LaunchViewModelOutput.bind(activity: LaunchActivity): List<Disposable> {
    return listOf(

    )
}

private fun LaunchViewModelOutput.bind(binding: ActivityLaunchBinding): List<Disposable> {
    return listOf(

    )
}