package com.sjwi.catalog.controller.setlist;

import com.sjwi.catalog.aspect.IgnoreAspect;
import com.sjwi.catalog.model.SetList;
import com.sjwi.catalog.service.SetListService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@IgnoreAspect
public class SetListPollController {

    @Autowired
    SetListService setListService;

	@RequestMapping(value = {"setlist/getSetListObject/{id}"}, method = RequestMethod.GET)
	@ResponseBody
	public SetList getSetListObject(@PathVariable int id){
		return setListService.getSetListById(id);
	}
}
