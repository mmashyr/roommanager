package com.mmashyr.roommanager.config;

import com.mmashyr.roommanager.model.ClientBookingInfo;
import com.mmashyr.roommanager.model.RoomType;
import com.mmashyr.roommanager.service.calculator.GenericRoomBooker;
import com.mmashyr.roommanager.service.calculator.RoomBooker;
import com.mmashyr.roommanager.service.calculator.UpscaleBooker;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Comparator;
import java.util.List;

@Configuration
public class ContextConfig {

    private static final Double ECONOMY_THRESHOLD = 100.0;

    @Bean
    @Qualifier("baseHotelChain")
    List<RoomBooker> getMatchers() {
        return List.of(
                premiumMatcher(),
                economyToPremiumUpgradedMatcher(),
                economyMatcher());
    }

    @Bean
    RoomBooker premiumMatcher() {
        return new GenericRoomBooker(RoomType.PREMIUM,
                clientInfo -> !clientInfo.isBooked() && ECONOMY_THRESHOLD <= clientInfo.getBudget(),
                Comparator.comparingDouble(ClientBookingInfo::getBudget).reversed());
    }

    @Bean
    RoomBooker economyMatcher() {
        return new GenericRoomBooker(RoomType.ECONOMY,
                clientInfo -> !clientInfo.isBooked() && ECONOMY_THRESHOLD > clientInfo.getBudget(),
                Comparator.comparingDouble(ClientBookingInfo::getBudget).reversed());
    }

    @Bean
    RoomBooker economyToPremiumUpgradedMatcher() {
        return new UpscaleBooker(RoomType.ECONOMY, RoomType.PREMIUM,
                clientInfo -> !clientInfo.isBooked() && ECONOMY_THRESHOLD > clientInfo.getBudget(),
                Comparator.comparingDouble(ClientBookingInfo::getBudget).reversed());
    }
}
