package com.personal.requestmobility.organizaciones

import com.personal.requestmobility.organizaciones.data.ds.remote.OrganizacionesRemotoDS
import com.personal.requestmobility.organizaciones.data.ds.remote.servicio.OrganizacionesApiRemoto
import com.personal.requestmobility.organizaciones.data.repositorio.OrganizacionesRepoImp
import com.personal.requestmobility.organizaciones.domain.interactors.ObtenerOrganizacionesCU
import com.personal.requestmobility.organizaciones.domain.repositorio.IRepoOrganizaciones
import org.koin.dsl.module
import retrofit2.Retrofit

val moduloOrganizaciones = module {


    //Retrofit
    single<OrganizacionesApiRemoto> { RetrofitServicioOrganizaciones(get<Retrofit>()) }
    single<OrganizacionesRemotoDS> { OrganizacionesRemotoDS(get<OrganizacionesApiRemoto>()) }

    //Repo
    single<OrganizacionesRepoImp> {
        OrganizacionesRepoImp(
            listOf(
                get<OrganizacionesRemotoDS>()
            )
        )
    }

    //CU
    single<ObtenerOrganizacionesCU> { ObtenerOrganizacionesCU(get<IRepoOrganizaciones>()) }

}


fun RetrofitServicioOrganizaciones(retrofit: Retrofit): OrganizacionesApiRemoto {
    return retrofit.create(OrganizacionesApiRemoto::class.java)
}