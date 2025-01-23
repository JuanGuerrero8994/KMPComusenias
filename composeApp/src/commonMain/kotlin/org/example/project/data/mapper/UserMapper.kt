package org.example.project.data.mapper



import dev.gitlive.firebase.auth.FirebaseUser
import org.example.project.data.model.auth.ResponseUser
import org.example.project.domain.model.auth.Rol
import org.example.project.domain.model.auth.User


object UserMapper  {
    fun toDomain(responseUser: ResponseUser): User {
        return User(
            uid = responseUser.uid,
            email = responseUser.email,
            displayName = responseUser.displayName,
            rol = if (responseUser.rol == "SPECIALIST") Rol.SPECIALIST else Rol.CHILDREN
        )
    }


    fun toResponseUser(firebaseUser: FirebaseUser): ResponseUser {
        return ResponseUser(
            uid = firebaseUser.uid,
            email = firebaseUser.email ?: "",
            displayName = firebaseUser.displayName ?: "",
            rol= Rol.CHILDREN.toString()
        )
    }

}