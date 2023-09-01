package com.example.presenter.saved_items.states

import com.example.core.base.BaseViewState
import com.example.core.base.StateErrorType
import com.example.presenter.models.ProductItemUiModel
import kotlinx.coroutines.flow.MutableSharedFlow

data class SavedItemsViewState(
    override val isRefreshing: Boolean = false,
    override val isNetworkError: Boolean = false,
    override val isLoading: Boolean = false,
    override val error: String? = null,
    override val errorEvents: MutableSharedFlow<StateErrorType>? = null,
    val productsItemUiModel: List<ProductItemUiModel> = listOf()
) : BaseViewState
