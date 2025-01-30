package ipt.dam.shopmate.atividades

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import ipt.dam.shopmate.R
import ipt.dam.shopmate.models.Item
import ipt.dam.shopmate.retrofit.RetrofitInitializer
import ipt.dam.shopmate.retrofit.service.CreateItemRequest
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


//usámos chatgpt no desenvolvimento da classe
class CreateItemActivity : AppCompatActivity() {

    private lateinit var editTextItemName: EditText
    private lateinit var editTextAmount: EditText
    private lateinit var btnUploadItem: Button

    // Metodo chamado quando a atividade é criada
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.create_item)

        editTextItemName = findViewById(R.id.editTextItemName)
        editTextAmount = findViewById(R.id.editTextAmount)
        btnUploadItem = findViewById(R.id.btnUploadItem)

        btnUploadItem.setOnClickListener {
            addItem()
        }
    }

    // Metodo para adicionar um item à lista
    private fun addItem() {
        val listId = intent.getIntExtra("listId", -1)
        val itemName = editTextItemName.text.toString().trim()
        val amountText = editTextAmount.text.toString().trim()

        // Validação dos campos
        if (itemName.isEmpty() || amountText.isEmpty()) {
            Toast.makeText(this, "Preencha todos os campos!", Toast.LENGTH_SHORT).show()
            return
        }

        val amount = amountText.toIntOrNull()
        if (amount == null || amount <= 0) {
            Toast.makeText(this, "Quantidade inválida!", Toast.LENGTH_SHORT).show()
            return
        }

        // Converte os dados para RequestBody
        val itemNamePart = itemName.toRequestBody("text/plain".toMediaTypeOrNull())
        val amountPart = amount.toString().toRequestBody("text/plain".toMediaTypeOrNull())
        val isCheckedPart = "false".toRequestBody("text/plain".toMediaTypeOrNull())

        // Envia para a API via Retrofit
        val call = RetrofitInitializer.usersService.createItem(
            listId,
            itemNamePart,
            amountPart,
            isCheckedPart
        )

        call.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Toast.makeText(
                        this@CreateItemActivity,
                        "Item criado com sucesso!",
                        Toast.LENGTH_SHORT
                    ).show()
                    val intent= Intent()
                    // Finalizar a atividade e voltar à tela anterior
                    setResult(RESULT_OK, intent)
                    // Fecha a atividade após sucesso
                    finish()
                } else {
                    // Exibe detalhes do erro
                    val errorMessage = response.errorBody()?.string()
                    Toast.makeText(
                        this@CreateItemActivity,
                        "Erro ao criar item! $errorMessage",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            // Metodo em caso de falha
            override fun onFailure(call: Call<Void>, t: Throwable) {
                Toast.makeText(this@CreateItemActivity, "Falha na conexão!", Toast.LENGTH_SHORT)
                    .show()
            }
        })
    }
}
