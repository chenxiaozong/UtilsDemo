package com.chen.utilsdemo.bean;


/**
 * 版权：chenxiaozong 版权所有
 * -------------------------
 * 作者：chenxiaozong
 * 邮箱：chenxzong@qq.com
 * 代码：https://gitee.com/chenxiaozong
 * 版本：1.0
 * 日期：2020/4/16 11:49 PM
 * 描述：com.chen.utilsdemo.bean/.java
 */
public class UserInfoJsonBean {

    /**
     * userInfo : {"name":"chenxzong","age":30,"info":"山东省青岛市李沧区"}
     */

    private UserInfoBean userInfo;

    public UserInfoBean getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfoBean userInfo) {
        this.userInfo = userInfo;
    }

    public static class UserInfoBean {
        /**
         * name : chenxzong
         * age : 30
         * info : 山东省青岛市李沧区
         */

        private String name;
        private int age;
        private String info;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        public String getInfo() {
            return info;
        }

        public void setInfo(String info) {
            this.info = info;
        }
    }

}
