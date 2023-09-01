package com.example.presenter.shopping_bag.states

import com.example.core.base.BaseViewState
import com.example.core.base.StateErrorType
import com.example.domain.models.AddressDomainModel
import com.example.presenter.models.AddressUiModel
import com.example.presenter.models.OrderItemUiModel
import com.example.presenter.models.OrderUiModel
import kotlinx.coroutines.flow.MutableSharedFlow

data class ShoppingViewState(
    override val isRefreshing: Boolean = false,
    override val isNetworkError: Boolean = false,
    override val isLoading: Boolean = false,
    override val error: String? = null,
    override val errorEvents: MutableSharedFlow<StateErrorType>? = null,
    val ordersUiModel : List<OrderUiModel> = listOf(),
    val orderItemsUiModel : List<OrderItemUiModel> = listOf(),
    val address : AddressUiModel? = null
) : BaseViewState
