package com.example.testapp;

import java.util.List;

/**
 * Created by yangc on 2017/10/11.
 */

public class Test {

    /**
     * code : 200
     * msg : 获取成功
     * result : {"rows":[{"id":"1014","title":"阅读的标题","reading":"100","feature_id":"15"},{"id":"1013","title":"阅读的标题","reading":"100","feature_id":"14"}]}
     */

    private String code;
    private String msg;
    private ResultEntity result;

    public void setCode(String code) {
        this.code = code;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setResult(ResultEntity result) {
        this.result = result;
    }

    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public ResultEntity getResult() {
        return result;
    }

    public static class ResultEntity {
        /**
         * rows : [{"id":"1014","title":"阅读的标题","reading":"100","feature_id":"15"},{"id":"1013","title":"阅读的标题","reading":"100","feature_id":"14"}]
         */

        private List<RowsEntity> rows;

        public void setRows(List<RowsEntity> rows) {
            this.rows = rows;
        }

        public List<RowsEntity> getRows() {
            return rows;
        }

        public static class RowsEntity {
            /**
             * id : 1014
             * title : 阅读的标题
             * reading : 100
             * feature_id : 15
             */

            private String id;
            private String title;
            private String reading;
            private String feature_id;

            public void setId(String id) {
                this.id = id;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public void setReading(String reading) {
                this.reading = reading;
            }

            public void setFeature_id(String feature_id) {
                this.feature_id = feature_id;
            }

            public String getId() {
                return id;
            }

            public String getTitle() {
                return title;
            }

            public String getReading() {
                return reading;
            }

            public String getFeature_id() {
                return feature_id;
            }
        }
    }
}
