package callApi;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class LocalSearchImg {
	 public StringBuilder sb;
	 public String clientId = "QUyHkL9SA1c0aTQjz197";
	 public String clientSecret = "d4nYetPHwT";
	 
	 public String getImage(String imgTitle,int num) {
	     
	      try {
	         String text = URLEncoder.encode(imgTitle, "utf-8");
	       
	         String apiURL = "https://openapi.naver.com/v1/search/image?query=" + text + "&display=" + 100 + "&";
	         URL url = new URL(apiURL);
	         HttpURLConnection con = (HttpURLConnection) url.openConnection();
	         con.setRequestMethod("GET");
	         con.setRequestProperty("X-Naver-Client-Id", clientId);
	         con.setRequestProperty("X-Naver-Client-Secret", clientSecret);
	         //System.out.println("URL : " + apiURL);
	         int responseCode = con.getResponseCode();
	         BufferedReader br;
	         if (responseCode == 200) {
	            br = new BufferedReader(new InputStreamReader(con.getInputStream()));
	         } else {
	            br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
	         }
	         sb = new StringBuilder();
	         String line;

	         while ((line = br.readLine()) != null) {
	            sb.append(line + "\n");
	         }

	         br.close();
	         con.disconnect();
	         
	         //System.out.println(sb);
	         String data = sb.toString();	         
	         String[] array;
	         array = data.split("\"");         
	      
	         if(num == 0) {  
		         for (int i = 0; i < array.length; i++) {
		            if (array[i].equals("thumbnail") && array[i+2]!=null) 
		               return array[i+2];
		         }
	         }else if(num == 1) { 
	        	 String result = "";
	        	 result += "<UrlData>";
	        	 int cnt = 0;	        
	        	 for (int i = 0; i < array.length; i++) {
			            if (array[i].equals("thumbnail")) { 
			            	if( ++cnt > 10) break; 
			            	result += "<Data>";
			            	result += "<imgUrl>" + array[i+2] + "</imgUrl>";
			            	result += "</Data>";
			            }
	         	}	 
	        	 result += "</UrlData>";         	
	         	System.out.println(result);
	         	return result;
	         }
	      }catch(Exception e) {
	         System.out.println(e);
	      }
	      return null;
	   }
}
