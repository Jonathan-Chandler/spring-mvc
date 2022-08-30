package com.jonathan.web.presentation.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.stereotype.Controller;

@Controller
public class IndexController
{
  @RequestMapping(value = "/")
  public String displayIndex(Model model)
  {
    model.addAttribute("test123");
    return "indexpage";
  }
}

