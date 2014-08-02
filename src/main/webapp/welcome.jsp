<!doctype html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta name="description"
	content="A layout example that shows off a responsive email layout.">
<title>Email &ndash; Layout Examples &ndash; Pure</title>
<link rel="stylesheet"
	href="http://yui.yahooapis.com/pure/0.5.0/pure-min.css">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/resources/css/layouts/email.css">
<title>Chat - WebSocket</title>
</head>
<body>
	<div class="content pure-g">
		<div class="pure-u-1-3"></div>
		<div id="main" class="pure-u-1-3">
			<h1 class="email-content-title">Hello there are you already
				signed in?</h1>
			<button class="button-success pure-button">Yes</button>
			<button class="button-error pure-button">No</button>

			<form class="pure-form pure-form-stacked">
				<fieldset>
					<legend>Sing in</legend>

					<label for="email">Email</label>
					<input id="email" type="email" placeholder="Email">
					<label for="password">Password</label>
					<input id="password" type="password" placeholder="Password">
					<label for="confirmPassword">Confirm Password</label>
                    <input id="confirmPassword" type="password" placeholder="Password">

					<button type="submit" class="pure-button pure-button-primary">Sign
						in</button>
				</fieldset>
			</form>
			
			<form class="pure-form pure-form-stacked">
                <fieldset>
                    <legend>Sing on</legend>

                    <label for="email">Email</label>
                    <input id="email" type="email" placeholder="Email">
                    <label for="password">Password</label>
                    <input id="password" type="password" placeholder="Password">

                    <button type="submit" class="pure-button pure-button-primary">Sign
                        in</button>
                </fieldset>
            </form>

		</div>
		<div class="pure-u-1-3"></div>
	</div>
</body>
</html>