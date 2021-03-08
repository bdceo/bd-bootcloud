package com.bdsoft.crawler.modules.delivery.controller;

import com.bdsoft.crawler.common.vo.Res;
import com.bdsoft.crawler.modules.delivery.service.ICostService;
import com.bdsoft.crawler.modules.delivery.vo.FundCostViewVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;

/**
 * 交割、成本
 */
@Slf4j
@RestController
@RequestMapping("/delivery")
public class DeliveryController {

    private final String PAGE_PREFIX = "/delivery/";

    @Resource(name="costServiceImpl")
    private ICostService costService;

    @GetMapping("/cost")
    public ModelAndView cost() {
        ModelAndView mv = new ModelAndView(PAGE_PREFIX + "cost");
        return mv;
    }

    @GetMapping("/fund/{code}")
    public ModelAndView fund(@PathVariable("code") String code) {
        ModelAndView mv = new ModelAndView(PAGE_PREFIX + "fund/" + code);
        return mv;
    }

    @GetMapping("/fund/{code}/data")
    @ResponseBody
    public Res<FundCostViewVO> getViewData(@PathVariable("code") String code) {
        Res<FundCostViewVO> vo = costService.getCostViewData(code);
        return vo;
    }

}
