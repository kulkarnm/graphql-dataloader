package com.test.graphql.tracer;

import org.bson.Document;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

public class DBQueryTracer {

    private String db;

    private String collection;

    private DBQuery query;

    private long start;

    private long duration;

    private int resultCount;

    private class DBQuery {
        private final Document filter;

        private final Document projection;

        private final Document sort;

        private long skip;

        private int limit;

        public DBQuery (Query q) {
            this.filter = q.getQueryObject();
            this.projection = q.getFieldsObject();
            this.sort = q.getSortObject();
            this.skip = q.getSkip();
            this.limit = q.getLimit();
        }

        public Document getFilter() {
            return filter;
        }

        public Document getProjection() {
            return projection;
        }

        public Document getSort() {
            return sort;
        }

        public long getSkip() {
            return skip;
        }

        public int getLimit() {
            return limit;
        }

        @Override
        public String toString() {
            return "DBQuery{" +
                    "filter=" + filter +
                    ", projection=" + projection +
                    ", sort=" + sort +
                    ", skip=" + skip +
                    ", limit=" + limit +
                    '}';
        }
    }

    public DBQueryTracer(String db, String collection, Query query) {
        this.db = db;
        this.collection = collection;
        this.query = new DBQuery(query);
    }

    public DBQueryTracer startTracing() {
        this.start = System.nanoTime();
        return this;
    }

    public DBQueryTracer stopTracing(Object results) {
        if(results != null) {
            if (results instanceof List) {
                List list = (List) results;
                this.resultCount = list.size();
            } else {
                this.resultCount = 1;
            }
        }
        long now = System.nanoTime();
        this.duration = (now - start) / 1000000;
        return this;
    }

    public String getDb() {
        return db;
    }

    public String getCollection() {
        return collection;
    }

    public DBQuery getQuery() {
        return query;
    }

    public long getStart() {
        return start;
    }

    public long getDuration() {
        return duration;
    }

    public int getResultCount() {
        return resultCount;
    }

    @Override
    public String toString() {
        return "DBQueryTracer{" +
                "db='" + db + '\'' +
                ", collection='" + collection + '\'' +
                ", query=" + query +
                ", start=" + start +
                ", duration=" + duration +
                ", resultCount=" + resultCount +
                '}';
    }
}
