package ipt.dam.shopmate.atividades

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ipt.dam.shopmate.R
import ipt.dam.shopmate.adapter.ItemsAdapter
import ipt.dam.shopmate.models.Item
import ipt.dam.shopmate.retrofit.RetrofitInitializer
import ipt.dam.shopmate.retrofit.service.CreateListRequest
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


//usámos chatgpt no desenvolvimento da classe
class CreateListActivity : AppCompatActivity() {
    // Metodo chamado quando a Atividade é criada
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Mostrar o popup quando a Atividade for aberta
        showCreateListPopup()
    }

    // Função que exibe o pop-up para criar uma lista
    private fun showCreateListPopup() {
        val dialogView = layoutInflater.inflate(R.layout.create_list, null)
        // Referências para os elementos
        val etListName = dialogView.findViewById<EditText>(R.id.etPopupListName) // Nome da Lista
        val btnSave = dialogView.findViewById<Button>(R.id.btnPopupSave) // Botão de Guardar
        val btnCancel = dialogView.findViewById<Button>(R.id.btnPopupCancel) // Botão de Cancelar

        // Criar um AlertDialog
        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .setCancelable(false) // Impede sair ao clicar fora do pop-up
            .create()

        // Ação ao clicar no "Guardar"
        btnSave.setOnClickListener {
            // Obter o nome da lista inserido
            val listName = etListName.text.toString().trim()
            if (listName.isNotEmpty()) {
                // Criar a lista
                createList(listName, dialog)
            } else {
                Toast.makeText(this, "O nome da lista não pode estar vazio", Toast.LENGTH_SHORT).show()
            }
        }

        // Ação ao clicar em "Cancelar"
        btnCancel.setOnClickListener {
            dialog.dismiss()
            finish()
        }
        // Mostrar o pop-up
        dialog.show()
    }

    // Função responsável por fazer a chamada à API para criar a lista
    private fun createList(name: String, dialog: AlertDialog) {
        // Cria o objeto
        val request = CreateListRequest(name = name)
        RetrofitInitializer.usersService.createList(request).enqueue(object : Callback<Void> {
            // Metodo chamado quando a resposta à API é recebida com sucesso
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@CreateListActivity, "Lista criada com sucesso", Toast.LENGTH_SHORT).show()
                    dialog.dismiss()
                    finish() // Fechar a Atividade após criar a lista
                } else {
                    Toast.makeText(this@CreateListActivity, "Erro ao criar a lista", Toast.LENGTH_SHORT).show()
                }
            }
            // Metodo chamado caso ocorra uma falha na comunicação com a API
            override fun onFailure(call: Call<Void>, t: Throwable) {
                Toast.makeText(this@CreateListActivity, "Erro ao conectar", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
