//package ipt.dam.shopmate
//
//import android.content.Intent
//import android.os.Bundle
//import android.widget.*
//import androidx.appcompat.app.AppCompatActivity
//import androidx.core.content.ContextCompat
//import ipt.dam.shopmate.retrofit.RetrofitInitializer
//import ipt.dam.shopmate.retrofit.service.LoginRequest
//import ipt.dam.shopmate.retrofit.service.LoginResponse
//import ipt.dam.shopmate.retrofit.service.SharedPreferences
//import retrofit2.Call
//import retrofit2.Callback
//import retrofit2.Response
////import android.webkit.CookieManager
//import java.net.CookieHandler
//import java.net.CookieManager
//import java.net.CookiePolicy
//
////usámos chatgpt no desenvolvimento da classe
//class LoginActivity : AppCompatActivity() {
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_login)
//        window.statusBarColor = ContextCompat.getColor(this, R.color.grey)
//
//        // Configurar o CookieManager global
//        val cookieManager = CookieManager()
//        cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL)
//        CookieHandler.setDefault(cookieManager)
//
//        val emailField = findViewById<EditText>(R.id.etEmail)
//        val passwordField = findViewById<EditText>(R.id.etPassword)
//        val loginButton = findViewById<Button>(R.id.btnLogin)
//        val registerText = findViewById<TextView>(R.id.tvRegister)
//
//        loginButton.setOnClickListener {
//            val email = emailField.text.toString()
//            val password = passwordField.text.toString()
//
//            if (email.isNotEmpty() && password.isNotEmpty()) {
//                login(email, password)
//            } else {
//                Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show()
//            }
//        }
//
//        registerText.setOnClickListener {
//            val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
//            startActivity(intent)
//        }
//    }
//
//    private fun login(email: String, password: String) {
//        val request = LoginRequest(email, password)
//
//        RetrofitInitializer(this).apiService.login(request).enqueue(object : Callback<LoginResponse> {
//            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
//                if (response.isSuccessful) {
//                    // Ir buscar os dados do utilizador na resposta
//                    val userName = response.body()?.userName
//                    val userEmail = response.body()?.email
//
//                    // Guardar os dados no SharedPreferences
//                    val userPreferences = SharedPreferences(this@LoginActivity)
//                    userName?.let { userPreferences.saveUserName(it) }
//                    userEmail?.let { userPreferences.saveUserEmail(it) }
//                    userPreferences.setLoggedIn(true);
//
//                    //Ir para a a main activity
//                    val intent = Intent(this@LoginActivity, MainActivity::class.java)
//                    startActivity(intent)
//                    Toast.makeText(this@LoginActivity, "Login bem-sucedido!", Toast.LENGTH_SHORT).show()
//                } else {
//                    Toast.makeText(this@LoginActivity, "Erro no login", Toast.LENGTH_SHORT).show()
//                }
//            }
//
//            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
//                Toast.makeText(this@LoginActivity, "Erro na conexão: ${t.message}", Toast.LENGTH_SHORT).show()
//            }
//        })
//    }
//}
