package ar.edu.uade.services

import ar.edu.uade.daos.DenunciaDAOFacade
import ar.edu.uade.daos.DenunciaDAOFacadeMySQLImpl
import ar.edu.uade.models.ComercioDenunciado
import ar.edu.uade.models.Denuncia
import ar.edu.uade.models.VecinoDenunciado
import ar.edu.uade.utilities.containers.ContainerDenunciaComercio
import io.ktor.server.config.*
import org.jetbrains.exposed.sql.transactions.transaction
import kotlin.math.ceil

class DenunciaService(config: ApplicationConfig) {
    private val dao: DenunciaDAOFacade = DenunciaDAOFacadeMySQLImpl()

    suspend fun getDenuncias(pagina: Int): List<Denuncia> {
        return dao.get10Denuncias(pagina)
    }

    suspend fun getDenunciaById(id: Int): Denuncia? {
        return  dao.getDenunciaById(id)
    }

    suspend fun addDenunciaComercio(denuncia: Denuncia,comercioDenunciado: ComercioDenunciado): Denuncia? {
        val denunciaDevuelta = dao.addDenuncia(denuncia)
        if (denunciaDevuelta != null) {
            dao.addComercioDenunciado(denunciaDevuelta, comercioDenunciado)
        }
        return denunciaDevuelta
    }

    suspend fun addDenunciaVecino(denuncia: Denuncia, vecinoDenunciado: VecinoDenunciado): Denuncia?{
        val denunciaDevuelta = dao.addDenuncia(denuncia)
        if (denunciaDevuelta != null) {
            dao.addVecinoDenunciado(denunciaDevuelta, vecinoDenunciado)
        }
        return denunciaDevuelta
    }

    suspend fun addImagenToDenuncia(idDenuncia: Int, urlImagen: String) {
        dao.addImagenToDenuncia(idDenuncia, urlImagen)
    }

    suspend fun getCantidadPaginas(): Int{
        val cantpaginas = dao.getAllCantidadPaginas()
        val resultado = cantpaginas.toDouble()/10
        return ceil(resultado).toInt()
    }

    suspend fun getFotos(id: Int): MutableList<String>{
        val resultado: MutableList<String> = ArrayList()
        for (foto in dao.getFotosById(id)){
            resultado.add(foto.urlImagen)
        }
        return resultado
    }
}