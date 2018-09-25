package com.thankjava.wchat.db.entity;

/**
 * 好友申请记录
 */
public class GroupApply extends BaseEntity{

    private String toGroupId;
    private String applyUserId;

    // 是否同意
    private Boolean agree;

    // 是否已处理
    private Boolean processed;

    // 申请备注
    private String remark;

    public String getToGroupId() {
        return toGroupId;
    }

    public void setToGroupId(String toGroupId) {
        this.toGroupId = toGroupId;
    }

    public String getApplyUserId() {
        return applyUserId;
    }

    public void setApplyUserId(String applyUserId) {
        this.applyUserId = applyUserId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Boolean getAgree() {
        return agree;
    }

    public void setAgree(Boolean agree) {
        this.agree = agree;
    }

    public Boolean getProcessed() {
        return processed;
    }

    public void setProcessed(Boolean processed) {
        this.processed = processed;
    }
}
