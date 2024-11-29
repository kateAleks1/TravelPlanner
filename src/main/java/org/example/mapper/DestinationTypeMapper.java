package org.example.mapper;

import org.example.DTO.DestinationTypeDto;
import org.example.DTO.UserDto;
import org.example.entity.DestinationType;
import org.example.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DestinationTypeMapper {
    DestinationTypeDto toEntity(DestinationTypeDto destinationTypeDto);
    DestinationTypeDto toDto(DestinationType destinationType);
}
