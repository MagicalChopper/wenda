package com.nowcoder.service;

import org.springframework.stereotype.Service;

/**
 * @Author: LiuHao
 * @Descirption:
 * @Date: 2018/8/26_19:35
 */
@Service
public class WendaService {

    public String getMessage(int userId) {
        return "Hello Message:" + String.valueOf(userId);
    }
}
