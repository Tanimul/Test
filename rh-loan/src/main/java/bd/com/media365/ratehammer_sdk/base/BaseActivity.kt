package bd.com.media365.ratehammer_sdk.base


import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding


abstract class BaseActivity<E : ViewBinding> : AppCompatActivity() {

    val TAG = javaClass.simpleName


    private var _binding: E? = null
    protected abstract fun getViewBinding(): E

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = getViewBinding()
        setContentView(_binding!!.root)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)
        window.statusBarColor = Color.TRANSPARENT
        init(savedInstanceState)
    }

    protected abstract fun init(savedInstanceState: Bundle?)


    val binding: E
        get() = _binding!!



    override fun onStop() {

        super.onStop()
    }

    override fun onResume() {
        super.onResume()

    }

}