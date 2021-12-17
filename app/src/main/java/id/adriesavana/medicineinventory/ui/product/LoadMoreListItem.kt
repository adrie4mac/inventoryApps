package id.adriesavana.medicineinventory.ui.product

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.mikepenz.fastadapter.items.AbstractItem
import id.adriesavana.medicineinventory.R

class LoadMoreListItem :
    AbstractItem<LoadMoreListItem, LoadMoreListItem.ViewHolder>() {

    override fun getType(): Int = hashCode()

    override fun getLayoutRes(): Int = R.layout.list_item_loadmore

    override fun getViewHolder(v: View): ViewHolder = ViewHolder(v)

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}
