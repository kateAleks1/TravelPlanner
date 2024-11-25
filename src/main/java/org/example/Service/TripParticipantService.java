package org.example.Service;

import lombok.RequiredArgsConstructor;
import org.example.Dal.Repository.TripParticipantRepository;
import org.example.Dal.Repository.TripRepository;
import org.example.entity.Trip;
import org.example.entity.TripPartcipants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TripParticipantService {

    private final TripParticipantRepository tripParticipantRepository;
    private final TripRepository tripRepository;
@Autowired
    public TripParticipantService(TripParticipantRepository tripParticipantRepository, TripRepository tripRepository) {
        this.tripParticipantRepository = tripParticipantRepository;
        this.tripRepository = tripRepository;
    }

    public List<Integer> getUserIdsByTripId(int tripId) {
        return tripParticipantRepository.findUserIdsByTripId(tripId);
    }
    public  List<TripPartcipants> getAllTripParticipants(){
    return tripParticipantRepository.findAll();
    }


    @Transactional
    public void removeUserFromTrip(int tripId, int userId) {
        tripParticipantRepository.deleteUserFromTrip(tripId, userId);
    }

    public void isGroup(int tripId){
Trip trip=tripRepository.findById(tripId).get();


// поездка 1
    }
}