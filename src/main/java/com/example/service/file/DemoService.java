package com.example.service.file;

import com.example.dao.mapper.AnimationMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DemoService {

    @Autowired
    private AnimationMapper animationMapper;

    public int testdb() {
        return animationMapper.test();
    }
}
