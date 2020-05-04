package org.javaboy.vhr.service;

import org.apache.commons.lang3.StringUtils;
import org.javaboy.vhr.mapper.MenuMapper;
import org.javaboy.vhr.mapper.MenuRoleMapper;
import org.javaboy.vhr.model.Hr;
import org.javaboy.vhr.model.Menu;
import org.javaboy.vhr.model.MenuRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @作者 江南一点雨
 * @公众号 江南一点雨
 * @微信号 a_java_boy
 * @GitHub https://github.com/lenve
 * @博客 http://wangsong.blog.csdn.net
 * @网站 http://www.javaboy.org
 * @时间 2019-09-27 7:13
 */
@Service
@CacheConfig(cacheNames = "menus_cache")
public class MenuService {
    @Autowired
    MenuMapper menuMapper;
    @Autowired
    MenuRoleMapper menuRoleMapper;

    private ArrayList<Menu> openMenus = new ArrayList<Menu>();
    private List<Menu> allOpenMenus = new ArrayList<Menu>();

    public List<Menu> getMenusByHrId() {
        return menuMapper.getMenusByHrId(((Hr) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId());
    }

    public List<Menu> getAllOpenMenus() {
        openMenus = new ArrayList<Menu>();
        allOpenMenus = menuMapper.getAllOpenMenus();
        //找出所有父菜单
        for (int i = allOpenMenus.size() - 1; i >= 0; i--) {
            Menu aParentMenu = allOpenMenus.get(i);
            if (aParentMenu.getParentId() == null){
                openMenus.add(aParentMenu);
                allOpenMenus.remove(i);
            }
        }
        //递归下去设置子菜单
        for (int i = openMenus.size() - 1; i >= 0; i--) {
            setChildenMenu(openMenus.get(i));
        }
        Collections.reverse(openMenus);
        System.out.println("请求了一次open路由菜单");
        System.out.println(allOpenMenus.size());
        System.out.println();
        return openMenus;
    }

    private void setChildenMenu(Menu menu) {
        ArrayList<Menu> childenList = new ArrayList<>();
        for (int i = allOpenMenus.size() - 1; i >= 0; i--) {
            if (allOpenMenus.get(i).getParentId().intValue() == menu.getId().intValue()){
                childenList.add(allOpenMenus.get(i));
                setChildenMenu(allOpenMenus.get(i));
                menu.setChildren(childenList);
                allOpenMenus.remove(i);
            }
        }
    }

    @Cacheable
    public List<Menu> getAllMenusWithRole() {
        return menuMapper.getAllMenusWithRole();
    }

    public List<Menu> getAllMenus() {
        return menuMapper.getAllMenus();
    }

    public List<Integer> getMidsByRid(Integer rid) {
        return menuMapper.getMidsByRid(rid);
    }

    @Transactional
    public boolean updateMenuRole(Integer rid, Integer[] mids) {
        menuRoleMapper.deleteByRid(rid);
        if (mids == null || mids.length == 0) {
            return true;
        }
        Integer result = menuRoleMapper.insertRecord(rid, mids);
        return result==mids.length;
    }
}
