package com.ps.config.tenant;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
import org.jboss.logging.Logger;


public class CurrentTenantIdentifierResolverImpl
        implements CurrentTenantIdentifierResolver {

	Logger logger = Logger.getLogger(CurrentTenantIdentifierResolverImpl.class);
	
    private static final String DEFAULT_TENANT_ID = "PaysquareDefault";

    @Override
    public String resolveCurrentTenantIdentifier() {
        
    	String tenant = TenantContextHolder.getTenant();
    	if(logger.isDebugEnabled()) logger.debug("In resolveCurrentTenantIdentifier tenant "+tenant);
    	if(logger.isDebugEnabled()) logger.debug("In resolveCurrentTenantIdentifier DEFAULT_TENANT_ID "+DEFAULT_TENANT_ID);

        return (!StringUtils.isBlank(tenant)) ? tenant : DEFAULT_TENANT_ID;
    }

    @Override
    public boolean validateExistingCurrentSessions() {
        return true;
    }
}
