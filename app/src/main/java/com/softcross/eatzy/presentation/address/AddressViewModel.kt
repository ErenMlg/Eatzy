package com.softcross.eatzy.presentation.address

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softcross.eatzy.R
import com.softcross.eatzy.common.ContextProvider
import com.softcross.eatzy.common.ResponseState
import com.softcross.eatzy.domain.model.Location
import com.softcross.eatzy.domain.repository.FirebaseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import javax.inject.Inject

@HiltViewModel
class AddressViewModel @Inject constructor(
    private val firebaseRepository: FirebaseRepository,
    savedStateHandle: SavedStateHandle,
    private val provider: ContextProvider
) : ViewModel() {

    private val _uiState = MutableStateFlow(AddressContract.UiState())
    val uiState: StateFlow<AddressContract.UiState> get() = _uiState.asStateFlow()

    private val _uiEffect by lazy { Channel<AddressContract.UiEffect>() }
    val uiEffect: Flow<AddressContract.UiEffect> by lazy { _uiEffect.receiveAsFlow() }

    init {
        getLocations()
        val selectedLocation =
            savedStateHandle.get<String>("data")?.let { Json.decodeFromString<Location>(it) }
        updateUiState {
            copy(
                selectedLocation = selectedLocation,
                title = selectedLocation?.title ?: "",
                country = selectedLocation?.country ?: "",
                city = selectedLocation?.city ?: "",
                district = selectedLocation?.district ?: "",
                openAddress = selectedLocation?.openAddress ?: ""
            )
        }
    }

    fun onAction(uiAction: AddressContract.UiAction) {
        when (uiAction) {
            is AddressContract.UiAction.OnSubmitClicked -> {
                addNewAddress()
            }

            is AddressContract.UiAction.OnTitleChanged -> updateUiState {
                copy(title = uiAction.title)
            }


            is AddressContract.UiAction.OnCountryChanged ->
                updateUiState {
                    copy(country = uiAction.country)
                }


            is AddressContract.UiAction.OnCityChanged ->
                updateUiState {
                    copy(city = uiAction.city)
                }


            is AddressContract.UiAction.OnDistrictChanged ->
                updateUiState {
                    copy(district = uiAction.district)
                }


            is AddressContract.UiAction.OnOpenAddressChanged ->
                updateUiState {
                    copy(openAddress = uiAction.openAddress)
                }

            is AddressContract.UiAction.OnClearFields -> clearFields()
        }
    }

    private fun getLocations() = viewModelScope.launch {
        when (val result = firebaseRepository.getLocations()) {
            is ResponseState.Success -> {
                updateUiState {
                    copy(
                        locationList = result.result,
                    )
                }
            }

            is ResponseState.Error -> {
                updateUiState {
                    copy(
                        errorMessage = provider.context.getString(R.string.error_on_fetching_locations)
                    )
                }
            }
        }
    }

    private fun clearFields() {
        updateUiState {
            copy(
                title = "",
                country = "",
                city = "",
                district = "",
                openAddress = ""
            )
        }
    }

    private fun addNewAddress() = viewModelScope.launch {
        if (uiState.value.selectedLocation == null) {
            when (val response = firebaseRepository.addLocation(
                Location(
                    id = "",
                    title = uiState.value.title,
                    country = uiState.value.country,
                    city = uiState.value.city,
                    district = uiState.value.district,
                    openAddress = uiState.value.openAddress
                )
            )) {
                is ResponseState.Success -> {
                    emitUiEffect(AddressContract.UiEffect.OnNavigateProfile)
                }

                is ResponseState.Error -> {
                    updateUiState {
                        copy(
                            errorMessage = response.exception.message ?: provider.context.getString(R.string.an_unknown_error_occurred)
                        )
                    }
                }
            }
        } else {
            when (val response = firebaseRepository.updateLocation(
                uiState.value.selectedLocation!!.id,
                uiState.value.selectedLocation!!.copy(
                    title = uiState.value.title,
                    country = uiState.value.country,
                    city = uiState.value.city,
                    district = uiState.value.district,
                    openAddress = uiState.value.openAddress
                )
            )) {
                is ResponseState.Success -> {
                    emitUiEffect(AddressContract.UiEffect.OnNavigateProfile)
                }

                is ResponseState.Error -> {
                    updateUiState {
                        copy(
                            errorMessage = response.exception.message ?: provider.context.getString(R.string.an_unknown_error_occurred)
                        )
                    }
                }
            }
        }
    }

    private fun updateUiState(block: AddressContract.UiState.() -> AddressContract.UiState) {
        _uiState.update(block)
    }

    private suspend fun emitUiEffect(uiEffect: AddressContract.UiEffect) {
        _uiEffect.send(uiEffect)
    }

}