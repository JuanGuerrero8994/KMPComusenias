package org.example.project.domain.model.auth

data class Specialist(
    val idSpecialist: String, // ID único para el especialista
    val speciality: String,   // Especialidad del especialista
    val childrenInCharge: List<Children> = listOf() // Niños a su cargo
) : User(uid = idSpecialist, email = null, displayName = null, rol = Rol.SPECIALIST)