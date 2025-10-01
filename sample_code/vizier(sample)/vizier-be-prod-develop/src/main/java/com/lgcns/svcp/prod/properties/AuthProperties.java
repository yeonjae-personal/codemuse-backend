package com.lgcns.svcp.prod.properties;

import java.util.Arrays;
import java.util.List;

public class AuthProperties {

    public final static List<String> ALLOW_URIS = Arrays.asList(
            "/prod/swagger-ui/index.html",
            "/prod/ui/admin/label/language",
            "/prod/ui/admin/label/i18n",
            "/prod/error"
    );

    public final static List<String> WEBSOCKET_URIS = Arrays.asList(
            "/prod/ws"
    );

    public final static List<String> ALLOW_INCLUDED_URIS = Arrays.asList(
            "/export",
            "/import",
            "/swagger-ui",
            "/api-docs"
    );

    public final static String FILE_UPLOAD = "/import";

    public final static List<String> ALLOW_FILES = Arrays.asList(
            "",
            ""
    );

}
