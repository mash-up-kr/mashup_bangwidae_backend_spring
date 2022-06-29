package kr.mashup.bangwidae.asked.external.map

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestParam

@FeignClient(name = "naver-map", url = "\${map.naver.host}")
interface NaverMapFeignClient {
    @GetMapping("/map-reversegeocode/v2/gc")
    fun reverseGeocode(
        @RequestHeader("X-NCP-APIGW-API-KEY-ID") clientId: String,
        @RequestHeader("X-NCP-APIGW-API-KEY") clientSecret: String,
        @RequestParam("coords") coordinates: String,
        @RequestParam("output") responseType: String,
    ): NaverReverseGeocodeResponse
}