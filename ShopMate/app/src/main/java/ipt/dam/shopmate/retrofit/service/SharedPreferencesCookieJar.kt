package ipt.dam.shopmate.retrofit.service
import android.content.Context
import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl

class SharedPreferencesCookieJar(context: Context) : CookieJar {

    private val sharedPreferences = SharedPreferences(context)

    override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {
        sharedPreferences.saveCookies(url, cookies)
    }

    override fun loadForRequest(url: HttpUrl): List<Cookie> {
        return sharedPreferences.getCookies(url)
    }
}