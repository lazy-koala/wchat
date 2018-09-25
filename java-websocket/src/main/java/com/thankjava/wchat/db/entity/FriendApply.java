package com.thankjava.wchat.db.entity;

/**
 * 好友申请记录
 */
public class FriendApply extends BaseEntity{

    private String toUserId;
    private String applyUserId;

    // 是否同意
    private Boolean agree;

    // 是否已处理
    private Boolean processed;

    // 好友申请备注
    private String remark;

    private Boolean isApplyUserRead;

    public String getToUserId() {
        return toUserId;
    }

    public void setToUserId(String toUserId) {
        this.toUserId = toUserId;
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

    public Boolean getApplyUserRead() {
        return isApplyUserRead;
    }

    public void setApplyUserRead(Boolean applyUserRead) {
        isApplyUserRead = applyUserRead;
    }
}
