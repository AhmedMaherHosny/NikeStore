package com.example.domain.usecases.get_products_grouped_by_category_and_sex

import com.example.core.enums.ProductCategory
import com.example.core.enums.Sex
import com.example.domain.models.ProductItemDomainModel
import com.example.domain.repository.remote.FirebaseRepository
import javax.inject.Inject

class GetProductsGroupedByCategoryAndSexUseCase @Inject constructor(
    private val repository: FirebaseRepository
) {
    suspend operator fun invoke(sex: Sex): Map<Pair<ProductCategory, Any>, List<ProductItemDomainModel>> =
        repository.getProducts().groupBy { Pair(it.productCategory, it.subCategory) }
            .mapValues { (_, products) ->
                products.filter { it.sex == sex }
            }
}
