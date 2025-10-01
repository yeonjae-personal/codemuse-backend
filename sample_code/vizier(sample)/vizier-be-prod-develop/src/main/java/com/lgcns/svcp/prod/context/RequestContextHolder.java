package com.lgcns.svcp.prod.context;

public class RequestContextHolder {
    private static final ThreadLocal<RequestMetadata> context = new ThreadLocal<>();

    private static RequestMetadata getOrCreate() {
        if (context.get() == null) {
            context.set(new RequestMetadata(null, null));
        }
        return context.get();
    }

    public static void setUuid(String uuid) {
        getOrCreate().setUuid(uuid);
    }

    public static void setCode(String code) {
        getOrCreate().setCode(code);
    }

    public static void set(String uuid, String code) {
        context.set(new RequestMetadata(uuid, code));
    }

    public static RequestMetadata get() {
        return context.get();
    }

    public static void clear() {
        context.remove();
    }

    public static class RequestMetadata {
        private String uuid;
        private String code;

        public RequestMetadata(String uuid, String code) {
            this.uuid = uuid;
            this.code = code;
        }

        public void setUuid(String uuid) { this.uuid = uuid; }
        public void setCode(String code) { this.code = code; }

        public String getUuid() { return uuid; }
        public String getCode() { return code; }
    }
}