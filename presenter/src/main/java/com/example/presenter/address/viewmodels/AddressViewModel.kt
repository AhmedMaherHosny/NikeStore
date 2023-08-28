package com.example.presenter.address.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.models.AddressDomainModel
import com.example.domain.usecases.get_address.GetAddressUseCase
import com.example.domain.usecases.upsert_address.UpsertAddressUseCase
import com.example.presenter.address.AddressEvent
import com.example.presenter.address.states.AddressViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddressViewModel @Inject constructor(
    private val upsertAddressUseCase: UpsertAddressUseCase,
    private val getAddressUseCase: GetAddressUseCase
) : ViewModel() {
    var addressViewState by mutableStateOf(AddressViewState())
        private set
    private val _eventClicks = MutableSharedFlow<AddressEvent>()
    private val eventClicks get() = _eventClicks.asSharedFlow()

    init {
        subscribeEvents()
        getAddress()
    }

    private fun subscribeEvents() {
        viewModelScope.launch {
            eventClicks.collect {
                handleEvents(it)
            }
        }
    }

    private fun handleEvents(event: AddressEvent) {
        when (event) {
            AddressEvent.OnSaveButtonClicked -> {
                upsert()
            }
        }
    }

    private fun upsert() {
        viewModelScope.launch {
            upsertAddressUseCase(
                AddressDomainModel(
                    id = if (addressViewState.id == null) null else addressViewState.id,
                    addressLine1 = addressViewState.addressLine1.text.value,
                    addressLine2 = addressViewState.addressLine2.text.value,
                    phoneNumber = addressViewState.phoneNumber.text.value,
                    zipCode = addressViewState.zipCode.text.value,
                    country = addressViewState.country.text.value,
                    city = addressViewState.city.text.value,
                )
            )
        }
    }

    private fun getAddress() {
        viewModelScope.launch {
            val address = getAddressUseCase()
            addressViewState = addressViewState.copy(
                id = address.id,
                addressLine1 = addressViewState.addressLine1.apply {
                    text.value = address.addressLine1 ?: ""
                },
                addressLine2 = addressViewState.addressLine2.apply {
                    text.value = address.addressLine2 ?: ""
                },
                phoneNumber = addressViewState.phoneNumber.apply {
                    text.value = address.phoneNumber ?: ""
                },
                zipCode = addressViewState.zipCode.apply {
                    text.value = address.zipCode ?: ""
                },
                country = addressViewState.country.apply {
                    text.value = address.country ?: ""
                },
                city = addressViewState.city.apply {
                    text.value = address.city ?: ""
                }
            )
        }
    }

    fun setEventClicks(event: AddressEvent) {
        viewModelScope.launch {
            _eventClicks.emit(event)
        }
    }
}