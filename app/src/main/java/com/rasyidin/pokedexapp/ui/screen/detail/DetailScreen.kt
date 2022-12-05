package com.rasyidin.pokedexapp.ui.screen.detail

import android.util.Log
import androidx.annotation.DrawableRes
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import com.rasyidin.pokedexapp.R
import com.rasyidin.pokedexapp.data.response.*
import com.rasyidin.pokedexapp.ui.component.PokemonType
import com.rasyidin.pokedexapp.ui.component.TopAppBar
import com.rasyidin.pokedexapp.ui.theme.LightGray
import com.rasyidin.pokedexapp.ui.theme.MediumGray
import com.rasyidin.pokedexapp.ui.theme.PokedexAppTheme
import com.rasyidin.pokedexapp.util.ResultState
import com.rasyidin.pokedexapp.util.downloadImagePokemon
import com.rasyidin.pokedexapp.util.parseStat

@Composable
fun DetailScreen(
    modifier: Modifier = Modifier,
    pokemonName: String,
    dominantColor: Color,
    viewModel: DetailViewModel = hiltViewModel(),
    navigateBack: () -> Unit
) {
    viewModel.pokemon.collectAsState(initial = ResultState.Idle()).value.let { resultState ->
        when (resultState) {
            is ResultState.Error -> {
                val message = resultState.message
                Log.d("Pokemon", "Error: $message")
            }
            is ResultState.Idle -> {
                viewModel.getDetailPokemon(pokemonName.lowercase())
                Log.d("Pokemon", "Idle")
            }
            is ResultState.Loading -> {
                Log.d("Pokemon", "Loading")
            }
            is ResultState.Success -> {
                val pokemon = resultState.data
                val isFavorite = viewModel.isFavorite.value
                pokemon?.let {
                    val painter = rememberAsyncImagePainter(
                        model = ImageRequest.Builder(LocalContext.current)
                            .decoderFactory(SvgDecoder.Factory())
                            .data(downloadImagePokemon(pokemon.id!!))
                            .crossfade(true)
                            .build(),
                    )
                    DetailContent(
                        dominantColor = dominantColor,
                        pokemon = pokemon,
                        navigateBack = navigateBack,
                        modifier = modifier
                            .padding(bottom = 42.dp)
                            .verticalScroll(rememberScrollState()),
                        painter = painter,
                        isFavorite = isFavorite,
                        onClickFavorite = {
                            if (isFavorite) {
                                viewModel.removeFavPokemon(pokemon)
                            } else {
                                viewModel.addFavPokemon(pokemon)
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun DetailContent(
    modifier: Modifier = Modifier,
    dominantColor: Color,
    pokemon: Pokemon,
    painter: AsyncImagePainter,
    navigateBack: () -> Unit,
    onClickFavorite: () -> Unit,
    isFavorite: Boolean
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(dominantColor),
        contentAlignment = Alignment.TopEnd
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_pokeball),
            contentDescription = null,
            modifier = Modifier
                .alpha(.3F)
                .padding(top = 12.dp)
        )
        Column() {
            Spacer(modifier = Modifier.height(16.dp))
            TopAppBar(
                title = pokemon.name?.capitalize(Locale.current).toString(),
                favoriteState = isFavorite,
                onClickBack = navigateBack,
                iconColor = Color.White,
                textColor = Color.White,
                onClickFavorite = onClickFavorite,
                showIconFav = true
            )
            Box(
                modifier = Modifier.padding(top = 48.dp),
                contentAlignment = Alignment.TopCenter
            ) {
                Box(
                    modifier = Modifier
                        .padding(top = 140.dp)
                        .fillMaxSize()
                        .clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp))
                        .background(color = MaterialTheme.colorScheme.onPrimary),
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 20.dp)
                    ) {
                        Spacer(modifier = Modifier.height(80.dp))
                        val types = mutableListOf<Type>()
                        pokemon.types?.forEach {
                            it?.let {
                                types.add(it.type!!)
                            }
                        }
                        PokemonType(types = types)
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "About",
                            style = MaterialTheme.typography.headlineLarge,
                            color = dominantColor
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        AboutSection(
                            height = pokemon.height.toString(),
                            weight = pokemon.weight.toString()
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Base Stats",
                            style = MaterialTheme.typography.headlineLarge,
                            color = dominantColor
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        PokemonBaseStats(
                            baseStats = pokemon.stats ?: emptyList(),
                            dominantColor = dominantColor
                        )
                    }
                }
                Image(
                    painter = painter,
                    contentDescription = null,
                    modifier = Modifier
                        .size(200.dp)
                )
            }
        }
    }
}


@Composable
fun AboutSection(
    modifier: Modifier = Modifier,
    height: String,
    weight: String
) {
    Row(
        modifier = modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        ItemAbout(
            imageRes = R.drawable.ic_weight,
            value = "$weight Kg",
            valueType = "Weight"
        )
        Spacer(
            modifier = Modifier
                .width(2.dp)
                .height(80.dp)
                .background(LightGray)
        )
        ItemAbout(
            imageRes = R.drawable.ic_height,
            value = "$height m",
            valueType = "Height"
        )
    }
}

@Composable
fun ItemAbout(
    modifier: Modifier = Modifier,
    @DrawableRes imageRes: Int,
    value: String,
    valueType: String,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = imageRes),
                contentDescription = null,
                modifier = Modifier.size(36.dp)
            )
            Text(
                text = value,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Black
            )
        }
        Spacer(modifier = Modifier.height(10.dp))
        Text(text = valueType, style = MaterialTheme.typography.bodyMedium, color = MediumGray)
    }
}

