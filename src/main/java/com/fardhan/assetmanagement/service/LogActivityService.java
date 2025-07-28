package com.fardhan.assetmanagement.service;

import com.fardhan.assetmanagement.dto.response.LogActivityResponse;
import com.fardhan.assetmanagement.entity.LogActivity;
import com.fardhan.assetmanagement.entity.User;
import com.fardhan.assetmanagement.repository.LogActivityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LogActivityService {
    private final LogActivityRepository logActivityRepository;

    @Transactional
    public void log(String activity, User user) {
        LogActivity log = new LogActivity();
        log.setActivity(activity);
        log.setUser(user);
        logActivityRepository.save(log);
    }

    @Transactional(readOnly = true)
    public List<LogActivityResponse> getAllLogs() {
        return logActivityRepository.findAll().stream()
                .map(log -> new LogActivityResponse(
                        log.getId(),
                        log.getActivity(),
                        log.getUser() != null ? log.getUser().getName() : null,
                        log.getCreatedAt()))
                .collect(Collectors.toList());
    }
}