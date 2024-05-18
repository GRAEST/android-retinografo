package br.com.graest.retinografo.data.model

import java.sql.Timestamp

data class ImageDetailsDataCloud(
    val imgSrc : String,
    val imgSrcIA: String,
    val patientName : String?,
    val patientAge : Int?,
    val createdAt : Timestamp,
    val potential : Double?,
    val insightAI: String
)
