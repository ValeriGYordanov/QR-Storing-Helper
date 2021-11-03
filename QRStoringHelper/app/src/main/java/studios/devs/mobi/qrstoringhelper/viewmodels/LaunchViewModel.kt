package studios.devs.mobi.qrstoringhelper.viewmodels

import android.app.Application
import android.graphics.Bitmap
import androidx.lifecycle.AndroidViewModel
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject
import studios.devs.mobi.qrstoringhelper.extensions.whenSuccess
import javax.inject.Inject

interface LaunchViewModelInputOutput {
    val input: LaunchViewModelInput
    val output: LaunchViewModelOutput
}

interface LaunchViewModelInput {
}

interface LaunchViewModelOutput {
}

class LaunchViewModel @Inject constructor(application: Application)
    : AndroidViewModel(application), LaunchViewModelInput, LaunchViewModelOutput, LaunchViewModelInputOutput {

    //region CONST Exposed Input and Output
    override val input: LaunchViewModelInput
        get() = this
    override val output: LaunchViewModelOutput
        get() = this
    //endregion

    //region override output

    //endregion

    //region local
    private val compositeDisposable = CompositeDisposable()
    private val textInputSubject = PublishSubject.create<String>()
    private val createQRSubject = PublishSubject.create<Unit>()

    //endregion

    init {

    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

}