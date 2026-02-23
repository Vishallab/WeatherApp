package MyPackage;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Scanner;
import java.util.TimeZone;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

/**
 * Servlet implementation class MyServlet
 */
@WebServlet("/MyServlet")
public class MyServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MyServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	
	
	
	//doPst me humne vo sara code likha jo hum apne index.jsp me update (post) krna chahte h 
	//isiliye vo sara code doPost me h 
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		//API Key
		//api setup
				String apiKey = "ADD-YOUR-API-KEY";
				// Get the city from the form input
//		        String city = request.getParameter("city"); 
		        String city = URLEncoder.encode(request.getParameter("city"), "UTF-8");


		        // Create the URL for the OpenWeatherMap API request
		        String apiUrl = "https://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=" + apiKey;
		        
		        
		        
		        try {
		        //try me is liye kyuki agr glt city dali to exception de rha h 
		        //API ko integrate krege
		
		            URL url = new URL(apiUrl);
		            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		            connection.setRequestMethod("GET");
		            
		            //reading the data from network
		            InputStream inputStream = connection.getInputStream();
	                InputStreamReader reader = new InputStreamReader(inputStream);
	                
	                //want to stroe in the string
	                StringBuilder responseContent = new StringBuilder();
	                
	                //input lene k liye
	                Scanner scanner = new Scanner(reader);
	                
	                
	                while (scanner.hasNext()) {
	                    responseContent.append(scanner.nextLine());
	                }
	                
	        
	                //ab scanner ka kam khtm
	                scanner.close();
//	                System.out.println(responseContent);
	                
	                //typecasting ya fir parsing the data into JSON
	                
	                // Parse the JSON response to extract temperature, date, and humidity
	                Gson gson = new Gson();
	                JsonObject jsonObject = gson.fromJson(responseContent.toString(), JsonObject.class);
//	                System.out.println(jsonObject);//consol me check krne k liye
	                
//	                
//	                //Date & Time
//	                long dateTimestamp = jsonObject.get("dt").getAsLong() * 1000;
//	                String date = new  Date(dateTimestamp).toString();//parsing ho gya yaha typecast
//	                
	                
	                // Date & Time
	                long dateTimestamp = jsonObject.get("dt").getAsLong() * 1000L;  // Convert to milliseconds
	                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); // Define date and time format
	                sdf.setTimeZone(TimeZone.getTimeZone("GMT+5:30")); // Set timezone if needed
	                String date = sdf.format(new java.util.Date(dateTimestamp)); // Format the date with time
	                  
	                
	                //Temperature
	                double temperatureKelvin = jsonObject.getAsJsonObject("main").get("temp").getAsDouble();//ye data double me krnan  h
	                int temperatureCelsius = (int) (temperatureKelvin - 273.15);
	               
	                //Humidity
	                int humidity = jsonObject.getAsJsonObject("main").get("humidity").getAsInt();
	                
	                //Wind Speed
	                double windSpeed = jsonObject.getAsJsonObject("wind").get("speed").getAsDouble();
	                
	                //Weather Condition
	                String weatherCondition = jsonObject.getAsJsonArray("weather").get(0).getAsJsonObject().get("main").getAsString();
	                //now we get all the data that we wanted to show
	              //to abhi tk hume data mil gya h to ab hume apne clint ko data bhejna h 
	                
	                
	                // Decode the city name before passing it to the JSP
	                String cityDecoded = URLDecoder.decode(city, "UTF-8");
	                
	                
	                // Set the data as request attributes (for sending to the jsp page)
	                request.setAttribute("date", date);
	                request.setAttribute("city", city);
	                request.setAttribute("temperature", temperatureCelsius);
	                request.setAttribute("weatherCondition", weatherCondition); 
	                request.setAttribute("humidity", humidity);    
	                request.setAttribute("windSpeed", windSpeed);
	                request.setAttribute("weatherData", responseContent.toString());
	                
	                connection.disconnect();
		        }catch(IOException e) {
		        e.printStackTrace(); 
		        }
	                
	                // Forward the request to the weather.jsp page for rendering
	                request.getRequestDispatcher("index.jsp").forward(request, response);
	                //request krega dispatch k liye or kon sa page bhejge index,jsp or forword kya krge age request and response
	                

	                
	}

}
