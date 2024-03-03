package ca.uwaterloo.treklogue.ui.composables

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun JournalEntryUI(
    onDetailClicked: (Any?) -> Unit,
) {
    var sliderPosition by remember { mutableFloatStateOf(0f) }
    val stroke = Stroke(width = 4f,
        pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 3f)
    )
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Spacer(modifier = Modifier.height(5.dp))
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Place:",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = "Eiffel Tower",
                textAlign = TextAlign.Center,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier
                    .background(color = Color.LightGray)
                    .weight(3f)
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Date:",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = "10th August 2024",
                textAlign = TextAlign.Center,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier
                    .background(color = Color.LightGray)
                    .weight(3f)
            )
        }
        Spacer(modifier = Modifier.height(25.dp))
        Text(
            text = "Journal:",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier.align(Alignment.Start)
        )
        TextField(
            value = "",
            onValueChange = {},
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.LightGray,
                unfocusedContainerColor = Color.LightGray,
                disabledContainerColor = Color.Transparent,
            ),
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .fillMaxWidth()
                .background(color = Color.LightGray, shape = RoundedCornerShape(size = 40.dp)),
            minLines = 5,
            label = { Text(text = "Enter journal here:") }
        )
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = "Upload your photos/videos here",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
        Button(
            onClick = { /*TODO*/ },
            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .drawBehind {
                    drawRoundRect(color = Color.LightGray, style = stroke)
                },
        ) {
            Text(
                text = "Upload your photos/videos here",
                color = Color.LightGray
            )
        }
        Spacer(modifier = Modifier.height(30.dp))
        Text(
            text = "How do you feel about this place?",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
        Slider(
            value = sliderPosition,
            onValueChange = { sliderPosition = it },
            colors = SliderDefaults.colors(
                thumbColor = Color.DarkGray,
                activeTrackColor = Color.Gray,
                inactiveTrackColor = Color.LightGray,
            ),
            steps = 1,
            valueRange = 0f..100f
        )
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Bad",
                textAlign = TextAlign.Start,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = "Average",
                textAlign = TextAlign.Center,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = "Love it!",
                textAlign = TextAlign.End,
                modifier = Modifier.weight(1f)
            )
        }
        Spacer(modifier = Modifier.height(20.dp))
        Button(
            onClick = { onDetailClicked("") },
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .height(40.dp)
        ) {
            Text(
                text = "Back"
            )
        }
    }
}