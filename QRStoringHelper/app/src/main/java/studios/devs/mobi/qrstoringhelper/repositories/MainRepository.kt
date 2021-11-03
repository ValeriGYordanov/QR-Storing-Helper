package studios.devs.mobi.qrstoringhelper.repositories

import android.graphics.Bitmap
import android.graphics.Color
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.MultiFormatWriter
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import studios.devs.mobi.qrstoringhelper.model.result.*
import java.lang.Exception
import java.util.*

class MainRepository : IMainRepository {

    override fun createBitmapFromText(text: String): Observable<Result<Bitmap>>{
        return Observable.create<Result<Bitmap>> { emitter ->
            try {
                val map = EnumMap<EncodeHintType, Any>(EncodeHintType::class.java)
                map[EncodeHintType.CHARACTER_SET] = "UTF-8"
                val matrix = MultiFormatWriter().encode(text, BarcodeFormat.QR_CODE, 500, 500, map)
                val w = matrix.width
                val h = matrix.height
                val pixels = IntArray(w * h)
                for (y in 0 until h) {
                    val offset = y * w
                    for (x in 0 until w) {
                        pixels[offset + x] = if (matrix[x, y]) Color.BLACK else Color.WHITE
                    }
                }
                val bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
                bitmap.setPixels(pixels, 0, w, 0, 0, w, h)
                emitter.onNext(Result.Success(bitmap))
            } catch (e: Exception) {
                emitter.onNext(Result.Error(object : IResultError{
                    override var description: String = "Unknown Error"
                }))
            }
        }
            .startWith(Result.Loading())
            .subscribeOn(Schedulers.io())
    }

}
