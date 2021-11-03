package studios.devs.mobi.qrstoringhelper.ui

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import io.reactivex.disposables.CompositeDisposable
import studios.devs.mobi.qrstoringhelper.model.ButtonAction
import studios.devs.mobi.qrstoringhelper.model.Screen
import java.io.Serializable


@SuppressLint("Registered")
open class BaseActivity : AppCompatActivity() {


    companion object {
        const val PARAM_DATA = "data"
    }

    val compositeDisposable = CompositeDisposable()


    override fun onStop() {
        super.onStop()
        compositeDisposable.clear()
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.dispose()
    }

    private fun getActivityClassFromString(activityName: String): Class<out Activity> {
        val fullActivityNameWithPath = "studios.devs.mobi.qrstoringhelper.ui." + activityName + "Activity"
        return Class.forName(fullActivityNameWithPath) as Class<Activity>
    }

    open fun startActivityByNameWithParams(screen: Screen<out Serializable?>) {
        // Cut the name of the Screen subclass
        val screenType = screen.toString().substringAfter('$').substringBefore('(')
        val intent = Intent(this, getActivityClassFromString(screenType))
        if (screen.someData != null) {
            intent.putExtra(PARAM_DATA, screen.someData)
        }
        startActivity(intent)
    }

    open fun startActivityByNameWithParamsAndFinish(screen: Screen<out Serializable?>) {
        startActivityByNameWithParams(screen)
        finish()
    }

    fun renderError(error: String){
        val errorDialog = AlertDialog.Builder(this)
        errorDialog.setMessage(error)
        errorDialog.create().show()
    }

    var loadingDialog: Dialog? = null

    fun renderLoading(shouldShow: Boolean){
        //FIXME Temporary solution
        if (loadingDialog == null && shouldShow){
//            loadingDialog = Dialog(this, R.style.LoadingDialogStyle)
//            loadingDialog?.setContentView(R.layout.dialog_loading)
//            loadingDialog?.findViewById<TextView>(R.id.loading_text)?.text = getString(R.string.loading)
            loadingDialog?.show()
        }
        if (!shouldShow){
            loadingDialog?.dismiss()
            loadingDialog = null
        }
    }

    fun getParameter(): Serializable {
        return intent.getSerializableExtra(PARAM_DATA)
    }

    fun showToastWithArgument(flag: Boolean, ifPositiveText: String, ifNegativeText: String){
        if (flag){
            showToast(ifPositiveText)
        }else{
            showToast(ifNegativeText)
        }
    }

    fun showWarning(title: String? = this.getAppName(), message: String, positiveButton: ButtonAction,
                    negativeButton: ButtonAction? = null, neutralButton: ButtonAction? = null){
        val messageDialog = AlertDialog.Builder(this)
        messageDialog.setTitle(title)
        messageDialog.setMessage(message)

        messageDialog.setPositiveButton(positiveButton.text) { _, _ -> positiveButton.action() }
        negativeButton?.let { messageDialog.setNegativeButton(it.text) { _, _ -> it.action() } }
        neutralButton?.let { messageDialog.setNeutralButton(it.text) { _, _ -> it.action() } }
        messageDialog.show()
    }

    private fun Context.getAppName(): String = applicationInfo.loadLabel(packageManager).toString()

    fun showToast(text: String){
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }

}