package com.research.service;
import com.research.entity.UserModel;
import java.util.List;

public interface UserModelService {

	public abstract long countAllUserModels();


	public abstract void deleteUserModel(UserModel userModel);


	public abstract UserModel findUserModel(Long id);


	public abstract List<UserModel> findAllUserModels();


	public abstract List<UserModel> findUserModelEntries(int firstResult, int maxResults);


	public abstract void saveUserModel(UserModel userModel);


	public abstract UserModel updateUserModel(UserModel userModel);

}
