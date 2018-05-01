package com.example.controller;

import javax.validation.Valid;

import com.example.model.BankAccountInfo;
import com.example.service.BankAccountDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import com.example.model.User;
import com.example.service.UserService;

import java.util.List;

@Controller
public class LoginController {
	@GetMapping("/index")
	public String home(ModelMap model) {
		model.put("message", "JSP!!!");
		return "index";
	}


	@Autowired
	private UserService userService;
	@Autowired
	private BankAccountDAO bankAccountDAO;
//@RequestMapping(value={"/", "/login"}, method = RequestMethod.GET)
	@RequestMapping(value="/login", method = RequestMethod.GET)
	public ModelAndView login(){
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("login");
		return modelAndView;
	}
	
	
	@RequestMapping(value="/registration", method = RequestMethod.GET)
	public ModelAndView registration(){
		ModelAndView modelAndView = new ModelAndView();
		User user = new User();
		modelAndView.addObject("user", user);
		modelAndView.setViewName("registration");
		return modelAndView;
	}
	
	@RequestMapping(value = "/registration", method = RequestMethod.POST)
	public ModelAndView createNewUser(@Valid User user, BindingResult bindingResult) {
		ModelAndView modelAndView = new ModelAndView();
		User userExists = userService.findUserByEmail(user.getEmail());
		if (userExists != null) {
			bindingResult
					.rejectValue("email", "error.user",
							"There is already a user registered with the email provided");
		}
		if (bindingResult.hasErrors()) {
			modelAndView.setViewName("registration");
		} else {
			userService.saveUser(user);
			modelAndView.addObject("successMessage", "User has been registered successfully");
			modelAndView.addObject("user", new User());
			modelAndView.setViewName("registration");
			
		}
		return modelAndView;
	}
	
	@RequestMapping(value="/admin/home", method = RequestMethod.GET)
	public ModelAndView home(){
		ModelAndView modelAndView = new ModelAndView();
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User user = userService.findUserByEmail(auth.getName());
		modelAndView.addObject("userName", "Welcome " + user.getName() + " " + user.getLastName() + " (" + user.getEmail() + ")");
		modelAndView.addObject("adminMessage","Message for User");
		modelAndView.setViewName("admin/home");
		return modelAndView;
	}
	@RequestMapping(value = "admin/catalog", method = RequestMethod.GET)
	public String showBankAccounts(Model model) {
		List<BankAccountInfo> list = bankAccountDAO.getBankAccounts();
		model.addAttribute("accountInfos", list);
		return "admin/catalog";
	}
/*	@RequestMapping(value = "admin/contacts", method = RequestMethod.GET)
	public  String contacts(Model model) {
		return "admin/contacts";
	}*/
@RequestMapping(value="/admin/contacts", method = RequestMethod.GET)
public ModelAndView contacts(){
	ModelAndView modelAndView = new ModelAndView();
	Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	User user = userService.findUserByEmail(auth.getName());
	modelAndView.addObject("userName", "Welcome " + user.getName() + " " + user.getLastName() + " (" + user.getEmail() + ")");
	modelAndView.addObject("adminMessage","Message for User");
	modelAndView.setViewName("admin/contacts");
	return modelAndView;
}

}
