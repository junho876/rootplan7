package dao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import dto.Address;
import dto.CustomerInfo;
import dto.DBRoute2Data;
import dto.DBRouteData;
import dto.Route2DataCall;

public class ConnectDB {
	static DataSource ds;
	static Connection connection;
	static Statement st;
	static ResultSet rs;
	PreparedStatement pstmt;
	
	public ConnectDB(){
		try {
			Context initCtx = new InitialContext();
			Context envCtx = (Context) initCtx.lookup("java:comp/env");   
			ds = (DataSource) envCtx.lookup("jboss/datasources/defaultDS");
		}catch(Exception e) {
			e.printStackTrace();
		}
		       
	}
	
	public Connection getConnection() {
		try {
			connection=ds.getConnection();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return connection;
	}
	
	
	public void CheckID(CustomerInfo info) {
		System.out.println("checkID");
		try {			
			connection = ds.getConnection();
			st = connection.createStatement();
			rs = st.executeQuery("show databases");	
			if (st.execute("SHOW DATABASES")) {
				rs = st.getResultSet();
			}
			while (rs.next()) {
				String str = rs.getNString(1);
				System.out.println(str);
			}
			rs = st.executeQuery("SELECT * FROM customer where id='"+ info.getId()+"'");			
			if(rs.next()) {
				
			}else{
				
				CreateDB(info);
				rs = st.executeQuery("SELECT * FROM customer");
				while (rs.next()) {
					String str = rs.getNString(1);
					System.out.println(str);
				}
			}
			rs.close();
			st.close();
			connection.close();
		} catch (SQLException SQLex) {
		
			System.out.println("SQLException: " + SQLex.getMessage());
			System.out.println("SQLState: " + SQLex.getSQLState());
		}
	}
	
	//insert into customer(id, email, gender ,age) values('1111' , '한글 테스트', '한글' , '22');
	public List<CustomerInfo> getList() {
		List<CustomerInfo> list=new ArrayList<>();	
		try {			
			connection = ds.getConnection();			
			String sql ="select * from customer ";
			pstmt=connection.prepareStatement(sql);						
			rs=pstmt.executeQuery();
							
			while (rs.next()) {
				String id =rs.getString("id");
				String email=rs.getString("email");
				int  gender=rs.getInt("gender");
				int age=rs.getInt("age");
				CustomerInfo info=new CustomerInfo(id, email, gender, age);
				list.add(info);
			}			
		} catch (SQLException SQLex) {		
			System.out.println("SQLException: " + SQLex.getMessage());
			System.out.println("SQLState: " + SQLex.getSQLState());
		}finally {
			try {
				if(rs!=null) rs.close();
				if(pstmt!=null)rs.close();
				if(connection!=null)connection.close();
			}catch(Exception e) {
				e.printStackTrace();
			}			
		}		
		return list;
	}
	
	
	//  데이터 삽입
	public void insertCustomer(CustomerInfo info) {
		try {
			String sql ="insert into customer(id, email, gender ,age) values(? , ?, ? , ?)";			
			connection=ds.getConnection();
			pstmt=connection.prepareStatement(sql);
			pstmt.setString(1, info.getId());
			pstmt.setString(2, info.getEmail());
			pstmt.setInt(3, info.getGender());
			pstmt.setInt(4, info.getAge());			
			pstmt.executeUpdate();
			
		}catch(Exception e) {
			e.getMessage();
		}finally {
			try {				
				if(pstmt!=null)rs.close();
				if(connection!=null)connection.close();
			}catch(Exception e) {
				e.printStackTrace();
			}			
		}	
	}
	
	
	private void CreateDB(CustomerInfo info) {
		
		try {
			connection = ds.getConnection();
			st = connection.createStatement();
			st.executeUpdate("INSERT INTO customer " +
					"VALUES('"+info.getId()+"','"+info.getEmail()
					+"',"+info.getGender()+","+info.getAge()+");");
			
			rs.close();
			st.close();
			connection.close();
		} catch (SQLException SQLex) {
			System.out.println("SQLException: " + SQLex.getMessage());
			System.out.println("SQLState: " + SQLex.getSQLState());
		}		
	}
	public String makeRID(LinkedList<Address> ad) {
		String result = "";
		int size = ad.size();
		for(int i = 0;i<size;i++) {
			//result+= Double.toString(ad.get(i).getLat()) + Double.toString(ad.get(i).getLng());
			result += ad.get(i).getAddress();
		}
		System.out.println("rID:" + result);
		return result;
	}
	
	public String CheckSameData(LinkedList<Address> ad, String cID, String what, String name) { 
		System.out.print("cID"+cID);
	
		String rID =  makeRID(ad);
		try {
			connection = ds.getConnection();
			st = connection.createStatement();
			rs = st.executeQuery("SELECT * FROM route where rid='"+rID+"' AND cid='"+cID+"'");
			if(rs.next()) { 
				
				rs.close();
				st.close();
				return "0";
			}else {
			 
				if(what.equals("1")) {
					DBRouteData data = DataIntoDBRouteData(ad, rID ,cID, name); 
					SaveData(data); 			
					rs = st.executeQuery("SELECT * FROM Route where cid='"+cID+"'"); 
					while (rs.next()) {
						String str = rs.getNString(1);
						System.out.println(str);
					}
				}
				rs.close();
				st.close();
				connection.close();
			}
		} catch (SQLException SQLex) {
		
			System.out.println("SQLException: " + SQLex.getMessage());
		}
		return "1";
	}
	
	
	private DBRouteData DataIntoDBRouteData(LinkedList<Address> ad,String rID,String cID, String name) {
		int size = ad.size();
		DBRouteData tmpData = new DBRouteData(rID, cID);
		tmpData.setName(name);
		tmpData.setDataSize(size);
		
		
		for(int i =0;i<size;i++) {
			tmpData.setAddress(i,ad.get(i).getAddress());
			tmpData.setLat(i, ad.get(i).getLat());
			tmpData.setLng(i, ad.get(i).getLng());
		}		
		return tmpData;
	}
	
	public void SaveData(DBRouteData data) { 
		String sqlStr = "INSERT INTO route VALUES('"
		        +data.getRid()+"',"+data.getDatasize()+",'"+data.getCid()+"','"+data.getName()+"','"
		        +data.getAddress(0)+"',"+ data.getLat(0) +","+data.getLng(0)+",'"
		        +data.getAddress(1)+"',"+ data.getLat(1) +","+data.getLng(1)+",'"
		        +data.getAddress(2)+"',"+ data.getLat(2) +","+data.getLng(2)+",'"
		        +data.getAddress(3)+"',"+ data.getLat(3) +","+data.getLng(3)+",'"
		        +data.getAddress(4)+"',"+ data.getLat(4) +","+data.getLng(4)+",'"
		        +data.getAddress(5)+"',"+ data.getLat(5) +","+data.getLng(5)+",'"
		        +data.getAddress(6)+"',"+ data.getLat(6) +","+data.getLng(6)+")";
	
		try {
			connection = ds.getConnection();
			st = connection.createStatement();
			st.executeUpdate(sqlStr);
			rs.close();
			st.close();
			connection.close();
		} catch (SQLException SQLex) {
			
		}	
	}
	
	public void SaveRoute2Data(DBRoute2Data tmp) { 
		System.out.println("Route2: INSERT INTO route2 VALUES('"+tmp.getRid()+"', '"+tmp.getCid()+"','"+
				tmp.getPt_order()+"', '"+tmp.getCar_order()+"',"+tmp.getSize()+","+tmp.getStart()+","+tmp.getLast()+")");
		try {
			connection = ds.getConnection();
			st = connection.createStatement();
			int Query = st.executeUpdate("INSERT INTO route2 VALUES('"+tmp.getRid()+"', '"+tmp.getCid()+"','"+
			tmp.getPt_order()+"', '"+tmp.getCar_order()+"',"+tmp.getSize()+","+tmp.getStart()+","+tmp.getLast()+")");
					       
			rs.close();
			st.close();
			connection.close();
		} catch (SQLException SQLex) {
			System.out.println("saveRoute2Data ");
			System.out.println("SQLException: " + SQLex.getMessage());
			//System.out.println("SQLState: " + SQLex.getSQLState());
		}	
	}
	
	public String GetAllData(String cID) { 
		String resultStr = "<SaveData>";
		try {	
			System.out.println("db ");
			String result =resultStr; 
			connection = ds.getConnection();
			st = connection.createStatement();
			String sqlStr= "SELECT * FROM route WHERE cid='"+cID+"'";
			System.out.println("GetAllData sqlStr : " + sqlStr);
			rs = st.executeQuery(sqlStr);
			
			while(rs.next()) {
				result += "<Data>";
				result += "<rID>" + rs.getString(1) + "</rID>";
				int size = rs.getInt(2); 
				result += "<size>" + Integer.toString(size) + "</size>";
				result += "<name>" + rs.getString(4) + "</name>";
				int addressCnt = 5;
				for(int i = 0;i<size;i++) {
					result += "<address"+Integer.toString(i)+">"+ rs.getString(addressCnt) +"</address"+Integer.toString(i)+">";
					addressCnt += 3;
				}
				result += "</Data>";
			}	
			resultStr = result + "</SaveData>";
			rs.close();
			st.close();
			connection.close();
		} catch (SQLException SQLex) {
			System.out.println("DB ");
			System.out.println("SQLException: " + SQLex.getMessage());
			System.out.println("SQLState: " + SQLex.getSQLState());
		}
		//System.out.println("result2 : " + resultStr);
		return resultStr;
	}
	
	public int DeleteData(String rID,String cID) { //
		System.out.println("DB삭제");
		try {		
			connection = ds.getConnection();
			st = connection.createStatement();
			st.executeUpdate("DELETE FROM route WHERE rid='"+rID+"' AND cid='"+cID+"'");	
			rs = st.executeQuery("SELECT * FROM route where cid='"+cID+"'"); //
			rs.close();
			st.close();
			connection.close();
			return 1; // db  
		} catch (SQLException SQLex) {
			
			System.out.println("SQLException: " + SQLex.getMessage());
			System.out.println("SQLState: " + SQLex.getSQLState());
		}
		return 0; // 
	}
	
	
	public DBRouteData CallDBData_INDEX(String rID,String cID) {		
		System.out.println(rID +", " + cID);
		DBRouteData tmpIndex = new DBRouteData(rID, cID);
		try {	
			connection = ds.getConnection();
			st = connection.createStatement();
			String sqlStr = "SELECT * FROM route WHERE cid='"+cID+"' AND rid='"+rID+"'";
			System.out.println("CallDBData_INDEX : " + sqlStr);
			rs = st.executeQuery(sqlStr);		
			while(rs.next()) {
				int size = rs.getInt(2);
				tmpIndex.setDataSize(size);
				int addressCnt = 5;
				int latCnt = 6, lngCnt = 7;
				for(int i =0;i<size;i++) {
					tmpIndex.setAddress(i, rs.getString(addressCnt));
					tmpIndex.setLat(i, Double.parseDouble(rs.getString(latCnt)));
					tmpIndex.setLng(i, Double.parseDouble(rs.getString(lngCnt)));
					addressCnt += 3;
					latCnt += 3;
					lngCnt += 3;			
				}			
			}
			rs.close();
			st.close();
			connection.close();
		}catch (SQLException SQLex) {
			System.out.println("CallDBData_INDEX 오류발생");
			System.out.println("SQLException: " + SQLex.getMessage());
		}					
		return tmpIndex;
	}
	
	public Route2DataCall GetSavedRoute2Data(String cID,String rID) { //		
		Route2DataCall result = new Route2DataCall();
		try {	
			connection = ds.getConnection();
			st = connection.createStatement();
			String sqlStr = "SELECT * FROM route2 WHERE cid='"+cID+"' AND rid='"+ rID+"'";
			rs = st.executeQuery(sqlStr);	
			System.out.println("GetSavedRoute2Data : " + sqlStr);
			while(rs.next()) {
				result.pushData(rs.getString(3), rs.getString(4), rs.getInt(5), rs.getInt(6), rs.getInt(7));
			}
			rs.close();
			st.close();
			connection.close();
		} catch (SQLException SQLex) {
			System.out.println("GetSavedRoute2Data ");
			System.out.println("SQLException: " + SQLex.getMessage());
			System.out.println("SQLState: " + SQLex.getSQLState());
		}
		return result;
	}	
}