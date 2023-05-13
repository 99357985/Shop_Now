package com.example.shopnow.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.example.shopnow.R
import com.example.shopnow.adapter.AllOrdersAdapter
import com.example.shopnow.databinding.FragmentMoreBinding
import com.example.shopnow.model.AllOrdersModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MoreFragment : Fragment() {

    private lateinit var binding : FragmentMoreBinding
    private lateinit var list: ArrayList<AllOrdersModel>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentMoreBinding.inflate(layoutInflater)

        list = ArrayList()

        val preference = requireContext().getSharedPreferences("user", AppCompatActivity.MODE_PRIVATE)

        Firebase.firestore.collection("allOrders").get().addOnSuccessListener {
            list.clear()

            for (doc in it) {
                val data = doc.toObject(AllOrdersModel::class.java)
                list.add(data)
            }
            binding.recyclerView.adapter = AllOrdersAdapter(requireContext(), list)
        }
        return binding.root
    }
}