package in.ashokit.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import in.ashokit.Dto.QuoteApiResponseDto;
import in.ashokit.Dto.ResetpwdDto;
import in.ashokit.Dto.UserDto;
import in.ashokit.entity.CityEntity;
import in.ashokit.entity.CountryEntity;
import in.ashokit.entity.StateEntity;
import in.ashokit.entity.UserEntity;
import in.ashokit.repo.CityRepo;
import in.ashokit.repo.CountryRepo;
import in.ashokit.repo.StateRepo;
import in.ashokit.repo.UserRepo;

@Service
public class UserServiceImpl implements UserService{

	@Autowired
	private CountryRepo countryrepo;
	
	@Autowired
	private StateRepo stateRepo;
	
	@Autowired
	private CityRepo cityRepo;
	
	@Autowired
	private UserRepo userrepo;
	
	@Autowired
	private EmailService emailService;
	
	
	@Override
	public Map<Integer, String> getCountries() {
		
		List<CountryEntity> all= countryrepo.findAll();//find all database madhun record anato
		
		Map<Integer, String> countriesMap=new HashMap<>();//empty hasmap tayar kelay jayat apn key value pair store kru
		
		all.forEach(c ->{countriesMap.put(c.getCountryid(),c.getcountryName());
		});
	
		return countriesMap;
	}

	@Override
	public Map<Integer, String> getStates(Integer countryId) {
		
		Map<Integer,String>statesMap=new HashMap<>();//empty hash map
		
		List<StateEntity>statesList=stateRepo.findByCountryCountryid(countryId);
		
		statesList.forEach(s->{statesMap.put(s.getStateId(),s.getStateName());
		});
		
		return statesMap;
	}

	@Override
	public Map<Integer, String> getCities(Integer statesId) {
		Map<Integer,String>CityMap= new HashMap<>();
		List<CityEntity>citiesList=cityRepo.findByStateStateId(statesId);
		
		citiesList.forEach(c -> {CityMap.put(c.getCityId(),c.getName());
		});
		return CityMap;
	}

	@Override
	public boolean isEmailUnique(String email) {
		
		UserEntity ByEmail=userrepo.findByEmail(email);
		
		return ByEmail==null;
	}

	@Override
	public boolean registerUser(UserDto userDto) {
		UserEntity entity = new UserEntity();//empty
		
		BeanUtils.copyProperties(userDto, entity);//comvertmeans copy userDto to entity
		
		Optional<CountryEntity>countryOptional=countryrepo.findById(userDto.getCountryId());
	         Optional<StateEntity>stateOptional=stateRepo.findById(userDto.getStateId());
	         Optional<CityEntity>cityOptional=cityRepo.findById(userDto.getCityId());
	         
	         entity.setCountry(countryOptional.get());
	         entity.setState(stateOptional.get());
	         entity.setCity(cityOptional.get());
	         
	         String pwd=generatedPwd();
	         
	         entity.setPwd(pwd);
	         entity.setPwdUpdate("NO");
	         
	         UserEntity savedentity = userrepo.save(entity);//save entity object
	         Integer userId=savedentity.getUserId();
	         
	         if(userId!=null) {
	        	 
	        	 String subject="your registration success @Ashok IT";
	        	 String body="your Temporary pwd:" + pwd;
	        	 emailService.sendEmail(userDto.getEmail(), subject, body);
	        	 return true;
	         }else {


		return false;
	         }
	}

	@Override
	public UserDto login(String email, String pwd) {
		UserEntity entity=userrepo.findByEmailAndPwd(email,pwd);//email and pwd vaprun database madhey tevto and milela userEntity entity tevto
		if(entity!=null) {
			UserDto userdto=new UserDto();
			BeanUtils.copyProperties(entity, userdto);
			return userdto;
		}
		
		return null;
	}

	@Override
	public QuoteApiResponseDto buildDashboard() {
		String apiUrl ="https://dummyjson.com/quotes/random";
		
		RestTemplate rt=new RestTemplate();
		
		ResponseEntity<QuoteApiResponseDto> forEntity =rt.getForEntity(apiUrl,QuoteApiResponseDto.class);
		
		
		QuoteApiResponseDto body=forEntity.getBody();
		return body;
	}
	private String generatedPwd() {
		StringBuffer buffer=new StringBuffer();
		
		int  pwdLength=5;
		String  charPool="ABCDEFGHIJKLMNOPQRSTUVWXYZ123456789";
		Random random=new Random();
		for(int i=0;i<=pwdLength;i++) {
			int index=random.nextInt(charPool.length());
			char charAt=charPool.charAt(index);
			buffer.append(charAt);
		}
		
		return buffer.toString();

}

	@Override
	public boolean resetPwd(ResetpwdDto resetPwdDto) {
		UserEntity ByEmail=userrepo.findByEmail(resetPwdDto.getEmail());
		ByEmail.setPwd(resetPwdDto.getNewPwd());
		ByEmail.setPwdUpdate("YES");
		userrepo.save(ByEmail);
		return false;
	}

	@Override
	public UserDto getUserByEmail(String email) {
		UserEntity byEmail=userrepo.findByEmail(email);
		if(byEmail !=null) {
			UserDto dto= new UserDto();
			BeanUtils.copyProperties(byEmail, dto);
			return dto;
		}
		
		return null;
	}
}