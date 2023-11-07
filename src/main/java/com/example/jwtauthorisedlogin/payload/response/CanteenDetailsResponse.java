package com.example.jwtauthorisedlogin.payload.response;

import com.example.jwtauthorisedlogin.Entity.Canteen;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CanteenDetailsResponse {
    private List<Canteen> canteenItems;
}
