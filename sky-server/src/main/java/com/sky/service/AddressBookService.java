package com.sky.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sky.context.BaseContext;
import com.sky.entity.AddressBook;
import com.sky.exception.AddressBookBusinessException;
import com.sky.mapper.AddressBookMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author: Joey
 * @Description:
 * @date:2024/9/18 23:38
 */
@Service
public class AddressBookService {

    @Autowired
    private AddressBookMapper addressBookMapper;

    public List<AddressBook> list(AddressBook addressBook){
        return addressBookMapper.selectList(new QueryWrapper<AddressBook>()
                .or(i -> i.eq("user_id", addressBook.getUserId())
                        .or()
                        .eq("phone", addressBook.getPhone())
                        .or()
                        .eq("is_default", addressBook.getIsDefault())));
    }

    @Transactional
    public void save(AddressBook addressBook){
        addressBook.setUserId(BaseContext.getCurrentId());
//        addressBook.setIsDefault(0);
        if(addressBook.getIsDefault() == 1){
            addressBook.setIsDefault(0);
            addressBook.setUserId(BaseContext.getCurrentId());
            addressBookMapper.updateIsDefaultByUserId(addressBook);
        }
        addressBook.setIsDefault(1);
        addressBookMapper.insert(addressBook);
    }

    @Transactional
    public void update(AddressBook addressBook){
        try {
            if(addressBook.getIsDefault() == 1){
                addressBook.setIsDefault(0);
                addressBook.setUserId(BaseContext.getCurrentId());
                addressBookMapper.updateIsDefaultByUserId(addressBook);
            }
            addressBook.setIsDefault(1);
            addressBookMapper.updateById(addressBook);

        } catch (Exception e) {
            throw new AddressBookBusinessException("更新地址簿失败");
        }
    }

    @Transactional
    public void setDefault(AddressBook addressBook){
        //1、将当前用户的所有地址修改为非默认地址


        //2、将当前地址改为默认地址
        addressBook.setIsDefault(1);
        addressBookMapper.updateById(addressBook);
    }



}
