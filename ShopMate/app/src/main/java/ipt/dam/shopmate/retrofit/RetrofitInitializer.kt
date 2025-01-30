package ipt.dam.shopmate.retrofit

import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import ipt.dam.shopmate.retrofit.service.ApiService
import ipt.dam.shopmate.retrofit.service.SharedPreferencesCookieJar
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.CookieManager
import java.net.CookiePolicy
import okhttp3.JavaNetCookieJar
import okhttp3.OkHttpClient

//object RetrofitInitializer {
//
//    private val gson: Gson = GsonBuilder().setLenient().create()
//
//    // Configurar o CookieManager para o OkHttpClient
//    private val cookieManager = CookieManager().apply {
//        setCookiePolicy(CookiePolicy.ACCEPT_ALL)
//    }
//
//    private val okHttpClient = OkHttpClient.Builder()
//        .cookieJar(JavaNetCookieJar(cookieManager)) // Suporte a cookies
//        .build()
//
//    private val retrofit = Retrofit.Builder()
//        //.baseUrl("http://10.0.2.2/")
//        .baseUrl("https://my-favorite-things.azurewebsites.net")
//        .client(okHttpClient) // Usar o cliente OkHttp configurado
//        .addConverterFactory(GsonConverterFactory.create(gson))
//        .build()
//
//    val usersService: ApiService = retrofit.create(ApiService::class.java)
//}

//class RetrofitInitializer(context: Context) {
//    private val gson: Gson = GsonBuilder().setLenient().create()
//    private val okHttpClient = OkHttpClient.Builder()
//        .cookieJar(SharedPreferencesCookieJar(context)) // Usa o CookieJar personalizado
//        .build()
//
//    private val retrofit = Retrofit.Builder()
//        .baseUrl("https://my-favorite-things.azurewebsites.net")
//        .client(okHttpClient)
//        .addConverterFactory(GsonConverterFactory.create())
//        .build()
//    val apiService: ApiService = retrofit.create(ApiService::class.java)
//}

object RetrofitInitializer {

    private lateinit var retrofit: Retrofit
    lateinit var apiService: ApiService

    fun init(context: Context) {
        val okHttpClient = OkHttpClient.Builder()
            .cookieJar(SharedPreferencesCookieJar(context))  // Usando o CookieJar personalizado
            .build()

        retrofit = Retrofit.Builder()
            .baseUrl("https://my-favorite-things.azurewebsites.net")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        apiService = retrofit.create(ApiService::class.java)
    }
}