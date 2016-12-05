package com.gome.gmp.ws.login.pi;

import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;

import com.gome.framework.AppContext;
import com.gome.framework.Env;

public class LoginUtil {

//	/**
//	 * 加密、解密key.
//	 */
//	private static final String PASSWORD_CRYPT_KEY = "gomedscn";

	public final static boolean checkLogin(String userId, String password) {
		Env env = AppContext.getEnv();
		if("false".equals(env.getProperty("gomeGmp.loginWs.validateSwitch"))){
			return true;
		}
		//String address = "http://10.58.53.17:801/LoginService.asmx";
		String address = env.getProperty("gomeGmp.loginWs.validateUrl");
		String cryptKey = env.getProperty("gomeGmp.loginWs.passwordCryptKey");
		//String address = "http://10.58.50.212:8998/DomainService.asmx"; // 此处最好用系统参数
		JaxWsProxyFactoryBean bean = new JaxWsProxyFactoryBean();
		bean.setAddress(address);
		bean.setServiceClass(DomainServiceSoap.class);
		DomainServiceSoap ws = (DomainServiceSoap) bean.create();
		try {

			String result = ws.validLogon(userId, DesUtil.encrypt(password,
					cryptKey));
			if(result != null & result.equals("Y")){
			return true;
			}else {
			return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
}
