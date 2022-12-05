package com.rasyidin.pokedexapp.ui.screen.profile

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.rasyidin.pokedexapp.R
import com.rasyidin.pokedexapp.data.response.Pokemon
import com.rasyidin.pokedexapp.data.response.Type
import com.rasyidin.pokedexapp.data.response.TypesItem
import com.rasyidin.pokedexapp.ui.component.PokemonType
import com.rasyidin.pokedexapp.ui.component.TopAppBar
import com.rasyidin.pokedexapp.ui.theme.LightGray
import com.rasyidin.pokedexapp.ui.theme.MediumGray
import com.rasyidin.pokedexapp.ui.theme.PaleBlue
import com.rasyidin.pokedexapp.util.downloadImagePokemon

@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    viewModel: ProfileViewModel = hiltViewModel(),
    navigateBack: () -> Unit,
    navigateToDetail: (pokemonName: String, dominantColor: Color) -> Unit
) {
    val favPokemon = viewModel.favListPokemon.collectAsState().value
    ProfileContent(
        favPokemon = favPokemon,
        modifier = modifier,
        navigateBack = navigateBack,
        navigateToDetail = navigateToDetail
    )
}

@Composable
fun ProfileContent(
    modifier: Modifier = Modifier,
    favPokemon: List<Pokemon>,
    navigateBack: () -> Unit,
    navigateToDetail: (pokemonName: String, dominantColor: Color) -> Unit
) {
    val showAnimEmpty by remember {
        mutableStateOf(favPokemon.isEmpty())
    }
    val composition by rememberLottieComposition(spec = LottieCompositionSpec.RawRes(R.raw.anim_empty))
    Box(
        modifier = modifier
            .fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            TopAppBar(
                title = "Profile",
                showIconFav = false,
                iconColor = Color.Black,
                textColor = Color.Black,
                onClickBack = navigateBack,
                onClickFavorite = {},
                modifier = Modifier.padding(top = 16.dp)
            )
            ProfileUser()
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Favorite",
                style = MaterialTheme.typography.headlineLarge,
                color = PaleBlue,
                modifier = Modifier.padding(horizontal = 12.dp)
            )
            AnimatedVisibility(visible = showAnimEmpty) {
                LottieAnimation(
                    composition = composition,
                    restartOnPlay = false,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            ListFavPokemon(
                favPokemon = favPokemon,
                onItemClick = navigateToDetail
            )
        }
        Image(
            painter = painterResource(id = R.drawable.ic_pokeball_black),
            contentDescription = null,
            modifier = Modifier
                .alpha(.03F)
                .size(400.dp)
                .offset(y = (-200).dp)
        )
    }
}

@Composable
fun ProfileUser(
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .padding(16.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.profile_me),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(64.dp)
                .clip(CircleShape)
        )
        Column(
            modifier = Modifier
                .padding(horizontal = 12.dp)
                .weight(1F),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Rafli Rasyidin",
                style = MaterialTheme.typography.headlineMedium
            )
            Text(
                text = "raflirasyidin20@gmail.com",
                style = MaterialTheme.typography.bodyMedium,
                color = MediumGray
            )
        }
    }
}

@Composable
fun ListFavPokemon(
    modifier: Modifier = Modifier,
    favPokemon: List<Pokemon>,
    viewModel: ProfileViewModel = hiltViewModel(),
    onItemClick: (pokemonName: String, dominantColor: Color) -> Unit
) {
    LazyColumn(modifier = modifier.padding(PaddingValues(bottom = 12.dp, end = 12.dp, start = 12.dp))) {
        items(favPokemon) { pokemon ->
            val types = mutableListOf<Type>()
            pokemon.types?.forEach {
                it?.let {
                    types.add(it.type!!)
                }
            }
            val defaultDominantColor = LightGray
            var dominantColor by remember {
                mutableStateOf(defaultDominantColor)
            }
            val painter = rememberAsyncImagePainter(
                model = ImageRequest.Builder(LocalContext.current)
                    .decoderFactory(SvgDecoder.Factory())
                    .data(downloadImagePokemon(pokemon.id!!))
                    .crossfade(true)
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
            ItemFavPokemon(
                number = pokemon.id.toString(),
                pokemonName = pokemon.name.toString(),
                pokemonTypes = types,
                dominantColor = dominantColor,
                painter = painter,
                onItemClick = onItemClick
            )
        }
    }
}

@Composable
fun ItemFavPokemon(
    modifier: Modifier = Modifier,
    number: String,
    pokemonName: String,
    pokemonTypes: List<Type>,
    dominantColor: Color,
    painter: AsyncImagePainter,
    onItemClick: (pokemonName: String, dominantColor: Color) -> Unit
) {
    Box(
        modifier = modifier
            .padding(top = 24.dp)
            .fillMaxWidth(),
        contentAlignment = Alignment.CenterEnd
    ) {
        Box(
            modifier = Modifier
                .clip(MaterialTheme.shapes.medium)
                .background(dominantColor)
                .clickable {
                    onItemClick(pokemonName, dominantColor)
                },
        ) {
            Row(modifier = Modifier.padding(12.dp)) {
                Column(
                    modifier = Modifier.weight(3F),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "#$number", style = MaterialTheme.typography.bodySmall)
                    Text(
                        text = pokemonName.capitalize(Locale.current),
                        style = MaterialTheme.typography.headlineLarge,
                        color = Color.White,
                        overflow = TextOverflow.Ellipsis
                    )
                    PokemonType(types = pokemonTypes)
                }
                Image(
                    painter = painterResource(id = R.drawable.ic_pokeball),
                    contentDescription = null,
                    modifier = Modifier
                        .weight(1F)
                        .alpha(.3F)
                )
            }
        }
        Image(
            painter = painter,
            contentDescription = null,
            modifier = Modifier
                .offset(y = (-24).dp)
                .padding(end = 20.dp)
                .size(96.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileContentPreview() {
    ProfileContent(
        favPokemon = listOf(
            Pokemon(
                name = "Bulbasaur",
                id = 1,
                types = listOf(TypesItem(type = Type("grass")))
            ),
            Pokemon(
                name = "Bulbasaur",
                id = 1,
                types = listOf(TypesItem(type = Type("grass")))
            ),
            Pokemon(
                name = "Bulbasaur",
                id = 1,
                types = listOf(TypesItem(type = Type("grass")))
            )
        ),
        navigateBack = {},
        navigateToDetail = {_, _ ->}
    )
}

@Preview(showBackground = true)
@Composable
fun ProfileUserPreview() {
    ProfileUser()
}

@Preview(showBackground = true)
@Composable
fun ItemFavPokemonPreview() {
    ItemFavPokemon(
        number = "1",
        pokemonName = "Bulbasaur",
        pokemonTypes = listOf(Type("grass"), Type("poison")),
        dominantColor = Color.Green,
        painter = rememberAsyncImagePainter(model = R.drawable.bulbasaur),
        modifier = Modifier.padding(horizontal = 12.dp),
        onItemClick = {_, _ ->}
    )
}
