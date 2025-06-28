package org.example.project.domain.usecase

import org.example.project.domain.repository.FirebaseInitRepository

class FirebaseInitUseCase(private val repository: FirebaseInitRepository) {
    suspend operator fun invoke(): Boolean  = repository.initialize()
}