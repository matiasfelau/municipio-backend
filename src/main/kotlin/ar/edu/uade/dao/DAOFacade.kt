package ar.edu.uade.dao

import ar.edu.uade.models.Rubro

interface DAOFacade {
    suspend fun allRubros(): List<Rubro>
}
