<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--@elvariable id="articles" type="java.lang.List<com.somanyfeeds.articlesdataaccess.Article>"--%>

<c:set var="title">damo.io</c:set>

<c:set var="page">
    <c:forEach items="${articles}" var="article">
        <article>
            <header>
                <h1><a href="${article.link}">${article.title}</a></h1>
                <h2>${article.date}</h2>
            </header>
            <section>
                ${article.content}
            </section>
        </article>
    </c:forEach>
</c:set>

<%@ include file="layout.jsp" %>
