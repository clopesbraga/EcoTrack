package br.com.tmg.ecotrack.di.module


import br.com.tmg.ecotrack.repository.ImagesRepository
import br.com.tmg.ecotrack.viewmodel.ImagesViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val appmodule = module {

    viewModelOf (::ImagesViewModel)
    single {ImagesRepository(get())}
}

