package com.example.presenter.main_activity.viewmodels

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.Constants.USER_MODEL
import com.example.domain.usecases.read_user_datastore.ReadUserDataFromDatastoreUseCase
import com.example.domain.usecases.set_offline_user.SetOfflineUserUseCase
import com.example.domain.usecases.set_online_user.SetOnlineUserUseCase
import com.example.presenter.mappers.toAppUserUiModel
import com.example.presenter.models.AppUserUiModel
import com.example.presenter.utils.appUserUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val readUserDataFromDatastoreUseCase: ReadUserDataFromDatastoreUseCase,
) : ViewModel() {
    private val _isInitialized = mutableStateOf(false)
    val isInitialized: State<Boolean> = _isInitialized

    suspend fun setAppUserUiModel() {
        val appUserDomainModel = readUserDataFromDatastoreUseCase(USER_MODEL)
        if (appUserDomainModel != null) {
            appUserUiModel = appUserDomainModel.toAppUserUiModel()
        }
        _isInitialized.value = true
    }
}