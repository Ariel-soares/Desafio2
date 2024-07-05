package desafio2UOL.entities;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "distribution_centers")
public class DistributionCenter {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String name;
	private String address;
	private String city;
	private String state;
	private String cep;

	@OneToMany
	private List<Item> items = new ArrayList<>();

	@OneToMany(mappedBy = "centerId")
	private List<Donation> donations = new ArrayList<>();
	
	public DistributionCenter() {
	}

	public DistributionCenter(Integer id, String name, String address, String city, String state, String cep) {
		super();
		this.id = id;
		this.name = name;
		this.address = address;
		this.city = city;
		this.state = state;
		this.cep = cep;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCep() {
		return cep;
	}

	public void setCep(String cep) {
		this.cep = cep;
	}

	public List<Item> getItems() {
		return items;
	}

	public List<Donation> getDonations() {
		return donations;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DistributionCenter other = (DistributionCenter) obj;
		return Objects.equals(id, other.id);
	}

	@Override
	public String toString() {
		return "DistributionCenter [id=" + id + ", name=" + name + ", address=" + address + ", city=" + city
				+ ", state=" + state + ", cep=" + cep + "]";
	}

}
