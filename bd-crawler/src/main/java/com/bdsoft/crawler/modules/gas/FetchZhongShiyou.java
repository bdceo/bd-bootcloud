package com.bdsoft.crawler.modules.gas;

import com.bdsoft.crawler.modules.gas.entity.StationDb;
import com.bdsoft.crawler.modules.gas.po.GasStation;
import com.bdsoft.crawler.modules.gas.mapper.StationDbMapper;
import com.hshc.basetools.json.JSONUtil;
import com.hshc.conform.poi.reader.CellMappings;
import com.hshc.conform.poi.reader.ExcelReaderBuilder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 中石油
 */
@Slf4j
@Component
public class FetchZhongShiyou {

    @Autowired
    private StationDbMapper stationDbMapper;

    public void fetch() throws Exception {
        Date now = new Date();
        File file = ResourceUtils.getFile(ResourceUtils.CLASSPATH_URL_PREFIX + "data");
        File[] excels = file.listFiles((dir, name) -> name.endsWith("xlsx"));

        // 特殊省份数据
        List<String> specialProvince = new ArrayList<>(3);
        specialProvince.add("350000");
        specialProvince.add("610000");
        specialProvince.add("650000");

        int total = excels.length;
        for (int i = 0; i < total; i = i + 2) {
            List<GasStation> stations1 = this.parseExcel(excels[i]);
            List<GasStation> stations2 = this.parseExcel(excels[i + 1]);

            // 去重
            List<String> nameList = stations1.stream().map(GasStation::getName).collect(Collectors.toList());
            stations2 = stations2.stream().filter(s -> !nameList.contains(s.getName())).collect(Collectors.toList());
            stations1.addAll(stations2);

            // 提取省编号
            String provinceCode = excels[i].getName();
            provinceCode = provinceCode.split("-")[0];

            // 过滤特殊省份
            if (specialProvince.contains(provinceCode)) {
                File txt = ResourceUtils.getFile(ResourceUtils.CLASSPATH_URL_PREFIX + "data/" + provinceCode + ".txt");
                List<GasStation> stations3 = this.parseTxt(txt);
                // 去重
                List<String> nameList2 = stations1.stream().map(GasStation::getName).collect(Collectors.toList());
                stations3 = stations3.stream().filter(s -> !nameList2.contains(s.getName())).collect(Collectors.toList());
                stations1.addAll(stations3);
            }
            log.info("province={}, size={}", provinceCode, stations1.size());

            // 入库
            for (GasStation station : stations1) {
                StationDb tmp = new StationDb();
                BeanUtils.copyProperties(station, tmp);
                tmp.setType(2); // 中石油
                tmp.setProvinceCode(provinceCode);
                tmp.setSource("https://www.95504.net/");
                tmp.setCreateTime(now);

                try {
                    stationDbMapper.insert(tmp);
                } catch (Exception e) {
                    log.error("error data={}", JSONUtil.json(tmp));
                }
            }
        }
    }

    /**
     * 解析加油站
     *
     * @param excel 源文件
     * @return
     */
    public List<GasStation> parseExcel(File excel) throws Exception {
        ExcelReaderBuilder builder = ExcelReaderBuilder.of(new FileInputStream(excel));
        CellMappings<Integer> mappings = CellMappings.newIndexMapping()
                .addMapping(0, "name")
                .addMapping(1, "address")
                .addMapping(2, "phone");
        builder.setMapping(mappings);
        List<GasStation> data = builder.out(GasStation.class);
        return data;
    }

    /**
     * 解析加油站
     *
     * @param txt 源文件
     * @return
     */
    public List<GasStation> parseTxt(File txt) throws Exception {
        List<GasStation> data = new ArrayList<>();
        List<String> lines = FileUtils.readLines(txt);
        for (String line : lines) {
            GasStation station = new GasStation();
            String[] cells = line.split("\t");
            station.setName(cells[0]);
            if (cells.length > 1) {
                station.setAddress(cells[1]);
            }
            if (cells.length > 2) {
                station.setPhone(cells[2]);
            }
            data.add(station);
        }
        return data;
    }

}
