package org.example.mapper;

import org.example.DTO.TripDto;
import org.example.entity.Trip;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TripMapper {
    Trip toEntity(TripDto tripDto);
    TripDto toDTO(Trip trip);
}
