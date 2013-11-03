package com.research.service;

import com.research.entity.FavourModel;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class FavourModelServiceImpl implements FavourModelService {

	public long countAllFavourModels() {
        return FavourModel.countFavourModels();
    }

	public void deleteFavourModel(FavourModel favourModel) {
        favourModel.remove();
    }

	public FavourModel findFavourModel(Long id) {
        return FavourModel.findFavourModel(id);
    }

	public List<FavourModel> findAllFavourModels() {
        return FavourModel.findAllFavourModels();
    }

	public List<FavourModel> findFavourModelEntries(int firstResult, int maxResults) {
        return FavourModel.findFavourModelEntries(firstResult, maxResults);
    }

	public void saveFavourModel(FavourModel favourModel) {
        favourModel.persist();
    }

	public FavourModel updateFavourModel(FavourModel favourModel) {
        return favourModel.merge();
    }
}
