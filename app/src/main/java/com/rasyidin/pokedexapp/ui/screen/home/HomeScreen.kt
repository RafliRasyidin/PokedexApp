package com.rasyidin.pokedexapp.ui.screen.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import coil.size.Size
import com.rasyidin.pokedexapp.R
import com.rasyidin.pokedexapp.ui.theme.*
import kotlin.math.roundToInt

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel(),
    navigateToDetail: (pokemonName: String, dominantColor: Color) -> Unit,
    navigateToProfile: () -> Unit
) {
    val toolbarHeight = 160.dp
    val toolbarHeightPx = with(LocalDensity.current) { toolbarHeight.roundToPx().toFloat() }
    val toolbarOffsetHeightPx = remember {
        mutableStateOf(0f)
    }
    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                val delta = available.y
                val newOffset = toolbarOffsetHeightPx.value + delta
                toolbarOffsetHeightPx.value = newOffset.coerceIn(-toolbarHeightPx, 0f)
                return Offset.Zero
            }
        }
    }

    val query by viewModel.query

    BoxWithConstraints(
        modifier = modifier
            .padding(bottom = 42.dp)
            .nestedScroll(nestedScrollConnection)
    ) {
        ListPokemon(
            toolbarHeight = toolbarHeight,
            navigateToDetail = navigateToDetail,
        )
        HomeTopAppBar(
            modifier = Modifier
                .height(toolbarHeight)
                .offset { IntOffset(x = 0, y = toolbarOffsetHeightPx.value.roundToInt()) },
            searchQuery = query,
            onValueChange = { text ->
                viewModel.searchPokemon(text)
            },
            onProfileClick = navigateToProfile
        )
    }

}

@Composable
fun ListPokemon(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel(),
    toolbarHeight: Dp,
    navigateToDetail: (pokemonName: String, dominantColor: Color) -> Unit,
) {
    val pokemonList by viewModel.pokedexUiState.collectAsState()
    val endReached by remember { viewModel.endReached }
    val isSearching by remember { viewModel.isSearching }
    val isLoading by remember { viewModel.isLoading }
    val visibleState = remember {
        MutableTransitionState(false).apply {
            targetState = !isLoading
        }
    }

    AnimatedVisibility(
        visibleState = visibleState,
        enter = fadeIn(
            animationSpec = spring(dampingRatio = Spring.DampingRatioLowBouncy),
        ),
        exit = fadeOut()
    ) {
        LazyVerticalGrid(
            columns = GridCells.Adaptive(180.dp),
            modifier = modifier,
            contentPadding = PaddingValues(top = toolbarHeight)
        ) {
            val itemCount = if (pokemonList.size % 2 == 0) {
                pokemonList.size / 2
            } else pokemonList.size / 2 + 1
            itemsIndexed(pokemonList) { index, pokemon ->

                if (index >= itemCount - 1 && !endReached && !isSearching) {
                    viewModel.getPokemonList()
                }
                ItemPokemon(
                    imageUrl = pokemon.imageUrl.toString(),
                    pokemonName = pokemon.name.toString(),
                    idPokemon = pokemon.index!!,
                    modifier = Modifier,
                    onClick = navigateToDetail
                )
            }
        }
    }


}

