<%@ page session="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

<form:label path="venueFields.id">Select Venue</form:label>
		<form:select id="venueId" path="venueFields.id" onchange='onChange(this.form.venueId);'>
			<form:option value="">Add New Venue</form:option>
			<c:forEach var="venues" items="${venueList}" varStatus="status">
			<form:option value="${status.index+1}">${venues}</form:option>
			</c:forEach>
		</form:select> 
		<script type="text/javascript">
			var addList = new Array();
			var hintList = new Array();
			var venueList = new Array();
		</script>
		
		<c:forEach var="venue" items="${venueList}" varStatus="status">
			<script type="text/javascript">
				venueList["${status.index+1}"]="${venue}";
			</script>
		</c:forEach>
		
		<c:forEach var="address" items="${addressList}" varStatus="status">
			<script type="text/javascript">
				addList["${status.index}"]="${address}";
			</script>
		</c:forEach>
		
		<c:forEach var="hint" items="${hintsList}" varStatus="status">
			<script type="text/javascript">
				hintList["${status.index}"]="${hint}";
			</script>
		</c:forEach>
		
		<form:label path="venueFields.name">Venue Name <form:errors path="venueFields.name" cssClass="error" /></form:label>
		<form:input id="venueName" path="venueFields.name" />
		<form:label path="venueFields.address">Venue Address <form:errors path="venueFields.address" cssClass="error" /></form:label>
		<form:input id="venueAddress" path="venueFields.address" />
		<form:label path="venueFields.locationHint">Location Hint <form:errors path="venueFields.locationHint" cssClass="error" /></form:label>
		<form:input id="venueLocationHint" path="venueFields.locationHint" />
			
		<script type="text/javascript">

			function onChange(dropdown)
			{	
				var index = dropdown.selectedIndex;
				if (index != 0){
					$("#venueName").val(venueList[index]);
				} else {
					$("#venueName").val("");
				}
				if (index != 0){
					$("#venueAddress").val(addList[index-1]);
				} else {
					$("#venueAddress").val("");
				}
				if (index != 0){
					$("#venueLocationHint").val(hintList[index-1]);
				} else {
					$("#venueLocationHint").val("");
				}
				if (index != 0) {
					$("#venueName").attr("readonly", true);
					$("#venueAddress").attr("readonly", true);
					$("#locationHint").attr("readonly", true);
					} else {
						$("#venueName").attr("readonly", false);
						$("#venueAddress").attr("readonly", false);
						$("#locationHint").attr("readonly", false);
					}
			}
		</script>