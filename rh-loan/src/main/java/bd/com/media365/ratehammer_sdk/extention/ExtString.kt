package bd.com.media365.ratehammer_sdk.extention

fun String.isValidateIBAN(): Boolean {
//    val pattern = Regex("^SA\\\\d{2}[0-9]{4}[A-Za-z]{2}[0-9]{14}\$")
//    return pattern.matches(this)

    return this.length == 24
}