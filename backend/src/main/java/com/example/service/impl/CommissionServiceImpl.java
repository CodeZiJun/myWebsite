package com.example.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.entity.dto.Account;
import com.example.entity.vo.response.CommissionVO;
import com.example.service.CommissionService;
import com.github.yulichang.query.MPJQueryWrapper;
import org.springframework.stereotype.Service;

@Service
public class CommissionServiceImpl extends AccountServiceImpl implements CommissionService {
    @Override
    public IPage<CommissionVO> selectCommissionByDetailPage(Page<CommissionVO> page, String detail) {
        if(!"".equals(detail))
            return accountMapper.selectJoinPage(page, CommissionVO.class,
                    new MPJQueryWrapper<Account>()
                            .selectAll(Account.class)
                            .like("t.username", detail).or()
                            .like("t.id",detail).or()
                            .like("t.email", detail).or()
                            .like("dep.department_name", detail)
                            .like("arc.real_name",detail)
                            .like("pos.position_name",detail)
                            .select("arc.real_name", "arc.entry_date")
                            .select("dep.department_name")
                            .select("pos.position_name")
                            .leftJoin("db_archives arc on t.archives_id = arc.id")
                            .leftJoin("db_department dep on arc.department_id = dep.id")
                            .leftJoin("db_position pos on arc.position_id = pos.id"));
        else
            return selectCommissionPage(page);
    }

    @Override
    public IPage<CommissionVO> selectCommissionPage(Page<CommissionVO> page) {
        return accountMapper.selectJoinPage(page, CommissionVO.class,
                new MPJQueryWrapper<Account>()
                        .selectAll(Account.class)
                        .select("arc.real_name", "arc.entry_date")
                        .select("dep.department_name")
                        .select("pos.position_name")
                        .leftJoin("db_archives arc on t.archives_id = arc.id")
                        .leftJoin("db_department dep on arc.department_id = dep.id")
                        .leftJoin("db_position pos on arc.position_id = pos.id"));
    }
}
