package com.example.presenter.home.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.Constants.USER_MODEL
import com.example.domain.usecases.delete_user_datastore.DeleteUserDataFromDatastoreUseCase
import com.example.domain.usecases.read_user_datastore.ReadUserDataFromDatastoreUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
) : ViewModel(){

}