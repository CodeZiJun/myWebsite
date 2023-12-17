package com.example.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.entity.dto.Position;
import com.example.entity.vo.request.PositionAddVO;
import com.example.entity.vo.response.PositionVO;
import com.example.mapper.PositionMapper;
import com.example.service.PositionService;
import com.github.yulichang.query.MPJQueryWrapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class PositionServiceImpl extends ServiceImpl<PositionMapper, Position> implements PositionService {
    @Resource
    PositionMapper mapper;
    @Override
    public IPage<PositionVO> selectPositionByDetailPage(Page<PositionVO> page, String detail) {
        if(!"".equals(detail))
            return mapper.selectJoinPage(page, PositionVO.class,
                    new MPJQueryWrapper<Position>()
                            .selectAll(Position.class)
                            .like("t.id", detail).or()
                            .like("t.position_name",detail).or()
                            .like("sal.amount", detail)
                            .select("sal.amount")
                            .leftJoin("db_salary sal on t.salary_id = sal.id"));
        else
            return selectPositionPage(page);
    }

    @Override
    public IPage<PositionVO> selectPositionPage(Page<PositionVO> page) {
        return mapper.selectJoinPage(page, PositionVO.class,
                new MPJQueryWrapper<Position>()
                        .selectAll(Position.class)
                        .select("sal.amount")
                        .leftJoin("db_salary sal on t.salary_id = sal.id"));
    }

    @Override
    public String addPosition(PositionAddVO vo) {
        if (this.save(new Position(null, vo.getPositionName(), vo.getSalaryId())))
            return null;
        else
            return "内部错误，请联系管理员";
    }
}
