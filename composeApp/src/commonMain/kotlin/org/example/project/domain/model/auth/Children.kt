package org.example.project.domain.model.auth


data class Children(
    val idChildren: String,  // ID único para el niño
    val isPremium: Boolean   // Si es premium o no
) : User(uid = idChildren, email = null, displayName = null, rol = Rol.CHILDREN)
