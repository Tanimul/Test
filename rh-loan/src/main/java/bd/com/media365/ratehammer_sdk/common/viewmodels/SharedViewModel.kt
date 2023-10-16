package bd.com.media365.ratehammer_sdk.common.viewmodels

import androidx.lifecycle.ViewModel
import bd.com.media365.ratehammer_sdk.models.applications_store.response.ModelApplicationStore
import bd.com.media365.ratehammer_sdk.models.applications_store.response.Offers
import bd.com.media365.ratehammer_sdk.models.select_application.response.SelectedOffer
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject constructor() : ViewModel() {

    var applicationStoreInfo: ModelApplicationStore = ModelApplicationStore()
    var offersInfo: List<Offers> = emptyList()
    var offers: Offers = Offers()
    var position: Int = 0
    var maxTotalPayable: Double = 0.0
    var maxMonthlyPayment: Double = 0.0
    var maxLikelyArp: Double = 0.0
    var isFirstTime: Boolean = false


    var applicationId: String = ""
    var offerId: String = ""
    var selectedOffer: SelectedOffer? = SelectedOffer()

    var bankAccountId: String = ""
}