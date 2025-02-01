package ipt.dam.shopmate.retrofit

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import ipt.dam.shopmate.retrofit.service.UsersService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.CookieManager
import java.net.CookiePolicy
import okhttp3.JavaNetCookieJar
import okhttp3.OkHttpClient

object RetrofitInitializer {

    private val gson: Gson = GsonBuilder().setLenient().create()

    // Configurar o CookieManager para o OkHttpClient
    private val cookieManager = CookieManager().apply {
        setCookiePolicy(CookiePolicy.ACCEPT_ALL)
    }

    private val okHttpClient = OkHttpClient.Builder()
        .cookieJar(JavaNetCookieJar(cookieManager)) // Suporte a cookies
        .build()

    private val retrofit = Retrofit.Builder()
        //.baseUrl("http://10.0.2.2/")
        .baseUrl("https://my-favorite-things.azurewebsites.net")
        .client(okHttpClient) // Usar o cliente OkHttp configurado
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()

    val usersService: UsersService = retrofit.create(UsersService::class.java)
}