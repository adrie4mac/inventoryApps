package id.adriesavana.medicineinventory.ui.product.detail

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.AppCompatEditText
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.button.MaterialButton
import dagger.hilt.android.AndroidEntryPoint
import id.adriesavana.medicineinventory.R
import id.adriesavana.medicineinventory.utils.BaseFragment
import id.adriesavana.medicineinventory.utils.LocaleId
import id.adriesavana.medicineinventory.utils.bind
import id.adriesavana.medicineinventory.utils.clearFormatting
import id.adriesavana.medicineinventory.utils.getDrawableCompat
import id.adriesavana.medicineinventory.utils.hideKeyboard
import id.adriesavana.medicineinventory.utils.onClick
import id.adriesavana.medicineinventory.utils.onValueChanged

@AndroidEntryPoint
class ProductDetailFragment : BaseFragment(R.layout.fragment_view_product) {
    private val toolbar by bind<MaterialToolbar>(R.id.toolbar)
    private val args: ProductDetailFragmentArgs by navArgs()

    private val etSku by bind<AppCompatEditText>(R.id.et_input_sku)
    private val etProductName by bind<AppCompatEditText>(R.id.et_input_product_name)
    private val etQuantity by bind<AppCompatEditText>(R.id.et_input_quantity)
    private val etPrice by bind<AppCompatEditText>(R.id.et_input_price)
    private val etUnit by bind<AppCompatEditText>(R.id.et_input_unit)
    private val btnSave by bind<MaterialButton>(R.id.btn_save_product)
    private val btnDelete by bind<MaterialButton>(R.id.btn_delete)

    private val viewModel: ProductDetailViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.onAction(ProductDetailViewAction.InitProduct(args.product))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        handlePhysicalBackPressed { navigateUp() }
        toolbar.apply {
            title = "Product Detail"
            navigationIcon = context.getDrawableCompat(R.drawable.vector_back_button)
            setNavigationOnClickListener { navigateUp() }
        }
        requireActivity().hideKeyboard()
        initView(args.editable)
        viewModel.viewState.observe(viewLifecycleOwner) { viewState ->
            viewState.sku.onValueChanged(etSku::setText)
            viewState.productName.onValueChanged(etProductName::setText)
            viewState.quantity.onValueChanged(etQuantity::setText)
            viewState.price.onValueChanged(etPrice::setText)
            viewState.unit.onValueChanged(etUnit::setText)
            viewState.navigateUp.onValueChanged(::onBackPrevPage)
            viewState.messageResult.onValueChanged(::showErrorMessage)
        }
    }

    private fun initView(editable: Boolean) {
        etSku.isEnabled = false
        etProductName.isEnabled = editable
        etQuantity.isEnabled = editable
        etPrice.isEnabled = editable
        etUnit.isEnabled = editable
        if (editable) {
            btnSave.visibility = View.VISIBLE
            btnSave.onClick {
                ProductDetailViewAction.UpdateProduct(
                    sku = etSku.text.toString(),
                    productName = etProductName.text.toString(),
                    quantity = etQuantity.text.toString(),
                    price = etPrice.text.toString(),
                    unit = etUnit.text.toString(),
                ).also(viewModel::onAction)
            }
            return
        }

        btnDelete.visibility = View.VISIBLE
        btnDelete.onClick {
            viewModel.onAction(ProductDetailViewAction.DeleteProduct(args.product))
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
