package org.example.project.data.model.auth

import kotlinx.serialization.Serializable

@Serializable
data class RequestUser(
    val email: String,
    val password: String,
    val displayName: String? = null,
    val isSpecialist: Boolean? = false,
    val phone: String? = null,
    val birthDate: String? = null,
    val specialty: String? = null
)