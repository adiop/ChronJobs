package com.warn.chron.chronjobs.application.common;

import lombok.Data;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

public @Data class RestServiceConfig {

    private String  protocol;

    private String hostName;

    private Integer  port;

    private String urlPrefix;

    private String key;

    private String subject;

    private String recipientTestEmail;

    private List<String> recipientCCTestEmailList;

    public UriComponentsBuilder getUri() {
        return UriComponentsBuilder.newInstance().scheme(getProtocol()).host(getHostName()).port(port).path(getUrlPrefix());
    }
}
