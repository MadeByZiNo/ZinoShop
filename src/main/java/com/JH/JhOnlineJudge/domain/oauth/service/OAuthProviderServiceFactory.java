package com.JH.JhOnlineJudge.domain.oauth.service;

import com.JH.JhOnlineJudge.domain.oauth.domain.OAuthProvider;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class OAuthProviderServiceFactory {

    private final Map<OAuthProvider, OAuthService> serviceMap = new HashMap<>();

    public OAuthProviderServiceFactory(List<OAuthService> services) {
        for (OAuthService service : services) {
            serviceMap.put(service.getProvider(), service);
        }
    }

    public OAuthService getProviderService(OAuthProvider provider) {
        return serviceMap.get(provider);
    }
}
