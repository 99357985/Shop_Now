package com.example.shopnow.activity

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.shopnow.R
import com.example.shopnow.databinding.ActivityAddressBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class AddressActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddressBinding
    private lateinit var preference : SharedPreferences
    private lateinit var totalCost : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddressBinding.inflate(layoutInflater)
        setContentView(binding.root)
        preference = this.getSharedPreferences("user", MODE_PRIVATE)

        totalCost = intent.getStringExtra("totalCost")!!
        loadUserInfo()

        binding.proceedToCheckout.setOnClickListener {
            validateData(
                binding.userNameAddressAct.text.toString(),
                binding.phoneNoAddressAct.text.toString(),
                binding.houseDetailsAddressAct.text.toString(),
                binding.cityAddressAct.text.toString(),
                binding.pinCodeAddressAct.text.toString(),
                binding.stateAddressAct.text.toString()
            )
        }
    }

    private fun validateData(
        name: String,
        number: String,
        houseDetails: String,
        city: String,
        pinCode: String,
        state: String
    ) {
        if (name.isEmpty() || number.isEmpty() || houseDetails.isEmpty() || city.isEmpty() || pinCode.isEmpty() || state.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
        } else {
            storeData(houseDetails, city, pinCode, state)
        }
    }

    private fun storeData(
        houseDetails: String,
        city: String,
        pinCode: String,
        state: String
    ) {
        val map = hashMapOf<String, Any>()
        map["houseDetails"] = houseDetails
        map["city"] = city
        map["pinCode"] = pinCode
        map["state"] = state

        Firebase.firestore.collection("users").document(preference.getString("phoneNumber", "7985135238")!!)
            .update(map)
            .addOnSuccessListener {
                val b = Bundle()
                b.putStringArrayList("productIds", intent.getStringArrayListExtra("productIds"))
                b.putString("totalCost", totalCost)
                intent.putExtras(b)
                val intent = Intent(this, CheckoutActivity::class.java)
                intent.putExtras(b)
                startActivity(intent)
            }
            .addOnFailureListener {
                Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show()
            }
    }

    private fun loadUserInfo() {

        val builder = AlertDialog.Builder(this)
            .setTitle("Loading....")
            .setMessage("Please Wait")
            .setCancelable(false)
            .create()
        builder.show()

        Firebase.firestore.collection("users").document(preference.getString("phoneNumber", "7985135238")!!)
            .get()
            .addOnSuccessListener {
                binding.apply {
                    userNameAddressAct.setText(it.getString("userName"))
                    phoneNoAddressAct.setText(it.getString("userPhoneNumber"))
                    houseDetailsAddressAct.setText(it.getString("houseDetails"))
                    cityAddressAct.setText(it.getString("city"))
                    pinCodeAddressAct.setText(it.getString("pinCode"))
                    stateAddressAct.setText(it.getString("state"))
                }
                builder.dismiss()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show()
            }
    }
}