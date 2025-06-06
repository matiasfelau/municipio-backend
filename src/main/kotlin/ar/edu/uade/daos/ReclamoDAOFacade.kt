package ar.edu.uade.daos

import ar.edu.uade.models.Reclamo
import ar.edu.uade.models.ReclamoImagen
import ar.edu.uade.utilities.Filtro

interface ReclamoDAOFacade {
    suspend fun get10Reclamos(pagina: Int): List<Reclamo>
    suspend fun get10ReclamosBySector(pagina: Int, sector: String): List<Reclamo>
    suspend fun get10ReclamosByDocumento(pagina: Int, documento: String): List<Reclamo>
    suspend fun getReclamoById(id: Int): Reclamo?
    suspend fun addReclamo(reclamo: Reclamo): Reclamo?
    suspend fun addImagenToReclamo(idReclamo: Int, urlImagen: String)
    suspend fun getAllCantidadPaginas(): Int
    suspend fun getAllCantidadPaginasByDocumento(documento:String): Int
    suspend fun getAllCantidadPaginasBySector(sector:String): Int
    suspend fun getFotosById(id: Int): List<ReclamoImagen>
}