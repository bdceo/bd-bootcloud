package com.bdsoft.excel;

import com.hshc.basetools.json.JSONUtil;
import com.hshc.conform.poi.reader.CellMappings;
import com.hshc.conform.poi.reader.ExcelReaderBuilder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * 中证行业指数
 */
@Slf4j
public class CsIndustryExcelTest {

    public static void main(String[] args) throws Exception {
        File file = new File("e:/download/cs-industry.xlsx");
        ExcelReaderBuilder builder = ExcelReaderBuilder.of(new FileInputStream(file));
        CellMappings<Integer> mappings = CellMappings.newIndexMapping()
                .addMapping(0, "code1")
                .addMapping(1, "name1")
                .addMapping(2, "code2")
                .addMapping(3, "name2")
                .addMapping(4, "code3")
                .addMapping(5, "name3")
                .addMapping(6, "code4")
                .addMapping(7, "name4")
                .addMapping(8, "remark");
        builder.setMapping(mappings).setStartRow(1);

        List<CsIndustry> data = builder.out(CsIndustry.class);

        List<IndustryModel> modelList = new ArrayList<>(data.size() * 3);
        for (CsIndustry item : data) {
            log.info("行业：{}", JSONUtil.json(item));
            if (StringUtils.isNotBlank(item.getCode1()) && StringUtils.isNotBlank(item.getName1())) {
                IndustryModel model = new IndustryModel(1, item.getCode1(), item.getName1());
                modelList.add(model);
                log.info("一级：{}", JSONUtil.json(model));
            }
            if (StringUtils.isNotBlank(item.getCode2()) && StringUtils.isNotBlank(item.getName2())) {
                String code1 = item.getCode2().substring(0, 2);
                IndustryModel model = new IndustryModel(2, item.getCode2(), item.getName2(), code1);
                modelList.add(model);
                log.info("二级：{}", JSONUtil.json(model));
            }
            if (StringUtils.isNotBlank(item.getCode3()) && StringUtils.isNotBlank(item.getName3())) {
                String code1 = item.getCode3().substring(0, 2);
                String code2 = item.getCode3().substring(0, 4);
                IndustryModel model = new IndustryModel(3, item.getCode3(), item.getName3(), code1, code2);
                modelList.add(model);
                log.info("三级：{}", JSONUtil.json(model));
            }
            if (StringUtils.isNotBlank(item.getCode4()) && StringUtils.isNotBlank(item.getName4())) {
                String code1 = item.getCode4().substring(0, 2);
                String code2 = item.getCode4().substring(0, 4);
                String code3 = item.getCode4().substring(0, 6);
                IndustryModel model = new IndustryModel(4, item.getCode4(), item.getName4(), item.getRemark(), code1, code2, code3);
                modelList.add(model);
                log.info("四级：{}", JSONUtil.json(model));
            }
        }
    }

    @Data
    public static class IndustryModel {
        /**
         * 级别
         */
        private int level;

        /**
         * 行业编号、名称、描述
         */
        private String code;
        private String name;
        private String remark;

        /**
         * 对应上级
         */
        private String code1;
        private String code2;
        private String code3;

        public IndustryModel(int level, String code, String name) {
            this.level = level;
            this.code = code;
            this.name = name;
        }

        public IndustryModel(int level, String code, String name, String code1) {
            this.level = level;
            this.code = code;
            this.name = name;
            this.code1 = code1;
        }

        public IndustryModel(int level, String code, String name, String code1, String code2) {
            this.level = level;
            this.code = code;
            this.name = name;
            this.code1 = code1;
            this.code2 = code2;
        }

        public IndustryModel(int level, String code, String name, String remark, String code1, String code2, String code3) {
            this.level = level;
            this.code = code;
            this.name = name;
            this.remark = remark;
            this.code1 = code1;
            this.code2 = code2;
            this.code3 = code3;
        }
    }

    @Data
    public static class CsIndustry {

        /**
         * 一级行业
         */
        private String code1;
        private String name1;

        /**
         * 二级行业
         */
        private String code2;
        private String name2;

        /**
         * 三级行业
         */
        private String code3;
        private String name3;

        /**
         * 四级行业
         */
        private String code4;
        private String name4;

        /**
         * 释义
         */
        private String remark;

    }
}
