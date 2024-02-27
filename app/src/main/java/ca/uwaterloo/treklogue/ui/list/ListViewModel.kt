package ca.uwaterloo.treklogue.ui.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ListViewModel : ViewModel() {
    private val _selectedLandmark = MutableLiveData<Landmark>()
    val selectedLandmark: LiveData<Landmark> get() = _selectedLandmark

    fun selectLandmark(landmark: Landmark) {
        _selectedLandmark.value = landmark
    }
}
