package ipt.dam.shopmate.atividades
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ipt.dam.shopmate.retrofit.service.UsersPreferences
class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Verificar se o utilizador está logado
        val userPreferences = UsersPreferences(this@SplashActivity)
        val isLoggedIn = userPreferences.isLoggedIn()

        // Se o utilizador estiver logado, vai para a MainActivity
        if (isLoggedIn) {
            startActivity(Intent(this, MainActivity::class.java))
        } else {
            // Caso contrário, vai para a LoginActivity
            startActivity(Intent(this, LoginActivity::class.java))
        }

        // Finaliza a SplashActivity para não voltar para ela
        finish()
    }
}
