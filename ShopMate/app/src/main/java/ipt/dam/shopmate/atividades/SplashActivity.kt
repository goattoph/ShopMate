package ipt.dam.shopmate.atividades
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import ipt.dam.shopmate.retrofit.RetrofitInitializer
import ipt.dam.shopmate.retrofit.service.LoginRequest
import ipt.dam.shopmate.retrofit.service.LoginResponse
import ipt.dam.shopmate.retrofit.service.SecureStorage
import ipt.dam.shopmate.retrofit.service.UsersPreferences
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.CookieHandler
import java.net.CookieManager
import java.net.CookiePolicy


class SplashActivity : AppCompatActivity() {

    fun login(email: String, password: String) {
        val request = LoginRequest(email, password)

        RetrofitInitializer.usersService.login(request).enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (!response.isSuccessful) {
                    // Se o login falhar, vai para a LoginActivity
                    startActivity(Intent(this@SplashActivity, LoginActivity::class.java))
                    finish() // Finaliza a SplashActivity
                } else {
                    // Vai para a MainActivity após o sucesso
                    startActivity(Intent(this@SplashActivity, MainActivity::class.java))
                    finish() // Finaliza a SplashActivity
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                // Em caso de erro na requisição
                Toast.makeText(this@SplashActivity, "Erro na conexão: ${t.message}", Toast.LENGTH_SHORT).show()

                // Vai para a LoginActivity
                startActivity(Intent(this@SplashActivity, LoginActivity::class.java))
                finish() // Finaliza a SplashActivity
            }
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val userPreferences = UsersPreferences(this@SplashActivity)
        val isLoggedIn = userPreferences.isLoggedIn()

        val cookieManager = CookieManager()
        cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL)
        CookieHandler.setDefault(cookieManager)

        if (isLoggedIn) {
            val email = userPreferences.getUserEmail()
            val secureStorage = SecureStorage(this)
            val password: String? = secureStorage.password

            if (email != null && password != null) {
                // Chama a função login e aguarda a resposta
                login(email, password)
            } else {
                startActivity(Intent(this, LoginActivity::class.java))
                finish() // Finaliza a SplashActivity
            }
        } else {
            startActivity(Intent(this, LoginActivity::class.java))
            finish() // Finaliza a SplashActivity
        }
    }

}
