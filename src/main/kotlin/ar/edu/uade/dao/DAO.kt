package ar.edu.uade.dao

import ar.edu.uade.models.Rubro
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction

object DAO {
    fun init() {
        val jdbcURL = "jdbc:mysql://localhost:3306/municipio"
        val driverClassName = "com.mysql.cj.jdbc.Driver"
        val user = "root"
        val password = "1234"
        val database = Database.connect(jdbcURL, driverClassName, user, password)
        transaction(database) {
            SchemaUtils.create(Rubro.Rubros)
        }
    }
    suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO) { block() }
}
