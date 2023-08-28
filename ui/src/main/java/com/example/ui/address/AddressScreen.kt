package com.example.ui.address

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.presenter.address.AddressEvent
import com.example.presenter.address.viewmodels.AddressViewModel
import com.example.presenter.models.AddressUiModel
import com.example.ui.common.components.BackButton
import com.example.ui.common.components.CustomButton
import com.example.ui.common.components.CustomTextField
import com.example.ui.theme.spacing
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.result.ResultBackNavigator

@Destination
@Composable
fun AddressScreen(
    viewModel: AddressViewModel = hiltViewModel(),
    navigator: DestinationsNavigator? = null,
    resultNavigator: ResultBackNavigator<AddressUiModel>
) {
    val addressViewState = viewModel.addressViewState
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(MaterialTheme.spacing.medium)
        ) {
            Box(
                modifier = Modifier
                    .padding(bottom = MaterialTheme.spacing.large)
            ) {
                BackButton(
                    modifier = Modifier
                        .background(
                            color = Color.White,
                            shape = CircleShape
                        ),
                    iconTint = MaterialTheme.colorScheme.secondary,
                    elevation = 10.dp
                ) {
                    navigator?.popBackStack()
                }
            }
            CustomTextField(
                placeholder = "Address line 1",
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next,
                shape = RoundedCornerShape(10.dp),
                inputWrapper = addressViewState.addressLine1,
            )
            CustomTextField(
                placeholder = "Address line 2",
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next,
                shape = RoundedCornerShape(10.dp),
                inputWrapper = addressViewState.addressLine2,
            )
            CustomTextField(
                placeholder = "Phone number",
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next,
                shape = RoundedCornerShape(10.dp),
                inputWrapper = addressViewState.phoneNumber,
            )
            CustomTextField(
                placeholder = "Zip code",
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next,
                shape = RoundedCornerShape(10.dp),
                inputWrapper = addressViewState.zipCode,
            )
            CustomTextField(
                placeholder = "Country",
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next,
                shape = RoundedCornerShape(10.dp),
                inputWrapper = addressViewState.country,
            )
            CustomTextField(
                placeholder = "City",
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Done,
                shape = RoundedCornerShape(10.dp),
                inputWrapper = addressViewState.city,
                onDone = {
                    val savedAddress = AddressUiModel(
                        id = addressViewState.id ?: 0,
                        addressLine1 = addressViewState.addressLine1.text.value,
                        addressLine2 = addressViewState.addressLine2.text.value,
                        phoneNumber = addressViewState.phoneNumber.text.value,
                        zipCode = addressViewState.zipCode.text.value,
                        country = addressViewState.country.text.value,
                        city = addressViewState.city.text.value,
                    )
                    viewModel.setEventClicks(
                        AddressEvent.OnSaveButtonClicked
                    )
                    resultNavigator.navigateBack(savedAddress)
                }
            )
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
        ) {
            CustomButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter),
                text = "SAVE",
                shape = RectangleShape
            ) {
                val savedAddress = AddressUiModel(
                    id = addressViewState.id ?: 0,
                    addressLine1 = addressViewState.addressLine1.text.value,
                    addressLine2 = addressViewState.addressLine2.text.value,
                    phoneNumber = addressViewState.phoneNumber.text.value,
                    zipCode = addressViewState.zipCode.text.value,
                    country = addressViewState.country.text.value,
                    city = addressViewState.city.text.value,
                )
                viewModel.setEventClicks(
                    AddressEvent.OnSaveButtonClicked
                )
                resultNavigator.navigateBack(savedAddress)
            }
        }
    }
}