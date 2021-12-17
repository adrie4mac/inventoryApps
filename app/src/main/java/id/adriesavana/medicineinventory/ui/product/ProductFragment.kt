package id.adriesavana.medicineinventory.ui.product

import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.appbar.MaterialToolbar
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.IItem
import com.mikepenz.fastadapter.adapters.ItemAdapter
import dagger.hilt.android.AndroidEntryPoint
import id.adriesavana.medicineinventory.R
import id.adriesavana.medicineinventory.data.model.Product
import id.adriesavana.medicineinventory.ui.product.detail.ProductDetailFragment
import id.adriesavana.medicineinventory.utils.BaseFragment
import id.adriesavana.medicineinventory.utils.bind
import id.adriesavana.medicineinventory.utils.getDrawableCompat
import id.adriesavana.medicineinventory.utils.onClick
import id.adriesavana.medicineinventory.utils.onValueChanged
import id.adriesavana.medicineinventory.utils.safeNavigate
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProductFragment : BaseFragment(R.layout.fragment_product_list) {
    private val viewModel: ProductViewModel by viewModels()

    private var itemAdapter = ItemAdapter<IItem<*, *>>()
    private var fastAdapter: FastAdapter<IItem<*, *>> = FastAdapter.with(itemAdapter)
    private val loadMoreListItem = LoadMoreListItem()

    private val toolbar by bind<MaterialToolbar>(R.id.toolbar)
    private val recyclerView by bind<RecyclerView>(R.id.rv_product)
    private val swipeRefreshLayout by bind<SwipeRefreshLayout>(R.id.srl_product)
    private val btnAddProduct by bind<AppCompatImageView>(R.id.iv_add_product)
    private val tvNoProduct by bind<AppCompatTextView>(R.id.tv_no_product)

    init {
        itemAdapter.add(loadMoreListItem)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        swipeRefreshLayout.apply {
            isRefreshing = true
            setOnRefreshListener {
                viewModel.onAction(ProductViewAction.LoadProduct)
            }
        }

        recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            itemAnimator = null
            adapter = fastAdapter
            setHasFixedSize(true)
        }

        handlePhysicalBackPressed { closeApps() }
        toolbar.apply {
            title = "Product list"
            navigationIcon = context.getDrawableCompat(R.drawable.vector_back_button)
            setNavigationOnClickListener { closeApps() }
        }

        btnAddProduct.onClick {
            findNavController().navigate(R.id.action_to_add_product)
        }

        with(viewModel) {
            viewState.observe(viewLifecycleOwner) { viewState ->
                viewState.hideLoadMore.onValueChanged(::removeLoadMore)
                viewState.loadProducts.onValueChanged(::loadProducts)
            }
            onAction(ProductViewAction.LoadProduct)
        }
    }

    private fun closeApps() {
        requireActivity().finish()
    }

    private fun removeLoadMore(doHideLoadMore: Unit?) {
        if (doHideLoadMore == null) return
        stopRefreshing()
        itemAdapter.run {
            if (!adapterItems.contains(loadMoreListItem)) return
            getAdapterPosition(loadMoreListItem).also(::remove)
        }
    }

    private fun loadProducts(productList: List<Product>?) {
        if (productList == null) return
        tvNoProduct.visibility = if (productList.isEmpty()) View.VISIBLE else View.GONE
        itemAdapter.clear()
        productList.map { ProductListItem(it, ::viewProduct) }.also(itemAdapter::add)
    }

    private fun viewProduct(product: Product, editable: Boolean) {
        ProductFragmentDirections.actionToProductDetail(
            product = product,
            editable = editable
        ).also(findNavController()::safeNavigate)
    }

    private fun stopRefreshing() {
        if (!swipeRefreshLayout.isRefreshing) return
        swipeRefreshLayout.isRefreshing = false
    }
}