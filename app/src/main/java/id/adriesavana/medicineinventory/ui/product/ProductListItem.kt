package id.adriesavana.medicineinventory.ui.product

import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.mikepenz.fastadapter.items.AbstractItem
import id.adriesavana.medicineinventory.R
import id.adriesavana.medicineinventory.data.model.Product
import id.adriesavana.medicineinventory.utils.LocaleId
import id.adriesavana.medicineinventory.utils.onClick

class ProductListItem(
    private val product: Product,
    private val openProduct: (Product, Boolean) -> Unit
) :
    AbstractItem<ProductListItem, ProductListItem.ViewHolder>() {

    override fun getType(): Int = hashCode()

    override fun getLayoutRes(): Int = R.layout.item_product

    override fun getViewHolder(v: View): ViewHolder = ViewHolder(v)

    override fun bindView(holder: ViewHolder?, payloads: MutableList<Any>?) {
        super.bindView(holder!!, payloads!!)
        with(product) {
            holder.productName?.text = productName
            holder.sku?.text = sku
            holder.price?.text = LocaleId.formatCurrency(product.price.toLong())
            holder.quantity?.text = "Qty: ${quantity.toString()}"
        }
        holder.btnViewProduct?.onClick {
            openProduct(product, false)
        }

        holder.btnEditProduct?.onClick {
            openProduct(product, true)
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var productName: AppCompatTextView? = null
        var sku: AppCompatTextView? = null
        var price: AppCompatTextView? = null
        var quantity: AppCompatTextView? = null
        var btnViewProduct: View? = null
        var btnEditProduct: AppCompatImageView? = null

        init {
            productName = itemView.findViewById(R.id.tv_product_name)
            sku = itemView.findViewById(R.id.tv_sku)
            price = itemView.findViewById(R.id.tv_price)
            quantity = itemView.findViewById(R.id.tv_quantity)
            btnViewProduct = itemView.findViewById(R.id.v_bg_product)
            btnEditProduct = itemView.findViewById(R.id.iv_edit)
        }
    }
}
