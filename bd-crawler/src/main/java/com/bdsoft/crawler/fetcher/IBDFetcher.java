package com.bdsoft.crawler.fetcher;

import org.jsoup.nodes.Document;

/**
 * 抓取器
 */
public interface IBDFetcher<E, Q> {


    /**
     *
     *
     * @param url 地址
     * @return 可入库的实体
     */
    E fetch(String url);

    Document parse(String url);

    E pickInfo(Document html);

    E saveDB(E entity);



}
