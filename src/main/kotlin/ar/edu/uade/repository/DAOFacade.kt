package ar.edu.uade.repository

import ar.edu.uade.model.Rubro

interface DAOFacade {
    suspend fun allRubros(): List<Rubro>
}
