package com.sumit.launcher.data.focus

object BlockedDomains {
    val DOMAINS: Set<String> = setOf(
        "pornhub.com",
        "xvideos.com",
        "xnxx.com",
        "xhamster.com",
        "redtube.com",
        "youporn.com",
        "onlyfans.com",
        "stripchat.com",
        "chaturbate.com",
        "spankbang.com",
        "tnaflix.com",
        "eporner.com",
    )

    fun matches(urlOrText: String): Boolean {
        if (urlOrText.isBlank()) return false
        val lower = urlOrText.lowercase()
        return DOMAINS.any { lower.contains(it) }
    }
}
