package com.personal.metricas.organizaciones

import com.personal.metricas.organizaciones.data.ds.remote.OrganizacionesRemotoDS
import com.personal.metricas.organizaciones.data.ds.remote.servicio.OrganizacionesApiRemoto
import com.personal.metricas.organizaciones.data.repositorio.OrganizacionesRepoImp
import com.personal.metricas.organizaciones.domain.interactors.ObtenerOrganizacionesCU
import com.personal.metricas.organizaciones.domain.repositorio.IRepoOrganizaciones
import org.koin.dsl.module
import retrofit2.Retrofit

val moduloOrganizaciones = module {


    //Retrofit
    single<OrganizacionesApiRemoto> { RetrofitServicioOrganizaciones(get<Retrofit>()) }
    single<OrganizacionesRemotoDS> { OrganizacionesRemotoDS(get<OrganizacionesApiRemoto>()) }

    //Repo
    single<IRepoOrganizaciones> {
        OrganizacionesRepoImp(
            listOf(
                get<OrganizacionesRemotoDS>()
            )
        )
    }


    //single<IRepoOrganizaciones> { OrganizacionesRepoImp() }
    //CU
    single<ObtenerOrganizacionesCU> {
        ObtenerOrganizacionesCU(get<IRepoOrganizaciones>())

    }

}


fun RetrofitServicioOrganizaciones(retrofit: Retrofit): OrganizacionesApiRemoto {
    return retrofit.create(OrganizacionesApiRemoto::class.java)
}