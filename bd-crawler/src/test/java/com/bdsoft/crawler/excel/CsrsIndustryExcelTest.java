package com.bdsoft.crawler.excel;

import com.bdsoft.crawler.common.CopyUtils;
import com.bdsoft.crawler.modules.index.entity.IndustryCsrc;
import com.bdsoft.crawler.modules.index.mapper.IndustryCsrcMapper;
import com.hshc.basetools.json.JSONUtil;
import com.hshc.conform.poi.reader.CellMappings;
import com.hshc.conform.poi.reader.ExcelReaderBuilder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 证监会行业分类
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class CsrsIndustryExcelTest {

    @Autowired
    private IndustryCsrcMapper industryCsrcMapper;

    /**
     * 解析证监会行业分类数据
     */
    @Test
    public void parseIndustry() throws Exception {
        File file = new File("E:\\download\\中证数据\\csrc-industry.xls");
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

        // 入库
        List<IndustryCsrc> indList = CopyUtils.copy(modelList, IndustryCsrc.class);
        Set<String> codeSet = new HashSet<>();
        for (IndustryCsrc item : indList) {
            if (!codeSet.contains(item.getCode())) {
                int rows = industryCsrcMapper.insert(item);
                log.info("insert {}", rows);
                codeSet.add(item.getCode());
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
