package com.ys.system.api.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.ys.common.core.annotation.Excel;
import com.ys.common.core.web.domain.BaseEntity;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Date;
import java.util.Map;


/**
 * Message Recipient tb_message
 *
 * @author ys
 * @date 2021-12-01
 */
@TableName("tb_message")
@Data
public class SysMessage extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** Message id */
    @TableId(value = "message_id", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long messageId;

    /** Message Content id */
    @Excel(name = "Message Content id")
    private String messageInfoId;

    private String messageInfoPath;

    /** SenderID */
    @Excel(name = "SenderID")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long messageSender;

    /** Recipient ID */
    @Excel(name = "Recipient ID")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long messageRecipient;

    /** Message  Type */
    @Excel(name = "Message  Type")
    private Integer messageType;

    /** Message Status */
    @Excel(name = "Message Status")
    private String messageStatus;

    /** Message title */
    @Excel(name = "Message title")
    private String messageInfoTitle;
    private String messageInfoTitleCn;

    /** Message Picture */
    @Excel(name = "Message Picture")
    private String messageInfoPhoto;

    /** Message Content  */
    @Excel(name = "Message Content ")
    private String messageInfoText;

    @TableField(exist = false)
    private String search;

    @TableField(exist = false)
    private String distanceDay;

    @TableField(exist = false)
    private Long[] messageRecipients;

    @TableField(exist = false)
    private String messageSenderName;

    @TableField(exist = false)
    private String messageRecipientName;

    @TableField(exist = false)
    private String messageSenderAvatar;

    @TableField(exist = false)
    private String messageRecipientAvatar;

    @TableField(exist = false)
    private String messageIcon;

    @TableField(exist = false)
    private String pathConfig;

    @TableField(exist = false)
    Map<String, Object> map1;

    @TableField(exist = false)
    Map<String, Object> map2;

    public String getDistanceDay(){
        long distanceDay = 0;
        String Company = " second";
        if(this.getCreateTime() != null) {
            Date createTime = this.getCreateTime();
            long dateTime = createTime.getTime();
            long nowTime = new Date().getTime();
            distanceDay = (nowTime - dateTime) / 1000;
            if(distanceDay>60){
                Company = " minute";
                distanceDay = distanceDay/60;
                if(distanceDay>60){
                    Company = " hour";
                    distanceDay = distanceDay/60;
                    if(distanceDay>24){
                        Company = " day";
                        distanceDay = distanceDay/24;
                    }
                }
            }
        }
        if(distanceDay>1){
            Company += "s";
        }
        return distanceDay+Company+" ago";
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("messageId", getMessageId())
            .append("messageInfoPath", getMessageInfoPath())
            .append("messageSender", getMessageSender())
            .append("messageRecipient", getMessageRecipient())
            .append("messageType", getMessageType())
            .append("messageStatus", getMessageStatus())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .append("messageInfoTitle", getMessageInfoTitle())
            .append("messageInfoText", getMessageInfoText())
            .toString();
    }
}
