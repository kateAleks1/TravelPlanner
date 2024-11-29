package org.example.Service.ServiceImpl;

import org.example.DTO.DestinationTypeDto;
import org.example.Dal.Repository.DestinationTypeRepository;
import org.example.Service.DestinationTypeService;
import org.example.entity.DestinationType;
import org.example.mapper.DestinationTypeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DestinationTypeServiceImpl implements DestinationTypeService {
    private DestinationTypeRepository destinationTypeRepository;
    private DestinationTypeMapper destinationTypeMapper;
@Autowired
    public DestinationTypeServiceImpl(DestinationTypeRepository destinationTypeRepository, DestinationTypeMapper destinationTypeMapper) {
        this.destinationTypeRepository = destinationTypeRepository;
        this.destinationTypeMapper = destinationTypeMapper;
    }

    @Override
    public DestinationType findDestinationTypeByTypeId(int typeId) {
        return destinationTypeRepository.findDestinationTypeByTypeId(typeId).get();
    }

    @Override
    public DestinationType findDestinationTypeByTypeName(int typeName) {
        return null;
    }

    @Override
    public List<DestinationType> findAllDestinationsType() {
        return destinationTypeRepository.findAll();
    }
}
