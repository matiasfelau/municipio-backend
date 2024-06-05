package ar.edu.uade.daos

import ar.edu.uade.models.Desperfecto

interface DesperfectoDAOFacade {
    suspend fun getAllDesperfectos(): List<Desperfecto>
}