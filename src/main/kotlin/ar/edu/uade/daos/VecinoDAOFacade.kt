package ar.edu.uade.daos

interface VecinoDAOFacade {
    suspend fun verifyVecino(documento: String): Boolean
}