package com.loal.pillai;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class HelpFragment extends Fragment {

    private TextView resultTextView;

    private Button updateButton;


    //TODO: Remove these and improve security
    public final String LOCAL_IP = "192.168.1.76";
    public final String LOCAL_PORT = "5432";
    public final String DATABASE_USER = "myuser";
    public final String DATABASE_PASSWORD = "mypassword";
    public final String DATABASE_NAME = "mydatabase";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_help, container, false);

        // Get views
        resultTextView = view.findViewById(R.id.resultTextView);
        updateButton = view.findViewById(R.id.updateButton);

        // Execute query when button is pressed
        updateButton.setOnClickListener(v -> new DatabaseTask().execute());

        return view;
    }

    private class DatabaseTask extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... voids) {
            // Collect the result from the query
            StringBuilder result = new StringBuilder();
            try {
                // Set up connection to database
                Class.forName("org.postgresql.Driver");
                String url = "jdbc:postgresql://" + LOCAL_IP + ":" + LOCAL_PORT + "/" + DATABASE_NAME;
                Connection conn = DriverManager.getConnection(url, DATABASE_USER, DATABASE_PASSWORD);

                // Create a statement (similar to a cursor)
                Statement statement = conn.createStatement();

                // Query database
                String sql = "SELECT * FROM products";
                ResultSet rs = statement.executeQuery(sql);

                // Handle the response
                while (rs.next()) {
                    result.append(rs.getString("name")).append("\n");
                }

                // Close connection
                rs.close();
                statement.close();
                conn.close();
            } catch (Exception e) {
                e.printStackTrace();
                result = new StringBuilder("Error fetching data.");
            }
            return result.toString();
        }

        @Override
        protected void onPostExecute(String result) {
            // After getting the response
            resultTextView.setText(result);
        }
    }
}

