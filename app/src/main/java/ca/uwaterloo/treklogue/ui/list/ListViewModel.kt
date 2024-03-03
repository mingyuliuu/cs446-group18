package ca.uwaterloo.treklogue.ui.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ca.uwaterloo.treklogue.data.mockModel.MockLandmark

class ListViewModel : ViewModel() {
    private val _selectedLandmark = MutableLiveData<MockLandmark>()
    val selectedLandmark: LiveData<MockLandmark> get() = _selectedLandmark

    fun selectLandmark(landmark: MockLandmark) {
        _selectedLandmark.value = landmark
    }
}
