package ar.edu.uade.daos

import ar.edu.uade.models.ImagenProfesional
import ar.edu.uade.models.Profesional
import java.math.BigDecimal
import java.time.LocalTime

interface ProfesionalDAOFacade {
    suspend fun getCantidadElementos(): Int
    suspend fun get10Profesionales(pagina: Int): List<Profesional>
    suspend fun addProfesional(nombre: String, direccion: String?, telefono: Int?, email: String?, latitud: BigDecimal?, longitud: BigDecimal?, inicioJornada: LocalTime?, finJornada: LocalTime?, documento: String): Profesional?
    suspend fun addImagenToProfesional(urlImagen: String, idProfesional: Int): ImagenProfesional?
}