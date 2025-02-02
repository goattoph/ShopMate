package ipt.dam.shopmate.retrofit.service

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import androidx.security.crypto.MasterKey.Builder

class SecureStorage(context: Context?) {
    private var sharedPreferences: SharedPreferences? = null

    init {
        try {
            val safeContext = context ?: throw IllegalArgumentException("Context cannot be null")

            val masterKey: MasterKey = MasterKey.Builder(safeContext)
                .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                .build()

            sharedPreferences = EncryptedSharedPreferences.create(
                safeContext,
                PREFS_NAME,
                masterKey,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun savePassword(password: String?) {
        val editor = sharedPreferences!!.edit()
        editor.putString("user_password", password)
        editor.apply()
    }

    val password: String?
        get() = sharedPreferences!!.getString("user_password", null)

    // Função para eliminar a password
    fun deletePassword() {
        val editor = sharedPreferences!!.edit()
        editor.remove("user_password")
        editor.apply()
    }
    companion object {
        private const val PREFS_NAME = "secure_prefs"
    }
}
