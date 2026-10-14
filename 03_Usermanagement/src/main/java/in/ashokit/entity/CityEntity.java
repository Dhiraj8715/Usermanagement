package in.ashokit.entity;

import jakarta.annotation.Generated;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name="city_master")
@Getter
@Setter
public class CityEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)

	private Integer cityId;
	
	private String name;
	
	

@ManyToOne
@JoinColumn(name="state_id")
 private StateEntity state;



public Integer getCityId() {
	return cityId;
}



public void setCityId(Integer cityId) {
	this.cityId = cityId;
}



public String getName() {
	return name;
}



public void setName(String name) {
	this.name = name;
}



public StateEntity getState() {
	return state;
}



public void setState(StateEntity state) {
	this.state = state;
}

}
