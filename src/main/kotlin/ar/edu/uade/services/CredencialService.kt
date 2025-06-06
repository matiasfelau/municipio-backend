package ar.edu.uade.services


import ar.edu.uade.daos.CredencialDAOFacade
import ar.edu.uade.daos.CredencialDAOFacadeMySQLImpl
import ar.edu.uade.daos.VecinoDAOFacade
import ar.edu.uade.daos.VecinoDAOFacadeMySQLImpl
import ar.edu.uade.mappers.requests.CredencialRequest
import ar.edu.uade.models.Credencial
import io.ktor.server.config.*
import java.nio.file.Files
import java.nio.file.Paths
import java.util.Properties
import javax.mail.*
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage
import kotlin.random.Random

class CredencialService() {
    private val credencialDAO : CredencialDAOFacade = CredencialDAOFacadeMySQLImpl()
    private val vecinoDAO : VecinoDAOFacade = VecinoDAOFacadeMySQLImpl()

    public fun getDAO(): CredencialDAOFacade {
        return credencialDAO
    }

    suspend fun solicitarCredencial(documento: String, email: String) : Credencial? {
        if (vecinoDAO.verifyVecino(documento)) {
            return credencialDAO.addNewCredencial(documento, email)
        }
        return null
    }

    suspend fun habilitarCredencial(credencial: Credencial): Boolean {
        var bool = false
        if (!credencial.habilitado) {
            val password = Random.nextInt(10000000, 99999999).toString()
            val habilitado = true
            val primerIngreso = true
            bool = credencialDAO.editHabilitadoCredencial(credencial.documento, password, habilitado, primerIngreso)
            //send mail
            val cr = credencialDAO.findCredencialByDocumento(credencial.documento)
            if (cr != null) {
                cr.password?.let {
                    sendEmail(cr.email, it, "src/main/resources/email_template.html")
                }
            }
        }


        return bool
    }

    private fun sendEmail(email: String, password: String, path: String) {
        val props = Properties().apply {
            put("mail.smtp.auth", "true")
            put("mail.smtp.starttls.enable", "true")
            put("mail.smtp.host", "smtp.mailersend.net")  // Cambia esto a tu servidor SMTP
            put("mail.smtp.port", "587")
        }


        val session = Session.getInstance(props, object : Authenticator() {
            override fun getPasswordAuthentication(): PasswordAuthentication {
                return PasswordAuthentication("MS_ad3RME@trial-jy7zpl9xwxpl5vx6.mlsender.net", "wBaAJtWflHV43KOl")
            }
        })

        try {
            var htmlContent = Files.readString(Paths.get(path))
            htmlContent = htmlContent.replace("{{password}}", password)
            val message = MimeMessage(session)
            message.setFrom(InternetAddress("MS_ad3RME@trial-jy7zpl9xwxpl5vx6.mlsender.net"))
            message.addRecipient(Message.RecipientType.TO, InternetAddress(email))
            message.subject = "Su cuenta en el municipio de Palmas de Mallorca fue habilitada"
            message.setContent(htmlContent,"text/html; charset=utf-8"); //TODO REEMPLAZAR NOMBRE Y PASSWORD O AL MENOS PASSWORD
            //message.setText("Su contraseña es: $password")
            Transport.send(message)
            println("Email sent successfully")
        } catch (e: MessagingException) {
            e.printStackTrace()
        }

    }

    suspend fun casoPrimerIngresoCredencial(credencial: CredencialRequest) : Boolean {
        val bd = credencialDAO.findCredencialByDocumento(credencial.documento)
        var bool = false
        if (bd != null) {
            if (bd.habilitado && bd.primerIngreso) {
                var password = bd.password
                val primerIngreso = false
                if (credencial.password != "") {
                    password = credencial.password
                }
                else {
                    password = bd.password
                }
                bool = password?.let {
                    credencialDAO.editPrimerIngresoCredencial(credencial.documento,
                        it, primerIngreso)
                } == true
            }
        }
        return bool
    }

    suspend fun recuperarCredencial(credencial: Credencial): Boolean {
        var bool = false
        if (credencial.habilitado) {
            val password = Random.nextInt(10000000, 99999999).toString()
            val primerIngreso = true
            bool = credencialDAO.editPasswordCredencial(credencial.documento, password, primerIngreso)
            //send mail
            val cr = credencialDAO.findCredencialByDocumento(credencial.documento)
            if (cr != null) {
                cr.password?.let {
                    sendEmail(cr.email, it, "src/main/resources/email_template2.html")
                }
            }
        }
        return bool
    }

    suspend fun find(documento: String): Credencial? {
        return credencialDAO.findCredencialByDocumento(documento)
    }
}