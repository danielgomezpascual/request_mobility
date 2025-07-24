package com.personal.metricas.notas.data.ds.local


import com.personal.metricas.core.data.ds.TIPO_DS
import com.personal.metricas.notas.data.ds.IDataSourceNotas
import com.personal.metricas.notas.data.ds.local.dao.NotasDao
import com.personal.metricas.notas.data.ds.local.entidades.NotasRoom
import com.personal.metricas.notas.data.ds.local.entidades.fromNotas
import com.personal.metricas.notas.data.ds.local.entidades.toNotas
import com.personal.metricas.notas.domain.entidades.Notas

class NotasLocalDS(private val dao: NotasDao) : IDataSourceNotas {
	override val tipo: TIPO_DS
		get() = TIPO_DS.ROOM

	override suspend fun getAll(): List<Notas> {
		return dao.todasNotas().map { it.toNotas() }
	}

	override suspend fun eliminar(notas: Notas) {
		dao.delete(NotasRoom().fromNotas(notas))
	}

	override suspend fun eliminarTodos() {
		dao.vaciarTabla()
	}

	override suspend fun guardar(notas: Notas): Long {
		return dao.insert(NotasRoom().fromNotas(notas))
	}

	override suspend fun getPorID(identificador: String): Notas {
		return (dao.getPorID(identificador) ?: NotasRoom()).toNotas()
	}
}