package studios.devs.mobi.qrstoringhelper.model

sealed class Screen<T>(var someData: T?){
    data class Main<T>(var data: T?) : Screen<T>(data)
}