@Composable
fun PokemonBaseStats(
    modifier: Modifier = Modifier,
    baseStats: List<StatsItem>,
    dominantColor: Color
) {
    Column(modifier = modifier) {
        val maxStat = baseStats.maxByOrNull { statsItem ->
            statsItem.baseStat ?: 0
        }
        baseStats.forEach { stat ->
            BaseStat(
                dominantColor = dominantColor,
                statName = parseStat(stat.stat!!),
                statValue = stat.baseStat ?: 0,
                maxBaseStat = maxStat?.baseStat ?: 100
            )
        }
    }
}

@Composable
fun BaseStat(
    modifier: Modifier = Modifier,
    dominantColor: Color,
    statName: String,
    statValue: Int,
    animDuration: Int = 750,
    maxBaseStat: Int
) {
    var animationPlayed by remember {
        mutableStateOf(false)
    }
    val maxStat = if (maxBaseStat < 100) 100 else maxBaseStat
    val statWithAnimation by animateFloatAsState(
        targetValue = if (animationPlayed) {
            statValue / maxStat.toFloat()
        } else 0F,
        animationSpec = tween(
            durationMillis = animDuration,
            easing = FastOutSlowInEasing
        )
    )
    LaunchedEffect(true) {
        animationPlayed = true
    }
    Row(
        modifier = modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = statName,
            style = MaterialTheme.typography.headlineMedium,
            color = dominantColor,
            modifier = Modifier.weight(1F)
        )
        Text(
            text = statValue.toString(),
            style = MaterialTheme.typography.bodyLarge,
            color = Color.Black
        )
        Box(
            modifier = Modifier
                .height(16.dp)
                .fillMaxWidth()
                .clip(CircleShape)
                .background(color = LightGray)
                .weight(6F)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(statWithAnimation)
                    .background(dominantColor)
                    .clip(CircleShape)
            )
        }
    }
}

@Preview
@Composable
fun DetailContentPreview() {
    PokedexAppTheme {
        DetailContent(
            dominantColor = Color.Green,
            pokemon = Pokemon(
                types = listOf(TypesItem(1, Type("grass")), TypesItem(1, Type("poison"))),
                baseExperience = 64,
                weight = 85,
                height = 6,
                sprites = Sprites(other = Other(dreamWorld = DreamWorld("", R.drawable.bulbasaur))),
                stats = listOf(
                    StatsItem(Stat("hp"), 45),
                    StatsItem(Stat("attack"), 70),
                    StatsItem(Stat("defense"), 69),
                    StatsItem(Stat("special-attack"), 67),
                    StatsItem(Stat("special-defense"), 86),
                    StatsItem(Stat("speed"), 45),
                ),
                name = "Bulbasaur",
                id = 1
            ),
            painter = rememberAsyncImagePainter(model = R.drawable.bulbasaur),
            navigateBack = {},
            isFavorite = false,
            onClickFavorite = {}
        )
    }
}

@Preview
@Composable
fun AboutSectionPreview() {
    AboutSection(height = "6.9", weight = "0,7")
}

@Preview(showBackground = true)
@Composable
fun ItemAboutPreview() {
    ItemAbout(
        imageRes = R.drawable.ic_weight,
        value = "6.4 Kg",
        valueType = "Weight"
    )
}

@Preview
@Composable
fun PokemonBaseStatsPreview() {
    PokemonBaseStats(
        baseStats = listOf(
            StatsItem(
                stat = Stat("hp"),
                baseStat = 45
            ),
            StatsItem(
                stat = Stat("attack"),
                baseStat = 49
            ),
            StatsItem(
                stat = Stat("defense"),
                baseStat = 49
            ),
            StatsItem(
                stat = Stat("special-attack"),
                baseStat = 65
            ),
            StatsItem(
                stat = Stat("special-defense"),
                baseStat = 65
            ),
            StatsItem(
                stat = Stat("speed"),
                baseStat = 45
            ),
        ),
        dominantColor = Color.Green
    )
}

@Preview
@Composable
fun BaseStatPreview() {
    BaseStat(dominantColor = Color.Green, statName = "HP", statValue = 45, maxBaseStat = 100)
}
