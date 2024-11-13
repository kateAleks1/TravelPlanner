package org.example.mapper;

import org.example.DTO.TripDto;
import org.example.entity.Trip;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TripMapper {
    @Mapping(target = "users", expression = "java(trip.getUsers().size())")
    TripDto toDTO(Trip trip);

    @Mapping(target = "users", ignore = true) // Skipping Set<User> for now
    Trip toEntity(TripDto tripDto);
}
