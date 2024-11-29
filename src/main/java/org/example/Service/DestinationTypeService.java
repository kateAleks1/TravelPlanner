package org.example.Service;

import org.example.DTO.DestinationTypeDto;
import org.example.entity.DestinationType;

import java.util.List;

public interface DestinationTypeService {
  DestinationType findDestinationTypeByTypeId(int typeId);
    DestinationType findDestinationTypeByTypeName(int typeName);
    List<DestinationType> findAllDestinationsType();
}
