package org.example.project.data.model.auth

import kotlinx.serialization.Serializable

@Serializable
data class ResponseUser(
    val uid: String,
    val email: String,
    val displayName: String,
    val rol: String
)