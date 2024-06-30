package com.mmashyr.roommanager.service.client;

import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
public class MockClientService implements ClientService {

    @Override
    public Collection<Double> getClientBudged() {
        return List.of(23.0, 45.0, 155.0, 374.0, 22.0, 99.99, 100.0, 101.0, 115.0, 209.0);
    }
}
