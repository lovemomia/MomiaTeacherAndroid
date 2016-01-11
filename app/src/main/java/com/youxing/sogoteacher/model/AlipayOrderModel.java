package com.youxing.sogoteacher.model;

import com.youxing.common.model.BaseModel;

import java.util.Map;

/**
 * Created by Jun Deng on 15/8/20.
 */
public class AlipayOrderModel extends BaseModel {

    private AlipayOrderData data;

    public AlipayOrderData getData() {
        return data;
    }

    public void setData(AlipayOrderData data) {
        this.data = data;
    }

    public static class AlipayOrderData {
        private String partner;
        private String seller_id;
        private String out_trade_no;
        private String subject;
        private String body;
        private String total_fee;
        private String notify_url;
        private String service;
        private String payment_type;
        private String input_charset;
        private String it_b_pay;
        private String show_url;
        private String sign;
        private String sign_type;
        private String appID;
        private boolean successful;
        private Map <String, String> extraParams;

        public String getPartner() {
            return partner;
        }

        public void setPartner(String partner) {
            this.partner = partner;
        }

        public String getSeller_id() {
            return seller_id;
        }

        public void setSeller_id(String seller_id) {
            this.seller_id = seller_id;
        }

        public String getOut_trade_no() {
            return out_trade_no;
        }

        public void setOut_trade_no(String out_trade_no) {
            this.out_trade_no = out_trade_no;
        }

        public String getSubject() {
            return subject;
        }

        public void setSubject(String subject) {
            this.subject = subject;
        }

        public String getBody() {
            return body;
        }

        public void setBody(String body) {
            this.body = body;
        }

        public String getTotal_fee() {
            return total_fee;
        }

        public void setTotal_fee(String total_fee) {
            this.total_fee = total_fee;
        }

        public String getNotify_url() {
            return notify_url;
        }

        public void setNotify_url(String notify_url) {
            this.notify_url = notify_url;
        }

        public String getService() {
            return service;
        }

        public void setService(String service) {
            this.service = service;
        }

        public String getPayment_type() {
            return payment_type;
        }

        public void setPayment_type(String payment_type) {
            this.payment_type = payment_type;
        }

        public String getInput_charset() {
            return input_charset;
        }

        public void setInput_charset(String input_charset) {
            this.input_charset = input_charset;
        }

        public String getIt_b_pay() {
            return it_b_pay;
        }

        public void setIt_b_pay(String it_b_pay) {
            this.it_b_pay = it_b_pay;
        }

        public String getShow_url() {
            return show_url;
        }

        public void setShow_url(String show_url) {
            this.show_url = show_url;
        }

        public String getSign() {
            return sign;
        }

        public void setSign(String sign) {
            this.sign = sign;
        }

        public String getSign_type() {
            return sign_type;
        }

        public void setSign_type(String sign_type) {
            this.sign_type = sign_type;
        }

        public String getAppID() {
            return appID;
        }

        public void setAppID(String appID) {
            this.appID = appID;
        }

        public boolean isSuccessful() {
            return successful;
        }

        public void setSuccessful(boolean successful) {
            this.successful = successful;
        }

        public Map getExtraParams() {
            return extraParams;
        }

        public void setExtraParams(Map extraParams) {
            this.extraParams = extraParams;
        }

        /**
         * create the order info. 创建订单信息
         *
         */
        public String getOrderInfo() {
            // 签约合作者身份ID
            String orderInfo = "partner=\"" + partner + "\"";

            // 签约卖家支付宝账号
            orderInfo += "&seller_id=\"" + seller_id + "\"";

            // 商户网站唯一订单号
            orderInfo += "&out_trade_no=\"" + out_trade_no + "\"";

            // 商品名称
            orderInfo += "&subject=\"" + subject + "\"";

            // 商品详情
            orderInfo += "&body=\"" + body + "\"";

            // 商品金额
            orderInfo += "&total_fee=\"" + total_fee + "\"";

            // 服务器异步通知页面路径
            orderInfo += "&notify_url=\"" + notify_url + "\"";

            // 服务接口名称， 固定值
            orderInfo += "&service=\"" + service + "\"";

            // 支付类型， 固定值
            orderInfo += "&payment_type=\"" + payment_type + "\"";

            // 参数编码， 固定值
            orderInfo += "&_input_charset=\"" + input_charset + "\"";

            // 设置未付款交易的超时时间
            // 默认30分钟，一旦超时，该笔交易就会自动被关闭。
            // 取值范围：1m～15d。
            // m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。
            // 该参数数值不接受小数点，如1.5h，可转换为90m。
            orderInfo += "&it_b_pay=\"" + it_b_pay + "\"";

            // extern_token为经过快登授权获取到的alipay_open_id,带上此参数用户将使用授权的账户进行支付
            // orderInfo += "&extern_token=" + "\"" + extern_token + "\"";

            // 支付宝处理完请求后，当前页面跳转到商户指定页面的路径，可空
            orderInfo += "&show_url=\"" + show_url + "\"";

            // 调用银行卡支付，需配置此参数，参与签名， 固定值 （需要签约《无线银行卡快捷支付》才能使用）
            // orderInfo += "&paymethod=\"expressGateway\"";

            orderInfo += "&sign=\"" + sign + "\"";

            orderInfo += "&sign_type=\"" + sign_type + "\"";

            if (extraParams != null) {
                for (String key : extraParams.keySet()) {
                    orderInfo += "&" + key + "=" + extraParams.get(key);
                }
            }

            return orderInfo;
        }
    }
}
