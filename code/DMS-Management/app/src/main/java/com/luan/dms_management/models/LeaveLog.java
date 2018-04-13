package com.luan.dms_management.models;

/**
 * Created by hanbiro on 13/12/2017.
 */

public class LeaveLog {
    private String leaveId;
    private String userCode;
    private String deveiceId;
    private String fromDate;
    private String toDate;
    private String leaveType;
    private String reason;
    private String remark;
    private String status;
    private String createDate;
    private String approveDate;
    private String approveUser;

    public LeaveLog() {
        this.leaveId = "";
        this.userCode = "";
        this.deveiceId = "";
        this.fromDate = "";
        this.toDate = "";
        this.leaveType = "";
        this.reason = "";
        this.remark = "";
        this.status = "";
        this.createDate = "";
        this.approveDate = "";
        this.approveUser = "";
    }

    public String getLeaveId() {
        return leaveId;
    }

    public void setLeaveId(String leaveId) {
        this.leaveId = leaveId;
    }

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public String getDeveiceId() {
        return deveiceId;
    }

    public void setDeveiceId(String deveiceId) {
        this.deveiceId = deveiceId;
    }

    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public String getToDate() {
        return toDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
    }

    public String getLeaveType() {
        return leaveType;
    }

    public void setLeaveType(String leaveType) {
        this.leaveType = leaveType;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getApproveDate() {
        return approveDate;
    }

    public void setApproveDate(String approveDate) {
        this.approveDate = approveDate;
    }

    public String getApproveUser() {
        return approveUser;
    }

    public void setApproveUser(String approveUser) {
        this.approveUser = approveUser;
    }
}
