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

    <link rel="apple-touch-icon" href="${pageContext.request.contextPath}/apple-touch-icon.png">
    <link rel="icon" type="image/png" href="${pageContext.request.contextPath}/favicon.png">
</head>
<body>
<header id="app-header">
    <h1>
        <svg version="1.1" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 1000 200">
            <circle
                    cx="595"
                    cy="100"
                    r="130"
                    fill="#00BCD4"
                    >
            </circle>
            <text
                    x="0"
                    y="160"
                    text-anchor="start"
                    font-family="Roboto Slab"
                    font-size="180"
                    font-weight="500"
                    fill="#FFFFFF"
                    >
                ${title}
            </text>
        </svg>
    </h1>

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
