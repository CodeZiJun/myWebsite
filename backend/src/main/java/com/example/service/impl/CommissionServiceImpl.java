package com.example.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.entity.dto.Account;
import com.example.entity.vo.response.CommissionVO;
import com.example.mapper.AccountMapper;
import com.example.service.CommissionService;
import com.github.yulichang.query.MPJQueryWrapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class CommissionServiceImpl extends ServiceImpl<AccountMapper, Account> implements CommissionService {

    @Resource
    AccountMapper accountMapper;
    @Override
    public IPage<CommissionVO> selectCommissionByDetailPage(Page<CommissionVO> page, String detail, Integer flag) {
        if(!"".equals(detail)) {
            MPJQueryWrapper<Account> wrapper = new MPJQueryWrapper<>();

            wrapper
                    .selectAll(Account.class)
                    .and(qw -> qw
                            .like("t.username", detail).or()
                            .like("t.id",detail).or()
                            .like("t.email", detail).or()
                            .like("dep.department_name", detail).or()
                            .like("arc.real_name",detail).or()
                            .like("pos.position_name",detail))
                    .select("arc.real_name", "arc.entry_date")
                    .select("dep.department_name")
                    .select("pos.position_name")
                    .leftJoin("db_archives arc on t.archives_id = arc.id")
                    .leftJoin("db_department dep on arc.department_id = dep.id")
                    .leftJoin("db_position pos on arc.position_id = pos.id");
            return getCommissionVOIPage(page, flag, wrapper);
        }
        else
            return selectCommissionPage(page, flag);
    }

    private IPage<CommissionVO> getCommissionVOIPage(Page<CommissionVO> page, Integer flag, MPJQueryWrapper<Account> wrapper) {
        if (flag == 0)
            return accountMapper.selectJoinPage(page, CommissionVO.class, wrapper);
        else if(flag == 1)
            wrapper.isNull("t.archives_id");
        else {
            wrapper.isNotNull("t.archives_id");
            if(flag == 2){
                wrapper.isNull("arc.department_id");
                wrapper.isNull("arc.position_id");
            } else if (flag == 3) {
                wrapper.isNull("arc.department_id");
                wrapper.isNotNull("arc.position_id");
            } else if (flag == 4) {
                wrapper.isNotNull("arc.department_id");
                wrapper.isNull("arc.position_id");
            } else if (flag == 5) {
                wrapper.isNotNull("arc.department_id");
                wrapper.isNotNull("arc.position_id");
            }
        }
        return accountMapper.selectJoinPage(page, CommissionVO.class, wrapper);
    }

    @Override
    public IPage<CommissionVO> selectCommissionPage(Page<CommissionVO> page, Integer flag) {
        MPJQueryWrapper<Account> wrapper =  new MPJQueryWrapper<>();
        wrapper
                .selectAll(Account.class)
                .select("arc.real_name", "arc.entry_date")
                .select("dep.department_name")
                .select("pos.position_name")
                .leftJoin("db_archives arc on t.archives_id = arc.id")
                .leftJoin("db_department dep on arc.department_id = dep.id")
                .leftJoin("db_position pos on arc.position_id = pos.id");
        return getCommissionVOIPage(page, flag, wrapper);
    }
}
