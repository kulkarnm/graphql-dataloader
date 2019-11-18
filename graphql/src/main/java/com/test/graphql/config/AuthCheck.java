package com.test.graphql.config;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.event.EventListener;

import javax.servlet.http.HttpServletRequest;

public class AuthCheck {


    @EventListener(ApplicationEvent.class)
    public void onApplicationStart() {
        try {
            this.loadAuthList();
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }

    private void loadAuthList() {

    }

    public String isAuthGranted(HttpServletRequest request, String query) {
        if (null == query) {
            query = "";
        }

        query = StringUtils.normalizeSpace(query);
        String queryHash = DigestUtils.sha256Hex(query);
        return queryHash;
    }
} 
