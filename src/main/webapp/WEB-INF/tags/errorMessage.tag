<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt"  prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ attribute name="attribute" required="true" type="java.lang.String" %>

<c:if test="${attribute != null}" >
    <div class="error">
        <span class="error-message">${attribute}</span>
    </div>
</c:if>

