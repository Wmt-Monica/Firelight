package cn.dreamjun.uc.service.impl;

import cn.dreamjun.uc.pojo.UcUserInfo;
import cn.dreamjun.uc.mapper.UcUserInfoMapper;
import cn.dreamjun.uc.service.IUserInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 翊
 * @since 2022-09-09
 */
@Service
public class UserInfoServiceImpl extends ServiceImpl<UcUserInfoMapper, UcUserInfo> implements IUserInfoService {

}
