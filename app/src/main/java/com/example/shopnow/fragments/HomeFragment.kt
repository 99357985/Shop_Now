package com.example.shopnow.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.denzcoskun.imageslider.models.SlideModel
import com.example.shopnow.R
import com.example.shopnow.adapter.CategoryAdapter
import com.example.shopnow.adapter.ProductAdapter
import com.example.shopnow.databinding.FragmentHomeBinding
import com.example.shopnow.model.AddProductModel
import com.example.shopnow.model.CategoryModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class BlankFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(layoutInflater)

        getCategory()
        getProducts()
        getSliderImage()

        val preference = requireContext().getSharedPreferences("info", AppCompatActivity.MODE_PRIVATE)
        if (preference.getBoolean("isCart", false))
             findNavController().navigate(R.id.action_homeFragment_to_cartFragment)

        return binding.root
    }

    private fun getSliderImage() {
                val imageList = ArrayList<SlideModel>()
                imageList.add(SlideModel(R.drawable.mac))
                imageList.add(SlideModel(R.drawable.iphone14))
                imageList.add(SlideModel(R.drawable.ipad))
                imageList.add(SlideModel(R.drawable.imac))

                binding.sliderHomeImageView.setImageList(imageList)
    }

    private fun getProducts() {
        val list = ArrayList<AddProductModel>()
        Firebase.firestore.collection("products")
            .get().addOnSuccessListener {
                list.clear()
                for (doc in it.documents) {
                    val data = doc.toObject(AddProductModel::class.java)
                    list.add(data!!)
                }
                binding.productRecyclerView.adapter = ProductAdapter(requireContext(), list)
            }
    }

    private fun getCategory() {
        val list = ArrayList<CategoryModel>()
        Firebase.firestore.collection("categories")
            .get().addOnSuccessListener {
                list.clear()
                for (doc in it.documents) {
                    val data = doc.toObject(CategoryModel::class.java)
                    list.add(data!!)
                }
                binding.categoryRecyclerView.adapter = CategoryAdapter(requireContext(), list)
            }
    }
}