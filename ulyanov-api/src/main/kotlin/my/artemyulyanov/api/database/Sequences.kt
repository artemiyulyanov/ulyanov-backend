package my.artemyulyanov.api.database

import org.ktorm.database.Database
import org.ktorm.entity.sequenceOf

val Database.portfolioItems
    get() = sequenceOf(PortfolioItems)

val Database.users
    get() = sequenceOf(Users)