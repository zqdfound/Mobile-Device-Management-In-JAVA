package com.easyback.infra.service;

import com.easyback.infra.entity.Permission;
import com.baomidou.mybatisplus.extension.service.IService;
import com.easyback.model.vo.PermissionVO;

import java.util.List;

/**
 * <p>
 * 权限表 服务类
 * </p>
 *
 * @author zhuangqingdian
 * @since 2023-07-04
 */
public interface PermissionService extends IService<Permission> {

    List<PermissionVO> getAllList();

    List<String> listAllName();

    List<String> getListByUserId(Integer userId);
}
