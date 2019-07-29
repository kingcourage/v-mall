package com.wcy.wmall.service;


import com.wcy.wmall.dto.PmsBrandDto;
import com.wcy.wmall.model.PmsBrand;

import java.util.List;

public interface DemoService {

    List<PmsBrand> listAllBrand();

    int createBrand(PmsBrandDto pmsBrandDto);

    int updateBrand(Long id, PmsBrandDto pmsBrandDto);

    int deleteBrand(Long id);

    List<PmsBrand> listBrand(int pageNum, int pageSize);

    PmsBrand getBrand(Long id);
}
