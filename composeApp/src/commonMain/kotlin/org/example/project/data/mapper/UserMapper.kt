package org.example.project.data.mapper


import dev.gitlive.firebase.auth.FirebaseUser
import org.example.project.data.model.auth.RequestUser
import org.example.project.data.model.auth.ResponseUser
import org.example.project.domain.model.user.Rol
import org.example.project.domain.model.user.User


object UserMapper {
    fun toDomain(responseUser: ResponseUser): User =
        User(
            uid = responseUser.uid,
            email = responseUser.email,
            displayName = responseUser.displayName,
            rol = if (responseUser.rol == "SPECIALIST") Rol.SPECIALIST else Rol.CHILDREN
        )

    fun toResponseUser(firebaseUser: FirebaseUser): ResponseUser = ResponseUser(
        uid = firebaseUser.uid,
        email = firebaseUser.email ?: "",
        displayName = firebaseUser.displayName ?: "",
        rol = Rol.CHILDREN.toString()
    )

    fun toRequestUser(user: User): RequestUser =
        RequestUser(
            email = user.email,
            password = user.password,
            displayName = user.displayName,
            isSpecialist = user.rol == Rol.SPECIALIST
        )

}