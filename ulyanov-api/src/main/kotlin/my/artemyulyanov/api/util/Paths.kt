package my.artemyulyanov.api.util

object Paths {
    const val ID_PARAMETER = "id"
    const val SCOPE_PARAMETER = "scope"

    const val GET_ALL_PORTFOLIO_ITEMS = "/api/portfolio"
    const val GET_PORTFOLIO_ITEM_BY_ID = "/api/portfolio/id/{${ID_PARAMETER}}"
    const val GET_PORTFOLIO_ITEMS_BY_SCOPE = "/api/portfolio/scope/{${SCOPE_PARAMETER}}"

    const val POST_PORTFOLIO_ITEM = "/api/portfolio"

    const val PUT_PORTFOLIO_ITEM = "/api/portfolio/{${ID_PARAMETER}}"
    const val DELETE_PORTFOLIO_ITEM = "/api/portfolio/{${ID_PARAMETER}}"

    const val GET_API_DOCS = "/docs"
}