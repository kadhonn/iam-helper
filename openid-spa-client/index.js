var userManager = new Oidc.UserManager({
    "authority": "https://localhost:9443/oauth2/oidcdiscovery/.well-known/openid-configuration",
    "client_id": "Tgf4bopZXYyhhNNd8i4Pn_M_pNQa",
    "redirect_uri": "http://localhost:8081/",
    "response_type": "code"
});

if (isAuthorizationResponse()) {
    userManager.signinRedirectCallback().then(function() {
        window.location.href="http://localhost:8081/";
    });
} else {
    userManager.getUser().then(function (user) {
        if (!!user) {
            loggedIn(user);
        } else {
            loggedOut();
        }
    });
}

document.getElementById("login").onclick = function () {
    userManager.signinRedirect().then(loggedIn);
};
document.getElementById("logout").onclick = function () {
    userManager.signoutRedirect().then(loggedOut);
};


function isAuthorizationResponse() {
    var url = new URL(window.location.href);
    return !!url.searchParams.get("code");
}

function loggedOut() {
    document.getElementById("login").style = "display:inline-block";
    document.getElementById("logout").style = "display:none";
    document.getElementById("userinfo").innerText = "You are currently not logged in!";
}
function loggedIn(user) {
    document.getElementById("login").style = "display:none";
    document.getElementById("logout").style = "display:inline-block";

    document.getElementById("userinfo").innerText = "Name: " + user.profile.sub + " Access Token: " + user.access_token + " Refresh Token: " + user.refresh_token;
}