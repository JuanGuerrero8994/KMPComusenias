package org.example.project.domain.model.user

// Specialist.kt
data class Specialist(
    val idSpecialist: String,
    val speciality: String,
    val childrenInCharge: List<Children> = listOf(),
    override var email: String? = null,
    override var password: String? = null,
    override var displayName: String? = null
) : User(
    uid = idSpecialist,
    email = email,
    password = password,
    displayName = displayName,
    rol = Rol.SPECIALIST
){
    override fun toString(): String {
        return "Specialist(idSpecialist=$idSpecialist, speciality=$speciality, childrenInCharge=$childrenInCharge, email=$email, displayName=$displayName)"
    }
}