package com.darioossa.poketest.di

import androidx.room.Room
import com.darioossa.poketest.BuildConfig
import com.darioossa.poketest.data.local.CryptoManager
import com.darioossa.poketest.data.local.FavoritesDataStore
import com.darioossa.poketest.data.local.PokeLocalDataSource
import com.darioossa.poketest.data.local.PokeLocalDataSourceImpl
import com.darioossa.poketest.data.local.PokemonDatabase
import com.darioossa.poketest.data.auth.AuthRepository
import com.darioossa.poketest.data.auth.AuthRepositoryImpl
import com.darioossa.poketest.data.auth.crypto.AuthCryptoManager
import com.darioossa.poketest.data.auth.local.AuthDataStore
import com.darioossa.poketest.data.auth.local.AuthLocalDataSource
import com.darioossa.poketest.data.auth.local.AuthLocalDataSourceImpl
import com.darioossa.poketest.data.auth.remote.AuthRemoteDataSource
import com.darioossa.poketest.data.auth.remote.AuthRemoteDataSourceImpl
import com.darioossa.poketest.data.auth.remote.GoogleAuthProvider
import com.darioossa.poketest.data.remote.PokeApiService
import com.darioossa.poketest.data.remote.PokeRemoteDataSource
import com.darioossa.poketest.data.remote.PokeRemoteDataSourceImpl
import com.darioossa.poketest.data.repository.PokemonRepository
import com.darioossa.poketest.data.repository.PokemonRepositoryImpl
import com.darioossa.poketest.domain.usecase.GetPokemonDetailUseCase
import com.darioossa.poketest.domain.usecase.GetPokemonListUseCase
import com.darioossa.poketest.domain.usecase.GetPokemonTypesUseCase
import com.darioossa.poketest.domain.usecase.LoginWithBiometricUseCase
import com.darioossa.poketest.domain.usecase.LoginWithGoogleUseCase
import com.darioossa.poketest.domain.usecase.LoginWithPasswordUseCase
import com.darioossa.poketest.domain.usecase.ObserveFavoritesUseCase
import com.darioossa.poketest.domain.usecase.ToggleFavoriteUseCase
import com.darioossa.poketest.ui.auth.LoginReducer
import com.darioossa.poketest.ui.auth.LoginViewModel
import com.darioossa.poketest.ui.pokedex.PokedexListReducer
import com.darioossa.poketest.ui.pokedex.PokedexListViewModel
import com.darioossa.poketest.ui.pokeDetail.PokemonDetailReducer
import com.darioossa.poketest.ui.pokeDetail.PokemonDetailViewModel
import com.squareup.moshi.Moshi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import com.darioossa.poketest.util.biometric.BiometricPromptManager
import org.koin.core.module.dsl.viewModel

private const val POKE_API_BASE_URL = "https://pokeapi.co/api/v2/"

val dataModule = module {
    single {
        HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.HEADERS
                else HttpLoggingInterceptor.Level.NONE
        }
    }
    single {
        OkHttpClient.Builder()
            .addInterceptor(get<HttpLoggingInterceptor>())
            .build()
    }
    single { Moshi.Builder().build() }
    single {
        Retrofit.Builder()
            .baseUrl(POKE_API_BASE_URL)
            .client(get())
            .addConverterFactory(MoshiConverterFactory.create(get()))
            .build()
    }
    single { get<Retrofit>().create(PokeApiService::class.java) }

    single {
        Room.databaseBuilder(
            androidContext(),
            PokemonDatabase::class.java,
            "pokemon.db"
        ).build()
    }
    single { get<PokemonDatabase>().pokemonDao() }
    single { CryptoManager() }
    single { FavoritesDataStore(androidContext(), get()) }
    single { AuthCryptoManager() }
    single { AuthDataStore(androidContext(), get(), get()) }
    single { BiometricPromptManager() }
    single { GoogleAuthProvider() }
    single<AuthLocalDataSource> { AuthLocalDataSourceImpl(get()) }
    single<AuthRemoteDataSource> { AuthRemoteDataSourceImpl(get(), get()) }
    single<AuthRepository> { AuthRepositoryImpl(get(), get()) }
    single<PokeLocalDataSource> { PokeLocalDataSourceImpl(get()) }
    single<PokeRemoteDataSource> { PokeRemoteDataSourceImpl(get()) }
    single<PokemonRepository> { PokemonRepositoryImpl(get(), get(), get()) }
}

val domainModule = module {
    factory { GetPokemonListUseCase(get()) }
    factory { GetPokemonDetailUseCase(get()) }
    factory { GetPokemonTypesUseCase(get()) }
    factory { ToggleFavoriteUseCase(get()) }
    factory { ObserveFavoritesUseCase(get()) }
    factory { LoginWithBiometricUseCase(get()) }
    factory { LoginWithGoogleUseCase(get()) }
    factory { LoginWithPasswordUseCase(get()) }
}

val uiModule = module {
    factory { LoginReducer() }
    factory { PokedexListReducer() }
    factory { PokemonDetailReducer() }
    viewModel {
        LoginViewModel(
            loginWithPasswordUseCase = get(),
            loginWithBiometricUseCase = get(),
            loginWithGoogleUseCase = get(),
            reducer = get()
        )
    }
    viewModel {
        PokedexListViewModel(
            getPokemonListUseCase = get(),
            getPokemonTypesUseCase = get(),
            toggleFavoriteUseCase = get(),
            observeFavoritesUseCase = get(),
            reducer = get()
        )
    }
    viewModel {
        PokemonDetailViewModel(
            getPokemonDetailUseCase = get(),
            reducer = get()
        )
    }
}

val appModules = listOf(dataModule, domainModule, uiModule)
