package studios.devs.mobi.qrstoringhelper.repositories

import android.graphics.Bitmap
import io.reactivex.Observable
import studios.devs.mobi.qrstoringhelper.model.result.Result

interface IMainRepository {
    fun createBitmapFromText(text: String): Observable<Result<Bitmap>>
}
