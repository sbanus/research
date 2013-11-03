package com.research.entity;
import com.research.service.UserModelService;
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
public class UserModelIntegrationTest {

    @Test
    public void testMarkerMethod() {
    }

	@Autowired
    UserModelDataOnDemand dod;

	@Autowired
    UserModelService userModelService;

	@Test
    public void testCountAllUserModels() {
        Assert.assertNotNull("Data on demand for 'UserModel' failed to initialize correctly", dod.getRandomUserModel());
        long count = userModelService.countAllUserModels();
        Assert.assertTrue("Counter for 'UserModel' incorrectly reported there were no entries", count > 0);
    }

	@Test
    public void testFindUserModel() {
        UserModel obj = dod.getRandomUserModel();
        Assert.assertNotNull("Data on demand for 'UserModel' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'UserModel' failed to provide an identifier", id);
        obj = userModelService.findUserModel(id);
        Assert.assertNotNull("Find method for 'UserModel' illegally returned null for id '" + id + "'", obj);
        Assert.assertEquals("Find method for 'UserModel' returned the incorrect identifier", id, obj.getId());
    }

	@Test
    public void testFindAllUserModels() {
        Assert.assertNotNull("Data on demand for 'UserModel' failed to initialize correctly", dod.getRandomUserModel());
        long count = userModelService.countAllUserModels();
        Assert.assertTrue("Too expensive to perform a find all test for 'UserModel', as there are " + count + " entries; set the findAllMaximum to exceed this value or set findAll=false on the integration test annotation to disable the test", count < 250);
        List<UserModel> result = userModelService.findAllUserModels();
        Assert.assertNotNull("Find all method for 'UserModel' illegally returned null", result);
        Assert.assertTrue("Find all method for 'UserModel' failed to return any data", result.size() > 0);
    }

	@Test
    public void testFindUserModelEntries() {
        Assert.assertNotNull("Data on demand for 'UserModel' failed to initialize correctly", dod.getRandomUserModel());
        long count = userModelService.countAllUserModels();
        if (count > 20) count = 20;
        int firstResult = 0;
        int maxResults = (int) count;
        List<UserModel> result = userModelService.findUserModelEntries(firstResult, maxResults);
        Assert.assertNotNull("Find entries method for 'UserModel' illegally returned null", result);
        Assert.assertEquals("Find entries method for 'UserModel' returned an incorrect number of entries", count, result.size());
    }

	@Test
    public void testFlush() {
        UserModel obj = dod.getRandomUserModel();
        Assert.assertNotNull("Data on demand for 'UserModel' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'UserModel' failed to provide an identifier", id);
        obj = userModelService.findUserModel(id);
        Assert.assertNotNull("Find method for 'UserModel' illegally returned null for id '" + id + "'", obj);
        boolean modified =  dod.modifyUserModel(obj);
        Integer currentVersion = obj.getVersion();
        obj.flush();
        Assert.assertTrue("Version for 'UserModel' failed to increment on flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }

	@Test
    public void testUpdateUserModelUpdate() {
        UserModel obj = dod.getRandomUserModel();
        Assert.assertNotNull("Data on demand for 'UserModel' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'UserModel' failed to provide an identifier", id);
        obj = userModelService.findUserModel(id);
        boolean modified =  dod.modifyUserModel(obj);
        Integer currentVersion = obj.getVersion();
        UserModel merged = userModelService.updateUserModel(obj);
        obj.flush();
        Assert.assertEquals("Identifier of merged object not the same as identifier of original object", merged.getId(), id);
        Assert.assertTrue("Version for 'UserModel' failed to increment on merge and flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }

	@Test
    public void testSaveUserModel() {
        Assert.assertNotNull("Data on demand for 'UserModel' failed to initialize correctly", dod.getRandomUserModel());
        UserModel obj = dod.getNewTransientUserModel(Integer.MAX_VALUE);
        Assert.assertNotNull("Data on demand for 'UserModel' failed to provide a new transient entity", obj);
        Assert.assertNull("Expected 'UserModel' identifier to be null", obj.getId());
        try {
            userModelService.saveUserModel(obj);
        } catch (final ConstraintViolationException e) {
            final StringBuilder msg = new StringBuilder();
            for (Iterator<ConstraintViolation<?>> iter = e.getConstraintViolations().iterator(); iter.hasNext();) {
                final ConstraintViolation<?> cv = iter.next();
                msg.append("[").append(cv.getRootBean().getClass().getName()).append(".").append(cv.getPropertyPath()).append(": ").append(cv.getMessage()).append(" (invalid value = ").append(cv.getInvalidValue()).append(")").append("]");
            }
            throw new IllegalStateException(msg.toString(), e);
        }
        obj.flush();
        Assert.assertNotNull("Expected 'UserModel' identifier to no longer be null", obj.getId());
    }

	@Test
    public void testDeleteUserModel() {
        UserModel obj = dod.getRandomUserModel();
        Assert.assertNotNull("Data on demand for 'UserModel' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'UserModel' failed to provide an identifier", id);
        obj = userModelService.findUserModel(id);
        userModelService.deleteUserModel(obj);
        obj.flush();
        Assert.assertNull("Failed to remove 'UserModel' with identifier '" + id + "'", userModelService.findUserModel(id));
    }
}
