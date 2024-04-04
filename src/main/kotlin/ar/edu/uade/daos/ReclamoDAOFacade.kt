package ar.edu.uade.daos
import ar.edu.uade.models.Reclamo

interface ReclamoDAOFacade {
    suspend fun findReclamoById(idReclamo: Int): Reclamo?
}