package com.example.marketdb.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.marketdb.R
import com.example.marketdb.application.MarketDBApp
import com.example.marketdb.data.ProductRepository
import com.example.marketdb.data.db.model.ProductEntity
import com.example.marketdb.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private var products: MutableList<ProductEntity> = mutableListOf()
    private lateinit var repository: ProductRepository

    private lateinit var productAdapter: ProductAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        repository = (application as MarketDBApp).repository

        productAdapter = ProductAdapter{ selectedProduct ->

            //Click al registro de cada producto

            val dialog = ProductDialog(newProduct = false, product = selectedProduct, updateUI = {
                updateUI()
            }, message = { text ->
                // Aquí va la funcion para los mensajes
                message(text)

            })

            dialog.show(supportFragmentManager, "dialog2")

        }

        //Establezco el recyclerview
        binding.rvProducts.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = productAdapter
        }


        updateUI()


    }

    fun click(view: View) {
        //Manejamos el click del floating action button

        val dialog = ProductDialog ( updateUI = {
            updateUI()
        }, message = { text ->
            // Aquí va el mensaje
            message(text)
        })

        dialog.show(supportFragmentManager, "dialog1")

    }

    private fun message(text: String) {

        Snackbar.make(
            binding.cl,
            text,
            Snackbar.LENGTH_SHORT
        )
            .setTextColor(getColor(R.color.white))
            .setBackgroundTint(getColor(R.color.snackbar))
            .show()

    }

    private fun updateUI(){
        lifecycleScope.launch {
            products = repository.getAllProducts()

            binding.tvSinRegistros.visibility =
                if(products.isNotEmpty()) View.INVISIBLE else View.VISIBLE

            productAdapter.updateList(products)
        }
    }
}