package bd.com.media365.ratehammer_sdk.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import bd.com.media365.ratehammer_sdk.databinding.ItemSdkBankInfoBinding
import bd.com.media365.ratehammer_sdk.models.banks.response.Data
import timber.log.Timber

class AdapterBank(
    private var mBankItems: List<Data>,
    private val onItemSelect: (Data) -> Unit,
) : RecyclerView.Adapter<AdapterBank.UserListViewHolder>() {

    inner class UserListViewHolder(val binding: ItemSdkBankInfoBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserListViewHolder {
        return UserListViewHolder(
            ItemSdkBankInfoBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: UserListViewHolder, position: Int) {
        Timber.d("onBindViewHolder: ${mBankItems.size}")

        val item = mBankItems[position]

        holder.itemView.setOnClickListener {
            onItemSelect.invoke(item)
        }

        holder.binding.data = item

    }

    override fun getItemCount(): Int {
        return mBankItems.size
    }

    // Update the list of banks
    fun updateData(newList: List<Data>) {
        mBankItems = newList
        notifyDataSetChanged()
    }
}
