package com.github.cuinipeng.hbase;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.ColumnFamilyDescriptor;
import org.apache.hadoop.hbase.client.ColumnFamilyDescriptorBuilder;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.client.TableDescriptor;
import org.apache.hadoop.hbase.client.TableDescriptorBuilder;
import org.apache.hadoop.hbase.shaded.org.apache.commons.lang3.StringUtils;
import org.apache.hadoop.hbase.util.Bytes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Description: HBase 基本操作
 * @Author: cuinipeng@163.com
 * @Date: 2019/7/28 20:48
 * @Version: HBase 2.2.0
 */
public class HBaseService {

    private Logger logger = LoggerFactory.getLogger(HBaseService.class);

    /**
     * 声明静态配置
     */
    private Configuration conf = null;
    private Connection connection = null;

    public HBaseService() {
        // Configuration conf = HBaseConfiguration.create();
        // conf.set("hbase.zookeeper.quorum", "192.168.100.135");
        // conf.set("hbase.client.keyvalue.maxsize", "10485760");
        this(HBaseConfiguration.create());
    }

    public HBaseService(Configuration conf) {
        this.conf = conf;
        try {
            connection = ConnectionFactory.createConnection(conf);
        } catch (IOException e) {
            logger.error("Get hbase connection failed.");
            e.printStackTrace();
        }
    }

    /**
     * @param tableName 表名
     * @param columnFamily 列族名
     * @return boolean 创建是否成功
     * @Description: 创建表
     */
    public boolean createTable(String tableName, List<String> columnFamily) {
        return createTable(tableName, columnFamily);
    }

    /**
     * @param tableName 表名
     * @param columnFamily 列族名
     * @param splitKeys 预分期region
     * @return boolean 创建是否成功
     * @Description: 创建表
     */
    public boolean createTable(String tableName, List<String> columnFamily, byte[][] splitKeys) {
        // 参数校验
        if (tableName == null || StringUtils.isBlank(tableName) ||
            columnFamily == null || columnFamily.size() == 0) {
            logger.error("Parameters tableName|columnFamily should not be null");
            return false;
        }
        Admin admin = null;
        try {
            admin = connection.getAdmin();
            // 设置列族
            List<ColumnFamilyDescriptor> familyDescriptors = new ArrayList<>(columnFamily.size());
            columnFamily.forEach(cf -> {
                familyDescriptors.add(
                    ColumnFamilyDescriptorBuilder.newBuilder(Bytes.toBytes(cf)).build()
                );
            });
            // 设置表描述信息
            TableDescriptor tableDescriptor = TableDescriptorBuilder.newBuilder(
                TableName.valueOf(tableName)).setColumnFamilies(familyDescriptors).build();

            if (admin.tableExists(TableName.valueOf(tableName))) {
                // 表存在
                logger.debug(MessageFormat.format("Table {0} exist already.", tableName));
            } else {
                // 创建表
                if (splitKeys != null) {
                    // 带预分区创建表
                    admin.createTable(tableDescriptor, splitKeys);
                } else {
                    admin.createTable(tableDescriptor);
                }
                logger.info(
                    MessageFormat.format("Create table {0} with columnFamily {1} successfully.",
                        tableName, columnFamily.toString()));
            }
        } catch (IOException e) {
            logger.error(MessageFormat.format("Create table {0} failed.", tableName), e);
            e.printStackTrace();
        } finally {
            close(admin, null, null);
        }
        return true;
    }

    /**
     * @Description: 通用方法关闭流
     */
    private void close(Admin admin, ResultScanner rs, Table table) {
        if (admin != null) {
            try {
                admin.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (rs != null) {
            rs.close();
        }

        if (table != null) {
            try {
                table.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
