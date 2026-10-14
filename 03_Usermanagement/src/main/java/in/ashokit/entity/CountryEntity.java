package in.ashokit.entity;

import jakarta.annotation.Generated;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name="country_master")
@Setter
@Getter
public class CountryEntity {
	
	
	@Id
    @ GeneratedValue(strategy = GenerationType.IDENTITY)//automatically generated primary key that why used generated value
	private Integer countryid;
	
	private String countryName;

	public Integer getCountryid() {
		return countryid;
	}

	public void setCountryid(Integer countryid) {
		this.countryid = countryid;
	}

	public String getcountryName() {
		return countryName;
	}

	public void setcountryName(String name) {
		countryName = name;
	}

}
