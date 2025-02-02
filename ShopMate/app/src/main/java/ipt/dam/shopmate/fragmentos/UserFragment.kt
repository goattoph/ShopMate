package ipt.dam.shopmate.fragmentos

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import ipt.dam.shopmate.R
import ipt.dam.shopmate.atividades.LoginActivity
import ipt.dam.shopmate.retrofit.RetrofitInitializer
import ipt.dam.shopmate.retrofit.service.SecureStorage
import ipt.dam.shopmate.retrofit.service.UsersPreferences
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class UserFragment : Fragment(R.layout.fragment_user) {

    private lateinit var tvUsername: TextView
    private lateinit var tvEmail: TextView
    private lateinit var btnLogOut: Button
    private lateinit var btnDeleteAccount: Button

    // Metodo chamado quando a view do fragmento é criada
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tvUsername = view.findViewById(R.id.tvUsername)
        tvEmail = view.findViewById(R.id.tvEmail)
        btnLogOut = view.findViewById(R.id.btnLogOut)
        btnDeleteAccount = view.findViewById(R.id.btnDeleteAccount)

        val userPreferences = UsersPreferences(requireContext())
        val username = userPreferences.getUserName()
        val email = userPreferences.getUserEmail()

        tvUsername.text = "Username: $username"
        tvEmail.text = "Email: $email"

        btnLogOut.setOnClickListener {
            RetrofitInitializer.usersService.logOut().enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if (response.isSuccessful) {
                        val secureStorage = SecureStorage(requireContext())
                        //Apagar a password
                        secureStorage.deletePassword()
                        //Apagar os dados do utilizador
                        userPreferences.clearPreferences()
                        val intent = Intent(requireContext(), LoginActivity::class.java)
                        startActivity(intent)
                        Toast.makeText(
                            requireContext(),
                            "Sair da conta realizado com sucesso",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        Toast.makeText(
                            requireContext(),
                            "Erro ao sair da conta: ${response.message()}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Toast.makeText(requireContext(), "Erro ao conectar", Toast.LENGTH_SHORT).show()
                }
            })
        }

        btnDeleteAccount.setOnClickListener {
            val context = requireContext()
            val alertDialog = androidx.appcompat.app.AlertDialog.Builder(context)
                .setTitle("Apagar Conta!")
                .setMessage("Tem a certeza de que deseja apagar a sua conta?")
                .setPositiveButton("Sim") { dialog, _ ->
                    // Caso sim, a conta é eliminada
                    RetrofitInitializer.usersService.deleteUser(id).enqueue(object : Callback<Void> {
                        override fun onResponse(call: Call<Void>, response: Response<Void>) {
                            if (response.isSuccessful) {
                                Toast.makeText(context, "Conta apagada com sucesso", Toast.LENGTH_SHORT).show()
                                userPreferences.clearPreferences()
                                val intent = Intent(requireContext(), LoginActivity::class.java)
                                startActivity(intent)
                            } else {
                                Toast.makeText(context, "Erro ao apagar a conta", Toast.LENGTH_SHORT).show()
                            }
                        }
                        // Metodo chamado em caso de erro
                        override fun onFailure(call: Call<Void>, t: Throwable) {
                            Toast.makeText(context, "Erro ao conectar", Toast.LENGTH_SHORT).show()
                        }
                    })
                    // Fecha o dialog
                    dialog.dismiss()
                }
                .setNegativeButton("Não") { dialog, _ ->
                    // Caso não, fecha o dialog
                    dialog.dismiss()
                }
                .create()
            // Mostrar o dialog
            alertDialog.show()
        }
    }
}