package studios.devs.mobi.qrstoringhelper.ui

import android.graphics.Bitmap
import android.graphics.Color.BLACK
import android.graphics.Color.WHITE
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.WriterException
import com.google.zxing.common.BitMatrix
import com.jakewharton.rxbinding2.view.RxView
import com.jakewharton.rxbinding2.widget.RxTextView
import dagger.android.AndroidInjection
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_main.*
import studios.devs.mobi.qrstoringhelper.R
import studios.devs.mobi.qrstoringhelper.databinding.ActivityMainBinding
import studios.devs.mobi.qrstoringhelper.extensions.addTo
import studios.devs.mobi.qrstoringhelper.viewmodels.MainViewModel
import studios.devs.mobi.qrstoringhelper.viewmodels.MainViewModelInput
import studios.devs.mobi.qrstoringhelper.viewmodels.MainViewModelInputOutput
import studios.devs.mobi.qrstoringhelper.viewmodels.MainViewModelOutput
import javax.inject.Inject


class MainActivity : BaseActivity() {

    lateinit var binding: ActivityMainBinding

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel: MainViewModelInputOutput by lazy {
        ViewModelProvider(this, viewModelFactory)
            .get(MainViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        setSupportActionBar(bottomMenuBar)

        // We do not need the burger menu for now so disabling it
        // will save us work later.
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
    }

    override fun onStart() {
        super.onStart()
        viewModel
            .bind(this)
            .addTo(compositeDisposable)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.bottomappbar_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        item?.let {
            when (it.itemId) {
                R.id.app_bar_fav -> Toast.makeText(
                    this,
                    "Fav menu item is clicked!",
                    Toast.LENGTH_SHORT
                ).show()
                R.id.app_bar_search -> Toast.makeText(
                    this,
                    "Search menu item is clicked!",
                    Toast.LENGTH_SHORT
                ).show()
                R.id.app_bar_settings -> Toast.makeText(
                    this,
                    "Settings item is clicked!",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        return true
    }
}

private fun MainViewModelInputOutput.bind(activity: MainActivity): List<Disposable> {
    return listOf(
        output.bind(activity.binding),
        output.bind(activity),
        input.bind(activity.binding),
        activity.binding.configureWith(activity)
    ).flatten()
}

private fun ActivityMainBinding.configureWith(activity: MainActivity): List<Disposable> {
    return listOf(
        //binding.
    )
}

private fun MainViewModelInput.bind(binding: ActivityMainBinding): List<Disposable> {
    return listOf(
        RxTextView.textChanges(binding.inputText).subscribe { textInput(it) },
        RxView.clicks(binding.selAllFab).subscribe { createQR() }
    )
}

private fun MainViewModelOutput.bind(activity: MainActivity): List<Disposable> {
    return listOf(

    )
}

private fun MainViewModelOutput.bind(binding: ActivityMainBinding): List<Disposable> {
    return listOf(
        isValidText.subscribe { binding.selAllFab.isEnabled = it },
        generatedQR.observeOn(AndroidSchedulers.mainThread()).subscribe { binding.qrImg.setImageBitmap(it) }
    )
}