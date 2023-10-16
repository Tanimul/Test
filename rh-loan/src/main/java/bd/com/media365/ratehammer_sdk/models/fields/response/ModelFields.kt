package bd.com.media365.ratehammer_sdk.models.fields.response

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class ModelFields(

    @SerializedName("genders") var genders: ArrayList<Genders> = arrayListOf(),
    @SerializedName("house_types") var houseTypes: ArrayList<HouseTypes> = arrayListOf(),
    @SerializedName("residential_types") var residentialTypes: ArrayList<ResidentialTypes> = arrayListOf(),
    @SerializedName("loan_purposes") var loanPurposes: ArrayList<LoanPurposes> = arrayListOf(),
    @SerializedName("loan_terms") var loanTerms: ArrayList<LoanTerms> = arrayListOf(),
    @SerializedName("loan_amounts") var loanAmounts: LoanAmounts? = LoanAmounts(),
    @SerializedName("available_languages") var availableLanguages: ArrayList<AvailableLanguages> = arrayListOf(),
    @SerializedName("application_stage") var applicationStage: ArrayList<ApplicationStage> = arrayListOf(),
    @SerializedName("contact_us_purposes") var contactUsPurposes: ArrayList<ContactUsPurposes> = arrayListOf()


)
