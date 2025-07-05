package org.example.project

import androidx.compose.runtime.Composable
import org.example.project.ui.viewModel.GestureViewModel
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI

@Composable
expect fun CameraView()

@Composable
expect fun CameraViewWithPermission()