package org.example.project.domain.model.user


// Children.kt
data class Children(
    val idChildren: String,
    val isPremium: Boolean,
    override val email: String? = null,
    override val password: String? = null,
    override val displayName: String? = null
) : User(
    uid = idChildren,
    email = email,
    password = password,
    displayName = displayName,
    rol = Rol.CHILDREN
){
    override fun toString(): String {
        return "Children(idChildren=$idChildren, isPremium=$isPremium, email=$email, displayName=$displayName)"
    }
}