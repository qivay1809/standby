package com.qivay.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.qivay.mapper.UpdateNoticeMapper;
import com.qivay.service.UpdateNoticeService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service("updateNoticeService")
public class UpdateNoticeServiceImpl implements UpdateNoticeService {
    @Resource
    private UpdateNoticeMapper updateNoticeMapper;

    @Override
    public JSONObject UpdateNotice(String key) {
        String obj = updateNoticeMapper.UpdateNotice(key);
        return JSONObject.parseObject(obj);
    }
}
