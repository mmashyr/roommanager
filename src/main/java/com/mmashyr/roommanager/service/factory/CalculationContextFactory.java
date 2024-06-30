package com.mmashyr.roommanager.service.factory;

import com.mmashyr.roommanager.model.RoomsAvailability;
import com.mmashyr.roommanager.service.calculator.CalculationContext;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
public class CalculationContextFactory {

    public CalculationContext createCalculationContext(Collection<RoomsAvailability> availabilities,
                                                       Collection<Double> clientBudgets) {
        return new CalculationContext(availabilities, clientBudgets);
    }
}