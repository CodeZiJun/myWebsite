package com.example.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.entity.dto.Position;
import com.example.entity.vo.request.PositionAddVO;
import com.example.entity.vo.response.PositionVO;

public interface PositionService extends IService<Position> {
    IPage<PositionVO> selectPositionByDetailPage(Page<PositionVO> page, String detail);
    IPage<PositionVO> selectPositionPage(Page<PositionVO> page);
    String addPosition(PositionAddVO vo);
}
