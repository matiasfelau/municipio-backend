package ar.edu.uade.daos

import ar.edu.uade.models.Vecino

interface VecinoDAOFacade {
    suspend fun verifyVecino(documento: String): Boolean
    suspend fun findVecino(documento: String): Vecino?
}