package com.fardhan.assetmanagement.config;

import com.fardhan.assetmanagement.entity.RequestType;
import com.fardhan.assetmanagement.repository.RequestTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DataInitializer {
    private final RequestTypeRepository requestTypeRepository;

    @PostConstruct
    public void init() {
        List<String> types = Arrays.asList("ASSET", "REPAIR", "PURCHASE", "SELF_REPAIR", "ASSIGN");
        for (String type : types) {
            requestTypeRepository.findByNameIgnoreCase(type)
                    .orElseGet(() -> {
                        RequestType rt = new RequestType();
                        rt.setName(type);
                        rt.setDescription("Request type for " + type.toLowerCase());
                        return requestTypeRepository.save(rt);
                    });
        }
    }
}