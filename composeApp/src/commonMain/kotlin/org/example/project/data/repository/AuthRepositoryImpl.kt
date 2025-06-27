package org.example.project.data.repository

import org.example.project.domain.repository.AuthRepository
import dev.gitlive.firebase.auth.FirebaseAuth
import dev.gitlive.firebase.firestore.FirebaseFirestore
import io.github.aakira.napier.Napier
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import org.example.project.constants.FirebaseConstants.USERS_COLLECTION
import org.example.project.data.core.ErrorHandler
import org.example.project.data.core.Resource
import org.example.project.data.mapper.UserMapper
import org.example.project.data.model.auth.ResponseUser
import org.example.project.domain.model.user.Rol
import org.example.project.domain.model.user.User

class AuthRepositoryImpl(
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
    private val errorHandler: ErrorHandler
) : AuthRepository {

    override suspend fun signIn(user: User): Flow<Resource<User>> = flow {
        emit(Resource.Loading)
        try {
            val requestUser = UserMapper.toRequestUser(user)
            val authResult = firebaseAuth.signInWithEmailAndPassword(requestUser.email!!, requestUser.password!!)
            val firebaseUser = authResult.user

            val responseUser = UserMapper.toResponseUser(firebaseUser!!)
            val user = UserMapper.toDomain(responseUser)
            emit(Resource.Success(user))

        } catch (e: Exception) {
            val appError = errorHandler.getError(e)
            Napier.e("❌ Error detectado en signIn: ${appError.message}", e)
            emit(Resource.Error(Exception(appError.message)))
        }
    }.catch { e ->
        val appError = errorHandler.getError(e)
        Napier.e("❌ Error inesperado en signIn (catch): ${appError.message}", e)
        emit(Resource.Error(Exception(appError.message)))
    }

    override suspend fun signUp(user: User): Flow<Resource<User>> = flow {
        emit(Resource.Loading)
        try {
            val requestUser = UserMapper.toRequestUser(user)
            val authResult = firebaseAuth.createUserWithEmailAndPassword(requestUser.email!!, requestUser.password!!)
            val firebaseUser = authResult.user

            if (firebaseUser != null) {
                val responseUser = ResponseUser(
                    uid = firebaseUser.uid,
                    email = firebaseUser.email ?: "",
                    displayName = requestUser.displayName ?: "",
                    rol = (if (requestUser.isSpecialist!!) Rol.SPECIALIST else Rol.CHILDREN).toString()
                )

                // ✅ Reemplazamos update() por set() para crear el documento si no existe
                firestore.collection(USERS_COLLECTION)
                    .document(responseUser.uid)
                    .set(responseUser)

                val user = UserMapper.toDomain(responseUser)
                emit(Resource.Success(user))
            } else {
                Napier.e("❌ Error al crear el usuario: FirebaseUser es null")
                emit(Resource.Error(Exception("Error al crear el usuario.")))
            }
        } catch (e: Exception) {
            val appError = errorHandler.getError(e)
            Napier.e("❌ Error detectado en signUp: ${appError.message}", e)
            emit(Resource.Error(Exception(appError.message)))
        }
    }.catch { e ->
        val appError = errorHandler.getError(e)
        Napier.e("❌ Error inesperado en signUp (catch): ${appError.message}", e)
        emit(Resource.Error(Exception(appError.message)))
    }

    override suspend fun signOut(): Flow<Resource<String>> = flow {
        emit(Resource.Loading)
        try {
            firebaseAuth.signOut()
            emit(Resource.Success("Sesión cerrada exitosamente"))
        } catch (e: Exception) {
            val appError = errorHandler.getError(e)
            Napier.e("❌ Error detectado en signOut: ${appError.message}", e)
            emit(Resource.Error(Exception(appError.message)))
        }
    }.catch { e ->
        val appError = errorHandler.getError(e)
        Napier.e("❌ Error inesperado en signOut (catch): ${appError.message}", e)
        emit(Resource.Error(Exception(appError.message)))
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
            Napier.e("❌ Error detectado en getCurrentUser: ${appError.message}", e)
            emit(Resource.Error(Exception(appError.message)))
        }
    }.catch { e ->
        val appError = errorHandler.getError(e)
        Napier.e("❌ Error inesperado en getCurrentUser (catch): ${appError.message}", e)
        emit(Resource.Error(Exception(appError.message)))
    }

    override suspend fun resetPassword(email: String): Flow<Resource<String>> = flow {
        emit(Resource.Loading)
        try {
            firebaseAuth.sendPasswordResetEmail(email)
            emit(Resource.Success("Correo de restablecimiento enviado correctamente"))
        } catch (e: Exception) {
            val appError = errorHandler.getError(e)
            Napier.e("❌ Error detectado en resetPassword: ${appError.message}", e)
            emit(Resource.Error(Exception(appError.message)))
        }
    }.catch { e ->
        val appError = errorHandler.getError(e)
        Napier.e("❌ Error inesperado en resetPassword (catch): ${appError.message}", e)
        emit(Resource.Error(Exception(appError.message)))
    }
}
