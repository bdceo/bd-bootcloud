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
 * 证监会行业指数
 */
@Slf4j
public class CsrsIndustryExcelTest {

    public static void main(String[] args) throws Exception {
        File file = new File("e:/download/csrc-industry.xls");
        ExcelReaderBuilder builder = ExcelReaderBuilder.of(new FileInputStream(file));
        CellMappings<Integer> mappings = CellMappings.newIndexMapping()
                .addMapping(0, "first")
                .addMapping(1, "second")
                .addMapping(2, "third");
        builder.setMapping(mappings).setStartRow(1);

        List<ExcelItem> data = builder.out(ExcelItem.class);

        List<IndustryModel> modelList = new ArrayList<>(data.size() * 2);
        String first = null;
        for (ExcelItem item : data) {
            log.info("行业：{}", JSONUtil.json(item));
            if (StringUtils.isNotBlank(item.getFirst()) && StringUtils.isNotBlank(item.getSecond())) {
                first = item.getFirst();
                IndustryModel model = new IndustryModel(1, item.getFirst(), item.getSecond());
                modelList.add(model);
                log.info("一级：{}", JSONUtil.json(model));
            }
            if (StringUtils.isNotBlank(item.getSecond()) && StringUtils.isNotBlank(item.getThird())) {
                IndustryModel model = new IndustryModel(2, item.getSecond(), item.getThird(), first);
                modelList.add(model);
                log.info("二级：{}", JSONUtil.json(model));
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
         * 行业编号、名称
         */
        private String code;
        private String name;

        /**
         * 对应上级
         */
        private String code1;

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
    }

    @Data
    public static class ExcelItem {

        /**
         * 门类、大类、中类
         */
        private String first;
        private String second;
        private String third;
    }

}
