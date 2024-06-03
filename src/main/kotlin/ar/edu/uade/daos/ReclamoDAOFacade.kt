package ar.edu.uade.daos

import ar.edu.uade.models.Reclamo
import ar.edu.uade.utilities.Filtro

interface ReclamoDAOFacade {
    suspend fun get10Reclamos(pagina: Int): List<Reclamo>
    suspend fun get10ReclamosBySector(pagina: Int, sector: String): List<Reclamo>
    suspend fun get10ReclamosByDocumento(pagina: Int, documento: String): List<Reclamo>
    suspend fun getReclamoById(id: Int): Reclamo?
    suspend fun addReclamo(reclamo: Reclamo)
    suspend fun getAllCantidadPaginas(): Int
    suspend fun getAllCantidadPaginasByDocumento(documento:String): Int
    suspend fun getAllCantidadPaginasBySector(sector:String): Int
}