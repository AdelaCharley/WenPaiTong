package com.qunshang.wenpaitong.equnshang.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class WenBanTongProductBean implements Serializable{

    Integer code;

    String msg;

    DataBean data;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean implements Serializable {

        Integer productId;

        String productName;

        String productDesc;

        String freight;

        ArrayList<String> swiperList;

        List<DataBean.AttributeList> attributeList;

        DataBean.Manufacture manufacture;

        List<String> detailInfo;

        String servicePhone;

        String evaluationPrice;

        String price;

        int stock;

        String sale;

        int initStock;

        String packetName;

        String companyAddress;

        String productPosterUrl;

        String status;

        String startTime;

        String endTime;

        String process;

        String realPrice;

        Discount discount;

        String tag;

        String purchaseTip;

        String priceTip;

        EvaluationPriceRes evaluationPriceRes;

        PriceRes priceRes;

        public EvaluationPriceRes getEvaluationPriceRes() {
            return evaluationPriceRes;
        }

        public void setEvaluationPriceRes(EvaluationPriceRes evaluationPriceRes) {
            this.evaluationPriceRes = evaluationPriceRes;
        }

        public PriceRes getPriceRes() {
            return priceRes;
        }

        public void setPriceRes(PriceRes priceRes) {
            this.priceRes = priceRes;
        }

        public String getPurchaseTip() {
            return purchaseTip;
        }

        public void setPurchaseTip(String purchaseTip) {
            this.purchaseTip = purchaseTip;
        }

        public String getPriceTip() {
            return priceTip;
        }

        public void setPriceTip(String priceTip) {
            this.priceTip = priceTip;
        }

        public int getInitStock() {
            return initStock;
        }

        public void setInitStock(int initStock) {
            this.initStock = initStock;
        }

        public String getTag() {
            return tag;
        }

        public void setTag(String tag) {
            this.tag = tag;
        }

        public Discount getDiscount() {
            return discount;
        }

        public void setDiscount(Discount discount) {
            this.discount = discount;
        }

        public String getRealPrice() {
            return realPrice;
        }

        public void setRealPrice(String realPrice) {
            this.realPrice = realPrice;
        }

        public String getSale() {
            return sale;
        }

        public void setSale(String sale) {
            this.sale = sale;
        }

        public String getPacketName() {
            return packetName;
        }

        public void setPacketName(String packetName) {
            this.packetName = packetName;
        }

        public String getProcess() {
            return process;
        }

        public void setProcess(String process) {
            this.process = process;
        }

        public String getStartTime() {
            return startTime;
        }

        public String getEndTime() {
            return endTime;
        }

        public void setEndTime(String endTime) {
            this.endTime = endTime;
        }

        public void setStartTime(String startTime) {
            this.startTime = startTime;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getProductPosterUrl() {
            return productPosterUrl;
        }

        public void setProductPosterUrl(String productPosterUrl) {
            this.productPosterUrl = productPosterUrl;
        }

        public String getCompanyAddress() {
            return companyAddress;
        }

        public void setCompanyAddress(String companyAddress) {
            this.companyAddress = companyAddress;
        }

        public int getStock() {
            return stock;
        }

        public void setStock(int stock) {
            this.stock = stock;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getEvaluationPrice() {
            return evaluationPrice;
        }

        public void setEvaluationPrice(String evaluationPrice) {
            this.evaluationPrice = evaluationPrice;
        }

        public Integer getProductId() {
            return productId;
        }

        public void setProductId(Integer productId) {
            this.productId = productId;
        }

        public String getProductName() {
            return productName;
        }

        public void setProductName(String productName) {
            this.productName = productName;
        }

        public String getProductDesc() {
            return productDesc;
        }

        public void setProductDesc(String productDesc) {
            this.productDesc = productDesc;
        }

        public String getFreight() {
            return freight;
        }

        public void setFreight(String freight) {
            this.freight = freight;
        }

        public ArrayList<String> getSwiperList() {
            return swiperList;
        }

        public void setSwiperList(ArrayList<String> swiperList) {
            this.swiperList = swiperList;
        }

        public List<AttributeList> getAttributeList() {
            return attributeList;
        }

        public void setAttributeList(List<AttributeList> attributeList) {
            this.attributeList = attributeList;
        }

        public Manufacture getManufacture() {
            return manufacture;
        }

        public void setManufacture(Manufacture manufacture) {
            this.manufacture = manufacture;
        }

        public List<String> getDetailInfo() {
            return detailInfo;
        }

        public void setDetailInfo(List<String> detailInfo) {
            this.detailInfo = detailInfo;
        }

        public String getServicePhone() {
            return servicePhone;
        }

        public void setServicePhone(String servicePhone) {
            this.servicePhone = servicePhone;
        }

        public static class AttributeList implements Serializable{
            String attributeName;

            String value;

            public String getAttributeName() {
                return attributeName;
            }

            public void setAttributeName(String attributeName) {
                this.attributeName = attributeName;
            }

            public String getValue() {
                return value;
            }

            public void setValue(String value) {
                this.value = value;
            }
        }

        public static class EvaluationPriceRes implements Serializable{

            String real;

            String commaSeparated;

            String format;

            public String getReal() {
                return real;
            }

            public void setReal(String real) {
                this.real = real;
            }

            public String getCommaSeparated() {
                return commaSeparated;
            }

            public void setCommaSeparated(String commaSeparated) {
                this.commaSeparated = commaSeparated;
            }

            public String getFormat() {
                return format;
            }

            public void setFormat(String format) {
                this.format = format;
            }
        }

        public static class PriceRes implements Serializable{

            String real;

            String commaSeparated;

            String format;

            public String getReal() {
                return real;
            }

            public void setReal(String real) {
                this.real = real;
            }

            public String getCommaSeparated() {
                return commaSeparated;
            }

            public void setCommaSeparated(String commaSeparated) {
                this.commaSeparated = commaSeparated;
            }

            public String getFormat() {
                return format;
            }

            public void setFormat(String format) {
                this.format = format;
            }
        }

        public static class Manufacture implements Serializable{
            Integer companyId;

            String companyName;

            String companyAvatar;

            public Integer getCompanyId() {
                return companyId;
            }

            public void setCompanyId(Integer companyId) {
                this.companyId = companyId;
            }

            public String getCompanyName() {
                return companyName;
            }

            public void setCompanyName(String companyName) {
                this.companyName = companyName;
            }

            public String getCompanyAvatar() {
                return companyAvatar;
            }

            public void setCompanyAvatar(String companyAvatar) {
                this.companyAvatar = companyAvatar;
            }
        }

        public static class Discount implements Serializable{

            String productDiscountPrice;

            String productDiscountPriceStr;

            String discountStr;

            String endTime;

            String productFormatDiscountPriceStr;

            public String getProductFormatDiscountPriceStr() {
                return productFormatDiscountPriceStr;
            }

            public void setProductFormatDiscountPriceStr(String productFormatDiscountPriceStr) {
                this.productFormatDiscountPriceStr = productFormatDiscountPriceStr;
            }

            public String getProductDiscountPrice() {
                return productDiscountPrice;
            }

            public void setProductDiscountPrice(String productDiscountPrice) {
                this.productDiscountPrice = productDiscountPrice;
            }

            public String getProductDiscountPriceStr() {
                return productDiscountPriceStr;
            }

            public void setProductDiscountPriceStr(String productDiscountPriceStr) {
                this.productDiscountPriceStr = productDiscountPriceStr;
            }

            public String getDiscountStr() {
                return discountStr;
            }

            public void setDiscountStr(String discountStr) {
                this.discountStr = discountStr;
            }

            public String getEndTime() {
                return endTime;
            }

            public void setEndTime(String endTime) {
                this.endTime = endTime;
            }
        }

    }

}
