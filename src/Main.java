import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.*;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.layout.StackPane;
import javafx.stage.*;
import javafx.util.Duration;

import java.util.Scanner;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.lang.reflect.Type;
import com.google.gson.reflect.TypeToken;
public class Main extends Application {
	
	

	//create variables
	
	private double xOffset = 0;
	private double yOffset = 0; 
	static String location = "Helsinki";
	static Label tempLabel = new Label("14");
	static Label wthrLabel = new Label("cloudy");
	static Label timeLabel = new Label();
	static Label locationLabel = new Label(location);
	
	public static void main(String[] args) {
	
		launch(args);
		
	
		
		
	}

	@Override
	public void start(Stage window) throws Exception {
	
		
			
			window.setTitle("Clock and Weather");
			
			//set styles for labels
			
			locationLabel.setStyle("-fx-font-size: 30;"+
						"-fx-text-fill:#fcfcfc");
			
				timeLabel.setStyle("-fx-font-size: 30;"+
							"-fx-text-fill:#fcfcfc"	);
			
			
				tempLabel.setStyle("-fx-font-size: 30;"+
							"-fx-text-fill:#fcfcfc"	);
				
			
				wthrLabel.setStyle("-fx-font-size: 30;"+
							"-fx-text-fill:#fcfcfc"	);
				
				
				
		//Create and setup JavaFx elements
		BorderPane pane = new BorderPane();
		Scene scene1 = new Scene(pane,600,170);
		
		
		HBox textbox = new HBox();
		textbox.setSpacing(20);
		textbox.setAlignment(Pos.BOTTOM_CENTER);
		textbox.getChildren().add(locationLabel);
		textbox.setStyle(
		        "-fx-background-color: rgba(255, 255, 255, 0);" +
		        "-fx-effect: dropshadow(gaussian, red, 30, 0, 0, 0);" +
		        "-fx-background-insets: 1;"
		    );
		
		HBox weatherbox = new HBox();
		weatherbox.setAlignment(Pos.CENTER);
		weatherbox.getChildren().addAll(tempLabel,wthrLabel);
		weatherbox.setSpacing(20);
		weatherbox.setStyle(
		        "-fx-background-color: rgba(255, 255, 255, 0);" +
		        "-fx-effect: dropshadow(gaussian, red, 30, 0, 0, 0);" +
		        "-fx-background-insets: 1;"
		    );
		
		HBox timebox = new HBox();
		timebox.setAlignment(Pos.TOP_CENTER);
		timebox.getChildren().add(timeLabel);
		timebox.setStyle(
		        "-fx-background-color: rgba(255, 255, 255, 0);" +
		        "-fx-effect: dropshadow(gaussian, red, 30, 0, 0, 0);" +
		        "-fx-background-insets: 1;"
		    );
		
		pane.setTop(textbox);
		pane.setCenter(weatherbox);
		pane.setBottom(timebox);
		pane.setStyle(   "-fx-background-color: rgba(255, 255, 255, 0);" +
		        "-fx-effect: dropshadow(gaussian, red, 30, 0, 0, 0);" +
		        "-fx-background-insets: 1;");
		
		// add padding to pane so the glow effect wont clip
		pane.setPadding(new Insets(20, 20, 20, 20));


		
		
		
	
		//set Scene background to transparent
		scene1.setFill(Color.TRANSPARENT);
		//set window stage to transparent
		window.initStyle(StageStyle.TRANSPARENT);
		window.setScene(scene1);
		window.show();
		
		//make bordelless window draggable
		scene1.setOnMousePressed(new EventHandler<MouseEvent>(){

			//get X and Y coordines of stage
			public void handle(MouseEvent event) {
				
				xOffset = event.getSceneX();
				yOffset = event.getSceneY();
			
				
			}
			
		});
		
		scene1.setOnMouseDragged(new EventHandler<MouseEvent>(){

			//calculate new coordines for stage
			public void handle(MouseEvent event) {
				
				window.setX(event.getScreenX() - xOffset);
				window.setY(event.getScreenY() - yOffset);
				
			}
			
		});
		
	
			 
	
				//get clock and date from calender class
				//Create Timeline object and use it to call getTimeAndDate(); method every second
				 Timeline getClock = new Timeline(new KeyFrame(Duration.ZERO, e -> {        
						
						 getTimeAndDate();		
						
						
				    }),
						 //set how keyframe duration
						 new KeyFrame(Duration.seconds(1))
				    );
				 		//set getClock to loop endless
				    getClock.setCycleCount(Animation.INDEFINITE);
				    getClock.play();
				    
				    
				  //get weather information from OpenWeatherMap API
				  //Create Timeline object and use it to callgetWeather(); method every minute
				  //With free subscription option OpenWeatherMap update rate is listed as <2H
				    Timeline getWeather = new Timeline(new KeyFrame(Duration.ZERO, e -> {     
				    	
				    	getWeather();
				    	
				    }),
				    		//set how keyframe duration	
				         new KeyFrame(Duration.seconds(60))
				    );
				    		//set getWaather to loop endless
				    getWeather.setCycleCount(Animation.INDEFINITE);
				    getWeather.play();

	
		
		
		
	}
	//get clock and date from calender class
	public static void getTimeAndDate() {
		//Create new Calender object using GregorianCalendar
		
		Calendar cal = new GregorianCalendar();
		
		//create string variables for time data
		//get time from calender using .get method
		//format string with zero in front if time date integer < 10
		String seconds =String.format("%02d",cal.get(Calendar.SECOND) ) ;
		String minutes = String.format("%02d",cal.get(Calendar.MINUTE) ) ;
		String hours = String.format("%02d",cal.get(Calendar.HOUR_OF_DAY) );
		//get day of month from calendar
	
		int days = cal.get(Calendar.DAY_OF_MONTH);
		//get month from calendar
		//return int value 
		// jan = 0
		//set monthString to corresponding month
		String monthString = "";
		
		int month = cal.get(Calendar.MONTH);
			if (month == 0) {
				monthString = "Jan";	
			}
			if (month == 1) {
				monthString = "Feb";	
				}
			if (month == 2) {
				monthString = "Mar";	
				}
			if (month == 3) {
				monthString = "Apr";	
				}
			if (month == 4) {
				monthString = "May";	
				}
			if (month == 5) {
				monthString = "Jun";	
				}
			if (month == 6) {
				monthString = "Jul";	
				}
			if (month == 7) {
				monthString = "Aug";	
				}
			if (month == 8) {
				monthString = "Sep";	
				}
			if (month == 9) {
				monthString = "Oct";	
				}
			if (month == 10) {
				monthString = "Nov";	
				}
			if (month == 11) {
				monthString = "Dec";	
				}
			
			
			//create string containing time and date data
		String curtime =  hours+":" + minutes+":" +seconds+" "+monthString +" "+days;
			//set time text to time
			timeLabel.setText(curtime);
		
		
		
		
		
		
	}
	  //get weather information from OpenWeatherMap API
	public static void getWeather() {
		
    	try {
			//create string where the json will be stored
			String jsonString = "";
			//create url object
			URL url = new URL("http://api.openweathermap.org/data/2.5/weather?q="+location+"&units=metric&APPID=d736d2e305775422d3875bdaa1432543");
			//cast url object to HttpURLConnection 
			HttpURLConnection conn = (HttpURLConnection)url.openConnection(); 
			//open connection stream
			conn.connect();
			//Use HttpURLConnection validate feature to check response code
			int responsecode = conn.getResponseCode();
			
			if(responsecode != 200)
				throw new RuntimeException("HttpResponseCode: " +responsecode);
				else
				{
					//create scanner that scans each line from API stream 
					//loop that loops as long as scanner finds nextLine
					Scanner streamScanner = new Scanner(url.openStream());
					while(streamScanner.hasNext())
					{
						jsonString+=streamScanner.nextLine();
					}
					
					streamScanner.close();
					
					
					//parse temp from json
					//create new jsonelement and parse jsonSring to it
					JsonElement element = new JsonParser().parse(jsonString);
					//create jasonobject from jsonelement
					JsonObject objectTemp = element.getAsJsonObject();
					//set object to "main" object from API
					objectTemp = objectTemp.getAsJsonObject("main");
					//get data from "main" object's "temp" and set temp label
					tempLabel.setText( objectTemp.get("temp").getAsString()+"°C");
					
					//parse weather from json
					//weather information stored in jsonarray 
					 Gson gson = new Gson();
					 //create class that containst list of weather objects and map json to it
					 Currentweather currentWeather = gson.fromJson(jsonString,
					 Currentweather.class);
					 //create list of weathers and pass list from currentweather to it
					List<Weather> weatherList = currentWeather.getWeather();
					 //get index 0 from weather list
					 Weather weather = weatherList.get(0);
					 //get weather description from index 0 
					 wthrLabel.setText(weather.getDescription().toString());
					    
				
					
				}
			
			
		} catch (Exception ex) {
			
			System.out.print(ex.getMessage());
		}
		
	}
	

}

