<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--@elvariable id="feeds" type="java.util.List<com.somanyfeeds.aggregator.FeedPresenter>"--%>
<%--@elvariable id="articles" type="java.util.List<com.somanyfeeds.articlesdataaccess.Article>"--%>

<c:set var="title">damo.io</c:set>

<c:set var="menu">
    <ul>
        <c:forEach items="${feeds}" var="feed">
            <li class="${feed.isSelected ? "selected" : "not-selected"}">
                <a href="${pageContext.request.contextPath}${feed.path}">
                        ${feed.name}
                </a>
            </li>
        </c:forEach>
    </ul>
</c:set>

<c:set var="section">
    <c:forEach items="${articles}" var="article">
        <article>
            <header>
                <h1><a href="${article.link}">${article.title}</a></h1>

                <h2 class="date">${article.date}</h2>
            </header>
            <section>
                    ${article.content}
            </section>
        </article>
    </c:forEach>
</c:set>

<%@ include file="layout.jsp" %>
