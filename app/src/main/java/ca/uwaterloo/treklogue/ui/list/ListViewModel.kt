package ca.uwaterloo.treklogue.ui.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ca.uwaterloo.treklogue.R
import ca.uwaterloo.treklogue.data.mockModel.MockLandmark

class ListViewModel : ViewModel() {
    private val _selectedLandmark = MutableLiveData<MockLandmark>()
    val selectedLandmark: LiveData<MockLandmark> get() = _selectedLandmark

    fun selectLandmark(landmark: MockLandmark) {
        _selectedLandmark.value = landmark
    }

    // Mock: List of landmarks
    val landmarks = listOf(
        MockLandmark(
            "Eiffel Tower-1",
            "15th March 2023",
            "The Eiffel Tower (/ˈaɪfəl/ EYE-fəl; French: Tour Eiffel [tuʁ ɛfɛl] ⓘ) is a wrought-iron lattice tower on the Champ de Mars in Paris, France. It is named after the engineer Gustave Eiffel, whose company designed and built the tower from 1887 to 1889. ",
            R.drawable.img_eiffel_tower
        ),
        MockLandmark(
            "Eiffel Tower-2",
            "30th August 2023",
            "The Eiffel Tower (/ˈaɪfəl/ EYE-fəl; French: Tour Eiffel [tuʁ ɛfɛl] ⓘ) is a wrought-iron lattice tower on the Champ de Mars in Paris, France. It is named after the engineer Gustave Eiffel, whose company designed and built the tower from 1887 to 1889. ",
            R.drawable.img_eiffel_tower
        ),
        MockLandmark(
            "Eiffel Tower-3",
            "1st January 2024",
            "The Eiffel Tower (/ˈaɪfəl/ EYE-fəl; French: Tour Eiffel [tuʁ ɛfɛl] ⓘ) is a wrought-iron lattice tower on the Champ de Mars in Paris, France. It is named after the engineer Gustave Eiffel, whose company designed and built the tower from 1887 to 1889. ",
            R.drawable.img_eiffel_tower
        )
    )
}
