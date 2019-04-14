package my.base.db;

import java.util.HashMap;

public class POJO {
    protected String[] allColumns = new String[0];
    protected HashMap<String, String> columnMap = new HashMap();
    protected String groupBy = "";
    protected String having = "";
    protected String limit = "";
    protected String orderBy = "";
    protected String rowId = "";
    protected String[] selectColumns = new String[0];
    protected String selection = "";
    protected String[] selectionArgs = new String[0];
    protected String tableCreateSql = "";
    protected String tableName = "";

    public String getTableName() {
        return this.tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getTableCreateSql() {
        return this.tableCreateSql;
    }

    public void setTableCreateSql(String tableCreateSql) {
        this.tableCreateSql = tableCreateSql;
    }

    public String getRowId() {
        return this.rowId;
    }

    public void setRowId(String rowId) {
        this.rowId = rowId;
    }

    public String[] getAllColumns() {
        return this.allColumns;
    }

    public void setAllColumns(String[] allColumns) {
        this.allColumns = allColumns;
    }

    public String[] getSelectColumns() {
        return this.selectColumns;
    }

    public void setSelectColumns(String[] selectColumns) {
        this.selectColumns = selectColumns;
    }

    public String getColumnValue(String columnName) {
        return (String) this.columnMap.get(columnName);
    }

    public String getSelection() {
        return this.selection;
    }

    public void setSelection(String selection) {
        this.selection = selection;
    }

    public String[] getSelectionArgs() {
        return this.selectionArgs;
    }

    public void setSelectionArgs(String[] selectionArgs) {
        this.selectionArgs = selectionArgs;
    }

    public String getGroupBy() {
        return this.groupBy;
    }

    public void setGroupBy(String groupBy) {
        this.groupBy = groupBy;
    }

    public String getHaving() {
        return this.having;
    }

    public void setHaving(String having) {
        this.having = having;
    }

    public String getOrderBy() {
        return this.orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    public String getLimit() {
        return this.limit;
    }

    public void setLimit(String limit) {
        this.limit = limit;
    }
}
