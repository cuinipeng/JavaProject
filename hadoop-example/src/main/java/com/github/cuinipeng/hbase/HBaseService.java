package com.github.cuinipeng.hbase;

import java.io.IOException;
import java.math.BigInteger;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import java.util.regex.Pattern;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CompareOperator;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.ColumnFamilyDescriptor;
import org.apache.hadoop.hbase.client.ColumnFamilyDescriptorBuilder;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.Delete;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.client.TableDescriptor;
import org.apache.hadoop.hbase.client.TableDescriptorBuilder;
import org.apache.hadoop.hbase.filter.ColumnPrefixFilter;
import org.apache.hadoop.hbase.filter.Filter;
import org.apache.hadoop.hbase.filter.PrefixFilter;
import org.apache.hadoop.hbase.filter.QualifierFilter;
import org.apache.hadoop.hbase.filter.RowFilter;
import org.apache.hadoop.hbase.filter.SubstringComparator;
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
     * @description: 创建表
     */
    public boolean createTable(String tableName, List<String> columnFamily) {
        return createTable(tableName, columnFamily, null);
    }

    /**
     * @param tableName 表名
     * @param columnFamily 列族名
     * @param splitKeys 预分期region
     * @return boolean 创建是否成功
     * @description: 预分区创建表
     */
    public boolean createTable(String tableName, List<String> columnFamily, byte[][] splitKeys) {
        // 参数校验
        if (tableName == null || StringUtils.isBlank(tableName) ||
            columnFamily == null || columnFamily.size() == 0) {
            logger.error("Parameters tableName|columnFamily should not be null.");
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
            e.printStackTrace();
        } finally {
            close(admin, null, null);
        }
        return true;
    }

    private void displaySplitKeys(byte[][] splitKeys) {
        List<String> result = new ArrayList<>();
        for (byte[] splitKey : splitKeys) {
            // 非线程安全
            StringBuilder builder = new StringBuilder();
            for (byte ch : splitKey) {
                builder.append(String.format("\\x%x", ch));
            }
            result.add(builder.toString());
        }
        logger.debug(MessageFormat.format("SplitKeys: {0}", result.toString()));
    }

    /**
     * @param keys 字符串表示的分区键
     * @return byte[][] 字节数组表示的分区键
     * @description 获取自定义分区键
     */
    public byte[][] getSplitKeys(String[] keys) {
        if (keys == null) {
            /**
             * 默认10个分区
             *
             * 为什么后面会跟着一个"|", 是因为在ASCII码中, "|"的值是124,
             * 大于所有的数字和字母等符号, 当然也可以用“~”(ASCII-126).
             * 分隔文件的第一行为第一个region的stopkey, 每行依次类推,
             * 最后一行不仅是倒数第二个region的stopkey, 同时也是最后一个
             * region的startkey. 也就是说分区文件中填的都是key取值范围的分隔点.
             */
            keys = new String[]{"1|", "2|", "3|", "4|",
                "5|", "6|", "7|", "8|", "9|"};
        }
        // 多维数组,留最后一维为空
        byte[][] splitKeys = new byte[keys.length][];
        // 升序排序
        TreeSet<byte[]> rows = new TreeSet<>(Bytes.BYTES_COMPARATOR);
        for (String key : keys) {
            rows.add(Bytes.toBytes(key));
        }
        Iterator<byte[]> rowKeyIter = rows.iterator();
        int i = 0;
        while (rowKeyIter.hasNext()) {
            byte[] tmpRow = rowKeyIter.next();
            splitKeys[i] = tmpRow;
            i++;
        }

        displaySplitKeys(splitKeys);
        return splitKeys;
    }

    /**
     * @param startKey 起始分区键
     * @param stopKey 结束分区键
     * @param numRegions 分区个数
     * @return byte[][] 字节数组表示的分区键
     * @description 获取自定义分区键
     */
    public byte[][] getSplitKeys(String startKey, String stopKey, int numRegions) {
        byte[][] splitKeys = new byte[numRegions - 1][];
        // 字符串转整数
        BigInteger lowestKey = new BigInteger(startKey, 16);
        BigInteger higestKey = new BigInteger(stopKey, 16);
        // 计算分区key的范围
        BigInteger range = higestKey.subtract(lowestKey);
        // 计算分区直接的间隔
        BigInteger regionIncrement = range.divide(BigInteger.valueOf(numRegions));
        lowestKey = lowestKey.add(regionIncrement);
        for (int i = 0; i < numRegions - 1; i++) {
            BigInteger key = lowestKey.add(regionIncrement.multiply(BigInteger.valueOf(i)));
            // 16 位十六进制(八个字节)
            byte[] b = String.format("%016x", key).getBytes();
            splitKeys[i] = b;
        }

        displaySplitKeys(splitKeys);
        return splitKeys;
    }

    /**
     * @return List<String>
     * @description: 查询所有HBase表
     */
    public List<String> getAllTableNames() {
        return getTableNames(".*");
    }

    /**
     * @param regex 过滤表名
     * @return List<String>
     * @description 查询指定表
     */
    public List<String> getTableNames(String regex) {
        if (regex == null || StringUtils.isBlank(regex)) {
            logger.error("Parameters regex should not be null.");
            return null;
        }
        List<String> tables = new ArrayList<>();

        Admin admin = null;
        try {
            admin = connection.getAdmin();
            // 正则表达式匹配表名
            Pattern pattern = Pattern.compile(regex);
            TableName[] tableNames = admin.listTableNames(pattern);
            for (TableName tableName : tableNames) {
                tables.add(tableName.getNameAsString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            close(admin, null, null);
        }
        return tables;
    }

    /**
     * @param tableName 表名
     * @return org.apache.hadoop.hbase.client.Table
     * @description 根据表名, 返回 Table 对象
     * @note 私有方法
     */
    private Table getTable(String tableName) throws IOException {
        return connection.getTable(TableName.valueOf(tableName));
    }

    /**
     * @param tableName 表名
     * @param rowKey 主键
     * @param columnFamily 列族
     * @param columns 列名列表
     * @param values 值列表
     * @return 插入成功与否
     * @description 插入列族数据(多列)
     */
    public boolean putData(String tableName, String rowKey,
        String columnFamily, String[] columns, String[] values) {

        if (columns == null || values == null || columns.length != values.length) {
            logger.error("Parameter columns|values is invalid.");
            return false;
        }

        Table table = null;

        try {
            table = getTable(tableName);

            Put put = new Put(Bytes.toBytes(rowKey));
            for (int i = 0; i < columns.length; i++) {
                if (columns[i] != null && values[i] != null) {
                    put.addColumn(
                        Bytes.toBytes(columnFamily),
                        Bytes.toBytes(columns[i]),
                        Bytes.toBytes(values[i]));
                } else {
                    throw new NullPointerException(MessageFormat.format(
                        "column and value can't be null, column: {0}, value: {1}",
                        columns[i], values[i]));
                }
            }

            // 写入数据
            table.put(put);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            close(null, null, table);
        }
        return true;
    }

    /**
     * @param tableName 表名
     * @param rowKey 主键
     * @param columnFamily 列族
     * @param column 列名
     * @param value 值
     * @return 插入成功与否
     * @description 插入列族数据(一列)
     */
    public boolean putData(String tableName, String rowKey,
        String columnFamily, String column, String value) {
        Table table = null;

        try {
            table = getTable(tableName);

            Put put = new Put(Bytes.toBytes(rowKey));
            put.addColumn(Bytes.toBytes(columnFamily),
                Bytes.toBytes(column),
                Bytes.toBytes(value));
            // 写入数据
            table.put(put);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            close(null, null, table);
        }
        return true;
    }

    // getResultScanner
    // getResultScanner
    // getResultScannerPrefixFilter
    // getResultScannerColumnPrefixFilter
    // getResultScannerRowFilter
    // getResultScannerQualifierFilter
    // queryData
    // getRowData
    // getColumnValue
    // getColumnValuesByVersion

    /**
     * @param tableName 表名
     * @param rowKey 主键
     * @return java.util.Map<String, String> 一行数据
     * @description 按 rowKey 返回一行数据
     */
    public Map<String, String> getRow(String tableName, String rowKey) {
        // 结果键值对
        Map<String, String> result = new HashMap<>();
        Table table = null;
        Get get = null;
        Result hTableResult = null;

        try {
            table = getTable(tableName);
            get = new Get(Bytes.toBytes(rowKey));
            // 查询数据
            hTableResult = table.get(get);

            if (hTableResult != null && !hTableResult.isEmpty()) {
                // 遍历数据
                for (Cell cell : hTableResult.listCells()) {
                    // 组装数据
                    String family = Bytes.toString(
                        cell.getFamilyArray(),
                        cell.getFamilyOffset(),
                        cell.getFamilyLength()
                    );
                    String qualifier = Bytes.toString(
                        cell.getQualifierArray(),
                        cell.getQualifierOffset(),
                        cell.getQualifierLength()
                    );
                    String value = Bytes.toString(
                        cell.getValueArray(),
                        cell.getValueOffset(),
                        cell.getValueLength()
                    );
                    long timestamp = cell.getTimestamp();
                    result.put(qualifier, value);
                    logger.debug("-------------------------------------------");
                    logger.debug("Family: " + family);
                    logger.debug("qualifier: " + qualifier);
                    logger.debug("Value: " + value);
                    logger.debug("Timestamp: " + timestamp);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            close(null, null, table);
        }
        return result;
    }

    /**
     * @param tableName 表名
     * @param rowKey 主键
     * @param column 列名
     * @return String 值
     * @description 按 rowKey:columnFamily:column 返回对应的值
     */
    public String getColumn(String tableName, String rowKey,
        String columnFamily, String column) {
        // 结果键值对
        String result = null;
        Table table = null;
        Get get = null;
        Result hTableResult = null;

        try {
            table = getTable(tableName);
            get = new Get(Bytes.toBytes(rowKey));
            // 查询数据
            hTableResult = table.get(get);

            if (hTableResult != null && !hTableResult.isEmpty()) {
                // 过滤数据
                Cell cell = hTableResult.getColumnLatestCell(
                    Bytes.toBytes(columnFamily), Bytes.toBytes(column)
                );
                if (cell != null) {
                    // 组装数据
                    // 列族
                    String family = Bytes.toString(
                        cell.getFamilyArray(),
                        cell.getFamilyOffset(),
                        cell.getFamilyLength()
                    );
                    // 列
                    String qualifier = Bytes.toString(
                        cell.getQualifierArray(),
                        cell.getQualifierOffset(),
                        cell.getQualifierLength()
                    );
                    // 值
                    String value = Bytes.toString(
                        cell.getValueArray(),
                        cell.getValueOffset(),
                        cell.getValueLength()
                    );
                    long timestamp = cell.getTimestamp();
                    logger.debug("-------------------------------------------");
                    logger.debug("Family: " + family);
                    logger.debug("qualifier: " + qualifier);
                    logger.debug("Value: " + value);
                    logger.debug("Timestamp: " + timestamp);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            close(null, null, table);
        }
        return result;
    }

    /**
     * @param tableName 表名
     * @param scan 过滤条件
     * @return Map<String, Map<String, String>>
     * @description 根据过滤条件返回多行以 rowKey 为键的 Map
     */
    private Map<String, Map<String, String>> query(String tableName, Scan scan) {
        // <rowKey, 对应的行数据>
        Map<String, Map<String, String>> result = new HashMap<>();
        ResultScanner rs = null;
        Table table = null;

        try {
            table = getTable(tableName);
            rs = table.getScanner(scan);
            for (Result r : rs) {
                // 每一行数据
                Map<String, String> row = new HashMap<>();
                // 主键
                String rowKey = null;
                // 列族
                String family = null;
                // 列
                String qualifier = null;
                // 值
                String value = null;
                // 最新时间戳
                long timestamp = 0;
                for (Cell cell : r.listCells()) {
                    if (rowKey == null) {
                        // 主键
                        rowKey = Bytes.toString(
                            cell.getRowArray(),
                            cell.getRowOffset(),
                            cell.getRowLength()
                        );
                    }
                    family = Bytes.toString(
                        cell.getFamilyArray(),
                        cell.getFamilyOffset(),
                        cell.getFamilyLength()
                    );
                    qualifier = Bytes.toString(
                        cell.getQualifierArray(),
                        cell.getQualifierOffset(),
                        cell.getQualifierLength()
                    );
                    value = Bytes.toString(
                        cell.getValueArray(),
                        cell.getValueOffset(),
                        cell.getValueLength()
                    );
                    timestamp = cell.getTimestamp();
                    row.put(qualifier, value);
                }

                if (rowKey != null) {
                    // 把每一行数据放到结果集中
                    result.put(rowKey, row);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            close(null, rs, table);
        }
        return result;
    }

    /**
     * @param tableName 表名
     * @return Map<String, Map<String, String>>
     * @description 查询表中所有数据(多行)
     */
    public Map<String, Map<String, String>> queryAllData(String tableName) {
        Scan scan = new Scan();
        return query(tableName, scan);
    }

    /**
     * @param tableName 表名
     * @param startRowKey 起始 rowKey
     * @param stopRowdKey 结束 rowKey
     * @return Map<String, Map<String, String>>
     * @description 根据startRowKey和stopRowKey遍历查询指定表中的所有数据(多行)
     */
    public Map<String, Map<String, String>> queryByRowKeyRange(String tableName,
        String startRowKey, String stopRowdKey) {
        Scan scan = new Scan();
        // 参数校验
        if (StringUtils.isNoneBlank(startRowKey) && StringUtils.isNoneBlank(stopRowdKey)) {
            scan.withStartRow(Bytes.toBytes(startRowKey));
            scan.withStopRow(Bytes.toBytes(stopRowdKey));
        }
        return query(tableName, scan);
    }

    /**
     * @param tableName 表名
     * @param prefix 以prefix开始的rowKey
     * @return Map<String, Map<String, String>>
     * @description 通过rowKey前缀过滤器查询数据(多行)
     */
    public Map<String, Map<String, String>> queryByRowKeyPrefix(String tableName, String prefix) {
        Scan scan = new Scan();
        // 参数校验
        if (StringUtils.isNoneBlank(prefix)) {
            Filter filter = new PrefixFilter(Bytes.toBytes(prefix));
            scan.setFilter(filter);
        }
        return query(tableName, scan);
    }

    /**
     * @param tableName 表名
     * @param prefix 以prefix开始的列名
     * @return Map<String, Map<String, String>>
     * @description 通过列前缀过滤器查询数据(多行)
     */
    public Map<String, Map<String, String>> queryByColumnPrefix(String tableName, String prefix) {
        Scan scan = new Scan();
        // 参数校验
        if (StringUtils.isNoneBlank(prefix)) {
            Filter filter = new ColumnPrefixFilter(Bytes.toBytes(prefix));
            scan.setFilter(filter);
        }
        return query(tableName, scan);
    }

    /**
     * @param tableName 表名
     * @param keyword 包含keyword的行键
     * @return Map<String, Map<String, String>>
     * @description 查询行键中包含特定字符的数据(多行)
     */
    public Map<String, Map<String, String>> queryByRowKeyFilter(String tableName, String keyword) {
        Scan scan = new Scan();
        // 参数校验
        if (StringUtils.isNoneBlank(keyword)) {
            Filter filter = new RowFilter(
                CompareOperator.GREATER_OR_EQUAL, new SubstringComparator(keyword));
            scan.setFilter(filter);
        }
        return query(tableName, scan);
    }

    /**
     * @param tableName 表名
     * @param keyword 包含keyword的列名
     * @return Map<String, Map<String, String>>
     * @description 查询列名中包含特定字符的数据(多行)
     */
    public Map<String, Map<String, String>> queryByColumnFilter(String tableName, String keyword) {
        Scan scan = new Scan();
        // 参数校验
        if (StringUtils.isNoneBlank(keyword)) {
            Filter filter = new QualifierFilter(
                CompareOperator.GREATER_OR_EQUAL,new SubstringComparator(keyword));
            scan.setFilter(filter);
        }
        return query(tableName, scan);
    }

    /**
     * @param tableName 表名
     * @return boolean 删除是否成功
     * @description 删除指定表
     */
    public boolean deleteTable(String tableName) {
        // 参数校验
        if (tableName == null || StringUtils.isBlank(tableName)) {
            logger.error("Parameters tableName should not be null.");
            return false;
        }

        Admin admin = null;
        try {
            admin = connection.getAdmin();
            TableName tn = TableName.valueOf(tableName);
            if (admin.tableExists(tn)) {
                // 先 disable 再 drop
                admin.disableTable(tn);
                admin.deleteTable(tn);
            } else {
                // 表不存在, 也返回 true
                logger.warn(MessageFormat.format("Table {0} is missing.", tableName));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            close(admin, null, null);
        }

        return true;
    }

    /**
     * @param tableName 表名
     * @param rowKey 主键
     * @return boolean 是否删除成功
     * @description 根据主键删除指定行数据
     */
    public boolean deleteRow(String tableName, String rowKey) {
        Table table = null;

        try {
            table = getTable(tableName);
            // 删除指定 rowkey
            Delete delete = new Delete(Bytes.toBytes(rowKey));
            table.delete(delete);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            close(null, null, table);
        }
        return true;
    }

    /**
     * @param tableName 表名
     * @param columnFamily 列族名
     * @return 是否删除成功
     * @description 删除指定表的列族
     */
    public boolean deleteColumnFamily(String tableName, List<String> columnFamily) {
        // 参数校验
        if (tableName == null || StringUtils.isBlank(tableName) ||
            columnFamily == null || columnFamily.size() == 0) {
            logger.error("Parameters tableName|columnFamily should not be null.");
            return false;
        }

        Admin admin = null;
        try {
            admin = connection.getAdmin();

            if (admin.tableExists(TableName.valueOf(tableName))) {
                for (String cf : columnFamily) {
                    // 删除列族
                    admin.deleteColumnFamily(TableName.valueOf(tableName), Bytes.toBytes(cf));
                }
                ;
            } else {
                // 表不存在, 也返回 true
                logger.warn(MessageFormat.format("Table {0} is missing.", tableName));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            close(admin, null, null);
        }
        return true;
    }

    /**
     * @param tableName 表名
     * @param rowKey 主键
     * @param familyName 列族
     * @param columnNames 列名列表
     * @return boolean 删除是否成功
     * @description 删除指定列
     */
    public boolean deleteColumn(String tableName, String rowKey,
        String familyName, List<String> columnNames) {
        Table table = null;

        try {
            table = getTable(tableName);

            Delete delete = new Delete(Bytes.toBytes(rowKey));
            // 设置删除的列
            for (String columnName : columnNames) {
                delete.addColumns(Bytes.toBytes(familyName), Bytes.toBytes(columnName));
            }
            table.delete(delete);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            close(null, null, table);
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

    public void run() {
        String tableName = "test";
        String rowKey = "row-1";
        List<String> columnFamily = new ArrayList<>(Arrays.asList("cf-1", "cf-2"));
        String[] columns_1 = new String[]{"key-11", "key-12", "key-13"};
        String[] values_1 = new String[]{"value-11", "value-12", "value-13"};
        String[] columns_2 = new String[]{"key-21", "key-22", "key-23"};
        String[] values_2 = new String[]{"value-21", "value-22", "value-23"};

        boolean success = false;

        // 删除表
        success = deleteTable(tableName);
        if (success != true) {
            logger.info(MessageFormat.format("Delete {0} successfully.", tableName));
        }

        // 创建表
        byte[][] splitKeys = getSplitKeys(null);    // 默认10个分区
        success = createTable(tableName, columnFamily, splitKeys);
        if (success != true) {
            logger.info(MessageFormat.format("Create {0} successfully.", tableName));
        }

        // 第一个列族
        success = putData(tableName, rowKey, columnFamily.get(0), columns_1, values_1);
        if (success != true) {
            logger.info(MessageFormat.format("Put {0} with {1} successfully.", tableName, rowKey));
        }
        // 第二个列族
        success = putData(tableName, rowKey, columnFamily.get(1), columns_2, values_2);
        if (success != true) {
            logger.info(MessageFormat.format("Put {0} with {1} successfully.", tableName, rowKey));
        }

        // 根据rowKey获取一行数据数据
        Map<String, String> r1 = getRow(tableName, rowKey);

        // 根据rowKey:columnFamily:Column获取一列数据
        String r2 = getColumn(tableName, rowKey, columnFamily.get(0), columns_1[0]);

        // 查询指定表的全部数据
        Map<String, Map<String, String>> r3 = queryAllData(tableName);
        logger.debug("All: " + r3.toString());

        // {
        //    row-1={
        //        key-11=value-11, key-12=value-12, key-13=value-13
        //        key-21=value-21, key-22=value-22, key-23=value-23}
        // }
        String startRowKey = "A";
        String stopRowKey = "Z";
        Map<String, Map<String, String>> r4 = queryByRowKeyRange(tableName, startRowKey, stopRowKey);
        logger.debug(String.format("RowKey Start(%s) -> Stop(%s) - %s",
            startRowKey, stopRowKey, r4.toString()));

        String rowKeyPrefix = "row-";
        Map<String, Map<String, String>> r5 = queryByRowKeyPrefix(tableName, rowKeyPrefix);
        logger.debug(String.format("RowKey Prefix(%s) - %s", rowKeyPrefix, r5.toString()));

        String columnPrefix = "key-2";
        Map<String, Map<String, String>> r6 = queryByColumnPrefix(tableName, columnPrefix);
        logger.debug(String.format("Column Prefix(%s) - %s", columnPrefix, r6.toString()));

        String rowKeyKeyword = "ow-";
        Map<String, Map<String, String>> r7 = queryByRowKeyFilter(tableName, rowKeyKeyword);
        logger.debug(String.format("RowKey Keyword(%s) - %s", rowKeyKeyword, r7.toString()));

        String columnKeyWord = "not-found";
        Map<String, Map<String, String>> r8 = queryByColumnFilter(tableName, columnKeyWord);
        logger.debug(String.format("Column Keyword(%s) - %s", columnKeyWord, r8.toString()));
    }
}
