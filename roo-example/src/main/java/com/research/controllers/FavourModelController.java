package com.research.controllers;
import com.research.entity.FavourModel;
import com.research.service.FavourModelService;
import com.research.service.UserModelService;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
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

@RequestMapping("/favourmodels")
@Controller
public class FavourModelController {

	@Autowired
    FavourModelService favourModelService;

	@Autowired
    UserModelService userModelService;

	@RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String create(@Valid FavourModel favourModel, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, favourModel);
            return "favourmodels/create";
        }
        uiModel.asMap().clear();
        favourModelService.saveFavourModel(favourModel);
        return "redirect:/favourmodels/" + encodeUrlPathSegment(favourModel.getId().toString(), httpServletRequest);
    }

	@RequestMapping(params = "form", produces = "text/html")
    public String createForm(Model uiModel) {
        populateEditForm(uiModel, new FavourModel());
        List<String[]> dependencies = new ArrayList<String[]>();
        if (userModelService.countAllUserModels() == 0) {
            dependencies.add(new String[] { "usermodel", "usermodels" });
        }
        uiModel.addAttribute("dependencies", dependencies);
        return "favourmodels/create";
    }

	@RequestMapping(value = "/{id}", produces = "text/html")
    public String show(@PathVariable("id") Long id, Model uiModel) {
        uiModel.addAttribute("favourmodel", favourModelService.findFavourModel(id));
        uiModel.addAttribute("itemId", id);
        return "favourmodels/show";
    }

	@RequestMapping(produces = "text/html")
    public String list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("favourmodels", favourModelService.findFavourModelEntries(firstResult, sizeNo));
            float nrOfPages = (float) favourModelService.countAllFavourModels() / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("favourmodels", favourModelService.findAllFavourModels());
        }
        return "favourmodels/list";
    }

	@RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String update(@Valid FavourModel favourModel, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, favourModel);
            return "favourmodels/update";
        }
        uiModel.asMap().clear();
        favourModelService.updateFavourModel(favourModel);
        return "redirect:/favourmodels/" + encodeUrlPathSegment(favourModel.getId().toString(), httpServletRequest);
    }

	@RequestMapping(value = "/{id}", params = "form", produces = "text/html")
    public String updateForm(@PathVariable("id") Long id, Model uiModel) {
        populateEditForm(uiModel, favourModelService.findFavourModel(id));
        return "favourmodels/update";
    }

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
    public String delete(@PathVariable("id") Long id, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        FavourModel favourModel = favourModelService.findFavourModel(id);
        favourModelService.deleteFavourModel(favourModel);
        uiModel.asMap().clear();
        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
        return "redirect:/favourmodels";
    }

	void populateEditForm(Model uiModel, FavourModel favourModel) {
        uiModel.addAttribute("favourModel", favourModel);
        uiModel.addAttribute("usermodels", userModelService.findAllUserModels());
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
