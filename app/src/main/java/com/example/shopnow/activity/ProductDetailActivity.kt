package com.example.shopnow.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.example.shopnow.MainActivity
import com.example.shopnow.databinding.ActivityProductDetailBinding
import com.example.shopnow.db.Product
import com.example.shopnow.db.ProductDao
import com.example.shopnow.db.ProductDatabase
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProductDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProductDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getProductDetails(intent.getStringExtra("id"))
    }

    private fun cartAction(
        productId: String,
        name: String?,
        productSp: String?,
        productCoverImage: String?
    ) {
        val productDao = ProductDatabase.getInstance(this).getProductDao()

        if (productDao.isExist(productId) != null) {
            binding.addToCart.text = "Go To Cart"
        } else {
            binding.addToCart.text = "Add to Cart"
        }
        binding.addToCart.setOnClickListener {
            if (productDao.isExist(productId) != null) {
                openCart()
            } else {
                addToCart(productDao, productId, name, productSp, productCoverImage)
            }
        }
    }

    private fun addToCart(
        productDao: ProductDao,
        productId: String,
        name: String?,
        productSp: String?,
        productCoverImage: String?
    ) {
        val product = Product(productId, name, productCoverImage, productSp)
        lifecycleScope.launch(Dispatchers.IO)  {
            productDao.insertProduct(product)
            binding.addToCart.text = "Go To Cart"
        }
    }

    private fun openCart() {
        val preference = this.getSharedPreferences("info", MODE_PRIVATE)
        val editor = preference.edit()
        editor.putBoolean("isCart", true)
        editor.apply()

        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    private fun getProductDetails(productId: String?) {
        Firebase.firestore.collection("products").document(productId!!).get()
            .addOnSuccessListener {
                val list = it.get("productImages") as ArrayList<String>
                binding.textView2.text = it.getString("productName")
                binding.textView3.text = it.getString("productSp")
                binding.textView4.text = it.getString("productDescription")

                val slideList = ArrayList<SlideModel>()
                for (data in list) {
                    slideList.add(SlideModel(data, ScaleTypes.CENTER_CROP))
                }
                binding.imageSlider.setImageList(slideList)

                cartAction(productId, it.getString("productName"), it.getString("productSp"), it.getString("productCoverImg"))
            }
            .addOnFailureListener {
                Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show()
            }
    }
}