package ipt.dam.shopmate.retrofit.service

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import androidx.security.crypto.MasterKey.Builder

// usámos chatgpt para o desenvolvimento desta classe
class SecureStorage(context: Context?) {

    private var sharedPreferences: SharedPreferences? = null

    init {
        try {
            val safeContext = context ?: throw IllegalArgumentException("Context cannot be null")

            // Cria uma chave (MasterKey) com o algoritmo AES-256-GCM para a criptografia dos dados
            val masterKey: MasterKey = MasterKey.Builder(safeContext)
                .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                .build()

            // Cria uma instância do EncryptedSharedPreferences a usar a chave
            sharedPreferences = EncryptedSharedPreferences.create(
                safeContext,
                PREFS_NAME,  // Nome do ficheiro de SharedPreferences
                masterKey,   // A chave criada acima
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,  // Criptografia das chaves
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM   // Criptografia dos valores
            )
        } catch (e: Exception) {
            // Em caso de falha mostra o erro no log.
            e.printStackTrace()
        }
    }

    // Função para guardar a password de forma segura
    fun savePassword(password: String?) {
        // Cria um editor para modificar o SharedPreferences
        val editor = sharedPreferences!!.edit()

        // Coloca a password no editor
        editor.putString("user_password", password)

        // Aplica as mudanças no SharedPreferences
        editor.apply()
    }

    // Recupera a password guardada de forma segura
    val password: String?
        // Retorna a password guardada ou null se não houver nenhuma password guardada
        get() = sharedPreferences!!.getString("user_password", null)

    // Função para eliminar a password guardada
    fun deletePassword() {
        // Cria um editor para modificar os SharedPreferences
        val editor = sharedPreferences!!.edit()

        // Elimina a password
        editor.remove("user_password")

        // Aplica as mudanças no SharedPreferences
        editor.apply()
    }

    // Define uma constante de nome do ficheiro de SharedPreferences
    companion object {
        private const val PREFS_NAME = "secure_prefs"
    }
}

