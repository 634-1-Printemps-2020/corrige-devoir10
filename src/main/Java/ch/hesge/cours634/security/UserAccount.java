package ch.hesge.cours634.security;

import ch.hesge.cours634.security.db.magic.DBCol;
import ch.hesge.cours634.security.db.magic.DBTable;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@DBTable(table = "USER_ACCOUNT")
public class UserAccount {

	@DBCol(column = "USER_NAME")
	private String name;

	@DBCol(column = "USER_PASSWORD")
	private String password;

	@DBCol(column = "IS_ACTIVE")
	private boolean isActif;

	@DBCol(column = "EXPIRATION_DATE")
	private LocalDate expirationDate;

	@DBCol(column = "USER_ROLES")
	private List<String> roles;
	private List<AccessEvent> accessEvents;

	public List<AccessEvent> getAccessEvents() {
		return accessEvents;
	}

	public void setAccessEvents(List<AccessEvent> accessEvents) {
		this.accessEvents = accessEvents;
	}

	public UserAccount(String name, String password, List<String> roles, LocalDate expirationDate) {
		this.name = name;
		this.password = password;
		this.expirationDate = expirationDate;
		this.roles = roles;
	}

	public UserAccount() {

	}

	private UserAccount(Builder builder) {
		setName(builder.name);
		setPassword(builder.password);
		setActif(builder.isActif);
		setExpirationDate(builder.expirationDate);
		setRoles(builder.roles);
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

	public List<String> getRoles() {
		return roles;
	}

	public void setRoles(List<String> roles) {
		this.roles = roles;
	}

	@Override public String toString() {
		return "UserAccount{" + "name='" + name + '\'' + ", password='" + password + '\'' + ", isActif=" + isActif + ", expirationDate=" + expirationDate + ", roles=" + roles + '}';
	}

	@Override public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		UserAccount that = (UserAccount) o;
		return isActif == that.isActif && Objects.equals(name, that.name) && Objects.equals(password, that.password) && Objects.equals(expirationDate, that.expirationDate) && Objects.equals(roles, that.roles);
	}

	@Override public int hashCode() {
		return Objects.hash(name, password, isActif, expirationDate, roles);
	}

	public static final class Builder {
		private String name;
		private String password;
		private boolean isActif;
		private LocalDate expirationDate;
		private List<String> roles;

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

		public Builder roles(List<String> val) {
			roles = val;
			return this;
		}

		public UserAccount build() {
			return new UserAccount(this);
		}
	}
}
