package ar.edu.uade.daos

import ar.edu.uade.models.Reclamo

interface ReclamoDAOFacade {
    suspend fun get10Reclamos(pagina: Int): List<Reclamo>
    suspend fun get10ReclamosBySector(pagina: Int, sector: String): List<Reclamo>
    suspend fun get10ReclamosByDocumento(pagina: Int, documento: String): List<Reclamo>
    suspend fun getReclamoById(id: Int): Reclamo?
}