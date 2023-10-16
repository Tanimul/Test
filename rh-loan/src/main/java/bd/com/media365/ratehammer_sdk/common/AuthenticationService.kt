package bd.com.media365.ratehammer_sdk.common

import com.auth0.jwt.JWT
import com.auth0.jwt.interfaces.DecodedJWT
import java.util.Date

class AuthenticationService {
    fun isTokenValid(token: String): Boolean {
        return try {
            val jwt: DecodedJWT = JWT.decode(token)
            jwt.expiresAt.after(Date())
        } catch (e: Exception) {
            false
        }
    }
}