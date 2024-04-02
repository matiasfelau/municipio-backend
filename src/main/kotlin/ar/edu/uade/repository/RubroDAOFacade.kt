package ar.edu.uade.repository

import ar.edu.uade.model.Rubro

interface RubroDAOFacade {
    suspend fun allRubros(): List<Rubro>
}
