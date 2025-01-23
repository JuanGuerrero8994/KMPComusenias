package org.example.project.data.repository


import org.example.project.domain.repository.AuthRepository
import dev.gitlive.firebase.auth.FirebaseAuth
import dev.gitlive.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.example.project.data.core.ErrorHandler
import org.example.project.data.core.Resource
import org.example.project.data.mapper.UserMapper
import org.example.project.data.model.auth.RequestUser
import org.example.project.data.model.auth.ResponseUser
import org.example.project.domain.model.auth.Rol
import org.example.project.domain.model.auth.User

class AuthRepositoryImpl(
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
    private val errorHandler: ErrorHandler // Agregar ErrorHandler como parámetro
) : AuthRepository {

    override suspend fun signIn(requestUser: RequestUser): Flow<Resource<User>> = flow {
        emit(Resource.Loading)
        try {
            val authResult = firebaseAuth.signInWithEmailAndPassword(requestUser.email, requestUser.password)
            val firebaseUser = authResult.user

            val responseUser = UserMapper.toResponseUser(firebaseUser!!)
            val user = UserMapper.toDomain(responseUser)
            emit(Resource.Success(user))
        } catch (e: Exception) {
            // Manejar errores utilizando ErrorHandler
            val appError = errorHandler.getError(e)
            emit(Resource.Error(Exception(appError.message)))
        }
    }

    override suspend fun signUp(requestUser: RequestUser): Flow<Resource<User>> = flow {
        emit(Resource.Loading)
        try {

            // Usar createUserWithEmailAndPassword para crear un nuevo usuario
            val authResult = firebaseAuth.createUserWithEmailAndPassword(requestUser.email, requestUser.password)
            val firebaseUser = authResult.user

            if (firebaseUser != null) {
                val responseUser = ResponseUser(
                    uid = firebaseUser.uid,
                    email = firebaseUser.email ?: "",
                    displayName = requestUser.displayName ?: "",
                    rol= (if (requestUser.isSpecialist!!) Rol.SPECIALIST else Rol.CHILDREN).toString()
                )

                // Guardar el usuario en Firestore
               /* firestore.collection("USERS")
                    .document(responseUser.uid)
                    .update(responseUser)
                    .set(responseUser)*/

                // Convertir el usuario al modelo de dominio y emitirlo
                val user = UserMapper.toDomain(responseUser)
                emit(Resource.Success(user))
            } else {
                emit(Resource.Error(Exception("Error al crear el usuario.")))
            }
        } catch (e: Exception) {
            // Manejar errores utilizando ErrorHandler
            val appError = errorHandler.getError(e)
            emit(Resource.Error(Exception(appError.message)))
        }
    }


    override suspend fun signOut(): Flow<Resource<String>> = flow {
        emit(Resource.Loading)
        try {
            firebaseAuth.signOut()
            emit(Resource.Success("Sesión cerrada exitosamente"))
        } catch (e: Exception) {
            // Manejar errores utilizando ErrorHandler
            val appError = errorHandler.getError(e)
            emit(Resource.Error(Exception(appError.message)))
        }
    }

    override suspend fun getCurrentUser(): Flow<Resource<User?>> = flow {
        emit(Resource.Loading)
        try {
            val firebaseUser = firebaseAuth.currentUser
            if (firebaseUser != null) {
                val responseUser = UserMapper.toResponseUser(firebaseUser)
                val user = UserMapper.toDomain(responseUser)
                emit(Resource.Success(user))
            } else {
                emit(Resource.Success(null)) // No hay un usuario autenticado
            }
        } catch (e: Exception) {
            val appError = errorHandler.getError(e)
            emit(Resource.Error(Exception(appError.message)))
        }
    }
}
