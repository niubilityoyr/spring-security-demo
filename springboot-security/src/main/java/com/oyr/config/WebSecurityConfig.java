package com.oyr.config;

import com.oyr.properties.CasServerConfig;
import com.oyr.properties.CasServiceConfig;
import org.jasig.cas.client.proxy.ProxyGrantingTicketStorageImpl;
import org.jasig.cas.client.session.SingleSignOutFilter;
import org.jasig.cas.client.session.SingleSignOutHttpSessionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.cas.ServiceProperties;
import org.springframework.security.cas.web.CasAuthenticationFilter;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.sql.DataSource;

/**
 * Created by Administrator on 2018/12/21.
 */
@EnableWebSecurity  // 开启spring security web支持
@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private AuthenticationSuccessHandler authenticationSuccessHandler;

    @Autowired
    private AuthenticationFailureHandler authenticationFailureHandler;

    @Autowired
    private AccessDeniedHandler accessDeniedHandler;

    @Autowired
    private LogoutSuccessHandler logoutSuccessHandler;

    @Autowired
    private DataSource dataSource;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private ServiceProperties serviceProperties;

    @Autowired
    private ProxyGrantingTicketStorageImpl proxyGrantingTicketStorage;

    @Autowired
    private AuthenticationProvider casAuthenticationProvider;

    @Autowired
    private AuthenticationEntryPoint authenticationEntryPoint;

    @Autowired
    private CasServiceConfig casServiceConfig;

    @Autowired
    private CasServerConfig casServerConfig;

    public CasAuthenticationFilter casAuthenticationFilter() throws Exception {
        CasAuthenticationFilter casAuthenticationFilter = new CasAuthenticationFilter();
        casAuthenticationFilter.setAuthenticationManager(authenticationManager()); // 设置认证过滤器
        casAuthenticationFilter.setFilterProcessesUrl(casServiceConfig.getLogin()); // 指定处理地址，不指定时默认将会是/j_spring_cas_security_check
        return casAuthenticationFilter;
    }

    // 配置一个SingleSignOutHttpSessionListener用于在Session过期时删除SingleSignOutFilter存放的对应信息
    @EventListener
    public SingleSignOutHttpSessionListener singleSignOutHttpSessionListener() {
        return new SingleSignOutHttpSessionListener();
    }

    // 单点登出filter
    public SingleSignOutFilter singleSignOutFilter() {
        return new SingleSignOutFilter();
    }

    /*public CasAuthenticationFilter casAuthenticationFilter() throws Exception {
        CasAuthenticationFilter casAuthenticationFilter = new CasAuthenticationFilter();
        casAuthenticationFilter.setAuthenticationManager(authenticationManager());
        casAuthenticationFilter.setServiceProperties(serviceProperties);
        casAuthenticationFilter.setProxyGrantingTicketStorage(proxyGrantingTicketStorage);
        casAuthenticationFilter.setProxyReceptorUrl(appServerHostUrl + "/proxyCallback");
        casAuthenticationFilter.setAuthenticationDetailsSource(new ServiceAuthenticationDetailsSource(serviceProperties));
        casAuthenticationFilter.setAuthenticationFailureHandler(new SimpleUrlAuthenticationFailureHandler(CasConfing.casServiceFailureHandler));
        return casAuthenticationFilter;
    }*/



    public LogoutFilter requestCasLogoutFilter() {
        // 指定登出成功后需要跳转的地址，这里指向Cas Server的登出URL，以实现单点登出
        LogoutFilter logoutFilter = new LogoutFilter(casServerConfig.getHost() + casServerConfig.getLogout(), new SecurityContextLogoutHandler());
        // 该Filter需要处理的地址，默认是Spring Security的默认登出地址/j_spring_security_logout
        logoutFilter.setFilterProcessesUrl("/logout");
        return logoutFilter;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception { // 主要做拦截请求
        // 认证的一些设置
      /*  http.formLogin().loginPage("/authentication/require")
                .loginProcessingUrl("/authentication/login").usernameParameter("username").passwordParameter("password")
                .successHandler(authenticationSuccessHandler).failureHandler(authenticationFailureHandler);*/

        // 记住我的一些设置
        /*http.rememberMe().tokenRepository(persistentTokenRepository()).userDetailsService(userDetailsService)
                .rememberMeParameter("remember_me").rememberMeCookieName("security-remember")
                .tokenValiditySeconds(180);*/

        // 登出的一些设置
        http.logout().logoutUrl("/logout").permitAll();
        //.deleteCookies("sessionId").logoutSuccessHandler(logoutSuccessHandler);

        // 403处理
//        http.exceptionHandling().accessDeniedHandler(accessDeniedHandler);

        // session的一些设置
//        http.sessionManagement().invalidSessionUrl("/session/invalid").maximumSessions(1);

        // 权限的一些设置
        http.authorizeRequests()
                .antMatchers("/", "/authentication/require", "/login", "/loginError", "/logoutSuccess", "/403", "/validateCode/code", "/session/invalid").permitAll() // 权限的一些设置url
                .antMatchers("/order*").hasRole("USER") // 指定要访问/order* 都必须要有user
                .antMatchers("/admin*").hasRole("ADMIN") // 指定要访问/admin* 都必须要有admin
                .anyRequest().authenticated() // 指定后面的所有请求都需要身份认证
                .and()
                .csrf().disable(); // 关闭csrf

        // 验证码过滤器
       /* ValidateCodeFilter validateCodeFilter = new ValidateCodeFilter();
        validateCodeFilter.setAuthenticationFailureHandler(authenticationFailureHandler);*/

        // 验证码过滤器添加到处理认证过滤器的前面
//        http.addFilterBefore(validateCodeFilter, UsernamePasswordAuthenticationFilter.class);

        // 将应用的登录认证入口改为使用CasAuthenticationEntryPoint
        http.exceptionHandling().authenticationEntryPoint(authenticationEntryPoint)
                .and()
                .addFilter(casAuthenticationFilter())   // 将 casAuthenticationFilter 新增到过滤器链中
                .addFilterBefore(singleSignOutFilter(), CasAuthenticationFilter.class) // SingleSignOutFilter放在CasAuthenticationFilter之前

        ;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception { // 配置user-detail服务
        // 修改security 认证，使用我们指定的 cas 认证
        auth.authenticationProvider(casAuthenticationProvider);
        // super.configure(auth);

        // 从db中取数据信息使用 userDetailsService，并且指定一个密码加密器
        // auth.userDetailsService(userDetailsService).passwordEncoder(new BCryptPasswordEncoder());

       /*
       // 设置一个密码加密器，不然程序会报错，定义两个用户存在内存中，密码必须要加密一下
       auth.inMemoryAuthentication().passwordEncoder(new BCryptPasswordEncoder())
                .withUser("user").password(new BCryptPasswordEncoder().encode("user")).roles("USER")
                .and().
                withUser("admin").password(new BCryptPasswordEncoder().encode("admin")).roles("USER", "ADMIN");*/
    }

    @Bean
    public PersistentTokenRepository persistentTokenRepository() {
        JdbcTokenRepositoryImpl tokenRepository = new JdbcTokenRepositoryImpl();
        tokenRepository.setDataSource(dataSource);
        return tokenRepository;
    }
}
