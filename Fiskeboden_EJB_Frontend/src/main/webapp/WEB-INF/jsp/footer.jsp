
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>


<footer>
    <div class="container">
        <p>&copy; <%= new java.text.SimpleDateFormat("yyyy").format(new java.util.Date()) %> Fiskeboden AB.</p>

        <div class="weather-bar">
            <span>📍 <span id="city">...</span></span>
            <span>🌡️ <span id="degree">...</span></span>
            <span>🌥️ <span id="weather">...</span></span>
            <span>🌅 <span id="sunrise">...</span></span>
            <span>🌇 <span id="sunset">...</span></span>
        </div>
    </div>
</footer>

<!-- jQuery -->
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.2/jquery.min.js"></script>

<!-- Väderlogik -->
<script>
$(document).ready(function() {
    if (navigator.geolocation) {
        navigator.geolocation.getCurrentPosition(showPosition);
    } else {
        alert("Din webbläsare stödjer inte geolocation.");
    }

    function showPosition(position) {
        const lat = position.coords.latitude;
        const lon = position.coords.longitude;

        $.ajax({
            method: "GET",
            url: "https://api.openweathermap.org/data/2.5/weather?lat="+lat+"&lon="+lon+"&units=metric&lang=sv&appid=124ae5c6217aa8ad0a6ede5fa27914f5",
            success: function(result) {
                const city = result.name;
                const temp = result.main.temp;
                const weather = result.weather[0].main;

                const sunrise = new Date(result.sys.sunrise * 1000).toLocaleTimeString("sv-SE");
                const sunset = new Date(result.sys.sunset * 1000).toLocaleTimeString("sv-SE");

                $("#city").text(city);
                $("#degree").text(temp + " ℃");
                $("#weather").text(weather);
                $("#sunrise").text(sunrise);
                $("#sunset").text(sunset);
            },
            error: function(xhr, status, error) {
                console.error("Väder-Ajax error:", status, error);
                alert("Kunde inte hämta väderdata.");
            }
        });
    }
});
</script>
