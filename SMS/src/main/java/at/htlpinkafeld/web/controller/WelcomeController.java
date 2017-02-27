package at.htlpinkafeld.web.controller;

import at.htlpinkafeld.dao.DutyDao;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import at.htlpinkafeld.dao.UserDao;
import at.htlpinkafeld.sms.pojo.Duty;
import at.htlpinkafeld.sms.pojo.User;

@Controller
public class WelcomeController {

	private static final Logger logger = LoggerFactory.getLogger(WelcomeController.class);

	@Autowired
	UserDao userDao;
        @Autowired
        DutyDao dutyDao;
	
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String welcome(Model model) {

		logger.debug("jens");

		//User user = userDao.findByName("jens");
		
		List<User> users = userDao.findAll();
                List<Duty> duty = dutyDao.findAll();

		System.out.println(users);
                System.out.println(duty);

		model.addAttribute("user", users);
                model.addAttribute("duty", duty);
		
		return "welcome";

	}

}
