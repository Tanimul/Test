package bd.com.media365.ratehammer_sdk.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import bd.com.media365.ratehammer_sdk.R
import bd.com.media365.ratehammer_sdk.databinding.ItemSdkOfferDetailsBinding
import bd.com.media365.ratehammer_sdk.models.applications_store.response.ModelApplicationStore
import bd.com.media365.ratehammer_sdk.models.applications_store.response.Offers
import timber.log.Timber
import java.text.DecimalFormat
import kotlin.math.roundToInt

class AdapterOfferDetails(
    private val mOfferItems: List<Offers>,
    private val mApplicationStoreItems: ModelApplicationStore,
    private var maxTotalPayable: Double = 0.0,
    private var maxMonthlyPayment: Double = 0.0,
    private var maxLikelyArp: Double = 0.0,
    private val onItemClicked: (String) -> Unit
) : RecyclerView.Adapter<AdapterOfferDetails.UserListViewHolder>() {

    private val formatter = DecimalFormat("#,###,###")
    inner class UserListViewHolder(val binding: ItemSdkOfferDetailsBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserListViewHolder {
        return UserListViewHolder(
            ItemSdkOfferDetailsBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: UserListViewHolder, position: Int) {
        Timber.d("onBindViewHolder: ${mOfferItems.size}")

        setupView(holder, position)

        holder.binding.btnChooseThisLender.setOnClickListener {
            onItemClicked.invoke("${mOfferItems[position].id}")
        }

    }

    override fun getItemCount(): Int {
        return mOfferItems.size
    }

    private fun setupView(holder: UserListViewHolder, position: Int) {
        val item = mOfferItems[position]
        val resources = holder.itemView.context.resources

        with(holder.binding) {
            with(item) {
                vLender = lender

                //<editor-fold desc="Lambda Expression">
                val formatDouble = { value: Double? -> formatter.format(value) }
                val getString = { resId: Int -> resources.getString(resId) }
                //</editor-fold>

                //<editor-fold desc="Format Values">
                val loanAmount = formatDouble(mApplicationStoreItems.loanAmount?.toDouble())
                val totalPayable = formatDouble(item.totalPayable?.toDouble())
                val totalInterest = formatDouble(item.totalInterest?.toDouble())
                val monthlyPayment = formatDouble(item.monthlyPayment?.toDouble())
                //</editor-fold>

                //<editor-fold desc="Text Value Set">
                pbTotalPayable.tvTitle.text = getString(R.string.total_repayable)
                pbMonthlyPayment.tvTitle.text = getString(R.string.monthly_payment)
                pbLikelyApr.tvTitle.text = getString(R.string.likely_apr)

                tvLoanAmountValue.text = "$loanAmount ${getString(R.string.sar)}"
                tvTotalRepayableValue.text = "$totalPayable ${getString(R.string.sar)}"
                tvAprValue.text = "${item.interestRate} ${getString(R.string._percent)}"
                tvTotalCostCreditValue.text = "$totalInterest ${getString(R.string.sar)}"
                tvLoanTermValue.text = "${mApplicationStoreItems.loanTermInMonth} ${getString(R.string.months)}"
                tvMonthlyPaymentValue.text = "$monthlyPayment ${getString(R.string.sar)}"
                tvAdminFeeValue.text = "0 ${getString(R.string.sar)}"
                tvOfferStatusValue.text = getString(R.string.likely_offer)
                //</editor-fold>

                //<editor-fold desc="Progress Value">
                val progressTotalPayable =
                    calculateProgress(item.totalPayable?.toDouble(), maxTotalPayable)
                pbTotalPayable.pbTotal.pbHorizontal.progress = progressTotalPayable.roundToInt()

                val progressMonthlyPayment =
                    calculateProgress(item.totalInterest?.toDouble(), maxMonthlyPayment)
                pbMonthlyPayment.pbTotal.pbHorizontal.progress = progressMonthlyPayment.toInt()

                val progressLikelyApr = calculateProgress(item.monthlyPayment?.toDouble(), maxLikelyArp)
                pbLikelyApr.pbTotal.pbHorizontal.progress = progressLikelyApr.toInt()
                //</editor-fold>

                //<editor-fold desc="View Visibility for First Item">
                val isNotPositionZero = position != 0
                pbTotalPayable.tvLowest.visibility = if (isNotPositionZero) View.INVISIBLE else View.VISIBLE
                pbMonthlyPayment.tvLowest.visibility = if (isNotPositionZero) View.INVISIBLE else View.VISIBLE
                pbLikelyApr.tvLowest.visibility = if (isNotPositionZero) View.INVISIBLE else View.VISIBLE
                layoutRecommended.visibility = if (isNotPositionZero) View.GONE else View.VISIBLE
                //</editor-fold>

            }
        }
    }

    private fun calculateProgress(value: Double?, maxValue: Double): Double {
        return value?.let { rate ->
            (100.0 * rate) / maxValue
        } ?: 0.0
    }

}