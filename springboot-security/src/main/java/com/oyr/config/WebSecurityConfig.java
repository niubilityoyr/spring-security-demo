package com.oyr.config;

import com.oyr.domain.Permission;
import com.oyr.properties.CasServerConfig;
import com.oyr.properties.CasServiceConfig;
import com.oyr.service.MyFilterSecurityInterceptor;
import com.oyr.service.PermissionService;
import org.jasig.cas.client.proxy.ProxyGrantingTicketStorageImpl;
import org.jasig.cas.client.session.SingleSignOutFilter;
import org.jasig.cas.client.session.SingleSignOutHttpSessionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.cas.ServiceProperties;
import org.springframework.security.cas.web.CasAuthenticationFilter;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.sql.DataSource;
import java.util.List;

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
    private PermissionService permissionService;

    @Autowired
    private AuthenticationProvider casAuthenticationProvider;

    @Autowired
    private AuthenticationEntryPoint authenticationEntryPoint;

    @Autowired
    private CasServiceConfig casServiceConfig;

    @Autowired
    private CasServerConfig casServerConfig;

    // cas 认证过滤器
    public CasAuthenticationFilter casAuthenticationFilter() throws Exception {
        CasAuthenticationFilter casAuthenticationFilter = new CasAuthenticationFilter();
        casAuthenticationFilter.setAuthenticationManager(authenticationManager()); // 设置认证管理器
        casAuthenticationFilter.setFilterProcessesUrl(casServiceConfig.getLogin()); // 指定处理地址，不指定时默认将会是/j_spring_cas_security_check
        return casAuthenticationFilter;
    }

    // 配置一个SingleSignOutHttpSessionListener用于在Session过期时删除SingleSignOutFilter存放的对应信息
    @Bean
    public SingleSignOutHttpSessionListener singleSignOutHttpSessionListener() {
        return new SingleSignOutHttpSessionListener();
    }

    // ===== cas 单点登出 start

    // client登出filter
    public SingleSignOutFilter singleSignOutFilter() {
        return new SingleSignOutFilter();
    }

    // 当前client 登出成功后，去请求真正的cas server 单点登出，会通知所有的client进行登出
    public LogoutFilter requestCasLogoutFilter() {
        // 指定client登出成功后需要跳转的地址，这里指向Cas Server的登出URL，以实现单点登出
        LogoutFilter logoutFilter = new LogoutFilter(casServerConfig.getHost() + casServerConfig.getLogout(), new SecurityContextLogoutHandler());
        // 该Filter需要处理的地址，默认是Spring Security的默认登出地址/j_spring_security_logout
        logoutFilter.setFilterProcessesUrl(casServiceConfig.getLogout());
        return logoutFilter;
    }

    // ===== cas 单点登出 end

/*    public MyFilterSecurityInterceptor myFilterSecurityInterceptor(AccessDecisionManager myAccessDecisionManager, ) {
        MyFilterSecurityInterceptor myFilterSecurityInterceptor = new MyFilterSecurityInterceptor();
        myFilterSecurityInterceptor.setMyAccessDecisionManager(myAccessDecisionManager);
        myFilterSecurityInterceptor.setSecurityMetadataSource();
        return myFilterSecurityInterceptor;
    }*/

    @Autowired
    private MyFilterSecurityInterceptor myFilterSecurityInterceptor;

    @Override
    protected void configure(HttpSecurity http) throws Exception { // 主要做拦截请求
        // 认证的一些设置
        http.formLogin().loginPage("/authentication/require")
                .loginProcessingUrl("/authentication/login").usernameParameter("username").passwordParameter("password")
                .successHandler(authenticationSuccessHandler).failureHandler(authenticationFailureHandler);

        // 记住我的一些设置
        /*http.rememberMe().tokenRepository(persistentTokenRepository()).userDetailsService(userDetailsService)
                .rememberMeParameter("remember_me").rememberMeCookieName("security-remember")
                .tokenValiditySeconds(180);*/

        // 登出的一些设置
        http.logout().logoutUrl("/logout").permitAll();
        //.deleteCookies("sessionId").logoutSuccessHandler(logoutSuccessHandler);

        // 403处理
        http.exceptionHandling().accessDeniedHandler(accessDeniedHandler);

        // session的一些设置
//        http.sessionManagement().invalidSessionUrl("/session/invalid").maximumSessions(1);


        // 权限的一些设置
        ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry authorizeRequests = http.authorizeRequests();
        authorizeRequests
                .antMatchers("/", "/authentication/require", "/login", "/loginError",
                        "/logoutSuccess", "/403", "/validateCode/code", "/session/invalid").permitAll(); // 这些url都是不需要检查权限
        /*List<Permission> permissionList = permissionService.findAll(); // 数据库保存的都要鉴权
        for (Permission permission : permissionList) {
            authorizeRequests.antMatchers(permission.getUrl()).hasAnyAuthority(permission.getTag());
        }*/

        authorizeRequests.antMatchers("/**").hasAnyAuthority("asdasdasdasd")
                //anyRequest().authenticated() // 指定后面的所有请求都需要身份认证
                .and().csrf().disable(); // 关闭csrf


        // 验证码过滤器
       /* ValidateCodeFilter validateCodeFilter = new ValidateCodeFilter();
        validateCodeFilter.setAuthenticationFailureHandler(authenticationFailureHandler);*/

        // 验证码过滤器添加到处理认证过滤器的前面
//        http.addFilterBefore(validateCodeFilter, UsernamePasswordAuthenticationFilter.class);

        // FILTER_SECURITY_INTERCEPTOR
        http.addFilterBefore(myFilterSecurityInterceptor, FilterSecurityInterceptor.class);

        // 将应用的登录认证入口改为使用CasAuthenticationEntryPoint
        /*http.exceptionHandling().authenticationEntryPoint(authenticationEntryPoint)
                .and()
                .addFilter(casAuthenticationFilter())   // 将 casAuthenticationFilter 新增到过滤器链中
                .addFilterBefore(singleSignOutFilter(), CasAuthenticationFilter.class) // SingleSignOutFilter放在CasAuthenticationFilter之前
                .addFilterBefore(requestCasLogoutFilter(), SingleSignOutFilter.class) // 请求登出Cas Server的过滤器，放在Spring Security的登出过滤器之前
        ;*/
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception { // 配置user-detail服务
        // 修改security 认证，使用我们指定的 cas 认证
        // auth.authenticationProvider(casAuthenticationProvider);
        // super.configure(auth);

        // 从db中取数据信息使用 userDetailsService，并且指定一个密码加密器
        auth.userDetailsService(userDetailsService).passwordEncoder(new BCryptPasswordEncoder());

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
