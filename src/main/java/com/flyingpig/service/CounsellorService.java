package com.flyingpig.service;

import com.flyingpig.entity.Counsellor;
import org.springframework.stereotype.Service;

@Service
public interface CounsellorService {
    Counsellor getCounsellorByNo(String username);
}
