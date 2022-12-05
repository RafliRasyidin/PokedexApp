package com.rasyidin.pokedexapp.ui.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun TopAppBar(
    modifier: Modifier = Modifier,
    title: String,
    onClickBack: () -> Unit,
    onClickFavorite: () -> Unit,
    iconColor: Color = Color.Black,
    textColor: Color = Color.Black,
    showIconFav: Boolean = false,
    favoriteState: Boolean = false
) {
    var isFavorite by remember {
        mutableStateOf(favoriteState)
    }
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Icon(
            imageVector = Icons.Filled.ArrowBack,
            contentDescription = null,
            tint = iconColor,
            modifier = Modifier.clickable { onClickBack() }
        )
        Text(
            text = title,
            style = MaterialTheme.typography.headlineLarge,
            color = textColor,
            modifier = Modifier
                .weight(1F)
        )
        AnimatedVisibility(visible = showIconFav) {
            Icon(
                imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.clickable {
                    onClickFavorite()
                    isFavorite = !favoriteState
                }
            )
        }
    }
}

@Preview(showBackground = false)
@Composable
fun TopAppBarPreview() {
    TopAppBar(title = "Bulbasaur", onClickBack = {}, iconColor = Color.Black, onClickFavorite = {})
}