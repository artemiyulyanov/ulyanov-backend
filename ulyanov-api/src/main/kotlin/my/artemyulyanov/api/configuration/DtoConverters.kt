package my.artemyulyanov.api.configuration

import dev.d1s.exkt.dto.DtoConverter
import dev.d1s.exkt.ktor.server.koin.configuration.ApplicationConfigurer
import dev.d1s.exkt.ktor.server.koin.configuration.builtin.ApplicationConfig
import io.ktor.server.application.*
import my.artemyulyanov.api.converters.*
import my.artemyulyanov.api.entities.PortfolioItemEntity
import my.artemyulyanov.api.entities.UserEntity
import my.artemyulyanov.common.PortfolioItem
import my.artemyulyanov.common.PortfolioItemCreation
import my.artemyulyanov.common.PortfolioItemModification
import my.artemyulyanov.common.User
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.core.qualifier.named
import org.koin.core.qualifier.qualifier

object DtoConverters : ApplicationConfigurer {
    val PortfolioItemDtoConverterQualifier = named("portfolio-item-dto-converter")
    val PortfolioItemCreationDtoConverterQualifier = named("portfolio-item-creation-dto-converter")
    val PortfolioItemModificationDtoConverterQualifier = named("portfolio-item-modification-dto-converter")

    val UserDtoConverterQualifier = named("user-dto-converter")

    override fun Application.configure(module: Module, config: io.ktor.server.config.ApplicationConfig) {
        module.apply {
            singleOf<DtoConverter<PortfolioItemEntity, PortfolioItem>>(::PortfolioItemDtoConverter) {
                qualifier = PortfolioItemDtoConverterQualifier
            }

            singleOf<DtoConverter<UserEntity, User>>(::UserDtoConverter) {
                qualifier = UserDtoConverterQualifier
            }

            singleOf<DtoConverter<PortfolioItemEntity, PortfolioItemCreation>>(::PortfolioItemCreationDtoConverter) {
                qualifier = PortfolioItemCreationDtoConverterQualifier
            }

            singleOf<DtoConverter<PortfolioItemEntity, PortfolioItemModification>>(::PortfolioItemModificationDtoConverter) {
                qualifier = PortfolioItemModificationDtoConverterQualifier
            }
        }
    }
}