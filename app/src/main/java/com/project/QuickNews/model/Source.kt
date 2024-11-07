package com.project.QuickNews.model

import java.io.Serializable

data class Source(
    val id: String,
    val name: String
): Serializable {
    override fun hashCode(): Int {
        var result = id?.hashCode() ?: 0
        result = 31 * result + (name?.hashCode() ?: 0)
        return result
    }
}