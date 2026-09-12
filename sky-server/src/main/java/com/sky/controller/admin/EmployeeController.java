package com.sky.controller.admin;

import com.sky.constant.JwtClaimsConstant;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.MenuDTO;
import com.sky.dto.MenuItem;
import com.sky.entity.Employee;
import com.sky.entity.Menu;
import com.sky.mapper.EmployeeMapper;
import com.sky.mapper.MenuMapper;
import com.sky.properties.JwtProperties;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.EmployeeService;
import com.sky.utils.JwtUtil;
import com.sky.vo.EmployeeLoginVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 员工管理
 */
@RestController
@RequestMapping("/admin/employee")
@Slf4j
@Api(tags = "员工相关接口")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private JwtProperties jwtProperties;
    @Autowired
    private MenuMapper menuMapper;

    /**
     * 登录
     * @param employeeLoginDTO
     * @return
     */
    @PostMapping("/login")
    @ApiOperation("员工登录")
    public Result<EmployeeLoginVO> login(@RequestBody EmployeeLoginDTO employeeLoginDTO) {
        log.info("员工登录：{}", employeeLoginDTO);

        Employee employee = employeeService.login(employeeLoginDTO);

        //登录成功后，生成jwt令牌
        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.EMP_ID, employee.getId());
        String token = JwtUtil.createJWT(
                jwtProperties.getAdminSecretKey(),
                jwtProperties.getAdminTtl(), //设置时间
                claims);                     //todo 设置为null 过期时间用redis检索

        List<Menu> menuList = menuMapper.getMenuList();
        List<MenuDTO> routeMenu = getRouteMenu(menuList);

        EmployeeLoginVO employeeLoginVO = EmployeeLoginVO.builder()
                .id(employee.getId())
                .userName(employee.getUsername())
                .name(employee.getName())
                .token(token)
                .menuTitles(menuList)
                .menuList(routeMenu)
                .build();

        return Result.success(employeeLoginVO);
    }


    /**
     * 获取动态菜单
     * @param menuList
     * @return
     */
    private List<MenuDTO> getRouteMenu(List<Menu> menuList) {
        List<MenuDTO> routeList = new ArrayList<>();
        for (int i = 1; i < 6; i++) { //5次
            MenuItem menuItem;
            List<MenuItem> menuItems;
            for (Menu e : menuList) {
                if(e.getId() == i){
                    MenuDTO route = new MenuDTO();
                    route.setId(e.getId());
                    route.setTitle(e.getName());
                    route.setIndex(e.getPath());
                    route.setIcon(e.getIcon());
                    menuItems = new ArrayList<>();
                    for (Menu e2 : menuList){
                        if(e2.getPid() == i){
                            menuItem = new MenuItem();
                            menuItem.setTitle(e2.getName());
                            menuItem.setIndex(e2.getPath());
//                            menuItem.setComponent(item.getComponent());
                            menuItems.add(menuItem);
                        }
                    }
                    route.setMenuItems(menuItems);
                    routeList.add(route);
                }
            }
        }
        return routeList;
    }

}
