package ca.uwaterloo.treklogue.ui.list

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun LandmarkDetail(landmark: Landmark) {

    Column(
        modifier = Modifier
            .size(100.dp)
    ) {
        Text("test")
        Text(landmark.name)
    }
}