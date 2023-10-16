package bd.com.media365.ratehammer_sdk.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import bd.com.media365.ratehammer_sdk.R
import bd.com.media365.ratehammer_sdk.databinding.ItemSdkOfferBinding
import bd.com.media365.ratehammer_sdk.extention.loadImageFromDrawable
import bd.com.media365.ratehammer_sdk.models.applications_store.response.ModelApplicationStore
import bd.com.media365.ratehammer_sdk.models.applications_store.response.Offers
import timber.log.Timber
import java.text.DecimalFormat
import kotlin.math.roundToInt

class AdapterOffer(
    private val mOfferItems: List<Offers>,
    private val onItemClicked: (List<Offers>, ModelApplicationStore, Int, Double, Double, Double) -> Unit,
    private val onItemSelect: (Int) -> Unit,
) : RecyclerView.Adapter<AdapterOffer.UserListViewHolder>() {

    private var selectedPosition = 0
    private var maxTotalPayable: Double = 0.0
    private var maxMonthlyPayment: Double = 0.0
    private var maxLikelyArp: Double = 0.0
    private var mApplicationStoreItems: ModelApplicationStore = ModelApplicationStore()
    private val formatter = DecimalFormat("#,###,###")

    inner class UserListViewHolder(val binding: ItemSdkOfferBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserListViewHolder {
        return UserListViewHolder(
            ItemSdkOfferBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: UserListViewHolder, position: Int) {
        Timber.d("onBindViewHolder: ${mOfferItems.size}")
        setupView(holder, position)

        holder.itemView.setOnClickListener {
            selectedPosition = position
            notifyDataSetChanged()

            mOfferItems[position].id?.let { it1 -> onItemSelect.invoke(it1) }
        }
        holder.binding.tvTapForDetails.setOnClickListener {
            onItemClicked.invoke(
                mOfferItems,
                mApplicationStoreItems,
                position,
                maxTotalPayable,
                maxMonthlyPayment,
                maxLikelyArp
            )
        }

    }

    override fun getItemCount(): Int {
        return mOfferItems.size
    }

    private fun setupView(holder: UserListViewHolder, position: Int) {
        val item = mOfferItems[position]
        val resources = holder.itemView.context.resources
        val isSelected = position == selectedPosition

        with(holder.binding) {
            with(item) {
                vLender = lender

                //<editor-fold desc="Lambda Expression">
                val formatDouble = { value: Double? -> formatter.format(value) }
                val getString = { resId: Int -> resources.getString(resId) }
                //</editor-fold>

                //<editor-fold desc="Format Values">
                val totalPayable = formatDouble(item.totalPayable?.toDouble())
                val monthlyPayment = formatDouble(item.monthlyPayment?.toDouble())
                //</editor-fold>

                //<editor-fold desc="Text Value Set">
                pbTotalPayable.tvTitle.text = getString(R.string.total_repayable)
                pbMonthlyPayment.tvTitle.text = getString(R.string.monthly_payment)
                pbLikelyApr.tvTitle.text = getString(R.string.likely_apr)

                pbTotalPayable.tvValue.text = "$totalPayable ${getString(R.string.sar)}"
                pbMonthlyPayment.tvValue.text = "$monthlyPayment ${getString(R.string.sar)}"
                pbLikelyApr.tvValue.text = "$interestRate ${getString(R.string._percent)}"

                //</editor-fold>

                //<editor-fold desc="Selected View">
                val colorResId = if (isSelected) R.color.color_FF1D25 else R.color.color_E2E8F0
                cardView.strokeColor = holder.itemView.context.getColor(colorResId)

                with(ivSelect) {
                    loadImageFromDrawable(
                        if (isSelected) R.drawable.ic_radio_button
                        else R.drawable.ic_radio_button_unselected
                    )
                }
                //</editor-fold>

                //<editor-fold desc="Progress Value">
                val progressTotalPayable =
                    calculateProgress(item.totalPayable?.toDouble(), maxTotalPayable)
                pbTotalPayable.pbTotal.pbHorizontal.progress = progressTotalPayable.roundToInt()

                val progressMonthlyPayment =
                    calculateProgress(item.monthlyPayment?.toDouble(), maxMonthlyPayment)
                pbMonthlyPayment.pbTotal.pbHorizontal.progress = progressMonthlyPayment.toInt()

                val progressLikelyApr =
                    calculateProgress(item.interestRate?.toDouble(), maxLikelyArp)
                pbLikelyApr.pbTotal.pbHorizontal.progress = progressLikelyApr.toInt()
                //</editor-fold>

                //<editor-fold desc="View Visibility for First Item">
                val isNotPositionZero = position != 0
                pbTotalPayable.tvLowest.visibility =
                    if (isNotPositionZero) View.INVISIBLE else View.VISIBLE
                pbMonthlyPayment.tvLowest.visibility =
                    if (isNotPositionZero) View.INVISIBLE else View.VISIBLE
                pbLikelyApr.tvLowest.visibility =
                    if (isNotPositionZero) View.INVISIBLE else View.VISIBLE
                tvRecommended.visibility = if (isNotPositionZero) View.INVISIBLE else View.VISIBLE
                //</editor-fold>
            }
        }
    }

    private fun calculateProgress(value: Double?, maxValue: Double): Double {
        Timber.d("calculateProgress : $value $maxValue")
        return value?.let { rate ->
            (100.0 * rate) / maxValue
        } ?: 0.0
    }

    fun updateTotalPayable(newMaxTotalPayable: Double) {
        maxTotalPayable = newMaxTotalPayable
    }

    fun updateMonthlyPayment(newMaxMonthlyPayment: Double) {
        maxMonthlyPayment = newMaxMonthlyPayment
    }

    fun updateMaxLikelyArp(newMaxLikelyArp: Double) {
        maxLikelyArp = newMaxLikelyArp
    }

    fun updateData(mdata: ModelApplicationStore) {
        mApplicationStoreItems = mdata
    }
}
