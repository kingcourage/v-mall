package com.wcy.wmall.service.impl;

import com.github.pagehelper.PageHelper;
import com.wcy.wmall.dto.PmsBrandDto;
import com.wcy.wmall.mapper.PmsBrandMapper;
import com.wcy.wmall.model.PmsBrand;
import com.wcy.wmall.model.PmsBrandExample;
import com.wcy.wmall.service.DemoService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ClassName DemoServiceImpl
 * @Description TODO
 * @Author wcy
 * @Date 2019-07-29 16:56
 * @Version 1.0
 **/
@Service
public class DemoServiceImpl implements DemoService {

    @Autowired
    PmsBrandMapper pmsBrandMapper;

    @Override
    public List<PmsBrand> listAllBrand() {
        return pmsBrandMapper.selectByExample(new PmsBrandExample());
    }

    @Override
    public int createBrand(PmsBrandDto pmsBrandDto) {
        PmsBrand pmsBrand = new PmsBrand();
        BeanUtils.copyProperties(pmsBrandDto,pmsBrand);
        return pmsBrandMapper.insertSelective(pmsBrand);
    }

    @Override
    public int updateBrand(Long id, PmsBrandDto pmsBrandDto) {
        PmsBrand pmsBrand = new PmsBrand();
        BeanUtils.copyProperties(pmsBrandDto,pmsBrand);
        pmsBrand.setId(id);
        return pmsBrandMapper.updateByPrimaryKeySelective(pmsBrand);
    }

    @Override
    public int deleteBrand(Long id) {
        return pmsBrandMapper.deleteByPrimaryKey(id);
    }

    @Override
    public List<PmsBrand> listBrand(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        return pmsBrandMapper.selectByExample(new PmsBrandExample());
    }

    @Override
    public PmsBrand getBrand(Long id) {
        return pmsBrandMapper.selectByPrimaryKey(id);
    }
}
