package kr.mashup.bangwidae.asked.utils

import org.springframework.data.mongodb.core.geo.GeoJsonPoint

class GeoUtils {
    companion object {
        fun geoJsonPoint(longitude: Double, latitude: Double): GeoJsonPoint {
            return GeoJsonPoint(longitude, latitude)
        }
    }
}

fun GeoJsonPoint.getLongitude(): Double = x

fun GeoJsonPoint.getLatitude(): Double = y