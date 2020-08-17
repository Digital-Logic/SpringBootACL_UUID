package net.digitallogic.aclUidTest.config;

import net.digitallogic.aclUidTest.security.Authorities;
import net.digitallogic.aclUidTest.security.sql.AclSqlSelector;
import org.springframework.cache.ehcache.EhCacheFactoryBean;
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.acls.AclPermissionEvaluator;
import org.springframework.security.acls.domain.*;
import org.springframework.security.acls.jdbc.BasicLookupStrategy;
import org.springframework.security.acls.jdbc.JdbcMutableAclService;
import org.springframework.security.acls.jdbc.LookupStrategy;
import org.springframework.security.acls.model.PermissionGrantingStrategy;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.sql.DataSource;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class AclSecurityConfig extends GlobalMethodSecurityConfiguration {

    private final DataSource dataSource;
    private final AclSqlSelector aclSqlSelector;

    public AclSecurityConfig(DataSource dataSource, AclSqlSelector aclSqlSelector) {
        this.dataSource = dataSource;
        this.aclSqlSelector = aclSqlSelector;
    }

    @Override
    protected MethodSecurityExpressionHandler createExpressionHandler() {
        DefaultMethodSecurityExpressionHandler expressionHandler = new DefaultMethodSecurityExpressionHandler();

        AclPermissionEvaluator permissionEvaluator = new AclPermissionEvaluator(createAclService());
        expressionHandler.setPermissionEvaluator(permissionEvaluator);
        return expressionHandler;
    }

    @Bean
    public JdbcMutableAclService createAclService() {
        JdbcMutableAclService aclService = new JdbcMutableAclService(
                dataSource,
                lookupStrategy(),
                aclCache()
        );

        aclService.setObjectIdentityPrimaryKeyQuery(aclSqlSelector.getObjectIdentityPrimaryKeyQuery());
        aclService.setInsertObjectIdentitySql(aclSqlSelector.getInsertObjectIdentitySql());
        aclService.setFindChildrenQuery(aclSqlSelector.getFindChildQuery());
        aclService.setClassIdentityQuery(aclSqlSelector.getClassIdentityQuery());
        aclService.setSidIdentityQuery(aclSqlSelector.getSidIdentityQuery());

        return aclService;
    }


    @Bean
    public LookupStrategy lookupStrategy() {
        BasicLookupStrategy strategy = new BasicLookupStrategy(
            dataSource,
            aclCache(),
            aclAuthorizationStrategy(),
            permissionGrantingStrategy()
        );

        strategy.setLookupObjectIdentitiesWhereClause(aclSqlSelector.getLookupObjectIdentitiesWhereClause());
        strategy.setSelectClause(aclSqlSelector.getSelectClauseColumn());

        return strategy;
    }

    @Bean
    public PermissionGrantingStrategy permissionGrantingStrategy() {
        return new DefaultPermissionGrantingStrategy(
                new ConsoleAuditLogger()
        );
    }

    @Bean
    public AclAuthorizationStrategy aclAuthorizationStrategy() {
        return new AclAuthorizationStrategyImpl(
                new SimpleGrantedAuthority(Authorities.ADMIN_ACL_AUTHORITY.name)
        );
    }

    @Bean
    public EhCacheManagerFactoryBean cacheManagerFactory() {
        return new EhCacheManagerFactoryBean();
    }

    @Bean
    public EhCacheFactoryBean cacheFactoryBean() {
        EhCacheFactoryBean factory = new EhCacheFactoryBean();
        factory.setCacheManager(cacheManagerFactory().getObject());
        factory.setName("aclCache");
        return factory;
    }

    @Bean
    public EhCacheBasedAclCache aclCache() {
        return new EhCacheBasedAclCache(
                cacheFactoryBean().getObject(),
                permissionGrantingStrategy(),
                aclAuthorizationStrategy()
        );
    }
}
