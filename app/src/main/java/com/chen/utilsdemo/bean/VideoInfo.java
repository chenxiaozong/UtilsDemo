package com.chen.utilsdemo.bean;

import java.io.Serializable;
import java.util.List;

public class VideoInfo {


    private List<MediaitemsBean> mediaitems;

    public List<MediaitemsBean> getMediaitems() {
        return mediaitems;
    }

    public void setMediaitems(List<MediaitemsBean> mediaitems) {
        this.mediaitems = mediaitems;
    }

    public static class MediaitemsBean implements Serializable{
        /**
         * title : DISCONTINUITY(video format change)
         * provider : All
         * videoTags : ["DISCONTINUITY"]
         * testTags : ["测试视频格式改变","time:29.611s"]
         * url : http://10.128.0.36:8080/test/hls/discontinuity/vformatchange/playlist.m3u8
         */

        private String title;
        private String provider;
        private String url;
        private List<String> videoTags;
        private List<String> testTags;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getProvider() {
            return provider;
        }

        public void setProvider(String provider) {
            this.provider = provider;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public List<String> getVideoTags() {
            return videoTags;
        }

        public void setVideoTags(List<String> videoTags) {
            this.videoTags = videoTags;
        }

        public List<String> getTestTags() {
            return testTags;
        }

        public void setTestTags(List<String> testTags) {
            this.testTags = testTags;
        }

        @Override
        public String toString() {
            return "MediaitemsBean{" +
                    "title='" + title + '\'' +
                    ", provider='" + provider + '\'' +
                    ", url='" + url + '\'' +
                    ", videoTags=" + videoTags +
                    ", testTags=" + testTags +
                    '}'+"\n";
        }
    }

    @Override
    public String toString() {
        return "VideoInfo{" +
                "mediaitems=" + mediaitems +
                '}';
    }
}
