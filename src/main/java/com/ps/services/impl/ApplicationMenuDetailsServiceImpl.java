package com.ps.services.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ps.RESTful.enums.SuccessCode;
import com.ps.RESTful.error.handler.NoContentFoundException;
import com.ps.beans.ApplicationMenuHierarchyBean;
import com.ps.entities.master.ApplicationMenus;
import com.ps.services.ApplicationMenuDetailsService;
import com.ps.services.dao.repository.master.ApplicationMenuDetailsRepository;
import com.ps.services.dao.repository.master.ApplicationMenusRepository;
import com.ps.util.StringUtils;

@Service
public class ApplicationMenuDetailsServiceImpl implements ApplicationMenuDetailsService {

	Logger logger = Logger.getLogger(getClass());

	@Autowired
	ApplicationMenuDetailsRepository applicationMenuDetailsRepository;

	@Autowired
	ApplicationMenusRepository applicationMenusRepository;

	@Override
	public List<ApplicationMenuHierarchyBean> getAllMenues() {

		if (logger.isDebugEnabled())
			logger.debug("In GetAllMenus from ApplicationMenuDetails Service");

		// Fetching-All-Exist-Menus
		// List<ApplicationMenuDetails> allExistMenus =
		// applicationMenuDetailsRepository.findAll();

		List<ApplicationMenus> allExistMenus = applicationMenusRepository.findAll();

		if (allExistMenus.isEmpty()) {
			if (logger.isDebugEnabled())
				logger.debug("Application Menu Details are not exist");
			throw new NoContentFoundException(SuccessCode.NO_CONTENT, "Application Menu Details not found!");
		}

		if (logger.isDebugEnabled())
			logger.debug("Total:" + allExistMenus.size() + " Application Menu Details found");

		// Set-information-into-bean
		List<ApplicationMenuHierarchyBean> menuBean = new ArrayList<ApplicationMenuHierarchyBean>();

		// Get-All-Parent-Menu
		List<ApplicationMenus> parentMenus = allExistMenus.stream()
				.filter(menufromDB -> menufromDB.getParentMenuId() == 0 && menufromDB.getIsActive() == true)
				.collect(Collectors.toList());

		// if-Parent-Exist-Search-for-its-childs
		if (!parentMenus.isEmpty()) {

			for (ApplicationMenus iterateByParent : parentMenus) {
				ApplicationMenuHierarchyBean parentsChildMenuBean = new ApplicationMenuHierarchyBean();

				// Searching-child's-for-this-parent
				parentsChildMenuBean.setApplicationMenuId(iterateByParent.getApplicationMenuId());
				parentsChildMenuBean.setParentMenuId(iterateByParent.getParentMenuId());
				parentsChildMenuBean.setMenuName(iterateByParent.getMenuName());
				parentsChildMenuBean.setMenuDescription(iterateByParent.getMenuDescription());
				parentsChildMenuBean.setIsActive(iterateByParent.getIsActive());
				parentsChildMenuBean.setCreatedBy(iterateByParent.getCreatedBy());
				parentsChildMenuBean.setCreatedDateTime(StringUtils.dateToString(iterateByParent.getCreatedDateTime()));

				// fetch-all-child-items
				List<ApplicationMenus> childItems = allExistMenus.stream()
						.filter(childs -> childs.getParentMenuId() == iterateByParent.getApplicationMenuId())
						.collect(Collectors.toList());

				List<ApplicationMenuHierarchyBean> childHierarchybean = new ArrayList<>();

				if (!childItems.isEmpty()) {
					childHierarchybean = getChildMenus(childItems, allExistMenus);
					parentsChildMenuBean.setChildItems(childHierarchybean);
				} else {
					parentsChildMenuBean.setChildItems(null);
				}

				// adding-into-mainBean
				menuBean.add(parentsChildMenuBean);
			} // for-Close-parentMenu

		} else {
			if (logger.isDebugEnabled())
				logger.debug("No Parent Menu found");

			throw new NoContentFoundException(SuccessCode.NO_CONTENT, "Parent Menus are not exist!");
			// break;
		}

		// }//forClose

		// return allExistMenus
		return menuBean;
	}

	// To-Find-out-Child-is-parent?-if-Yes-Collect-his-hierarchy-and-return
	private List<ApplicationMenuHierarchyBean> getChildMenus(List<ApplicationMenus> childs,
			List<ApplicationMenus> allExistMenus) {

		List<ApplicationMenuHierarchyBean> childBeanslist = new ArrayList<>();

		for (ApplicationMenus childIterate : childs) {

			ApplicationMenuHierarchyBean childBeans = new ApplicationMenuHierarchyBean();

			childBeans.setApplicationMenuId(childIterate.getApplicationMenuId());
			childBeans.setParentMenuId(childIterate.getParentMenuId());
			childBeans.setMenuName(childIterate.getMenuName());
			childBeans.setMenuDescription(childIterate.getMenuDescription());
			childBeans.setIsActive(childIterate.getIsActive());
			childBeans.setCreatedBy(childIterate.getCreatedBy());
			childBeans.setCreatedDateTime(StringUtils.dateToString(childIterate.getCreatedDateTime()));

			List<ApplicationMenus> childAsParentList = allExistMenus.stream()
					.filter(child -> child.getParentMenuId() == childIterate.getApplicationMenuId())
					.collect(Collectors.toList());

			List<ApplicationMenuHierarchyBean> childAsParentBean = new ArrayList<ApplicationMenuHierarchyBean>();

			if (!childAsParentList.isEmpty()) {
				if (logger.isDebugEnabled())
					logger.debug("Child is parent of " + childAsParentList.size() + " Child/'s");

				childAsParentBean = getChildMenus(childAsParentList, allExistMenus);

				childBeans.setChildItems(childAsParentBean);
			} else {
				if (logger.isDebugEnabled())
					logger.debug("Child is not parent");
				childBeans.setChildItems(null);
				
				//Add-Possible-Access-Rights
				HashMap<String, Boolean> possibleAccessRights = new HashMap<String, Boolean>();
				possibleAccessRights.put("readAccess", null);
				possibleAccessRights.put("writeAccess", null);
				possibleAccessRights.put("modifyAccess", null);
				possibleAccessRights.put("deleteAccess", null);

				childBeans.setPossibleAccessRights(possibleAccessRights);

			}

			childBeanslist.add(childBeans);

		} // Child-Iterate

		return childBeanslist;

	}// functionClose

}
