package kr.mashup.bangwidae.asked.controller

import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import kr.mashup.bangwidae.asked.controller.dto.ApiResponse
import kr.mashup.bangwidae.asked.controller.dto.PlaceDto
import kr.mashup.bangwidae.asked.service.place.PlaceService
import org.springframework.web.bind.annotation.*

@Api(tags = ["장소 컨트롤러"])
@RestController
@RequestMapping("/api/v1/place")
class PlaceController(
    private val placeService: PlaceService,
) {
    @ApiOperation("위경도를 주소로 변환")
    @GetMapping("/reverse/geocode")
    fun reverseGeocode(
        @RequestParam latitude: Double,
        @RequestParam longitude: Double,
    ): ApiResponse<PlaceDto> {
        return placeService.reverseGeocode(
            longitude = longitude,
            latitude = latitude,
        ).let { ApiResponse.success(PlaceDto.from(it)) }
    }
}
