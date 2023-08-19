package com.example.presenter.home.states

import com.example.core.base.BaseViewState
import com.example.core.base.StateErrorType
import com.example.core.enums.ProductCategory
import com.example.presenter.models.ProductItemUiModel
import kotlinx.coroutines.flow.MutableSharedFlow

data class HomeViewState(
    override val isRefreshing: Boolean = false,
    override val isNetworkError: Boolean = false,
    override val isLoading: Boolean = false,
    override val error: String? = null,
    override val errorEvents: MutableSharedFlow<StateErrorType>? = null,
    val productsGroupedByCategory: Map<Pair<ProductCategory, Any>, List<ProductItemUiModel>> = mapOf()
) : BaseViewState
