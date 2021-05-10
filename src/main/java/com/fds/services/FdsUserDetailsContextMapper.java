package com.fds.services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.core.DirContextAdapter;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.ldap.userdetails.UserDetailsContextMapper;

import com.fds.entities.FdsLdapUserDetails;
import com.fds.entities.FdsSysUser;
import com.vaadin.spring.annotation.SpringComponent;

@SpringComponent
public class FdsUserDetailsContextMapper implements UserDetailsContextMapper {
	@Autowired
	private SysUserService sysUserService;
	@Autowired
	private SysTxnService sysTxnService;
	private static final String DOMAIN_NAME = "@scb.com.vn";
	private static final String DEFAULT_USER_TYPE = "OFF";

	@Override
	public UserDetails mapUserFromContext(DirContextOperations ctx, String username, Collection<? extends GrantedAuthority> arg2) {
		FdsSysUser sysUser = sysUserService.findByUserid(username.toLowerCase());
		FdsLdapUserDetails userDetails = new FdsLdapUserDetails();
		if (sysUser != null) {
//			sysUserService.updateLastLogin(sysUser.getUserid());
			userDetails.setId(sysUser.getUserid());
			userDetails.setAuthorities(setAuthority(sysUser.getUserid(), sysUser.getUsertype()));
			userDetails.setUsername(sysUser.getEmail());
			userDetails.setLastlogin(sysUser.getLastlogin());
			userDetails.setFullname(sysUser.getFullname());
		}
		return userDetails;
	}

	@Override
	public void mapUserToContext(UserDetails arg0, DirContextAdapter arg1) {
		// TODO Auto-generated method stub

	}

	private Collection<GrantedAuthority> setAuthority(String userid, String userType) {
		List<Object[]> listMenu = sysTxnService.findAllByUserId(userid);
		Collection<GrantedAuthority> listAuth = new ArrayList<>();
		listAuth.add(new SimpleGrantedAuthority(userType));
		if (!listMenu.isEmpty()) {
			for (Object[] b : listMenu) {
				if (b[1] != null) {
					listAuth.add(new SimpleGrantedAuthority(String.valueOf(b[1]).toUpperCase()));
				}
			}
		}
		return listAuth;
	}

}
