package com.kunleen.sn.sportnewsapplication.network.bean;

/**
 * Created by ysy on 2018/3/22.
 */

public class Response_ForumTop extends TResponse {
    private forumListTop circleAndNoteVo;

    public forumListTop getCircleAndNoteVo() {
        return circleAndNoteVo;
    }

    public void setCircleAndNoteVo(forumListTop circleAndNoteVo) {
        this.circleAndNoteVo = circleAndNoteVo;
    }

    public class forumListTop {
        private int cid;
        private String imgUrl;
        private String cName;
        private int follow;
        private int theNumber;
        private int postNumber;
        private int replyNumber;
        private String descrip;
        private String littleImgUrl;

        public String getLittleImgUrl() {
            return littleImgUrl;
        }

        public void setLittleImgUrl(String littleImgUrl) {
            this.littleImgUrl = littleImgUrl;
        }

        public int getCid() {
            return cid;
        }

        public void setCid(int cid) {
            this.cid = cid;
        }

        public String getImgUrl() {
            return imgUrl;
        }

        public void setImgUrl(String imgUrl) {
            this.imgUrl = imgUrl;
        }

        public String getcName() {
            return cName;
        }

        public void setcName(String cName) {
            this.cName = cName;
        }

        public int getFollow() {
            return follow;
        }

        public void setFollow(int follow) {
            this.follow = follow;
        }

        public int getTheNumber() {
            return theNumber;
        }

        public void setTheNumber(int theNumber) {
            this.theNumber = theNumber;
        }

        public int getPostNumber() {
            return postNumber;
        }

        public void setPostNumber(int postNumber) {
            this.postNumber = postNumber;
        }

        public int getReplyNumber() {
            return replyNumber;
        }

        public void setReplyNumber(int replyNumber) {
            this.replyNumber = replyNumber;
        }

        public String getDescrip() {
            return descrip;
        }

        public void setDescrip(String descrip) {
            this.descrip = descrip;
        }
    }
}
