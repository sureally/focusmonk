package com.netease.focusmonk.common;

/**
 * @author hejiecheng
 * @Date 2019-04-28
 */
public class JsonResult {
        private String status;
        private String desc;
        private Object detail;

        public static JsonResult getCustomResult(ResultCode resultCode) {
            return new JsonResult(resultCode.getCode(), resultCode.getMessage(), "");
        }

        public static JsonResult getCustomResult(ResultCode resultCode, Object detail) {
            return new JsonResult(resultCode.getCode(), resultCode.getMessage(), detail);
        }

        public static JsonResult getSuccessResult() {
            return getSuccessResult("");
        }

        public static JsonResult getSuccessResult(Object detail) {
            return new JsonResult(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getMessage(), detail);
        }

        public static JsonResult getErrorResult() {
            return getErrorResult("");
        }

        public static JsonResult getErrorResult(Object detail) {
            return new JsonResult(ResultCode.UNKNOWN.getCode(), ResultCode.UNKNOWN.getMessage(), detail);
        }

        public String getStatus() {
            return this.status;
        }

        public String getDesc() {
            return this.desc;
        }

        public Object getDetail() {
            return this.detail;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public void setDetail(Object detail) {
            this.detail = detail;
        }

        @Override
        public boolean equals(Object o) {
            if (o == this) {
                return true;
            } else if (!(o instanceof JsonResult)) {
                return false;
            } else {
                JsonResult other = (JsonResult)o;
                if (!other.canEqual(this)) {
                    return false;
                } else {
                    label59: {
                        Object this$status = this.getStatus();
                        Object other$status = other.getStatus();
                        if (this$status == null) {
                            if (other$status == null) {
                                break label59;
                            }
                        } else if (this$status.equals(other$status)) {
                            break label59;
                        }

                        return false;
                    }

                    Object this$desc = this.getDesc();
                    Object other$desc = other.getDesc();
                    if (this$desc == null) {
                        if (other$desc != null) {
                            return false;
                        }
                    } else if (!this$desc.equals(other$desc)) {
                        return false;
                    }

                    Object this$detail = this.getDetail();
                    Object other$detail = other.getDetail();
                    if (this$detail == null) {
                        if (other$detail != null) {
                            return false;
                        }
                    } else if (!this$detail.equals(other$detail)) {
                        return false;
                    }

                    return true;
                }
            }
        }

        protected boolean canEqual(Object other) {
            return other instanceof JsonResult;
        }

        @Override
        public int hashCode() {
            boolean PRIME = true;
            int result = 1;
            Object $status = this.getStatus();
            result = result * 59 + ($status == null ? 43 : $status.hashCode());
            Object $desc = this.getDesc();
            result = result * 59 + ($desc == null ? 43 : $desc.hashCode());
            Object $detail = this.getDetail();
            result = result * 59 + ($detail == null ? 43 : $detail.hashCode());
            return result;
        }

        @Override
        public String toString() {
            return "JsonResult(status=" + this.getStatus() + ", desc=" + this.getDesc() + ", detail=" + this.getDetail() + ")";
        }

        public JsonResult(String status, String desc, Object detail) {
            this.status = status;
            this.desc = desc;
            this.detail = detail;
        }
}
