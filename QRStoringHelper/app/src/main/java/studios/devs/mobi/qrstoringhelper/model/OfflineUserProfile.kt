package studios.devs.mobi.qrstoringhelper.model

class OfflineUserProfile(val id: Long = 0, val name: String = "", var email: String? = null) {
    companion object {
        const val USER_ID = "accountId"
        const val USER_NAME = "accountName"
    }
}