package com.bdsoft.crawler.modules.gas;

import com.bdsoft.crawler.common.Utils;
import com.bdsoft.crawler.modules.gas.entity.StationDb;
import com.bdsoft.crawler.modules.gas.po.GasStation;
import com.bdsoft.crawler.modules.gas.mapper.StationDbMapper;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 中石化
 */
@Slf4j
@Component
public class FetchZhongShihua {

    @Autowired
    private StationDbMapper stationDbMapper;

    /**
     * 抓取中石化官网-加油站列表
     */
    public void fetch() {
        Date now = new Date();

        // 加载省份列表
        List<String> provinceList = this.getProvinceList();

        // 遍历加载省份-加油站列表
        for (String provinceId : provinceList) {
            List<GasStation> stationList = this.getStationList(provinceId);

            for (GasStation station : stationList) {
                StationDb tmp = new StationDb();
                BeanUtils.copyProperties(station, tmp);
                tmp.setType(1); // 中石化
                tmp.setSource("https://www.sinopecsales.com/");
                tmp.setCreateTime(now);

                stationDbMapper.insert(tmp);
            }
        }
    }

    /**
     * 加载省加油站列表
     *
     * @param provinceId 省编号
     * @return
     */
    public List<GasStation> getStationList(String provinceId) {
        // 准备参数：总页数
        int pageSize = this.getStationPageSize(provinceId);
        int stationCharge = 2;
        log.info("provinceId={} pageSize={}", provinceId, pageSize);

        List<GasStation> stationList = new ArrayList<>(100);
        // 分页抓取
        for (int pageNo = 1; pageNo <= pageSize; pageNo++) {
            String url = MessageFormat.format(GasConfig.ZHSHH_STATIONS, provinceId, pageNo, stationCharge);
            log.info("load station url={}", url);
            HttpResponse<String> res = Unirest.post(url).asString();
            if (res.isSuccess()) {
                Document html = Jsoup.parse(res.getBody());
                // 页面第2个table的所有行，去除标题行
                Element ele = html.select("body table").get(1);
                Elements rows = ele.getElementsByTag("tr");
                rows.remove(0);
                for (Element row : rows) {
                    // 序号，名称，地址，售卡充值，电话，电子充值卡发票，增值税发票
                    Elements cells = row.getElementsByTag("td");

                    GasStation station = new GasStation();
                    station.setName(cells.get(1).text());
                    station.setAddress(cells.get(2).text());
                    station.setPhone(cells.get(4).text());
                    station.setProvinceCode(provinceId + "0000");
                    stationList.add(station);
                    log.info("站点：{}", station.toString());
                }
                try {
                    int waitMs = Math.max(1_000, GasConfig.RANDOM.nextInt(3_000));
                    log.info("thread sleep {}ms", waitMs);
                    Thread.sleep(waitMs);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                log.error("load station error, url={}", url);
            }
        }

        return stationList;
    }

    /**
     * 获取省加油站总页数
     *
     * @param provinceId 省编号
     * @return
     */
    public int getStationPageSize(String provinceId) {
        // 默认参数：页码，充值类型
        int pageNo = 1;
        int stationCharge = 2;
        String url = MessageFormat.format(GasConfig.ZHSHH_STATIONS, provinceId, pageNo, stationCharge);
        log.info("load total-page url={}", url);

        // 从第一页获取总页数
        HttpResponse<String> res = Unirest.post(url).asString();
        if (res.isSuccess()) {
            Document html = Jsoup.parse(res.getBody());
            Element ele = html.select("body a").last();
            // 西藏，默认返回1页
            if (ele == null && provinceId.equals("54")) {
                log.info("没发现尾页链接，默认返回1页");
                return 1;
            }
            return Utils.getNumber(ele.attr("onclick"));
        }

        return 0;
    }

    /**
     * 加载省份列表
     */
    public List<String> getProvinceList() {
        List<String> provinceList = new ArrayList<>();
        // 加载省份列表，指定字符集
        Unirest.config().setDefaultResponseEncoding(GasConfig.ZHSHH_CHARSET);
        HttpResponse<String> res = Unirest.get(GasConfig.ZHSHH_PROVINCES).asString();
        if (res.isSuccess()) {
            log.info("fetch suc url={}", GasConfig.ZHSHH_PROVINCES);
            Document html = Jsoup.parse(res.getBody());

            Elements eles = html.select("li a");
            log.info("省份数量：{}", eles.size());
            for (Element ele : eles) {
                String provinceId = GasConfig.getZhshhProvinceId(ele.attr("href"));
                String provinceName = ele.text().trim().replaceAll(" ", "");
                log.info("省份: {}={}", provinceId, provinceName);
                provinceList.add(provinceId);
            }
        }

        return provinceList;
    }
}
