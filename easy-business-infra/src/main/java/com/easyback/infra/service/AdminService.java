package com.easyback.infra.service;

import cn.dev33.satoken.stp.SaTokenInfo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.easyback.infra.base.PageVO;
import com.easyback.infra.entity.Admin;
import com.easyback.model.dto.*;
import com.easyback.model.vo.AdminVO;
import com.easyback.model.vo.MyInfoVO;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * <p>
 * 管理员表 服务类
 * </p>
 *
 * @author zhuangqingdian
 * @since 2023-07-04
 */
public interface AdminService extends IService<Admin> {

    SaTokenInfo adminLogin(AdminLoginDTO dto, HttpServletRequest request);


    PageVO<AdminVO> getAdminPage(AdminQryDTO dto);
    PageVO<AdminVO> getAdminPageWithoutSuperAdmin(AdminQryDTO dto);
    List<AdminVO> getAdminListWithoutSuperAdmin(AdminQryDTO dto);

    AdminVO getAdminDetail(Integer id);

    Boolean enabled(Integer id);

    Boolean addUser(AdminAddDTO dto);

    Boolean updateUser(AdminUpdateDTO dto);

    MyInfoVO myInfo();

    Boolean updateMyInfo(MyInfoUpdateDTO dto);


}
