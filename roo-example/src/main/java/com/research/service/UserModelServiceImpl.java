package com.research.service;

import com.research.entity.UserModel;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserModelServiceImpl implements UserModelService {

	public long countAllUserModels() {
        return UserModel.countUserModels();
    }

	public void deleteUserModel(UserModel userModel) {
        userModel.remove();
    }

	public UserModel findUserModel(Long id) {
        return UserModel.findUserModel(id);
    }

	public List<UserModel> findAllUserModels() {
        return UserModel.findAllUserModels();
    }

	public List<UserModel> findUserModelEntries(int firstResult, int maxResults) {
        return UserModel.findUserModelEntries(firstResult, maxResults);
    }

	public void saveUserModel(UserModel userModel) {
        userModel.persist();
    }

	public UserModel updateUserModel(UserModel userModel) {
        return userModel.merge();
    }
}
