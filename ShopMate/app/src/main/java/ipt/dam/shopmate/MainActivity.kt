//package ipt.dam.shopmate
//
//import android.os.Bundle
//import androidx.activity.enableEdgeToEdge
//import androidx.appcompat.app.AppCompatActivity
//import androidx.core.view.ViewCompat
//import androidx.core.view.WindowInsetsCompat
//
//class MainActivity : AppCompatActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
//        setContentView(R.layout.activity_main)
////        val logOutText = findViewById<TextView>(R.id.tvLogOut)
////        logOutText.setOnClickListener{
////            RetrofitInitializer.usersService.logOut().enqueue(object : Callback<Void> {
////                override fun onResponse(call: Call<Void>, response: Response<Void>) {
////                    if (response.isSuccessful) {
////
////                        val userPreferences = UsersPreferences(this@MainActivity)
////                        userPreferences.clearPreferences()
////                        val intent = Intent(this@MainActivity, LoginActivity::class.java)
////                        startActivity(intent)
////                        Toast.makeText(this@MainActivity, "Sair da conta realizado com sucesso", Toast.LENGTH_SHORT).show()
////                    } else {
////                        Toast.makeText(this@MainActivity, "Erro ao sair da conta: ${response.message()}", Toast.LENGTH_SHORT).show()
////                    }
////                }
////
////                override fun onFailure(call: Call<Void>, t: Throwable) {
////                    Toast.makeText(this@MainActivity, "Erro ao conectar", Toast.LENGTH_SHORT).show()
////                }
////            })
////        }
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
//            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
//            insets
//        }
//    }
//}