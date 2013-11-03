package com.research.entity;
import com.research.service.FavourModelService;
import java.util.Iterator;
import java.util.List;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

@Configurable
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:/META-INF/spring/applicationContext*.xml")
@Transactional
public class FavourModelIntegrationTest {

    @Test
    public void testMarkerMethod() {
    }

	@Autowired
    FavourModelDataOnDemand dod;

	@Autowired
    FavourModelService favourModelService;

	@Test
    public void testCountAllFavourModels() {
        Assert.assertNotNull("Data on demand for 'FavourModel' failed to initialize correctly", dod.getRandomFavourModel());
        long count = favourModelService.countAllFavourModels();
        Assert.assertTrue("Counter for 'FavourModel' incorrectly reported there were no entries", count > 0);
    }

	@Test
    public void testFindFavourModel() {
        FavourModel obj = dod.getRandomFavourModel();
        Assert.assertNotNull("Data on demand for 'FavourModel' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'FavourModel' failed to provide an identifier", id);
        obj = favourModelService.findFavourModel(id);
        Assert.assertNotNull("Find method for 'FavourModel' illegally returned null for id '" + id + "'", obj);
        Assert.assertEquals("Find method for 'FavourModel' returned the incorrect identifier", id, obj.getId());
    }

	@Test
    public void testFindAllFavourModels() {
        Assert.assertNotNull("Data on demand for 'FavourModel' failed to initialize correctly", dod.getRandomFavourModel());
        long count = favourModelService.countAllFavourModels();
        Assert.assertTrue("Too expensive to perform a find all test for 'FavourModel', as there are " + count + " entries; set the findAllMaximum to exceed this value or set findAll=false on the integration test annotation to disable the test", count < 250);
        List<FavourModel> result = favourModelService.findAllFavourModels();
        Assert.assertNotNull("Find all method for 'FavourModel' illegally returned null", result);
        Assert.assertTrue("Find all method for 'FavourModel' failed to return any data", result.size() > 0);
    }

	@Test
    public void testFindFavourModelEntries() {
        Assert.assertNotNull("Data on demand for 'FavourModel' failed to initialize correctly", dod.getRandomFavourModel());
        long count = favourModelService.countAllFavourModels();
        if (count > 20) count = 20;
        int firstResult = 0;
        int maxResults = (int) count;
        List<FavourModel> result = favourModelService.findFavourModelEntries(firstResult, maxResults);
        Assert.assertNotNull("Find entries method for 'FavourModel' illegally returned null", result);
        Assert.assertEquals("Find entries method for 'FavourModel' returned an incorrect number of entries", count, result.size());
    }

	@Test
    public void testFlush() {
        FavourModel obj = dod.getRandomFavourModel();
        Assert.assertNotNull("Data on demand for 'FavourModel' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'FavourModel' failed to provide an identifier", id);
        obj = favourModelService.findFavourModel(id);
        Assert.assertNotNull("Find method for 'FavourModel' illegally returned null for id '" + id + "'", obj);
        boolean modified =  dod.modifyFavourModel(obj);
        Integer currentVersion = obj.getVersion();
        obj.flush();
        Assert.assertTrue("Version for 'FavourModel' failed to increment on flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }

	@Test
    public void testUpdateFavourModelUpdate() {
        FavourModel obj = dod.getRandomFavourModel();
        Assert.assertNotNull("Data on demand for 'FavourModel' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'FavourModel' failed to provide an identifier", id);
        obj = favourModelService.findFavourModel(id);
        boolean modified =  dod.modifyFavourModel(obj);
        Integer currentVersion = obj.getVersion();
        FavourModel merged = favourModelService.updateFavourModel(obj);
        obj.flush();
        Assert.assertEquals("Identifier of merged object not the same as identifier of original object", merged.getId(), id);
        Assert.assertTrue("Version for 'FavourModel' failed to increment on merge and flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }

	@Test
    public void testSaveFavourModel() {
        Assert.assertNotNull("Data on demand for 'FavourModel' failed to initialize correctly", dod.getRandomFavourModel());
        FavourModel obj = dod.getNewTransientFavourModel(Integer.MAX_VALUE);
        Assert.assertNotNull("Data on demand for 'FavourModel' failed to provide a new transient entity", obj);
        Assert.assertNull("Expected 'FavourModel' identifier to be null", obj.getId());
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
        Assert.assertNotNull("Expected 'FavourModel' identifier to no longer be null", obj.getId());
    }

	@Test
    public void testDeleteFavourModel() {
        FavourModel obj = dod.getRandomFavourModel();
        Assert.assertNotNull("Data on demand for 'FavourModel' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'FavourModel' failed to provide an identifier", id);
        obj = favourModelService.findFavourModel(id);
        favourModelService.deleteFavourModel(obj);
        obj.flush();
        Assert.assertNull("Failed to remove 'FavourModel' with identifier '" + id + "'", favourModelService.findFavourModel(id));
    }
}
