package ipt.dam.shopmate.atividades

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import android.Manifest
import android.util.Log
import com.bumptech.glide.Glide
import android.view.inputmethod.InputMethodManager
import ipt.dam.shopmate.R
import ipt.dam.shopmate.models.Item
import ipt.dam.shopmate.retrofit.RetrofitInitializer
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream

class CreateItemActivity : AppCompatActivity() {

    // Define a var itemId do tipo Int?, pode armazenar um int ou null
    private var itemId: Int? = null

    // Elementos da interface
    private lateinit var editTextItemName: EditText
    private lateinit var editTextAmount: EditText
    private lateinit var btnUploadItem: Button
    private lateinit var btnCancelItem: Button
    private lateinit var imageView: ImageView
    private lateinit var takePhoto: Button

    // Código de requisição da camara
    private val CAMERA_REQUEST_CODE = 100
    // Parte da imagem que vai ser enviada
    private var itemImagePart: MultipartBody.Part? = null

    // Metodo chamado quando a atividade é criada
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Define o layout
        setContentView(R.layout.new_item)

        // Inicializa os elementos da interface
        editTextItemName = findViewById(R.id.etPopupListName)
        editTextAmount = findViewById(R.id.etQuantity)
        btnUploadItem = findViewById(R.id.btnUploadItem)
        btnCancelItem = findViewById(R.id.btnCancelItem)
        imageView = findViewById(R.id.imageViewItem)
        takePhoto = findViewById(R.id.btnTakePhoto)

        // Verifica as permissões para a camara
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), CAMERA_REQUEST_CODE)
        }

        // Ação para tirar uma foto
        takePhoto.setOnClickListener {
            // Abre a camara para tirar a foto
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            // Inicia a captura
            startActivityForResult(intent, CAMERA_REQUEST_CODE)
        }

        // Botão para adicionar um item
        btnUploadItem.setOnClickListener {
            if (itemImagePart != null) {
                // Adiciona o item com a imagem
                addItem(itemImagePart!!)
            } else {
                // Adiciona o item sem imagem
                addItem(null)
            }
        }

        // Botão para cancelar a criação de um item
        btnCancelItem.setOnClickListener {
            finish()
        }



        // Recupera os dados da Intent para editar um item
        itemId = intent.getIntExtra("itemId", -1)
        val itemName = intent.getStringExtra("itemName")
        val amount = intent.getIntExtra("amount", 1)
        val image = intent.getStringExtra("image")

        // Se o itemId for válido (item existente) preenche os campos para edição com os dados do item
        if (itemId != -1) {
            editTextItemName.setText(itemName)
            editTextAmount.setText(amount.toString())

            // Carrega a imagem (se existir)
            if (!image.isNullOrEmpty()) {
                Glide.with(this)
                    .load("https://my-favorite-things.azurewebsites.net/Imagens/$image")
                    .into(imageView)
            }

            // btnUploadItem.setImageResource(R.drawable.ic_launcher_add_foreground)
        }
    }

    // Metodo chamado quando a foto é tirada
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK) {
            // Vai buscar a imagem tirada
            val imageBitmap = data?.extras?.get("data") as Bitmap
            // Exibe a imagem no imageView
            imageView.setImageBitmap(imageBitmap)
            window.statusBarColor = ContextCompat.getColor(this, R.color.grey)
            // Salva a imagem num arquivo temporário
            try {
                val file = createTempFile("item_image", ".jpg", cacheDir)
                val outputStream = FileOutputStream(file)
                imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
                outputStream.flush()
                outputStream.close()
                // Utilizar o arquivo para criar a requisição para enviar a imagem
                val imageFile = file
                val requestBody = imageFile.asRequestBody("image/jpeg".toMediaTypeOrNull())
                itemImagePart = MultipartBody.Part.createFormData("itemImage", imageFile.name, requestBody)
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(this, "Erro ao salvar a imagem!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Metodo para adicionar um item à lista
    private fun addItem(imagePart: MultipartBody.Part?) {
        val itemName = editTextItemName.text.toString().trim()
        val amountText = editTextAmount.text.toString().trim()

        // Verifica se os campos obrigatórios estão preenchidos
        if (amountText.isEmpty() || itemName.isEmpty() && imagePart == null) {
            Toast.makeText(this, "O nome ou a imagem é obrigatório, e a quantidade deve ser preenchida!", Toast.LENGTH_SHORT).show()
            return
        }
        // Tentar converter o amount para um INT
        val amount = amountText.toIntOrNull()
        if (amount == null || amount <= 0) {
            Toast.makeText(this, "Quantidade inválida!", Toast.LENGTH_SHORT).show()
            return
        }
        // Cria os dados a serem enviados para o servidor
        val itemNamePart = itemName.toRequestBody("text/plain".toMediaTypeOrNull())
        val amountPart = amount.toString().toRequestBody("text/plain".toMediaTypeOrNull())
        val isCheckedPart = "false".toRequestBody("text/plain".toMediaTypeOrNull())

        // Se o itemId for válido (item existem)
        val call = if (itemId != null && itemId != -1) {
            // Atualizar item existente
            Log.d("CreateItemActivity", "Editando item com ID: $itemId")
            RetrofitInitializer.usersService.editItem(
                itemId!!, itemNamePart, amountPart, isCheckedPart, imagePart
            )
        } else {
            // Criar um novo item
            val listId = intent.getIntExtra("listId", -1)
            RetrofitInitializer.usersService.createItem(
                listId, itemNamePart, amountPart, isCheckedPart, imagePart
            )
        }
        // Envia o request ao servidor
        call.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@CreateItemActivity, "Item salvo com sucesso!", Toast.LENGTH_SHORT).show()
                    setResult(RESULT_OK, Intent())
                    // Fecha a atividade
                    finish()
                } else {
                    Toast.makeText(this@CreateItemActivity, "Erro ao salvar item!", Toast.LENGTH_SHORT).show()
                }
            }
            // Metodo chamado em caso de erro
            override fun onFailure(call: Call<Void>, t: Throwable) {
                Toast.makeText(this@CreateItemActivity, "Falha na conexão!", Toast.LENGTH_SHORT).show()
            }
        })
    }
}