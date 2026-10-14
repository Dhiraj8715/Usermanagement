package in.ashokit.Controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import in.ashokit.Dto.QuoteApiResponseDto;
import in.ashokit.Dto.ResetpwdDto;
import in.ashokit.Dto.UserDto;
import in.ashokit.service.UserService;

@Controller
public class UserController {
	
	@Autowired
	private UserService userService;
	
	
	@GetMapping("/")
	public String index(Model model) {
		UserDto userDto=new UserDto();
		model.addAttribute("user", userDto);
		return "index";
	}
	
	@PostMapping("/login")
	public  String logincheck(@ModelAttribute("user") UserDto user,Model model) {
		
		UserDto userDto= userService.login(user.getEmail(),user.getPwd());
		
		if(userDto==null) {
			model.addAttribute("emsg", "Invalid credentials");
			return "index";
		}
		
		String pwdUpdated=userDto.getPwdUpdate();//userDto मधून password update झाला आहे का नाही (Yes/No) हे घेतो.
		if(pwdUpdated.equals("No")) {//जर password अजून update केलेला नसेल (No) तर:
			
			return "resetPwd";//Reset Password page वर पाठवतो.
		}else {
			QuoteApiResponseDto dashboardDto=userService.buildDashboard();//userService.buildDashboard() call करून dashboard साठी data मिळवतो आणि तो dashboardDto मध्ये store करतो.
			model.addAttribute("dashBoardInfo",dashboardDto);//dashboardDto मधला data dashBoardInfo नावाने dashboard HTML page ला पाठवतो.
			return "dashboard";//dashboard.html page open/display करतो.
		}
		
		
	}
	
	@GetMapping("/register")
	public String registerPage(Model model) {
		UserDto userdto=new UserDto();
		
		model.addAttribute("user", userdto);
		model.addAttribute("countries", userService.getCountries());
		return "register";
		
		
	}
	
	@GetMapping("/states")
	@ResponseBody
	public Map<Integer,String> getstates(@RequestParam("countryId") Integer countryId){
		return userService.getStates(countryId);
		
	}
	
	@GetMapping("/cities")
	@ResponseBody
	public Map<Integer,String>getCities(@RequestParam("StatesId") Integer statesId){
		return userService.getCities(statesId);
	}
	@PostMapping("/register")
	public String handleRegistration(@ModelAttribute("user")UserDto userdto,Model model) {//model attribute ith frontend krun alela data backend UserDto userdto la blind krtoy model model tyala prt controller la detoy
		
		boolean emailUnique=userService.isEmailUnique(userdto.getEmail());
		if(!emailUnique) {
			model.addAttribute("emsg","duplicate email found");//duplicate email msg found ha error message HTML page kade pathvto
		}
		
		boolean registerUser=userService.registerUser(userdto);//ही line registration process execute करते आणि registration successful आहे की नाही ते true/false मध्ये घेते.
		//registerUser(userdto)-->userdto मधला user registration data registerUser() method ला pass करतो.
		if(registerUser) {
			model.addAttribute("smsg","Registration success,chcek your email for login details");
		}else {
			model.addAttribute("emsg","registration failed");
		}
		
		return "register" ;
		
	}
	
	@PostMapping("/resetPwd")
	public String resetPwd(@ModelAttribute("resetpwd")  ResetpwdDto resetpwdDto,Model model) {//with help of modelattribute we access data in UI page ...here we can access resetpwd in UI page
		
		UserDto user=userService.getUserByEmail(resetpwdDto.getEmail());
		if(user!=null&& !user.getPwd().equals(resetpwdDto.getOldPwd())){
			model.addAttribute("emsg","old pwd is correct");
			
		if(!resetpwdDto.getNewPwd().equals(resetpwdDto.getConfirmPwd())) {
			model.addAttribute("emsg","new pwd &confirm pwd not same");
		}
			boolean resetPwd=userService.resetPwd(resetpwdDto);//resetpwdDto मधला password-reset data घेऊन resetPwd() method execute केली जाते.
			if(resetPwd) {
				model.addAttribute("smsg","Pwd updated");
			}
			
		}
		
		return "resetPwd";
	}
	
	@GetMapping("/quote")
	public String getQuote(Model model) {
		QuoteApiResponseDto dashboardDto=userService.buildDashboard();
		model.addAttribute("dashboardInfo",dashboardDto);
		
		return "dashboard";
		
	}
	@GetMapping("/logout")
	public String logout(Model model) {
		UserDto userdto=new UserDto();
		model.addAttribute("user",userdto);
		return "index";
		
	}
}
