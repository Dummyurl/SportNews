package com.kunleen.sn.sportnewsapplication.network.bean;

import java.util.List;

/**
 * Created by ysy on 2018/3/31.
 */

public class Response_Carouse extends TResponse {
    private int signCoce;
    private List<Carousel> carousel;

    public int getSignCoce() {
        return signCoce;
    }

    public void setSignCoce(int signCoce) {
        this.signCoce = signCoce;
    }

    public List<Carousel> getCarousel() {
        return carousel;
    }

    public void setCarousel(List<Carousel> carousel) {
        this.carousel = carousel;
    }

    public class Carousel {
        private int carouselId;
        private int appid;
        private int groupid;
        private String carouselName;
        private String carouselImage;
        private int carouselType;
        private String carouselValue;
        private String weight;
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

        public String getWeight() {
            return weight;
        }

        public void setWeight(String weight) {
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
