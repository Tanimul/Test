package bd.com.media365.ratehammer_sdk.models.profile.response

import com.google.gson.annotations.SerializedName


data class User(

    @SerializedName("id") var id: Int? = null,
    @SerializedName("first_name") var firstName: String? = null,
    @SerializedName("last_name") var lastName: String? = null,
    @SerializedName("email") var email: String? = null,
    @SerializedName("country_calling_code") var countryCallingCode: String? = null,
    @SerializedName("contact_no") var contactNo: String? = null,
    @SerializedName("gender") var gender: String? = null,
    @SerializedName("dob") var dob: String? = null,
    @SerializedName("national_id") var nationalId: String? = null,
    @SerializedName("nationality") var nationality: String? = null,
    @SerializedName("profile_photo") var profilePhoto: ProfilePhoto? = ProfilePhoto()

)