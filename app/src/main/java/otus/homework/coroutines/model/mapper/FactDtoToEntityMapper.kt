package otus.homework.coroutines.model.mapper

import otus.homework.coroutines.model.entity.Fact
import otus.homework.coroutines.model.network.dto.FactDto
import otus.homework.coroutines.model.network.dto.ImageDto

class FactDtoToEntityMapper {

    fun toEntity(factDto: FactDto, imageDto: ImageDto): Fact {
        return Fact(factDto.fact, imageDto.url)
    }
}
