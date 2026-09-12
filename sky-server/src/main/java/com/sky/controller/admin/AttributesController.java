package com.sky.controller.admin;

import com.sky.entity.Attributes;
import com.sky.result.Result;
import com.sky.service.GoodsAttributeService;
import com.sky.vo.TypeInfoVO;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author: Joey
 * @Description: 商品规格相关接口
 * @date:2024/5/30 14:26
 */
@RestController
@RequestMapping("/admin/attributes")
@Slf4j
@Api(tags = "商品规格相关接口")
public class AttributesController {

    @Autowired
    private GoodsAttributeService goodsAttributeService;
    /**
     * 规格列表
     * @return
     */
    @GetMapping("/list")
    public Result<List<Attributes>> typeList(){
        return Result.success(goodsAttributeService.query());
    }
}
