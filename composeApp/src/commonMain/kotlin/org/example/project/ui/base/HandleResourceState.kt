package org.example.project.ui.base

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import org.example.project.data.core.Resource
import org.example.project.ui.components.customViews.LoadingDialog

@Composable
fun <T> HandleResourceState(
    resource: Resource<T>,
    isLoading: State<Boolean>,
    onSuccess: @Composable (T) -> Unit,
    onError: @Composable ((Throwable) -> Unit)? = null,
    loadingContent: @Composable (() -> Unit)? = { LoadingDialog(isLoading = isLoading) }
) {
    when (resource) {
        is Resource.Loading -> loadingContent?.invoke()
        is Resource.Success -> onSuccess(resource.data)
        is Resource.Error -> onError?.invoke(resource.exception)
    }
}