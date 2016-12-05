package com.gome.gmp.common.mybaits.plugins.page;


import org.apache.commons.lang3.StringUtils;

public class MySQLDialect extends Dialect{

	public boolean supportsLimitOffset(){
		return true;
	}
	
    public boolean supportsLimit() {   
        return true;   
    }  
    
	public String getLimitString(String sql, int offset,String offsetPlaceholder, int limit, String limitPlaceholder,String sortOrder,String sortField) {
        if (StringUtils.isNotEmpty(sortOrder) && StringUtils.isNotEmpty(sortField)){
            sql += " order by " + sortField + " " + sortOrder;
        }
        if (offset > 0) {
        	return sql + " limit "+offsetPlaceholder+","+limitPlaceholder; 
        } else {   
            return sql + " limit "+limitPlaceholder;
        }  
	}   
  
}
