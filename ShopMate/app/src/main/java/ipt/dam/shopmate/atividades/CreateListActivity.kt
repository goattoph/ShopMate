package ipt.dam.shopmate.atividades

import android.content.Context
import android.os.Bundle

import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import ipt.dam.shopmate.R
import ipt.dam.shopmate.retrofit.RetrofitInitializer
import ipt.dam.shopmate.retrofit.service.CreateListRequest
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


//usámos chatgpt no desenvolvimento da classe
class CreateListActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Defininir o layout da atividade
        setContentView(R.layout.new_list)
        window.statusBarColor = ContextCompat.getColor(this, R.color.grey)
        // Inicializando os componentes do layout
        val etListName = findViewById<EditText>(R.id.etPopupListName)
        val btnSave = findViewById<Button>(R.id.btnPopupSave)
        val btnCancel = findViewById<Button>(R.id.btnPopupCancel)

        btnSave.setOnClickListener {
            val listName = etListName.text.toString().trim()
            if (listName.isNotEmpty()) {
                createList(listName)
            } else {
                Toast.makeText(this, "O nome da lista não pode estar vazio", Toast.LENGTH_SHORT).show()
            }
        }

        // Configurando o clique no botão de cancelar (caso queira fechar a atividade)
        btnCancel.setOnClickListener {
            finish() // Fechar a atividade
        }
    }

     //Função responsável por fazer a chamada à API para criar a lista
    private fun createList(name: String) {
        // Cria o objeto
        val request = CreateListRequest(name = name)
        RetrofitInitializer.usersService.createList(request).enqueue(object : Callback<Void> {
            // Metodo chamado quando a resposta à API é recebida com sucesso
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@CreateListActivity, "Lista criada com sucesso", Toast.LENGTH_SHORT).show()
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
