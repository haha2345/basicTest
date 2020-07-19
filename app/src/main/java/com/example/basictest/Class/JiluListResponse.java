package com.example.basictest.Class;

import java.util.List;

public class JiluListResponse {

    /**
     * total : 2
     * rows : [{"searchValue":null,"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"remark":null,"params":{},"id":122,"caseCode":"GZ20200630145023285","caseRegCode":"","userId":12,"userIp":"144.0.22.205","fStatus":"1","pStatus":"0","rStatus":"0","timeC":"2020-06-30 14:50:23","applyTime":"2020-06-30 14:53:59","auditUserId":null,"auditUserNum":"","auditUserName":"","auditUserIp":"","auditTime":null,"auditComment":"","auditReason":null,"coId":4,"coName":"工商银行","loanCode":"","loanName":"","loanUserName":"","loanUserMobile":"","loanUserIdnum":"","loanMoney":null,"loanRatio":null,"loanFromTo":"","loanTime":null},{"searchValue":null,"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"remark":null,"params":{},"id":148,"caseCode":"GZ20200713135921944","caseRegCode":"CRC123456789","userId":12,"userIp":"144.0.22.236","fStatus":"21","pStatus":"0","rStatus":"0","timeC":"2020-07-13 13:59:21","applyTime":"2020-07-13 14:01:43","auditUserId":103,"auditUserNum":"ZHC15610557006","auditUserName":"赵慧超","auditUserIp":"144.0.22.236","auditTime":"2020-07-13 14:26:57","auditComment":"","auditReason":null,"coId":1,"coName":"交通银行","loanCode":"HT_1106","loanName":"赵慧超的合同","loanUserName":"赵慧超","loanUserMobile":"15610557006","loanUserIdnum":"","loanMoney":80000000,"loanRatio":1.68,"loanFromTo":"2020.01.01-2021.01.01","loanTime":"2020-07-01 19:34:30"}]
     * code : 200
     * msg : 查询成功
     */

    private int total;
    private int code;
    private String msg;
    private List<JiluEntity> rows;

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

    public List<JiluEntity> getRows() {
        return rows;
    }

    public void setRows(List<JiluEntity> rows) {
        this.rows = rows;
    }

}
