package com.personal.requestmobility.kpi

import com.personal.requestmobility.core.room.AppDatabase
import com.personal.requestmobility.kpi.data.ds.local.KpisRoomDS
import com.personal.requestmobility.kpi.data.ds.local.dao.KpisDao
import com.personal.requestmobility.kpi.data.repositorios.KpisRepositorioImp
import com.personal.requestmobility.kpi.domain.interactors.EliminarKpiCU
import com.personal.requestmobility.kpi.domain.interactors.EliminarTodosKpisCU
import com.personal.requestmobility.kpi.domain.interactors.GuardarKpiCU
import com.personal.requestmobility.kpi.domain.interactors.ObtenerKpiCU
import com.personal.requestmobility.kpi.domain.interactors.ObtenerKpisCU
import com.personal.requestmobility.kpi.domain.repositorios.KpisRepositorio
import com.personal.requestmobility.kpi.ui.screen.detalle.DetalleKpiVM
import com.personal.requestmobility.kpi.ui.screen.listado.KpisListadoVM
import com.personal.requestmobility.paneles.domain.interactors.GuardarPanelCU
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val moduloKpis = module {



    // Database
    single<KpisDao> { get<AppDatabase>().kpisDao() }
    single<KpisRoomDS>{ KpisRoomDS(get<KpisDao>())}


    //Repositiorio
    single<KpisRepositorio>{
        KpisRepositorioImp(listOf(
            get<KpisRoomDS>(),
           ))
    }

    //Casos de uso
    single<EliminarKpiCU>{ EliminarKpiCU(get<KpisRepositorio>()) }
    single<EliminarTodosKpisCU>{ EliminarTodosKpisCU(get<KpisRepositorio>()) }
    single<GuardarKpiCU>{ GuardarKpiCU(get<KpisRepositorio>()) }
    single<ObtenerKpiCU>{ ObtenerKpiCU(get<KpisRepositorio>()) }
    single<ObtenerKpisCU>{ ObtenerKpisCU(get<KpisRepositorio>()) }

    //ViewMOdel
    viewModel { KpisListadoVM(get<ObtenerKpisCU>()) }
    viewModel { DetalleKpiVM(
        get<ObtenerKpiCU>(),
        get<GuardarKpiCU>(),
        get<EliminarKpiCU>(),
        get<GuardarPanelCU>()

    ) }


}
