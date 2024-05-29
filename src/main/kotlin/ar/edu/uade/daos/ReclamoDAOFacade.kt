package ar.edu.uade.daos

import ar.edu.uade.models.Reclamo
import kotlinx.html.I

interface ReclamoDAOFacade {
    suspend fun getAllReclamosBy10(pagina: Int): List<Reclamo>

    suspend fun get10ReclamosBySector(pagina: Int, sector: String): List<Reclamo>

    suspend fun get10ReclamosByPertenencia(pagina: Int, propio: Boolean, documento: String): List<Reclamo>

    suspend fun getReclamoById(id: Int): Reclamo?
}