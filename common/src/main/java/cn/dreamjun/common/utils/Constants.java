package cn.dreamjun.common.utils;

/**
 * @Classname Constants
 * @Description TODO
 * @Date 2022/9/10 11:30
 * @Created by 翊
 */
public interface Constants {

    interface User {
        String KEY_MAIL_CODE_IP = "key_mail_code_ip_";
        String KEY_MAIL_CODE_ADDRESS = "key_mail_code_address_";
        String KEY_MAIL_CODE = "key_mail_code_";
        String DEFAULT_AVATAR = "https://s1.ax1x.com/2022/09/10/vOP0Ug.png";
        //正常的
        String STATUS_NORMAL = "1";
        //禁用的
        String STATUS_DISABLE = "0";
        //token的Key
        String KEY_TOKEN = "key_token_";
        String KEY_ADMIN_INIT_STATE = "admin_init_state";
        String KEY_ADMIN_USER_ID = "admin_user_id";
        String KEY_FIRELIGHT_TOKEN = "firelight_token";
        String KEY_SALT = "key_salt_";
        String SUPER_ROLE_NAME = "SUPER_ADMIN";
        String SUPER_ROLE_LABEL = "超级管理员";
    }

    //毫秒
    interface Millions {
        long SECOND = 1000;
        long MIN = SECOND * 60;
        long HOUR = MIN * 60;
        long TWO_HOUR = 2 * HOUR;
        long DAY = 24 * HOUR;
        long MONTH = 30 * DAY;
    }

    interface TimeSecond {
        int ONE = 1;
        int MIN = 60 * ONE;
        int FIVE_MIN = 5 * 60 * ONE;
        int HOUR = 60 * MIN;
        int TWO_HOUR = 2 * HOUR;
        int DAY = 24 * HOUR;
    }

    // imageUploadUrl
    interface ImageUploadCategory {
        String PORTAL_FIREFLY_CATEGORY = "user/firefly/";
        String PORTAL_AVATAR_CATEGORY = "user/avatar/";
        String PORTAL_COVER_CATEGORY = "user/cover/";
        String ADMIN_CATEGORY = "admin/";
    }

    // URL_TYPE
    interface URLType {
        String ZHI_HU = "zhihu";
        String BI_LI_BI_LI = "bilibili";
    }

    // ThumbUp
    interface ThumbUp {
        String FISH_THUMB_UP_KEY = "fish_thumb_up_";
        String COMMENT_THUMB_UP_KEY = "comment_thumb_up_";
        String COMMENT_SUB_THUMB_UP_KEY = "comment_sub_thumb_up_";
    }

    int EMAIL_CODE_SEND_RETRY_MAX_TIMES = 10;

    int DEFAULT_SIZE = 5;

    int DEFAULT_HOST_SIZE = 10;
}
