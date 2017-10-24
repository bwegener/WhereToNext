package edu.orangecoastcollege.cs273.wheretonext;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.Toast;

import java.util.List;

/**
 * The <code>CollegeListActivity</code> creates a list of colleges.
 * The list shows basic information as well.
 *
 * @author Brian Wegener
 * @version 1.0
 *
 * Create on 10/23/2017
 */
public class CollegeListActivity extends AppCompatActivity {

    private DBHelper db;
    private List<College> collegesList;
    private CollegeListAdapter collegesListAdapter;
    private ListView collegesListView;

    private EditText mNameEditText;
    private EditText mPopulationEditText;
    private EditText mTuitionEditText;
    private RatingBar mRatingBar;


    /**
     * The onCreate sets up the database, sets up the layout for user input
     * and fills the list with some predetermined colleges.
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_college_list);

        this.deleteDatabase(DBHelper.DATABASE_NAME);
        db = new DBHelper(this);


        mNameEditText = (EditText) findViewById(R.id.nameEditText);
        mPopulationEditText = (EditText) findViewById(R.id.populationEditText);
        mTuitionEditText = (EditText) findViewById(R.id.tuitionEditText);
        mRatingBar = (RatingBar) findViewById(R.id.collegeRatingBar);

        // COMPLETED: Comment this section out once the colleges below have been added to the database
        // COMPLETED: so they are not added multiple times (prevent duplicate entries)
        db.addCollege(new College("UC Berkeley", 42082, 14068, 4.7, "ucb.png"));
        db.addCollege(new College("UC Irvine", 31551, 15026.47, 4.3, "uci.png"));
        db.addCollege(new College("UC Los Angeles", 43301, 25308, 4.5, "ucla.png"));
        db.addCollege(new College("UC San Diego", 33735, 20212, 4.4, "ucsd.png"));
        db.addCollege(new College("CSU Fullerton", 38948, 6437, 4.5, "csuf.png"));
        db.addCollege(new College("CSU Long Beach", 37430, 6452, 4.4, "csulb.png"));

        // COMPLETED:  Fill the collegesList with all Colleges from the database
        collegesList = db.getAllColleges();

        // COMPLETED:  Connect the list adapter with the list
        collegesListAdapter = new CollegeListAdapter(this, R.layout.college_list_item, collegesList);

        collegesListView = (ListView) findViewById(R.id.collegeListView);

        // COMPLETED:  Set the list view to use the list adapter
        collegesListView.setAdapter(collegesListAdapter);
    }

    /**
     * This allows the user to get a closer look at the details of
     * the clicked on college.
     * @param view
     */
    public void viewCollegeDetails(View view) {

        // COMPLETED: Implement the view college details using an Intent
        Intent detailsIntent = new Intent(this, CollegeDetailsActivity.class);
        LinearLayout selectedItem = (LinearLayout) view;
        College selectedCollege = (College) selectedItem.getTag();

        detailsIntent.putExtra("Name", selectedCollege.getName());
        detailsIntent.putExtra("Population", selectedCollege.getPopulation());
        detailsIntent.putExtra("Tuition", (float) selectedCollege.getTuition());
        detailsIntent.putExtra("Rating", (float) selectedCollege.getRating());
        detailsIntent.putExtra("ImageName", selectedCollege.getImageName());

        startActivity(detailsIntent);
    }

    /**
     * This adds a college to the list.
     * @param view
     */
    public void addCollege(View view) {

        // COMPLETED: Implement the code for when the user clicks on the addCollegeButton
        String name = mNameEditText.getText().toString();
        String population = mPopulationEditText.getText().toString();
        String tuition = mTuitionEditText.getText().toString();
        float rating = mRatingBar.getRating(); //Added rating to the list of variables being grabbed form UI
        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(population) || TextUtils.isEmpty(tuition))
            Toast.makeText(this, "You cannot leave any fields empty", Toast.LENGTH_SHORT).show();
        else {
            /*
            int populationValue = Integer.parseInt(mPopulationEditText.getText().toString());//Grab actual values after check for empty
            double tuitionValue = Double.parseDouble(mTuitionEditText.getText().toString());
            */
            College newCollege = new College(name, Integer.parseInt(population), Double.parseDouble(tuition), rating);
            db.addCollege(newCollege);

            // Notify the adapter
            collegesList.add(newCollege);
            collegesListAdapter.notifyDataSetChanged();

            mNameEditText.setText("");
            mPopulationEditText.setText("");
            mTuitionEditText.setText("");
            mRatingBar.setRating(0.0f);
        }
    }
}
