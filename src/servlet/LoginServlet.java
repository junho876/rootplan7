package servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.ConnectDB;
import dto.CustomerInfo;


@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
   private static final long serialVersionUID = 1L;
   static ConnectDB db = new ConnectDB();
   static int customerCnt = 0;
   static int customerSize = 0;
   static int[] log = new int[20];
   static Map<String,Integer> logCheck = new HashMap<String,Integer>();   
    public LoginServlet() {    
       super();
   }
       
   protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
       response.getWriter().append("Served at: ").append(request.getContextPath());
       this.doPost(request, response);
   }

   protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
      response.setContentType("text/html;charset=UTF-8");         
      PrintWriter out = response.getWriter();
            
      String ID=request.getParameter("customerID");   
      int menuIndex = Integer.parseInt(request.getParameter("menuIndex"));
      
      switch(menuIndex){
      case 0: 
         String email = request.getParameter("email");
         String cid = request.getParameter("cID");
         String gender = request.getParameter("gender");
         String age = request.getParameter("age");
         System.out.println("= "+email+", cid= "+cid+" , gender= "+gender+" , age=" + age);
         CustomerInfo tmp = new CustomerInfo(cid, email, gender, age);
         
         if(logCheck.containsKey(cid)) {
        	 
        	 out.print(logCheck.get(cid));
         }else{
        	 
            for(int i =0;i<20;i++) { 
               if(log[i] == 0) {
                  log[i] = 1;
                  customerSize++;
                  logCheck.put(cid, i);
                  out.print(i);
                  break;
               }
            }
         }
         db.CheckID(tmp);   
         break;
         
      case 1: 
         String cID = request.getParameter("cID");
         System.out.println("cID : " + cID);
         int customerCnt =  logCheck.get(cID);
         log[customerCnt] = 0;
         if(customerSize == customerCnt)
            customerCnt--;
         customerSize--;
         
         logCheck.remove(cID);
         break;      
      }
   }
}