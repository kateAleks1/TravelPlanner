package org.example.Dal.Repository;

import org.example.entity.DestinationType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DestinationTypeRepository extends JpaRepository<DestinationType,Integer> {

    Optional<DestinationType> findDestinationTypeByTypeId(int typeId);
    DestinationType findDestinationTypeByTypeName(String typeName);



}
