package proleag.iyl.trimfitapp.models

import java.io.Serializable

data class UserData(
    val gender: String = "Male",
    val birthday: Long,
    val weight: Int,
    val height: Int,
    val steps: Int,
    val goalWeight: Int
) : Serializable
