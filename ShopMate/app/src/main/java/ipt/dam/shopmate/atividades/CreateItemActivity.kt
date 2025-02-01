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
import android.view.inputmethod.InputMethodManager
import ipt.dam.shopmate.R
import ipt.dam.shopmate.models.Item
import ipt.dam.shopmate.retrofit.RetrofitInitializer
import ipt.dam.shopmate.retrofit.service.CreateItemRequest
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

    private lateinit var editTextItemName: EditText
    private lateinit var editTextAmount: EditText
    private lateinit var btnUploadItem: Button
    private lateinit var btnCancelItem: Button
    private lateinit var imageView: ImageView
    private lateinit var takePhoto: Button

    private val CAMERA_REQUEST_CODE = 100
    private var itemImagePart: MultipartBody.Part? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.new_item)

        editTextItemName = findViewById(R.id.etPopupListName)
        editTextAmount = findViewById(R.id.etQuantity)
        btnUploadItem = findViewById(R.id.btnUploadItem)
        btnCancelItem = findViewById(R.id.btnCancelItem)
        imageView = findViewById(R.id.imageViewItem)
        takePhoto = findViewById(R.id.btnTakePhoto)

        // Verifica permissões
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), CAMERA_REQUEST_CODE)
        }

        takePhoto.setOnClickListener {
            // Abre a câmera para tirar a foto
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(intent, CAMERA_REQUEST_CODE)
        }

        btnUploadItem.setOnClickListener {
            // Verifique se a imagem foi tirada e se o itemImagePart não é nulo
            if (itemImagePart != null) {
                addItem(itemImagePart!!)
            } else {
                addItem(null)
            }
        }

        btnCancelItem.setOnClickListener {
            finish()
        }


    }

    // Metodo chamado quando a foto é tirada
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap
            imageView.setImageBitmap(imageBitmap)
            window.statusBarColor = ContextCompat.getColor(this, R.color.grey)
            // Salvar a imagem em um arquivo temporário
            try {
                val file = createTempFile("item_image", ".jpg", cacheDir)
                val outputStream = FileOutputStream(file)
                imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
                outputStream.flush()
                outputStream.close()

                // Log para ver se o arquivo foi criado corretamente
                Log.d("CreateItemActivity", "Foto salva em: ${file.absolutePath}")

                // Agora você pode usar esse arquivo para enviar na requisição
                val imageFile = file
                val requestBody = imageFile.asRequestBody("image/jpeg".toMediaTypeOrNull())
                itemImagePart = MultipartBody.Part.createFormData("itemImage", imageFile.name, requestBody)

                // Verificar se o itemImagePart não está nulo
                Log.d("CreateItemActivity", "itemImagePart criado: ${itemImagePart != null}")
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(this, "Erro ao salvar a imagem!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Metodo para adicionar um item à lista
    private fun addItem(imagePart: MultipartBody.Part?) {
        val listId = intent.getIntExtra("listId", -1)
        val itemName = editTextItemName.text.toString().trim()
        val amountText = editTextAmount.text.toString().trim()

        // Validação dos campos
        if (amountText.isEmpty() || itemName.isEmpty() && imagePart == null) {
            Toast.makeText(this, "O nome ou a imagem é obrigatório, e a quantidade deve ser preenchida!", Toast.LENGTH_SHORT).show()
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

        // Envia para a API via Retrofit com a imagem, se houver
        val call = RetrofitInitializer.usersService.createItem(
            listId,
            itemNamePart,
            amountPart,
            isCheckedPart,
            imagePart
        )

        call.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Toast.makeText(
                        this@CreateItemActivity,
                        "Item criado com sucesso!",
                        Toast.LENGTH_SHORT
                    ).show()
                    val intent = Intent()
                    setResult(RESULT_OK, intent)
                    finish()
                } else {
                    val errorMessage = response.errorBody()?.string()
                    Toast.makeText(
                        this@CreateItemActivity,
                        "Erro ao criar item! $errorMessage",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Toast.makeText(this@CreateItemActivity, "Falha na conexão!", Toast.LENGTH_SHORT).show()
            }
        })
    }
}