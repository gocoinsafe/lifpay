package org.hcm.lifpay.user.constant;

public class Constant {

    // 用户协议授权状态
    public static class IsAgree {
        public static final Integer NOT_AGREE = 0;
        public static final Integer AGREE = 1;
    }


    public static class ServiceTermsStatus{
        public static final Integer STOP = 0;
        public static final Integer IN_USE = 1;
    }



    public static class QrCode{

        public enum QrCodeStatus {
            // wait scan
            WAIT_SCAN,
            // 已扫码
            WAIT_CONFIRM,
            // 已确认
            CONFIRM_LOGIN,
            // invalid
            INVALID
        }
    }

}
