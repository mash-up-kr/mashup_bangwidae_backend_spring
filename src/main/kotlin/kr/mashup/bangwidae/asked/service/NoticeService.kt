package kr.mashup.bangwidae.asked.service

import kr.mashup.bangwidae.asked.model.Notice
import kr.mashup.bangwidae.asked.repository.NoticeRepository
import org.bson.types.ObjectId
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service

@Service
class NoticeService(
    private val noticeRepository: NoticeRepository,
) {

    fun getNoticeList(lastId: ObjectId?, size: Int): List<Notice> {
        return noticeRepository.findAllByIdBeforeAndDeletedFalseOrderByIdDesc(
            lastId = lastId ?: ObjectId(),
            pageRequest = PageRequest.of(0, size),
        )
    }

    fun register(notice: Notice): Notice {
        return noticeRepository.save(notice)
    }
}