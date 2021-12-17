package id.adriesavana.medicineinventory.ui.product.addproduct

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import androidx.fragment.app.viewModels
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.button.MaterialButton
import dagger.hilt.android.AndroidEntryPoint
import id.adriesavana.medicineinventory.R
import id.adriesavana.medicineinventory.utils.BaseFragment
import id.adriesavana.medicineinventory.utils.LocaleId
import id.adriesavana.medicineinventory.utils.bind
import id.adriesavana.medicineinventory.utils.clearFormatting
import id.adriesavana.medicineinventory.utils.getDrawableCompat
import id.adriesavana.medicineinventory.utils.onClick
import id.adriesavana.medicineinventory.utils.onValueChanged

@AndroidEntryPoint
class AddNewProductFragment : BaseFragment(R.layout.fragment_view_product) {
    private val toolbar by bind<MaterialToolbar>(R.id.toolbar)

    private val etSku by bind<AppCompatEditText>(R.id.et_input_sku)
    private val etProductName by bind<AppCompatEditText>(R.id.et_input_product_name)
    private val etQuantity by bind<AppCompatEditText>(R.id.et_input_quantity)
    private val etPrice by bind<AppCompatEditText>(R.id.et_input_price)
    private val etUnit by bind<AppCompatEditText>(R.id.et_input_unit)
    private val btnSave by bind<MaterialButton>(R.id.btn_save_product)

    private val viewModel: AddNewProductViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        handlePhysicalBackPressed { navigateUp() }
        toolbar.apply {
            title = "Add Product"
            navigationIcon = context.getDrawableCompat(R.drawable.vector_back_button)
            setNavigationOnClickListener { navigateUp() }
        }
        initView(true)
        viewModel.viewState.observe(viewLifecycleOwner) { viewState ->
            viewState.navigateUp.onValueChanged(::onBackPrevPage)
            viewState.messageResult.onValueChanged(::showErrorMessage)
        }
    }

    private fun initView(editable: Boolean) {
        etSku.isEnabled = editable
        etProductName.isEnabled = editable
        etQuantity.isEnabled = editable
        etPrice.isEnabled = editable
        etUnit.isEnabled = editable
        btnSave.text = "Add Product"
        btnSave.visibility = View.VISIBLE
        btnSave.onClick {
            AddNewProductViewAction.AddProduct(
                sku = etSku.text.toString(),
                productName = etProductName.text.toString(),
                quantity = etQuantity.text.toString(),
                price = etPrice.text.toString(),
                unit = etUnit.text.toString(),
            ).also(viewModel::onAction)
        }

        etPrice.addTextChangedListener(formatAmountTextListener())
    }

    private fun formatAmountTextListener() = object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            // not required
        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            // not required
        }

        override fun afterTextChanged(e: Editable?) {
            etPrice.removeTextChangedListener(this)

            try {
                val formattedAmount = LocaleId.formatCurrencyFromLong(
                    e.toString().clearFormatting()
                ).replace("Rp", "", true)

                etPrice.setText(formattedAmount)
                etPrice.setSelection(if (formattedAmount.length > 10) 10 else formattedAmount.length)
            } catch (e: NumberFormatException) {
                // do nothing
            }

            etPrice.addTextChangedListener(this)
        }
    }
}
