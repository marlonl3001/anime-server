package br.com.mdr.di

import br.com.mdr.repository.HeroRepository
import br.com.mdr.repository.HeroRepositoryImpl
import org.koin.dsl.module

val koinModule = module {
    single<HeroRepository> { HeroRepositoryImpl() }
}