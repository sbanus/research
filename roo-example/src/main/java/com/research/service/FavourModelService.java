package com.research.service;
import com.research.entity.FavourModel;
import java.util.List;

public interface FavourModelService {

	public abstract long countAllFavourModels();


	public abstract void deleteFavourModel(FavourModel favourModel);


	public abstract FavourModel findFavourModel(Long id);


	public abstract List<FavourModel> findAllFavourModels();


	public abstract List<FavourModel> findFavourModelEntries(int firstResult, int maxResults);


	public abstract void saveFavourModel(FavourModel favourModel);


	public abstract FavourModel updateFavourModel(FavourModel favourModel);

}
