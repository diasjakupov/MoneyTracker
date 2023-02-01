package com.example.myapplication.ui.elements

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import com.example.myapplication.data.models.Type

@Composable
fun SelectionList(textValue: String,
                  listOfValues: List<Type>,
                  modifier: Modifier,
                  onValueChange: (text: Type) -> Unit) {
    LazyColumn(
        modifier = modifier
            .shadow(10.dp, shape = RoundedCornerShape(12.dp))
            .background(Color.White, RoundedCornerShape(12.dp))
            .padding(top = 4.dp)
    ) {

        items(listOfValues) { text ->
            Column(modifier = Modifier.clickable {
                onValueChange(text)
            }) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(end = 12.dp)
                ) {
                    Text(
                        text.name, modifier = Modifier.padding(8.dp),
                        fontFamily = FontFamily.Default
                    )
                    if (text.name == textValue) {
                        Icon(
                            imageVector = Icons.Default.Done,
                            contentDescription = "selected icon"
                        )
                    }
                }
            }
            Divider()

        }
    }
}