package com.research.controllers;
import com.research.entity.UserModel;
import com.research.service.FavourModelService;
import com.research.service.UserModelService;
import java.io.UnsupportedEncodingException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.UriUtils;
import org.springframework.web.util.WebUtils;

@RequestMapping("/usermodels")
@Controller
public class UserModelController {

	@Autowired
    UserModelService userModelService;

	@Autowired
    FavourModelService favourModelService;

	@RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String create(@Valid UserModel userModel, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, userModel);
            return "usermodels/create";
        }
        uiModel.asMap().clear();
        userModelService.saveUserModel(userModel);
        return "redirect:/usermodels/" + encodeUrlPathSegment(userModel.getId().toString(), httpServletRequest);
    }

	@RequestMapping(params = "form", produces = "text/html")
    public String createForm(Model uiModel) {
        populateEditForm(uiModel, new UserModel());
        return "usermodels/create";
    }

	@RequestMapping(value = "/{id}", produces = "text/html")
    public String show(@PathVariable("id") Long id, Model uiModel) {
        uiModel.addAttribute("usermodel", userModelService.findUserModel(id));
        uiModel.addAttribute("itemId", id);
        return "usermodels/show";
    }

	@RequestMapping(produces = "text/html")
    public String list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("usermodels", userModelService.findUserModelEntries(firstResult, sizeNo));
            float nrOfPages = (float) userModelService.countAllUserModels() / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("usermodels", userModelService.findAllUserModels());
        }
        return "usermodels/list";
    }

	@RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String update(@Valid UserModel userModel, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, userModel);
            return "usermodels/update";
        }
        uiModel.asMap().clear();
        userModelService.updateUserModel(userModel);
        return "redirect:/usermodels/" + encodeUrlPathSegment(userModel.getId().toString(), httpServletRequest);
    }

	@RequestMapping(value = "/{id}", params = "form", produces = "text/html")
    public String updateForm(@PathVariable("id") Long id, Model uiModel) {
        populateEditForm(uiModel, userModelService.findUserModel(id));
        return "usermodels/update";
    }

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
    public String delete(@PathVariable("id") Long id, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        UserModel userModel = userModelService.findUserModel(id);
        userModelService.deleteUserModel(userModel);
        uiModel.asMap().clear();
        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
        return "redirect:/usermodels";
    }

	void populateEditForm(Model uiModel, UserModel userModel) {
        uiModel.addAttribute("userModel", userModel);
        uiModel.addAttribute("favourmodels", favourModelService.findAllFavourModels());
    }

	String encodeUrlPathSegment(String pathSegment, HttpServletRequest httpServletRequest) {
        String enc = httpServletRequest.getCharacterEncoding();
        if (enc == null) {
            enc = WebUtils.DEFAULT_CHARACTER_ENCODING;
        }
        try {
            pathSegment = UriUtils.encodePathSegment(pathSegment, enc);
        } catch (UnsupportedEncodingException uee) {}
        return pathSegment;
    }
}
