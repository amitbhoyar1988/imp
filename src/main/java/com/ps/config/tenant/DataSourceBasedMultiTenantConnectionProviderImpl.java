package com.ps.config.tenant;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.sql.DataSource;

import org.hibernate.engine.jdbc.connections.spi.AbstractDataSourceBasedMultiTenantConnectionProviderImpl;
import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.ps.RESTful.enums.ErrorCode;
import com.ps.RESTful.error.handler.ResourceNotFoundException;
import com.ps.entities.master.GroupDBMaster;
import com.ps.services.GroupDBMasterService;


@Configuration
public class DataSourceBasedMultiTenantConnectionProviderImpl
        extends AbstractDataSourceBasedMultiTenantConnectionProviderImpl {

	Logger logger = Logger.getLogger(DataSourceBasedMultiTenantConnectionProviderImpl.class);

    private static final long serialVersionUID = 1L;

    /**
     * Injected MasterTenantRepository to access the tenant information from the master_tenant table
     */

    @Autowired
    private GroupDBMasterService groupDBMasterService;
    
    /**
     * Map to store the tenant ids as key and the data source as the value
     */
    private Map<String, DataSource> dataSourcesMap = new TreeMap<>();

    @Override
    protected DataSource selectAnyDataSource() {
        // This method is called more than once. So check if the data source map
        // is empty. If it is then rescan master_tenant table for all tenant
        // entries.
        if (dataSourcesMap.isEmpty()) {
            List<GroupDBMaster> masterTenants = groupDBMasterService.getAll();
            logger.info(">>>> selectAnyDataSource() -- Total tenants:" + masterTenants.size());
            for (GroupDBMaster masterTenant : masterTenants) {
            	logger.info(">>>> selectAnyDataSource() --creating for client  datasource:" + masterTenant.getDatabaseName());
            	dataSourcesMap.put(masterTenant.getGroupName(),
                        DataSourceUtil.createAndConfigureDataSource(masterTenant));
            }
        }
        return this.dataSourcesMap.values().iterator().next();
    }

    @Override
    protected DataSource selectDataSource(String tenantIdentifier) {
        // If the requested tenant id is not present check for it in the master
        // database 'master_tenant' table

       // tenantIdentifier = initializeTenantIfLost(tenantIdentifier);

        if(logger.isDebugEnabled()) logger.debug("In selectDataSource() -- tenant:" + tenantIdentifier);
        
        if (!this.dataSourcesMap.containsKey(tenantIdentifier)) {
        	List<GroupDBMaster> masterTenants = groupDBMasterService.getAll();
        	if(logger.isDebugEnabled()) logger.debug(
                    ">>>> selectDataSource() -- tenant:" + tenantIdentifier + " Total tenants:" + masterTenants.size());
            for (GroupDBMaster masterTenant : masterTenants) {
                dataSourcesMap.put(masterTenant.getDatabaseName(),
                        DataSourceUtil.createAndConfigureDataSource(masterTenant));
            }
        }
        //check again if tenant exist in map after rescan master_db, if not, throw UsernameNotFoundException
        if (!this.dataSourcesMap.containsKey(tenantIdentifier)) {
            logger.warn("Trying to get tenant:" + tenantIdentifier + " which was not found in datasource map");
                  
            throw new ResourceNotFoundException(ErrorCode.RESOURCE_NOT_FOUND, "Tenant Not found");
        }
                    
         ComboPooledDataSource ds = (ComboPooledDataSource) this.dataSourcesMap.get(tenantIdentifier);
         if(ds != null) {
             if(logger.isDebugEnabled()) logger.debug(" --Current tenant:" + tenantIdentifier + " -- tenant url" + ds.getJdbcUrl()); 
         }               
         
        return ds;
    }

//    private String initializeTenantIfLost(String tenantIdentifier) {
//		
//		  if (TenantContextHolder.getTenant() == null) {
//		  
//		  SecurityContext securityContext = SecurityContextHolder.getContext();
//		  Authentication authentication = securityContext.getAuthentication();
//		  CustomUserDetails customUserDetails = null; 
//		  if (authentication != null) {
//		  Object principal = authentication.getPrincipal(); 
//		  customUserDetails = principal instanceof CustomUserDetails ? (CustomUserDetails) principal : null;
//		  } 
//		  TenantContextHolder.setTenantId(customUserDetails.getTenant()); 
//		}
//
//		  if (tenantIdentifier != TenantContextHolder.getTenant()) { tenantIdentifier =
//		  TenantContextHolder.getTenant(); }
//		 
//        return tenantIdentifier;
//    }
    
}
