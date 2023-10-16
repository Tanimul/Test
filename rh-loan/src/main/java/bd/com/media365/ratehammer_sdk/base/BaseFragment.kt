package bd.com.media365.ratehammer_sdk.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import bd.com.media365.ratehammer_sdk.R
import bd.com.media365.ratehammer_sdk.dialogs.LoadingDialog

abstract class BaseFragment<E : ViewBinding> : Fragment() {

    val TAG = javaClass.simpleName

    private var _binding: E? = null
    protected abstract fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?): E
    protected abstract fun init()

    private var mContext: Context? = null

    private var loadingDialog: LoadingDialog? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initArguments()

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = getViewBinding(inflater, container)
        return _binding?.root
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    open fun initArguments() {

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    fun initLoadingDialog() {
        loadingDialog = LoadingDialog(mContext, text = mContext?.getString(R.string.label_loading))
    }

    val binding: E
        get() = _binding!!

    fun showLoadingDialog() {
        loadingDialog?.show()
    }

    fun dismissLoadingDialog() {
        loadingDialog?.dismiss()
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}
