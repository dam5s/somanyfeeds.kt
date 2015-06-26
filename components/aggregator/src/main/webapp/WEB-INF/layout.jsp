<!doctype html>
<html>
<head>
    <title>${title}</title>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, minimum-scale=1.0, initial-scale=1.0, user-scalable=yes">

    <link rel="stylesheet" href="http://fonts.googleapis.com/css?family=Roboto+Slab:300,400,700|Roboto:100,300,400,700">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/reset.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/base.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/small-screens.css">
</head>
<body>
    <header id="app-header">
        <h1>${title}</h1>

        <aside id="app-menu">
            ${menu}
        </aside>
    </header>
    <section id="app-content">
        ${section}
    </section>
    <script type="application/javascript" src="${pageContext.request.contextPath}/js/app.js"></script>
</body>
</html>
