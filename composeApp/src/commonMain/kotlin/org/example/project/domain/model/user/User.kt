package org.example.project.domain.model.user


// User.kt
open class User(
    open val uid: String? = null,
    open val email: String? = null,
    open val password: String? = null,
    open val displayName: String? = null,
    open val rol: Rol? = null
){
    override fun toString(): String {
        return "User(email=$email, displayName=$displayName, rol=$rol)"
    }
}

