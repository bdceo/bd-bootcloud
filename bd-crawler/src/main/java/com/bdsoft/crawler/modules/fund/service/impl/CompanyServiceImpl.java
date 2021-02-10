package com.bdsoft.crawler.modules.fund.service.impl;

import com.bdsoft.crawler.modules.fund.entity.Company;
import com.bdsoft.crawler.modules.fund.mapper.CompanyMapper;
import com.bdsoft.crawler.modules.fund.service.ICompanyService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 基金公司 服务实现类
 * </p>
 *
 * @author bdceo
 * @since 2021-02-10
 */
@Service
public class CompanyServiceImpl extends ServiceImpl<CompanyMapper, Company> implements ICompanyService {

}
