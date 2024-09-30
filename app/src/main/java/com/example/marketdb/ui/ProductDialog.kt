package com.example.marketdb.ui

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import com.example.marketdb.R
import com.example.marketdb.application.MarketDBApp
import com.example.marketdb.data.ProductRepository
import com.example.marketdb.data.db.model.ProductEntity
import com.example.marketdb.databinding.ProductDialogBinding
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException



class ProductDialog (
    private val newProduct: Boolean = true,
    private var product: ProductEntity = ProductEntity(
        title = "",
        status = "",
        brand = ""
    ),
    private val updateUI: () -> Unit,
    private val message: (String) -> Unit
    ): DialogFragment() {

        private var _binding: ProductDialogBinding? = null
        private val binding get() = _binding!!

        private lateinit var builder: AlertDialog.Builder
        private lateinit var dialog: Dialog

        private var saveButton: Button? = null

        private lateinit var repository: ProductRepository

        //Aquí se crea y configura de forma inicial el dialog
        override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
            _binding = ProductDialogBinding.inflate(requireActivity().layoutInflater)

            //Obtenemos dentro del dialog fragment una instancia al repositorio
            repository = (requireContext().applicationContext as MarketDBApp).repository

            builder = AlertDialog.Builder(requireContext())

            //Establecemos en los text input edit text los valores del objeto product
            binding.apply {
                tietTitle.setText(product.title)
                tietStatus.setText(product.status)
                tietBrand.setText(product.brand)
            }

            dialog = if(newProduct)
                buildDialog("Guardar", "Cancelar", {
                    //Acción de guardar

                    //Obtenemos los textos ingresados y se los
                    //asignamos a nuestro objeto product
                    binding.apply {
                        product.apply {
                            title = tietTitle.text.toString()
                            status = tietStatus.text.toString()
                            brand = tietBrand.text.toString()
                        }
                    }

                    try{

                        lifecycleScope.launch(Dispatchers.IO) {
                            val result = async {
                                repository.insertProduct(product)
                            }

                            // Con esto nos esperamos a que se termine esta accion antes de ejecutar lo siguiente
                            result.await()

                            // Con esto mandamos la ejecucion de messge y updateUI al hilo principal
                            withContext(Dispatchers.Main) {
                                message(getString(R.string.save_success))
                                updateUI()
                            }


                        }


                    }catch (e: IOException){
                        message(getString(R.string.save_error))
                    }

                }, {
                    //Acción de cancelar

                })
            else
                buildDialog("Actualizar", "Borrar", {
                    //Acción de actualizar

                    //Obtenemos los textos ingresados y se los
                    //asignamos a nuestro objeto product
                    binding.apply {
                        product.apply {
                            title = tietTitle.text.toString()
                            status = tietStatus.text.toString()
                            brand = tietBrand.text.toString()
                        }
                    }

                    try{

                        lifecycleScope.launch(Dispatchers.IO) {
                            val result = async {
                                repository.updateProduct(product)
                            }

                            result.await()

                            withContext(Dispatchers.Main) {

                                message(getString(R.string.update_success))
                                updateUI()
                            }
                        }


                    }catch (e: IOException){
                        message(getString(R.string.update_error))
                    }

                }, {
//Acción de borrar

                    // Almacenamos el contexto en una variable antes de mandar el dialogo nuevo
                    val context = requireContext()

                    AlertDialog.Builder(requireContext())
                        .setTitle(getString(R.string.confirm))

                        .setMessage(getString(R.string.confirm_product, product.title))

                        .setPositiveButton(getString(R.string.ok)){ _, _ ->
                            try{
                                lifecycleScope.launch(Dispatchers.IO) {
                                    val result = async {
                                        repository.deleteProduct(product)
                                    }
                                    result.await()

                                    withContext(Dispatchers.Main) {
                                        message(context.getString(R.string.product_removed))
                                        updateUI()
                                    }
                                }

                            }catch (e: IOException){
                                message(getString(R.string.delete_error))
                            }
                        }
                        .setNegativeButton(getString(R.string.no)){ dialog, _ ->
                            dialog.dismiss()
                        }
                        .create().show()
                })



            return dialog
        }

        //Aquí es cuando se destruye
        override fun onDestroy() {
            super.onDestroy()
            _binding = null
        }

        //Se llama después de que se muestra el diálogo en pantalla
        override fun onStart() {
            super.onStart()

            //Debido a que la clase dialog no me permite referenciarme a sus botones
            val alertDialog = dialog as AlertDialog

            saveButton = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE)
            saveButton?.isEnabled = false

            binding.apply {
                setupTextWatcher(
                    tietTitle,
                    tietStatus,
                    tietBrand
                )
            }

        }

        private fun validateFields(): Boolean
                = binding.tietTitle.text.toString().isNotEmpty() &&
                binding.tietStatus.text.toString().isNotEmpty() &&
                binding.tietBrand.text.toString().isNotEmpty()

        private fun setupTextWatcher(vararg textFields: TextInputEditText){
            val textWatcher = object: TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

                override fun afterTextChanged(s: Editable?) {
                    saveButton?.isEnabled = validateFields()
                }
            }

            textFields.forEach { textField ->
                textField.addTextChangedListener(textWatcher)
            }
        }

        private fun buildDialog(
            btn1Text: String,
            btn2Text: String,
            positiveButton: () -> Unit,
            negativeButton: () -> Unit
        ): Dialog =
            builder.setView(binding.root)
                .setTitle(R.string.product)
                .setPositiveButton(btn1Text){ _, _ ->
                    //Acción para el botón positivo
                    positiveButton()
                }.setNegativeButton(btn2Text){ _, _ ->
                    //Acción para el botón negativo
                    negativeButton()
                }
                .create()

    }