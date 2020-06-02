package ch.hesge.cours634.security;


import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Entity
public class UserAccount {

	@Id
	private String name;
	private String password;
	private boolean isActif;
	private LocalDate expirationDate;

	@OneToMany(mappedBy = "user", fetch=FetchType.LAZY, cascade = CascadeType.ALL)
	private List<AccessEvent> accessEvents;


	public List<AccessEvent> getAccessEvents() {
		return accessEvents;
	}

	public void setAccessEvents(List<AccessEvent> accessEvents) {
		this.accessEvents = accessEvents;
	}

	public UserAccount(String name, String password, LocalDate expirationDate) {
		this.name = name;
		this.password = password;
		this.expirationDate = expirationDate;
	}

	public UserAccount() {

	}

	private UserAccount(Builder builder) {
		setName(builder.name);
		setPassword(builder.password);
		setActif(builder.isActif);
		setExpirationDate(builder.expirationDate);
		setAccessEvents(builder.accessEvents);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isActif() {
		return isActif;
	}

	public void setActif(boolean actif) {
		isActif = actif;
	}

	public LocalDate getExpirationDate() {
		return expirationDate;
	}

	public void setExpirationDate(LocalDate expirationDate) {
		this.expirationDate = expirationDate;
	}

	@Override public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		UserAccount that = (UserAccount) o;
		return Objects.equals(name, that.name);
	}

	@Override public int hashCode() {
		return Objects.hash(name);
	}

	public static final class Builder {
		private String name;
		private String password;
		private boolean isActif;
		private LocalDate expirationDate;
		private List<AccessEvent> accessEvents;

		public Builder() {
		}

		public Builder name(String val) {
			name = val;
			return this;
		}

		public Builder password(String val) {
			password = val;
			return this;
		}

		public Builder isActif(boolean val) {
			isActif = val;
			return this;
		}

		public Builder expirationDate(LocalDate val) {
			expirationDate = val;
			return this;
		}

		public Builder accessEvents(List<AccessEvent> val) {
			accessEvents = val;
			return this;
		}

		public UserAccount build() {
			return new UserAccount(this);
		}
	}
}
