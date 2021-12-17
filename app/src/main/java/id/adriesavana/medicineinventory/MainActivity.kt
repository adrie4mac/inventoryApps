package id.adriesavana.medicineinventory

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.NavGraph
import androidx.navigation.findNavController
import dagger.hilt.android.AndroidEntryPoint
import id.adriesavana.medicineinventory.utils.hideKeyboard
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val navController: NavController by lazy { findNavController(R.id.nav_host_fragment) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        lifecycleScope.launch {
            navController.graph =
                navController.navInflater.inflate(R.navigation.app_navigation_graph)
                    .apply(::startDestination)
        }
    }

    private fun startDestination(nav: NavGraph) {
        nav.startDestination = R.id.login_fragment
    }

    override fun onSupportNavigateUp(): Boolean {
        hideKeyboard()
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}
