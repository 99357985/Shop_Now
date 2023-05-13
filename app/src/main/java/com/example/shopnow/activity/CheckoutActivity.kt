package com.example.shopnow.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.example.shopnow.MainActivity
import com.example.shopnow.R
import com.example.shopnow.db.Product
import com.example.shopnow.db.ProductDatabase
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CheckoutActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkout)

        uploadData()
    }

    private fun uploadData() {
        val id = intent.getStringArrayListExtra("productIds")
        for (currentId in id!!) {
            fetchData(currentId)
        }
    }

    private fun fetchData(productId: String?) {
        val dao = ProductDatabase.getInstance(this).getProductDao()
        Firebase.firestore.collection("products")
            .document(productId!!).get()
            .addOnSuccessListener {

                lifecycleScope.launch(Dispatchers.IO) {
                    dao.deleteProduct(Product(productId))
                }
                saveData(productId, it.getString("productName"), it.getString("productSp"))
            }
    }

    private fun saveData(productId: String, productName: String?, productSp: String?) {
        val preference = this.getSharedPreferences("user", MODE_PRIVATE)
        val data = hashMapOf<String, Any>()

        data["name"] = productName!!
        data["price"] = productSp!!
        data["productId"] = productId
        data["status"] = "Ordered"
        data["userId"] = preference.getString("phoneNumber", "7985135238")!!

        val firestore = Firebase.firestore.collection("allOrders")
        val key = firestore.document().id
        data["orderId"] = key

        firestore.document(key).set(data)
            .addOnSuccessListener {
                Toast.makeText(this, "Order Placed", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show()
            }
    }
}