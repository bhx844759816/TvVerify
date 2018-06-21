package com.acloudchina.app.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Arrays;
import java.util.List;

/**
 * 广告的对象
 */
public class AdBean implements Parcelable {
    // 本地数据库表名
    public static final String TABLE_NAME = "AdInfo";
    // 广告的Id
    public static final String NAME_AD_ID = "ad_id";
    // 广告的名字
    public static final String NAME_AD_NAME = "ad_name";
    // 广告的类型 首页广告 贴片广告
    public static final String NAME_AD_TYPE_ID = "ad_type_id";
    // 广告的内容类型 图片 视频
    public static final String NAME_AD_CONTENT_ID = "ad_content_id";
    // 广告的描述
    public static final String NAME_AD_DESC = "ad_desc";
    // 广告的开启时间
    public static final String NAME_VALID_BEGIN = "ad_valid_begin";
    // 广告的结束时间
    public static final String NAME_VALID_END = "ad_valid_end";
    // 广告的Url
    public static final String NAME_AD_URL = "ad_url";
    // 广告的循环播放的周期
    public static final String NAME_AD_CIRCLE = "ad_circle";
    // 广告的播放时长
    public static final String NAME_AD_PLAY_TIME = "play_time";
    // 广告视频存储的地址
    public static final String NAME_AD_VIDEO_SAVE_PATH = "video_save_path";
    // 广告播放的次数
    public static final String NAME_AD_PLAY_COUNT = "play_count";
    // 广告允许播放的时间段 0900-1010|1200-1400这种格式进行拼接
    public static final String NAME_AD_TIME_RULE = "time_rule";


    @SerializedName("ad_id")
    private String id;
    @SerializedName("ad_name")
    private String name;
    @SerializedName("ad_type_id")
    private String typeId;//1 表示开机广告 2表示贴片广告
    @SerializedName("ad_content_id")
    private String contentId;//1表示图片广告 2表示视频广告
    @SerializedName("ad_desc")
    private String describe;//广告描述
    @SerializedName("ad_valid_begin")
    private String validBegin;//广告开始播放的时间
    @SerializedName("ad_valid_end")
    private String validEnd;//广告结束播放的时间
    @SerializedName("ad_url")
    private String url;// 广告的地址
    @SerializedName("ad_circle")
    private String circle;//广告播放的间隔时间
    @SerializedName("play_time")
    private String playTime;//
    @SerializedName("time_rule")
    private List<String> timeRule;
    @Expose
    private String savePath;//存储路径
    @Expose
    private int playCount;//播放次数

    @Override
    public String toString() {
        return "AdBean{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", typeId='" + typeId + '\'' +
                ", contentId='" + contentId + '\'' +
                ", describe='" + describe + '\'' +
                ", validBegin='" + validBegin + '\'' +
                ", validEnd='" + validEnd + '\'' +
                ", url='" + url + '\'' +
                ", circle='" + circle + '\'' +
                ", playTime='" + playTime + '\'' +
                ", timeRule=" + timeRule +
                ", savePath='" + savePath + '\'' +
                ", playCount=" + playCount +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    public String getContentId() {
        return contentId;
    }

    public void setContentId(String contentId) {
        this.contentId = contentId;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public String getValidBegin() {
        return validBegin;
    }

    public void setValidBegin(String validBegin) {
        this.validBegin = validBegin;
    }

    public String getValidEnd() {
        return validEnd;
    }

    public void setValidEnd(String validEnd) {
        this.validEnd = validEnd;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getCircle() {
        return circle;
    }

    public void setCircle(String circle) {
        this.circle = circle;
    }

    public String getPlayTime() {
        return playTime;
    }

    public void setPlayTime(String playTime) {
        this.playTime = playTime;
    }

    public List<String> getTimeRule() {
        return timeRule;
    }

    public void setTimeRule(List<String> timeRule) {
        this.timeRule = timeRule;
    }

    public String getSavePath() {
        return savePath;
    }

    public void setSavePath(String savePath) {
        this.savePath = savePath;
    }

    public int getPlayCount() {
        return playCount;
    }

    public void setPlayCount(int playCount) {
        this.playCount = playCount;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.name);
        dest.writeString(this.typeId);
        dest.writeString(this.contentId);
        dest.writeString(this.describe);
        dest.writeString(this.validBegin);
        dest.writeString(this.validEnd);
        dest.writeString(this.url);
        dest.writeString(this.circle);
        dest.writeString(this.playTime);
        dest.writeStringList(this.timeRule);
        dest.writeString(this.savePath);
        dest.writeInt(this.playCount);
    }

    public AdBean() {
    }

    protected AdBean(Parcel in) {
        this.id = in.readString();
        this.name = in.readString();
        this.typeId = in.readString();
        this.contentId = in.readString();
        this.describe = in.readString();
        this.validBegin = in.readString();
        this.validEnd = in.readString();
        this.url = in.readString();
        this.circle = in.readString();
        this.playTime = in.readString();
        this.timeRule = in.createStringArrayList();
        this.savePath = in.readString();
        this.playCount = in.readInt();
    }

    public static final Creator<AdBean> CREATOR = new Creator<AdBean>() {
        @Override
        public AdBean createFromParcel(Parcel source) {
            return new AdBean(source);
        }

        @Override
        public AdBean[] newArray(int size) {
            return new AdBean[size];
        }
    };
}
