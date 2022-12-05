package com.rasyidin.pokedexapp.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rasyidin.pokedexapp.data.response.Type
import com.rasyidin.pokedexapp.util.parseTypeColor

@Composable
fun PokemonType(
    modifier: Modifier = Modifier,
    types: List<Type>
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        types.forEach { type ->
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(color = parseTypeColor(type)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = type.name.toString(),
                    style = MaterialTheme.typography.headlineSmall,
                    color = Color.White,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }
        }
    }
}

@Preview
@Composable
fun PokemonTypePreview() {
    PokemonType(types = listOf(Type("Fire"), Type("Poison")))
}