package ar.edu.uade.daos

import ar.edu.uade.models.Desperfecto
import ar.edu.uade.models.Rubro

interface RubroDAOFacade {
    suspend fun getAllRubros(): List<Rubro>
}