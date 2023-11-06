package com.example.jwtauthorisedlogin.service;

import com.example.jwtauthorisedlogin.Entity.Canteen;
import com.example.jwtauthorisedlogin.payload.request.CanteenRequest;
import com.example.jwtauthorisedlogin.payload.response.MessageResponse;
import com.example.jwtauthorisedlogin.repository.CanteenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CanteenService {
    private final CanteenRepository canteenRepository;

    public MessageResponse createCanteen(CanteenRequest request){
        var canteen=new Canteen();
        canteen.setName(request.getName());
        canteen.setEmail(request.getEmail());
        canteen.setDescription(request.getDescription());
        canteen.setCanteenImage(request.getCanteenImage());
        canteenRepository.save(canteen);

        return MessageResponse.builder().message(request.getName()+" was added").build();
    }

}