@Composable
fun ItemPokemon(
    modifier: Modifier = Modifier,
    onClick: (pokemonName: String, dominantColor: Color) -> Unit,
    imageUrl: String,
    pokemonName: String,
    idPokemon: Int,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val defaultDominantColor = LightGray
    var dominantColor by remember {
        mutableStateOf(defaultDominantColor)
    }
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
    ) {
        Card(
            modifier = Modifier
                .padding(top = 24.dp)
                .clickable { onClick(pokemonName, dominantColor) },
            shape = RoundedCornerShape(16.dp),
        ) {
            Column(
                modifier = Modifier
                    .shadow(5.dp, RoundedCornerShape(16.dp))
                    .background(
                        Brush.linearGradient(
                            listOf(
                                defaultDominantColor,
                                dominantColor
                            ),
                            start = Offset(0f, Float.POSITIVE_INFINITY),
                            end = Offset(Float.POSITIVE_INFINITY, 0f),
                            tileMode = TileMode.Repeated
                        )
                    ),
            ) {
                Spacer(modifier = Modifier.height(64.dp))
                Row(
                    modifier = Modifier
                        .padding(horizontal = 12.dp, vertical = 12.dp)
                        .clip(MaterialTheme.shapes.large)
                        .background(color = DarkGray)
                        .padding(vertical = 4.dp, horizontal = 8.dp)
                ) {
                    Text(
                        text = "#$idPokemon",
                        style = MaterialTheme.typography.headlineSmall,
                        color = dominantColor
                    )
                    Text(
                        text = pokemonName,
                        style = MaterialTheme.typography.headlineSmall,
                        color = Color.White,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
            }
        }
        val painter = rememberAsyncImagePainter(
            model = ImageRequest.Builder(LocalContext.current)
                .decoderFactory(SvgDecoder.Factory())
                .data(imageUrl)
                .crossfade(true)
                .size(Size.ORIGINAL)
                .build(),
            onSuccess = {
                viewModel.findDominantColor(it.result.drawable) { color ->
                    dominantColor = color
                }
            }
        )
        val state = painter.state
        if (state is AsyncImagePainter.State.Loading) {
            CircularProgressIndicator(
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
            )
        }
        Image(
            painter = painter,
            contentDescription = null,
            modifier = Modifier
                .padding(top = 12.dp)
                .size(86.dp)
                .offset(y = (-30).dp),
            alignment = Alignment.TopCenter
        )
    }
}

@Composable
fun HomeTopAppBar(
    modifier: Modifier = Modifier,
    searchQuery: String,
    onValueChange: (text: String) -> Unit,
    onProfileClick: () -> Unit
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(bottomStart = 12.dp, bottomEnd = 12.dp))
            .background(color = PaleBlue)
            .padding(bottom = 12.dp, start = 16.dp, end = 12.dp, top = 24.dp)
            .wrapContentHeight()
            .fillMaxWidth(),
    ) {
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = stringResource(id = R.string.pokedex),
                    style = MaterialTheme.typography.headlineLarge,
                    modifier = Modifier.weight(1F),
                    color = Color.White
                )
                Icon(
                    imageVector = Icons.Filled.AccountCircle,
                    contentDescription = null,
                    modifier = Modifier
                        .size(36.dp)
                        .clickable { onProfileClick() },
                    tint = Color.White
                )
            }
            SearchTextField(
                value = searchQuery,
                onValueChange = onValueChange
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchTextField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (text: String) -> Unit,
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp, horizontal = 4.dp)
            .clip(MaterialTheme.shapes.medium)
            .heightIn(min = 48.dp),
        colors = TextFieldDefaults.textFieldColors(
            textColor = MediumGray,
            disabledIndicatorColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        ),
        leadingIcon = {
            Icon(
                imageVector = Icons.Filled.Search,
                contentDescription = null,
                tint = MediumGray
            )
        },
        textStyle = MaterialTheme.typography.bodyMedium,
        placeholder = {
            Text(text = "Search Pokemon", style = MaterialTheme.typography.bodyMedium)
        }
    )
}

@Preview(showBackground = true)
@Composable
fun ItemPokemonPreview() {
    Box(
        contentAlignment = Alignment.TopCenter
    ) {
        Card(
            modifier = Modifier
                .padding(top = 12.dp)
                .clickable { },
            shape = RoundedCornerShape(16.dp),
        ) {
            Column(
                modifier = Modifier
                    .shadow(5.dp, RoundedCornerShape(16.dp))
                    .background(
                        Brush.linearGradient(
                            listOf(
                                LightGray,
                                PaleBlue
                            ),
                            start = Offset(0f, Float.POSITIVE_INFINITY),
                            end = Offset(Float.POSITIVE_INFINITY, 0f),
                            tileMode = TileMode.Repeated
                        )
                    )
                    .padding(top = 12.dp, bottom = 12.dp),
            ) {
                Spacer(modifier = Modifier.height(64.dp))
                Row(
                    modifier = Modifier
                        .padding(horizontal = 12.dp, vertical = 12.dp)
                        .clip(MaterialTheme.shapes.small)
                        .background(color = DarkGray)
                        .padding(4.dp)
                ) {
                    Text(
                        text = "#1",
                        style = MaterialTheme.typography.headlineSmall,
                        color = PaleBlue
                    )
                    Text(
                        text = "Charmander",
                        style = MaterialTheme.typography.headlineSmall,
                        color = Color.White,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
            }
        }
        Image(
            painter = painterResource(id = R.drawable.charmander),
            contentDescription = null,
            modifier = Modifier
                .padding(top = 12.dp)
                .size(120.dp),
            alignment = Alignment.TopCenter
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SearchTextFieldPreview() {
    SearchTextField(onValueChange = {}, value = "Search Pokemon")
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    PokedexAppTheme {
        HomeScreen(navigateToDetail = { _, _ -> }, navigateToProfile = {})
    }
}
