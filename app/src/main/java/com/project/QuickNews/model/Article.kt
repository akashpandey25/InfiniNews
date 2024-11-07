package com.project.QuickNews.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "articles")
data class Article(
    @PrimaryKey(autoGenerate = true)
    var id:Int?=null,
    val author: String?,
    val content: String?,
    val description: String?,
    val publishedAt: String?,
    val source: Source?, //this is not DB supported datatype do we use typeconverter
    val title: String?,
    var url: String?,
    val urlToImage: String?,
    val category: String?
): Serializable  //it is used to convert into  a format that can be easily stored
{
    override fun hashCode(): Int {
        var result = id ?: 0
        result = 31 * result + author.hashCode()
        result = 31 * result + content.hashCode()
        result = 31 * result + description.hashCode()
        result = 31 * result + publishedAt.hashCode()
        result = 31 * result + source.hashCode()
        result = 31 * result + title.hashCode()
        result = 31 * result + url.hashCode()
        result = 31 * result + urlToImage.hashCode()
        return result
    }
}