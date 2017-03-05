package webapp;

import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.web.csrf.CsrfToken;

import com.google.gson.Gson;

import database.DbConnection;
import security.TokenGenerator;

@SuppressWarnings("serial")
public class AnimeNews extends HttpServlet{
	@Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
    		throws ServletException, IOException {
		
		TokenGenerator csrf = new TokenGenerator();
		CsrfToken checkToken = csrf.loadToken(req);
		ArrayList<String> resList = new ArrayList<String>();

		if(checkToken == null) {
			resList.add("missing or invalid csrf-token!");
		} else {
		
			DbConnection dbConn = new DbConnection();
			Connection conn = dbConn.iniConnection();

			resList = dbConn.selectValue(conn, "weekly");
			if(resList.size() == 0) {
				resList.add("empty");
			}
		
			dbConn.closeConnection(conn);
		}
		
		String json = new Gson().toJson(resList);
		resp.setHeader("Access-Control-Allow-Origin", "*");
		resp.setHeader("Accept", "application/json");
		resp.setHeader("Content-Type", "application/json; charset=utf-8");
        resp.getWriter().println(json);
    }
}
