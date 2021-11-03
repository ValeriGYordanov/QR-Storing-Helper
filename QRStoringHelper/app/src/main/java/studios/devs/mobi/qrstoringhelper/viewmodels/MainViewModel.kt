package studios.devs.mobi.qrstoringhelper.viewmodels

import android.app.Application
import android.graphics.Bitmap
import android.graphics.Color
import androidx.lifecycle.AndroidViewModel
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.MultiFormatWriter
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject
import studios.devs.mobi.qrstoringhelper.extensions.whenSuccess
import studios.devs.mobi.qrstoringhelper.repositories.IMainRepository
import java.util.*
import javax.inject.Inject


interface MainViewModelInputOutput {
    val input: MainViewModelInput
    val output: MainViewModelOutput
}

interface MainViewModelInput {
    fun textInput(charSequence: CharSequence)
    fun createQR()
}

interface MainViewModelOutput {
    val isValidText: Observable<Boolean>
    val generatedQR: Observable<Bitmap>
}

class MainViewModel @Inject constructor(
    private val repository: IMainRepository, application: Application
) : AndroidViewModel(application), MainViewModelInput, MainViewModelOutput, MainViewModelInputOutput {

    //region CONST Exposed Input and Output
    override val input: MainViewModelInput
        get() = this
    override val output: MainViewModelOutput
        get() = this
    //endregion

    //region override output
    override val isValidText: Observable<Boolean>
    override val generatedQR: Observable<Bitmap>
    //endregion

    //region local
    private val compositeDisposable = CompositeDisposable()
    private val textInputSubject = PublishSubject.create<String>()
    private val createQRSubject = PublishSubject.create<Unit>()

    //endregion

    init {
        isValidText = textInputSubject.map { it.isNotEmpty() }.startWith(false)
        generatedQR = createQRSubject.withLatestFrom(textInputSubject,
            { _, text -> repository.createBitmapFromText(text) })
            .flatMap { it }
            .whenSuccess(false)
    }

    override fun textInput(charSequence: CharSequence) {
        textInputSubject.onNext(charSequence.toString())
    }

    override fun createQR() {
        createQRSubject.onNext(Unit)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

}