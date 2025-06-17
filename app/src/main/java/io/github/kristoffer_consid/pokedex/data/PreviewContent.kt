package io.github.kristoffer_consid.pokedex.data

import co.pokeapi.pokekotlin.model.ApiResource
import co.pokeapi.pokekotlin.model.NamedApiResource
import co.pokeapi.pokekotlin.model.PokemonSpecies
import co.pokeapi.pokekotlin.model.PokemonSpeciesFlavorText

val PreviewSpecies = PokemonSpecies(
    id = 1,
    name = "bulbasaur",
    order = 1,
    genderRate = 1,
    captureRate = 45,
    baseHappiness = 70,
    isBaby = false,
    isLegendary = false,
    isMythical = false,
    hatchCounter = 20,
    hasGenderDifferences = false,
    formsSwitchable = false,
    growthRate = NamedApiResource(
        name = "medium-slow",
        url = "https://pokeapi.co/api/v2/growth-rate/4/"
    ),
    eggGroups = listOf(
        NamedApiResource("monster", "https://pokeapi.co/api/v2/egg-group/1/"),
        NamedApiResource("plant", "https://pokeapi.co/api/v2/egg-group/7/")
    ),
    color = NamedApiResource(
        name = "green",
        url = "https://pokeapi.co/api/v2/pokemon-color/5/"
    ),
    shape = NamedApiResource(
        name = "quadruped",
        url = "https://pokeapi.co/api/v2/pokemon-shape/8/"
    ),
    evolvesFromSpecies = null,
    evolutionChain = ApiResource(
        url = "https://pokeapi.co/api/v2/evolution-chain/1/"
    ),
    habitat = NamedApiResource(
        name = "grassland",
        url = "https://pokeapi.co/api/v2/pokemon-habitat/3/"
    ),
    generation = NamedApiResource(
        name = "generation-i",
        url = "https://pokeapi.co/api/v2/generation/1/"
    ),
    flavorTextEntries = listOf(
        PokemonSpeciesFlavorText(
            flavorText = "A strange seed was\nplanted on its\nback at birth. The plant sprouts\nand grows with\nthis POKéMON.",
            language = NamedApiResource("en", "https://pokeapi.co/api/v2/language/9/"),
            version = NamedApiResource("red", "https://pokeapi.co/api/v2/version/1/")
        ),
        PokemonSpeciesFlavorText(
            flavorText = "A strange seed was\nplanted on its\nback at birth. The plant sprouts\nand grows with\nthis POKéMON.",
            language = NamedApiResource("en", "https://pokeapi.co/api/v2/language/9/"),
            version = NamedApiResource("blue", "https://pokeapi.co/api/v2/version/2/")
        ),
        PokemonSpeciesFlavorText(
            flavorText = "It can go for days\nwithout eating a\nsingle morsel. In the bulb on\nits back, it\nstores energy.",
            language = NamedApiResource("en", "https://pokeapi.co/api/v2/language/9/"),
            version = NamedApiResource("yellow", "https://pokeapi.co/api/v2/version/3/")
        ),
    ),
    pokedexNumbers = emptyList(),
    names = emptyList(),
    palParkEncounters = emptyList(),
    formDescriptions = emptyList(),
    genera = emptyList(),
    varieties = emptyList()
)
