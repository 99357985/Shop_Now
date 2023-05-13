package com.example.shopnow.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.example.shopnow.activity.AddressActivity
import com.example.shopnow.adapter.CartAdapter
import com.example.shopnow.databinding.FragmentCartBinding
import com.example.shopnow.db.Product
import com.example.shopnow.db.ProductDatabase

class CartFragment : Fragment() {
    private lateinit var binding: FragmentCartBinding
    private lateinit var listProductIds: ArrayList<String>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCartBinding.inflate(layoutInflater)

        val preference = requireContext().getSharedPreferences("info", AppCompatActivity.MODE_PRIVATE)
        val editor = preference.edit()
        editor.putBoolean("isCart", false)
        editor.apply()

        val dao = ProductDatabase.getInstance(requireContext()).getProductDao()

        listProductIds = ArrayList()

        dao.getAllProducts().observe(requireActivity()) {
            binding.cartRecyclerView.adapter = CartAdapter(requireContext(), it)

            listProductIds.clear()
            for (data in it) {
                listProductIds.add(data.productId)
            }
            totalCost(it)
        }

        return binding.root
    }

    private fun totalCost(list: List<Product>?) {
        var total = 0
        for (item in list!!) {
            total += item.productSp!!.substring(1).toInt()
        }
        binding.apply {
            bottomTv.text = "Total item in cart : ${list.size}"
            totalCostTV.text = "Total cost : ₹$total"
        }
        binding.checkOut.setOnClickListener {
            val intent = Intent(context, AddressActivity::class.java)
            val b = Bundle()
            b.putStringArrayList("productIds", listProductIds)
            b.putString("totalCost", total.toString())
            intent.putExtras(b)
            startActivity(intent)
        }
    }
}