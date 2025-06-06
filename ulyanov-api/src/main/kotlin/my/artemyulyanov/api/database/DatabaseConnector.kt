package my.artemyulyanov.api.database

import dev.d1s.exkt.ktor.server.requiredJdbcProperties
import dev.d1s.ktor.liquibase.plugin.LiquibaseMigrations
import dev.d1s.ktor.liquibase.plugin.changeLogPath
import io.ktor.server.application.*
import io.ktor.server.config.*
import org.koin.core.component.KoinComponent
import org.ktorm.database.Database
import org.ktorm.support.postgresql.PostgreSqlDialect
import org.lighthousegames.logging.logging
import org.vibur.dbcp.ViburDBCPDataSource
import javax.sql.DataSource

interface DatabaseConnector {

    val database: Database

    fun Application.initialize(config: ApplicationConfig)

    fun reconnect()
}

class DefaultDatabaseConnector : DatabaseConnector, KoinComponent {
    override val database: Database
        get() = internalDatabase ?: error("Database not yet initialized")

    private var internalDatabase: Database? = null

    private val logger = logging()

    override fun Application.initialize(config: ApplicationConfig) {
        logger.i {
            "Initializing database connection..."
        }

        val dataSource = initDataSource(config)
        val database = connect(dataSource)

        internalDatabase = database

        migrate(database, config)
    }

    override fun reconnect() {
        logger.i {
            "Reconnecting database..."
        }

        internalDatabase?.useConnection { }
    }

    private fun initDataSource(config: ApplicationConfig) =
        ViburDBCPDataSource().apply {
            config.requiredJdbcProperties.let {
                jdbcUrl = it.url
                username = it.user
                password = it.password
            }

            start()
        }

    private fun connect(dataSource: DataSource) =
        Database.connect(
            dataSource = dataSource,
            dialect = PostgreSqlDialect()
        )

    private fun Application.migrate(database: Database, config: ApplicationConfig) {
        database.useConnection {
            install(LiquibaseMigrations) {
                changeLogPath = config.changeLogPath
                connection = it
            }
        }
    }
}