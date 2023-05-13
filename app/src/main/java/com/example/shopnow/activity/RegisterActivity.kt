package com.example.shopnow.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.shopnow.databinding.ActivityRegisterBinding
import com.example.shopnow.model.User
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.signUpBtnRegActivity.setOnClickListener {
            validateUser()
        }
    }

    private fun validateUser() {
        if (binding.userNumbRegActivity.text!!.isEmpty() || binding.userNameRegActivity.text!!.isEmpty())
            Toast.makeText(this, "Please fill all Fields", Toast.LENGTH_SHORT).show()
        else
            storeData()
    }

    private fun storeData() {
        val builder = AlertDialog.Builder(this)
            .setTitle("Loading....")
            .setMessage("Please Wait")
            .setCancelable(false)
            .create()
        builder.show()

        val preference = this.getSharedPreferences("user", MODE_PRIVATE)
        val editor = preference.edit()
        editor.putString("number", binding.userNameRegActivity.text.toString())
        editor.putString("name", binding.userNumbRegActivity.text.toString())
        editor.apply()

        val data = User(binding.userNameRegActivity.text.toString(), binding.userNumbRegActivity.text.toString())

        Firebase.firestore.collection("users").document(binding.userNumbRegActivity.text.toString())
            .set(data)
            .addOnSuccessListener {
                Toast.makeText(this, "User Registered", Toast.LENGTH_SHORT).show()
                builder.dismiss()
                openLoginScreen()
            }
            .addOnFailureListener {
                builder.dismiss()
                Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show()
            }
    }

    private fun openLoginScreen() {
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }
}