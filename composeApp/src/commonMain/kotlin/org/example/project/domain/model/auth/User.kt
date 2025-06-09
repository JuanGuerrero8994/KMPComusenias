package org.example.project.domain.model.auth


open class User (
    val uid: String,
    val email: String?,
    val displayName: String? = null,
    val rol: Rol?=null
){
    override fun toString(): String {
        return super.toString()
    }
}