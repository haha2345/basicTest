package com.example.basictest.Class;

import java.io.Serializable;
import java.util.List;

public class bankListResponse implements Serializable {


    /**
     * total : 12
     * rows : [{"searchValue":null,"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"remark":null,"params":{},"id":1,"coName":"交通银行","fStatus":"1","timeC":"2020-06-09 09:30:52","urlVerify":null,"urlSync":null,"rpcToken":null},{"searchValue":null,"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"remark":null,"params":{},"id":2,"coName":"农业银行","fStatus":"1","timeC":"2020-06-09 09:31:03","urlVerify":null,"urlSync":null,"rpcToken":null},{"searchValue":null,"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"remark":null,"params":{},"id":3,"coName":"华夏银行","fStatus":"1","timeC":"2020-06-09 09:31:15","urlVerify":null,"urlSync":null,"rpcToken":null},{"searchValue":null,"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"remark":null,"params":{},"id":4,"coName":"工商银行","fStatus":"1","timeC":"2020-06-09 09:31:23","urlVerify":null,"urlSync":null,"rpcToken":null},{"searchValue":null,"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"remark":null,"params":{},"id":5,"coName":"青岛银行","fStatus":"1","timeC":"2020-06-09 09:31:28","urlVerify":null,"urlSync":null,"rpcToken":null},{"searchValue":null,"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"remark":null,"params":{},"id":6,"coName":"建设银行","fStatus":"1","timeC":"2020-06-09 09:31:50","urlVerify":null,"urlSync":null,"rpcToken":null},{"searchValue":null,"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"remark":null,"params":{},"id":7,"coName":"日照银行","fStatus":"0","timeC":"2020-06-09 09:32:02","urlVerify":null,"urlSync":null,"rpcToken":null},{"searchValue":null,"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"remark":null,"params":{},"id":8,"coName":"中国银行","fStatus":"1","timeC":"2020-06-09 09:32:32","urlVerify":null,"urlSync":null,"rpcToken":null},{"searchValue":null,"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"remark":null,"params":{},"id":9,"coName":"恒丰银行","fStatus":"0","timeC":"2020-06-09 09:32:50","urlVerify":null,"urlSync":null,"rpcToken":null},{"searchValue":null,"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"remark":null,"params":{},"id":10,"coName":"招商银行","fStatus":"1","timeC":"2020-06-09 09:32:57","urlVerify":null,"urlSync":null,"rpcToken":null},{"searchValue":null,"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"remark":null,"params":{},"id":11,"coName":"农商银行","fStatus":"0","timeC":"2020-06-11 09:19:28","urlVerify":null,"urlSync":null,"rpcToken":null},{"searchValue":null,"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"remark":null,"params":{},"id":12,"coName":"宁波银行","fStatus":"1","timeC":"2020-06-11 09:20:08","urlVerify":null,"urlSync":null,"rpcToken":null}]
     * code : 200
     * msg : 查询成功
     */

    private int total;
    private int code;
    private String msg;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<bankEntity> getRows() {
        return rows;
    }

    public void setRows(List<bankEntity> rows) {
        this.rows = rows;
    }

    private java.util.List<bankEntity> rows;
}
