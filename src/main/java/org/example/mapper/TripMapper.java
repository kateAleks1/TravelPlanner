package org.example.mapper;

import org.example.DTO.TripDto;
import org.example.entity.Trip;
import org.example.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.HashSet;
import java.util.Set;

//@Mapper(componentModel = "spring")
public interface TripMapper {
    @Mapping(target = "users", expression = "java(trip.getUsers().size())")
    @Mapping(target = "tripId", source = "tripId")  // Убедитесь, что имя поля совпадает
    TripDto toDTO(Trip trip);

    @Mapping(target = "users", ignore = true)
    @Mapping(target = "tripId", ignore = true) // Убедитесь, что вы не повторяете tripId
    Trip toEntity(TripDto tripDto);
}
