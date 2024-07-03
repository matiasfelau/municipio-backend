package ar.edu.uade.services

import ar.edu.uade.daos.DenunciaDAOFacade
import ar.edu.uade.daos.DenunciaDAOFacadeMySQLImpl
import ar.edu.uade.models.ComercioDenunciado
import ar.edu.uade.models.Denuncia
import ar.edu.uade.models.VecinoDenunciado
import ar.edu.uade.utilities.CloudinaryConfig
import io.ktor.server.config.*
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.ceil

class DenunciaService(config: ApplicationConfig) {
    private val dao: DenunciaDAOFacade = DenunciaDAOFacadeMySQLImpl()

    suspend fun getDenuncias(pagina: Int,documento: String): List<Denuncia> {
        return dao.get10Denuncias(pagina, documento)
    }

    suspend fun getDenunciaById(id: Int): Denuncia? {
        return  dao.getDenunciaById(id)
    }

    suspend fun getDenunciado(id: Int): String? {
        val vecinodenunciado = dao.getVecinoDenunciado(id)
        val comerciodenunciado = dao.getComercioDenunciado(id)
        var resultado = ""
        if (vecinodenunciado != null) {
            resultado = "VECINO DENUNCIADO: "+vecinodenunciado.nombre +" "+ vecinodenunciado.apellido
        }else if(comerciodenunciado != null) {
            resultado = "COMERCIO DENUNCIADO: "+comerciodenunciado.nombre
        }else resultado = ""
        return resultado
    }

    suspend fun addDenunciaComercio(denuncia: Denuncia,comercioDenunciado: ComercioDenunciado): Denuncia? {
        val denunciaDevuelta = dao.addDenuncia(denuncia)
        if (denunciaDevuelta != null) {
            denunciaDevuelta.idDenuncia?.let { dao.addComercioDenunciado(it, comercioDenunciado) }
        }
        return denunciaDevuelta
    }

    suspend fun addDenunciaVecino(denuncia: Denuncia, vecinoDenunciado: VecinoDenunciado): Denuncia?{
        val denunciaDevuelta = dao.addDenuncia(denuncia)
        if (denunciaDevuelta != null) {
            denunciaDevuelta.idDenuncia?.let { dao.addVecinoDenunciado(it, vecinoDenunciado) }
        }
        return denunciaDevuelta
    }

    suspend fun addFileToDenuncia(idDenuncia: Int, fileBase64: String, cloudinaryConfig: CloudinaryConfig) {
        val fileBytes = Base64.getDecoder().decode(fileBase64)

        val fileType = detectFileType(fileBytes)

        val uploadParams = mapOf(
            "resource_type" to when (fileType) {
                FileType.PDF, FileType.RAW -> "auto"
                FileType.IMAGE -> "image"
                FileType.UNKNOWN -> "auto"  // Dejamos que Cloudinary decida
            }
        )

        val uploadResult = cloudinaryConfig.cloudinary.uploader().upload(fileBytes, uploadParams)
        val urlFile = uploadResult["url"] as String
        println("URL: $urlFile")

        when (fileType) {
            FileType.PDF, FileType.RAW -> dao.addImagenToDenuncia(idDenuncia, urlFile)
            FileType.IMAGE -> dao.addImagenToDenuncia(idDenuncia, urlFile)
            FileType.UNKNOWN -> {
                // Decidir basado en el tipo de recurso que Cloudinary detectó
                val detectedType = uploadResult["resource_type"] as String
                when (detectedType) {
                    "image" -> dao.addImagenToDenuncia(idDenuncia, urlFile)
                    else -> dao.addImagenToDenuncia(idDenuncia, urlFile)
                }
            }
        }
    }

    enum class FileType {
        PDF, IMAGE, RAW, UNKNOWN
    }

    fun detectFileType(bytes: ByteArray): FileType {
        return when {
            bytes.size >= 4 && bytes[0] == 0x25.toByte() && bytes[1] == 0x50.toByte() &&
                    bytes[2] == 0x44.toByte() && bytes[3] == 0x46.toByte() -> FileType.PDF
            bytes.size >= 2 && bytes[0] == 0xFF.toByte() && bytes[1] == 0xD8.toByte() -> FileType.IMAGE // JPEG
            bytes.size >= 8 && bytes[0] == 0x89.toByte() && bytes[1] == 0x50.toByte() &&
                    bytes[2] == 0x4E.toByte() && bytes[3] == 0x47.toByte() -> FileType.IMAGE // PNG
            bytes.size >= 3 && bytes[0] == 0x47.toByte() && bytes[1] == 0x49.toByte() &&
                    bytes[2] == 0x46.toByte() -> FileType.IMAGE // GIF
            else -> FileType.RAW  // Asumimos que es un archivo raw si no lo reconocemos
        }
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