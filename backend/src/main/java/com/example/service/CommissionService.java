package com.example.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.entity.dto.Account;
import com.example.entity.vo.response.CommissionVO;

public interface CommissionService extends IService<Account> {
    IPage<CommissionVO> selectCommissionByDetailPage(Page<CommissionVO> page, String detail, Integer flag);
    IPage<CommissionVO> selectCommissionPage(Page<CommissionVO> page, Integer flag);
}
