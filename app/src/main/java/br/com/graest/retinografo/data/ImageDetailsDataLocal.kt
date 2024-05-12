package br.com.graest.retinografo.data

import java.sql.Timestamp

data class ImageDetailsDataLocal(
    val imgSrc : String,
    val patientName : String?,
    val patientAge : Int?,
    val createdAt : Timestamp,
    val potential : Double?
)
