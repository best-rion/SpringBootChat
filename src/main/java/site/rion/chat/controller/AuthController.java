package site.rion.chat.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import site.rion.chat.dto.UserDTO;
import site.rion.chat.model.ChatUser;
import site.rion.chat.repository.UserRepository;

@Controller
public class AuthController {
	@Autowired
	private UserRepository userRepository;
	
	@GetMapping("/login")
	public String loginGet()
	{
		return "login";
	}
	
	@GetMapping("/signup")
	public String signupGet(Model model)
	{
		model.addAttribute("userInfo", new UserDTO());
		
		return "signup";
	}
	
	@PostMapping("/signup")
	public String signupPost(@ModelAttribute UserDTO user)
	{
    	if ( user.getPassword().equals(user.getConfirm_password()))
    	{
    		ChatUser newUser = new ChatUser();
    		newUser.setUsername(user.getUsername());
    		newUser.setPassword(user.getPassword());
    		
    		userRepository.save(newUser);
    		return "redirect:/login";
    	}
    	else
    	{
    		return "redirect:/signup";
    	}
	}
}