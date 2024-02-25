package ca.uwaterloo.treklogue.ui.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * Composable for the list view that expects [onNextButtonClicked] lambda that expects
 * the selected quantity to save and triggers the navigation to next screen
 */
@Composable
fun ListScreen(
    onNextButtonClicked: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text("you are list")

            Button(
                onClick = { onNextButtonClicked(0) },
                modifier = modifier.widthIn(min = 250.dp)
            ) {
                Text("go to profile")
            }
        }
    }
}