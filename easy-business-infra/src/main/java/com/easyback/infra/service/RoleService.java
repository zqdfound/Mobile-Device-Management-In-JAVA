package com.easyback.infra.service;

import com.easyback.infra.base.PageVO;
import com.easyback.infra.entity.Role;
import com.baomidou.mybatisplus.extension.service.IService;
import com.easyback.model.dto.RoleAddDTO;
import com.easyback.model.dto.RoleQryDTO;
import com.easyback.model.dto.RoleUpdateDTO;
import com.easyback.model.vo.RolePermVO;
import com.easyback.model.vo.RoleVO;

import java.util.List;

/**
 * <p>
 * 角色表 服务类
 * </p>
 *
 * @author zhuangqingdian
 * @since 2023-07-04
 */
public interface RoleService extends IService<Role> {

    PageVO<RoleVO> getRolePage(RoleQryDTO dto);

    List<RoleVO> getAllList();

    Boolean addRole(RoleAddDTO dto);

    Boolean updateRole(RoleUpdateDTO dto);

    RolePermVO getRolePermissionDetail(Integer id);

    List<String> getListByUserId(Integer userId);
}
