package ipt.dam.shopmate.atividades
import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import ipt.dam.shopmate.R
import ipt.dam.shopmate.retrofit.RetrofitInitializer
import ipt.dam.shopmate.retrofit.service.RegisterRequest
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

//usámos chatgpt no desenvolvimento da classe
class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_register)

        // Altera a cor da barra
        window.statusBarColor = ContextCompat.getColor(this, R.color.grey)

        // Inicializar os componentes do layout
        val emailField = findViewById<EditText>(R.id.etRegisterEmail)
        val usernameField = findViewById<EditText>(R.id.etUsername)
        val passwordField = findViewById<EditText>(R.id.etRegisterPassword)
        val confirmPasswordField = findViewById<EditText>(R.id.etRegisterConfirmPassword)
        val registerButton = findViewById<Button>(R.id.btnRegister)
        val loginText = findViewById<TextView>(R.id.tvLogin)

        // Define o comportamento do botão de registo
        registerButton.setOnClickListener {
            // Obtém os valores inseridos
            val username = usernameField.text.toString()
            val email = emailField.text.toString()
            val password = passwordField.text.toString()
            val confirmPassword = confirmPasswordField.text.toString()

            // Verifica se todos os campos estão preenchidos
            if (username.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty() && confirmPassword.isNotEmpty()) {

                // Verifica se as passwords coincidem
                if (password == confirmPassword) {
                    // Se coincidirem, chama a função register
                    register(username, email, password)
                } else {
                    // Se as passwords não coincidirem, mostra uma mensagem de erro
                    Toast.makeText(this, "As palavras passes não coincidem", Toast.LENGTH_SHORT).show()
                }
            } else {
                // Se algum campo estiver vazio, mostra uma mensagem de erro
                Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show()
            }
        }

        // Define o comportamento do texto de login
        loginText.setOnClickListener {
            // Se o utilizador carregar no texto, ele é direcionado para a LoginActivity
            val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    // Metodo para fazer o registo
    private fun register(username: String, email: String, password: String) {
        // Cria um objeto de registo com os dados do utilizador
        val request = RegisterRequest(username, email, password)

        RetrofitInitializer.usersService.register(request).enqueue(object : Callback<Void> {
            // Metodo chamado se a requisição correr bem
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                // Verifica se a resposta foi  de sucesso
                if (response.isSuccessful) {
                    // Se o registo correr bem, mostra uma mensagem de sucesso e redireciona para a LoginActivity
                    Toast.makeText(this@RegisterActivity, "Registo realizado com sucesso", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
                    startActivity(intent)
                } else {
                    // Se o registo falhou, mostra uma mensagem de erro com a mensagem retornada pelo servidor
                    Toast.makeText(this@RegisterActivity, "Erro ao registar: ${response.message()}", Toast.LENGTH_SHORT).show()
                }
            }

            // Metodo chamado se houver erro
            override fun onFailure(call: Call<Void>, t: Throwable) {
                // Mostra uma mensagem de erro se não for possível conectar ao servidor
                Toast.makeText(this@RegisterActivity, "Erro ao conectar", Toast.LENGTH_SHORT).show()
            }
        })
    }
}