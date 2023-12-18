package com.ahmed.fynd;

import static java.lang.Double.min;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

public class ChartActivity extends AppCompatActivity {

    ArrayList<Product> products;
    FyndDatabase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);

        db = new FyndDatabase(this);
        fill_top_products();
        // Get the PieChart view from XML
        PieChart pieChart = findViewById(R.id.pieChart);

        // Sample data for the pie chart
        ArrayList<PieEntry> entries = new ArrayList<>();
        for(int i =0;i<products.size();i++){
            entries.add(new PieEntry(
                    Float.valueOf(products.get(i).getSales()),
                    products.get(i).getName())
            );
        }


        // Create a dataset for the pie chart
        PieDataSet dataSet = new PieDataSet(entries, "Sample Pie Chart");
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        dataSet.setValueTextSize(12f);

        // Create the data object for the pie chart
        PieData pieData = new PieData(dataSet);

        // Set the data for the pie chart
        pieChart.setData(pieData);

        // Optional: Customize the pie chart
        pieChart.setDescription(null);  // Remove description label
        pieChart.setUsePercentValues(true);  // Display values as percentages
        pieChart.setEntryLabelColor(Color.BLACK);
        pieChart.setCenterText("Pie Chart");
        pieChart.setCenterTextSize(18f);

        // Refresh the pie chart
        pieChart.invalidate();

        findViewById(R.id.chart_go_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), HomeActivity.class);
                startActivity(i);
            }
        });

    }

    private void fill_top_products() {
        ArrayList<Product> p = db.top_selling_products();
        products = new ArrayList<>();
        for(int i = 0;i< min(p.size(),5);i++){
            Log.i("chart_stuff","I am in the loop");
            products.add(p.get(i));
        }
    }
}