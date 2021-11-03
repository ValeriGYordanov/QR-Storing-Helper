package studios.devs.mobi.qrstoringhelper.repositories

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.SharedPreferences
import android.os.Build
import android.provider.Settings
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.Profile
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import studios.devs.mobi.qrstoringhelper.model.OfflineUserProfile

class LaunchRepository(private val sharedPreferences: SharedPreferences, private val contentResolver: ContentResolver) : ILaunchRepository{

    private fun setupFacebookAuth(facebookLoginCallback: CallbackManager) {

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

    private fun fetchOfflineUserProfile(): OfflineUserProfile? {
        val defaultValue = "Unknown"
        val name = sharedPreferences.getString("accountName", defaultValue) //"No name defined" is the default value.
        val id = sharedPreferences.getLong("accountId", 0L) //0 is the default value.
        return if (name != "Unknown" && id != 0L) {
            OfflineUserProfile(id, name ?: "")
        } else {
            null
        }
    }

    @SuppressLint("HardwareIds")
    private fun createOfflineUserProfile(googleAccount: GoogleSignInAccount? = null, facebookAccount: Profile? = null) {
        googleAccount?.let { return }
        facebookAccount?.let { return }

        val editor = sharedPreferences.edit()

        var username = Settings.Secure.getString(contentResolver, "bluetooth_name")
        if (username.isNullOrBlank()){ username = Build.MANUFACTURER + Build.MODEL }

        editor.putString(OfflineUserProfile.USER_NAME, username)
        editor.putLong(OfflineUserProfile.USER_ID, username.hashCode().toLong())
        editor.apply()
    }
}