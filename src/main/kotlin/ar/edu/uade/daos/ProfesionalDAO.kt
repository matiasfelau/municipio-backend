package ar.edu.uade.daos

import ar.edu.uade.models.Profesional
import ar.edu.uade.models.Reclamo

interface ProfesionalDAO {
    suspend fun getCantidadPaginas(): Int
    suspend fun get10Profesionales(pagina: Int): List<Profesional>
}