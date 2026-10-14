package in.ashokit.service;

import java.util.Map;

import in.ashokit.Dto.QuoteApiResponseDto;
import in.ashokit.Dto.ResetpwdDto;
import in.ashokit.Dto.UserDto;

public interface UserService {
	
    public Map<Integer,String> getCountries();
	
	public Map<Integer,String> getStates(Integer countryId);
	
	public Map<Integer,String> getCities(Integer statesId);
	
	public boolean isEmailUnique(String email);
	
	public boolean registerUser(UserDto userDto);
	
	public UserDto login(String email,String pwd);
	
	public boolean  resetPwd(ResetpwdDto resetPwdDto);
	
	public QuoteApiResponseDto buildDashboard();
	
	public UserDto getUserByEmail(String email);

}
