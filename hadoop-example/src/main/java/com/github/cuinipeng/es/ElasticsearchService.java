package com.github.cuinipeng.es;

import org.elasticsearch.client.transport.TransportClient;

/**
 * @author cuinipeng@163.com
 * @date 2019/8/24 22:01
 * @description Elasticsearch 7.2.0
 */
public class ElasticsearchService {

    // es 集群
    private String clusterName = "es_cuinipeng";
    // es 索引
    private String indexName = "twitter";
    // es 类型
    private String typeName = "_doc";
    // es 连接
    TransportClient client = null;

    public ElasticsearchService() {

    }

    public TransportClient getClient() {
        return null;
    }

    public void run() {

    }

}
