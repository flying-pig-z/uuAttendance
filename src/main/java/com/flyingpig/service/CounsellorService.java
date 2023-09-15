package com.flyingpig.service;

import org.springframework.stereotype.Service;

@Service
public interface CounsellorService {
    Counsellor getCounsellorByNo(String username);
}
