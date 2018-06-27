package com.duofuen.repair.util;

public final class Const {

    public static final String MSG_PREFIX = "【253云通讯】，你的验证码是";

    public static final String ROLE_CODE_MANAGER = "00";
    public static final String ROLE_CODE_REPAIRMAN = "01";

    public static final String ORDER_STATE_OPEN = "00";
    public static final String ORDER_STATE_FINISH = "01";
    public static final Integer ORDER_PER_PAGE = 10;


    public final class Rest {
        public static final String ROOT_PATH = "/rest";

        public static final String SUCCESS = "00";
        public static final String FAIL = "99";
        public static final String UNAUTHORIZED = "40";

        public static final String HTTP_HEADER_TOKEN = "RestToken";

        public static final String LOGIN_PHONENUM = "phoneNum";
        public static final String LOGIN_PASSWORD = "pwd";
        public static final String LOGIN_OPEN_ID = "openId";

        public static final String ORDER_USER_ID = "userId";
        public static final String ORDER_STEP = "step";
        public static final String ORDER_ORDER_ID = "orderId";

        public static final String NOTE_USER_ID = "userId";
        public static final String NOTE_ORDER_ID = "orderId";
        public static final String NOTE_CONTENT = "content";

        public static final String IMG = "img";

    }

    public final class Wx {
        public static final String APP_ID = "wxa691be29e7554dcc";
        public static final String SECRET = "19f042bcce790a9fd71e9174651e5a06";


        public static final String ACCESS_TOKEN = "access_token";
        public static final String JSAPI_TICKET = "jsapi_ticket";
    }

}
