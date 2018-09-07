package com.kunleen.sn.sportnewsapplication.network.bean;

import java.util.ArrayList;

/**
 * Created by ysy on 2018/2/1.
 */

public class TRespon_Carousel extends TResponse {
    private ArrayList<Carousel> rows;

    public ArrayList<Carousel> getRows() {
        return rows;
    }

    public void setRows(ArrayList<Carousel> rows) {
        this.rows = rows;
    }

    public class Carousel {
        private int carouselId;
        private int appid;
        private int groupid;
        private String carouselName;
        private String carouselImage;
        private int carouselType;
        private String carouselValue;
        private int weight;
        private String createTime;

        public int getCarouselId() {
            return carouselId;
        }

        public void setCarouselId(int carouselId) {
            this.carouselId = carouselId;
        }

        public int getAppid() {
            return appid;
        }

        public void setAppid(int appid) {
            this.appid = appid;
        }

        public int getGroupid() {
            return groupid;
        }

        public void setGroupid(int groupid) {
            this.groupid = groupid;
        }

        public String getCarouselName() {
            return carouselName;
        }

        public void setCarouselName(String carouselName) {
            this.carouselName = carouselName;
        }

        public String getCarouselImage() {
            return carouselImage;
        }

        public void setCarouselImage(String carouselImage) {
            this.carouselImage = carouselImage;
        }

        public int getCarouselType() {
            return carouselType;
        }

        public void setCarouselType(int carouselType) {
            this.carouselType = carouselType;
        }

        public String getCarouselValue() {
            return carouselValue;
        }

        public void setCarouselValue(String carouselValue) {
            this.carouselValue = carouselValue;
        }

        public int getWeight() {
            return weight;
        }

        public void setWeight(int weight) {
            this.weight = weight;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

    }
}
