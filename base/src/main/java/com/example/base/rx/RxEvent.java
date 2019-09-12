package com.example.base.rx;



public class RxEvent {

    private int eventType;
    private Object data;

    public RxEvent(int eventType, Object data) {
        this.eventType = eventType;
        this.data = data;
    }

    /**
     * 事件类型
     */
    public static class EventType {

        /**
         * 文章列表页刷新
         */
        public static final int DISCOVERY_ARTICLE_LIST_REFRESH = 3000;

        /**
         * 退出登录
         */
        public static final int SYSTEM_LOGIN_OUT = 10001;

        /**
         * 弹出取消关注的dialog
         */
        public static final int OPEN_NATIVE_DIALOG = 10002;

    }

    public int getEventType() {
        return eventType;
    }

    public void setEventType(int eventType) {
        this.eventType = eventType;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
