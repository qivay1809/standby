package com.qivay.mapper;

import org.apache.ibatis.annotations.Mapper;


@Mapper
public interface UpdateNoticeMapper {
    String UpdateNotice(String key);
}
