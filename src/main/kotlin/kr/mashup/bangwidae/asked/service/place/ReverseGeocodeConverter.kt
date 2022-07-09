package kr.mashup.bangwidae.asked.service.place

import kr.mashup.bangwidae.asked.model.Region

interface ReverseGeocodeConverter {
    fun convert(longitude: Double, latitude: Double): Region
}