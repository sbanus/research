package com.research.entity;
import com.research.service.FavourModelService;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Component;

@Component
@Configurable
public class FavourModelDataOnDemand {

	private Random rnd = new SecureRandom();

	private List<FavourModel> data;

	@Autowired
    UserModelDataOnDemand userModelDataOnDemand;

	@Autowired
    FavourModelService favourModelService;

	public FavourModel getNewTransientFavourModel(int index) {
        FavourModel obj = new FavourModel();
        setDescription(obj, index);
        setUserModel(obj, index);
        return obj;
    }

	public void setDescription(FavourModel obj, int index) {
        String description = "description_" + index;
        obj.setDescription(description);
    }

	public void setUserModel(FavourModel obj, int index) {
        UserModel userModel = userModelDataOnDemand.getRandomUserModel();
        obj.setUserModel(userModel);
    }

	public FavourModel getSpecificFavourModel(int index) {
        init();
        if (index < 0) {
            index = 0;
        }
        if (index > (data.size() - 1)) {
            index = data.size() - 1;
        }
        FavourModel obj = data.get(index);
        Long id = obj.getId();
        return favourModelService.findFavourModel(id);
    }

	public FavourModel getRandomFavourModel() {
        init();
        FavourModel obj = data.get(rnd.nextInt(data.size()));
        Long id = obj.getId();
        return favourModelService.findFavourModel(id);
    }

	public boolean modifyFavourModel(FavourModel obj) {
        return false;
    }

	public void init() {
        int from = 0;
        int to = 10;
        data = favourModelService.findFavourModelEntries(from, to);
        if (data == null) {
            throw new IllegalStateException("Find entries implementation for 'FavourModel' illegally returned null");
        }
        if (!data.isEmpty()) {
            return;
        }
        
        data = new ArrayList<FavourModel>();
        for (int i = 0; i < 10; i++) {
            FavourModel obj = getNewTransientFavourModel(i);
            try {
                favourModelService.saveFavourModel(obj);
            } catch (final ConstraintViolationException e) {
                final StringBuilder msg = new StringBuilder();
                for (Iterator<ConstraintViolation<?>> iter = e.getConstraintViolations().iterator(); iter.hasNext();) {
                    final ConstraintViolation<?> cv = iter.next();
                    msg.append("[").append(cv.getRootBean().getClass().getName()).append(".").append(cv.getPropertyPath()).append(": ").append(cv.getMessage()).append(" (invalid value = ").append(cv.getInvalidValue()).append(")").append("]");
                }
                throw new IllegalStateException(msg.toString(), e);
            }
            obj.flush();
            data.add(obj);
        }
    }
}
