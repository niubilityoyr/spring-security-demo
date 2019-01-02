package com.oyr.config;

import com.oyr.properties.CasServerConfig;
import com.oyr.properties.CasServiceConfig;
import com.oyr.service.MyUserDetailsService;
import org.jasig.cas.client.proxy.ProxyGrantingTicketStorageImpl;
import org.jasig.cas.client.validation.Cas20ServiceTicketValidator;
import org.jasig.cas.client.validation.TicketValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.cas.ServiceProperties;
import org.springframework.security.cas.authentication.CasAuthenticationProvider;
import org.springframework.security.cas.web.CasAuthenticationEntryPoint;
import org.springframework.security.web.AuthenticationEntryPoint;

/**
 * Created by Administrator on 2018/12/28.
 */
@Configuration
public class CasSecurityConfig {


    @Autowired
    private CasServerConfig casServerConfig;

    @Autowired
    private CasServiceConfig casServiceConfig;


    /**
     * 指定service（Cas概念）相关属性，AuthenticationEntryPoint需要这个bean
     * 主要是指定在Cas Server认证成功后将要跳转的地址。
     *
     * @return
     */
    @Bean
    public ServiceProperties serviceProperties() {
        ServiceProperties serviceProperties = new ServiceProperties();
        // Cas Server认证成功后的跳转地址，这里要跳转到我们的Spring Security应用，之后会由CasAuthenticationFilter处理，
        // 默认处理地址为/j_spring_cas_security_check，这里已经改变默认值了，那么就随之改变
        serviceProperties.setService(casServiceConfig.getHost() + casServiceConfig.getLogin());
        serviceProperties.setAuthenticateAllArtifacts(true);
        return serviceProperties;
    }

    // 认证的入口，用于cas认证处理
    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint(@Qualifier("serviceProperties") ServiceProperties properties) {
        CasAuthenticationEntryPoint entryPoint
                = new CasAuthenticationEntryPoint();
        entryPoint.setLoginUrl(casServerConfig.getHost() + casServerConfig.getLogin());// Cas Server的登录地址
        entryPoint.setServiceProperties(properties); // service相关属性
        return entryPoint;
    }

    @Bean
    public ProxyGrantingTicketStorageImpl proxyGrantingTicketStorage() {
        return new ProxyGrantingTicketStorageImpl();
    }

    // 配置TicketValidator在登录认证成功后验证ticket
    @Bean
    public TicketValidator ticketValidator(@Qualifier("proxyGrantingTicketStorage") ProxyGrantingTicketStorageImpl proxyGrantingTicketStorage) {
        // 指定 cas 校验器
        /*Cas20ProxyTicketValidator ticketValidator = new Cas20ProxyTicketValidator(casServerHostUrl);
        ticketValidator.setProxyGrantingTicketStorage(proxyGrantingTicketStorage);*/
        // 构造参数Cas Server访问地址的前缀，即根路径
        Cas20ServiceTicketValidator ticketValidator = new Cas20ServiceTicketValidator(casServerConfig.getHost());
        return ticketValidator;
    }

    /**
     * CasAuthenticationFilter会将封装好的包含Cas Server传递过来的ticket的Authentication对象传递给AuthenticationManager进行认证。
     * 我们知道默认的AuthenticationManager实现类为ProviderManager，而ProviderManager中真正进行认证的是AuthenticationProvider。
     * 所以接下来我们要在AuthenticationManager中配置一个能够处理CasAuthenticationFilter传递过来的Authentication对象的AuthenticationProvider实现，
     * CasAuthenticationProvider。CasAuthenticationProvider首先会利用TicketValidator（Cas概念）对Authentication中包含的ticket信息进行认证。
     * 认证通过后将利用持有的AuthenticationUserDetailsService根据认证通过后回传的Assertion对象中拥有的username加载用户对应的UserDetails，
     * 即主要是加载用户的相关权限信息GrantedAuthority。然后构造一个CasAuthenticationToken进行返回。之后的逻辑就是正常的Spring Security的逻辑了。
     *
     * @param properties
     * @param validator
     * @param userDetails
     * @return
     */
    // cas认证
    @Bean
    public CasAuthenticationProvider casAuthenticationProvider(@Qualifier("serviceProperties") ServiceProperties properties,
                                                               @Qualifier("ticketValidator") TicketValidator validator,
                                                               MyUserDetailsService userDetails) {
        CasAuthenticationProvider provider = new CasAuthenticationProvider();
        provider.setUserDetailsService(userDetails); // 具体实现的UserDetailsService，通过username获取用户信息
        provider.setServiceProperties(properties); // service的属性
        provider.setTicketValidator(validator); // 配置TicketValidator在登录认证成功后验证ticket
        provider.setKey("key4CasAuthenticationProvider"); // 这个key暂时不知道干嘛的
        return provider;
    }
}
