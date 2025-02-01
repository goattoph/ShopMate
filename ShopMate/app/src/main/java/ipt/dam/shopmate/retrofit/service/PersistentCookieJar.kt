package ipt.dam.shopmate.retrofit.service
import android.content.Context
import android.content.SharedPreferences
import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl


class PersistentCookieJar(context: Context) : CookieJar {

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("CookiePrefs", Context.MODE_PRIVATE)

    override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {
        val editor = sharedPreferences.edit()
        val cookieSet = cookies.mapNotNull { it.encodeCookie() }.toSet()
        editor.putStringSet(url.host, cookieSet)
        editor.apply()
    }

    override fun loadForRequest(url: HttpUrl): List<Cookie> {
        val cookieStrings = sharedPreferences.getStringSet(url.host, emptySet()) ?: return emptyList()
        return cookieStrings.mapNotNull { Cookie.decodeCookie(url, it) }
    }
}

/**
 * Função de extensão para serializar um cookie em formato seguro.
 */
fun Cookie.encodeCookie(): String? {
    return "${name}=${value};domain=${domain};path=${path};expiresAt=${expiresAt};secure=${secure};httpOnly=${httpOnly}"
}

/**
 * Função para desserializar um cookie guardado.
 */
fun Cookie.Companion.decodeCookie(url: HttpUrl, cookieString: String): Cookie? {
    val parts = cookieString.split(";")
    val nameValue = parts[0].split("=")
    if (nameValue.size < 2) return null

    val builder = Cookie.Builder()
        .name(nameValue[0])
        .value(nameValue[1])
        .domain(url.host) // Certificar que o domínio é o correto
        .path("/")

    parts.forEach { part ->
        when {
            part.startsWith("domain=") -> builder.domain(part.substring(7))
            part.startsWith("path=") -> builder.path(part.substring(5))
            part.startsWith("secure") -> builder.secure()
            part.startsWith("httpOnly") -> builder.httpOnly()
            part.startsWith("expiresAt=") -> {
                val expiry = part.substring(10).toLongOrNull()
                if (expiry != null) builder.expiresAt(expiry)
            }
        }
    }
    return builder.build()
}
