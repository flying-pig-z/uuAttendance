package com.flyingpig.service;

import com.flyingpig.dataobject.entity.Counsellor;
import org.springframework.stereotype.Service;

@Service
public interface CounsellorService {
    Counsellor getCounsellorByNo(String username);
}
