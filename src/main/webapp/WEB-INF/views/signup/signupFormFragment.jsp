<%@ page session="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

	<fieldset>
		<form:label path="firstName">First Name <form:errors path="firstName" cssClass="error" /></form:label>
		<form:input path="firstName" />
		<form:label path="lastName">Last Name <form:errors path="lastName" cssClass="error" /></form:label>
		<form:input path="lastName" />
		<form:label path="email">Email (never shared, used for correspondence) <form:errors path="email" cssClass="error" /></form:label>
		<form:input path="email" />	
		<form:label path="confirmEmail">Confirm Email <form:errors path="confirmEmail" cssClass="error" /></form:label>
		<form:input path="confirmEmail" />	
		<form:label path="password">Password (at least 6 characters) <form:errors path="password" cssClass="error" /></form:label>
		<form:password path="password" />
		<form:label path="gender">Gender</form:label>
		<form:select path="gender">
			<form:option value="MALE" label="Male" />
			<form:option value="FEMALE" label="Female" />
		</form:select>
		<!-- TODO only one error message that considers all 3 fields -->	
		<form:label path="month">Birthday (never shared, used to display age) <form:errors path="month" cssClass="error" /></form:label>		
		<div class="multiple">
			<form:select path="month">
				<form:option value="">Month</form:option>
				<form:option value="1">January</form:option>
				<form:option value="2">February</form:option>
				<form:option value="3">March</form:option>
				<form:option value="4">April</form:option>
				<form:option value="5">May</form:option>
				<form:option value="6">June</form:option>
				<form:option value="7">July</form:option>
				<form:option value="8">August</form:option>
				<form:option value="9">September</form:option>
				<form:option value="10">October</form:option>
				<form:option value="11">November</form:option>
				<form:option value="12">December</form:option>
			</form:select>
			<form:select path="day">
				<form:option value="">Day</form:option>		
				<form:option value="1" />
				<form:option value="2" />
				<form:option value="3" />
				<form:option value="4" />
				<form:option value="5" />
				<form:option value="6" />
				<form:option value="7" />
				<form:option value="8" />
				<form:option value="9" />
				<form:option value="10" />
				<form:option value="11" />
				<form:option value="12" />
				<form:option value="13" />
				<form:option value="14" />
				<form:option value="15" />
				<form:option value="16" />
				<form:option value="17" />
				<form:option value="18" />
				<form:option value="19" />
				<form:option value="20" />
				<form:option value="21" />
				<form:option value="22" />
				<form:option value="23" />
				<form:option value="24" />
				<form:option value="25" />
				<form:option value="26" />
				<form:option value="27" />
				<form:option value="28" />
				<form:option value="29" />
				<form:option value="30" />
				<form:option value="31" />
			</form:select>
			<form:select path="year">
				<form:option value="">Year</form:option>
				<form:option value="2010" />
				<form:option value="2009" />
				<form:option value="2008" />
				<form:option value="2007" />
				<form:option value="2006" />
				<form:option value="2005" />
				<form:option value="2004" />
				<form:option value="2003" />
				<form:option value="2002" />
				<form:option value="2001" />
				<form:option value="2000" />
				<form:option value="1999" />
				<form:option value="1998" />
				<form:option value="1997" />
				<form:option value="1996" />
				<form:option value="1995" />
				<form:option value="1994" />
				<form:option value="1993" />
				<form:option value="1992" />
				<form:option value="1991" />
				<form:option value="1990" />
				<form:option value="1989" />
				<form:option value="1988" />
				<form:option value="1987" />
				<form:option value="1986" />
				<form:option value="1985" />
				<form:option value="1984" />
				<form:option value="1983" />
				<form:option value="1982" />
				<form:option value="1981" />
				<form:option value="1980" />
				<form:option value="1979" />
				<form:option value="1978" />
				<form:option value="1977" />
				<form:option value="1976" />
				<form:option value="1975" />
				<form:option value="1974" />
				<form:option value="1973" />
				<form:option value="1972" />
				<form:option value="1971" />
				<form:option value="1970" />
				<form:option value="1969" />
				<form:option value="1968" />
				<form:option value="1967" />
				<form:option value="1966" />
				<form:option value="1965" />
				<form:option value="1964" />
				<form:option value="1963" />
				<form:option value="1962" />
				<form:option value="1961" />
				<form:option value="1960" />
				<form:option value="1959" />
				<form:option value="1958" />
				<form:option value="1957" />
				<form:option value="1956" />
				<form:option value="1955" />
				<form:option value="1954" />
				<form:option value="1953" />
				<form:option value="1952" />
				<form:option value="1951" />
				<form:option value="1950" />
				<form:option value="1949" />
				<form:option value="1948" />
				<form:option value="1947" />
				<form:option value="1946" />
				<form:option value="1945" />
				<form:option value="1944" />
				<form:option value="1943" />
				<form:option value="1942" />
				<form:option value="1941" />
				<form:option value="1940" />
			</form:select>
		</div>		
	</fieldset>