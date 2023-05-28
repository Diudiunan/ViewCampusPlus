package com.qin.viewcampus.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qin.viewcampus.dao.UserInformationDao;
import com.qin.viewcampus.entity.UserInformation;
import com.qin.viewcampus.service.IUserInformationService;
import org.springframework.stereotype.Service;

@Service
public class UserInformationServiceImpl extends ServiceImpl<UserInformationDao, UserInformation> implements IUserInformationService {

}
