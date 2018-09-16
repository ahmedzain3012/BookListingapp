package com.example.android.az.booklistingapp;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class BookActivity extends AppCompatActivity {
    private BookAdapter mAdapter;
    /**
     * URL for book data from the API dataset
     */
    private static final String REQUEST_URL =
            "https://www.googleapis.com/books/v1/volumes?q=";

    /**
     * TextView for no data found text
     */
    private TextView emptyView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);

        // Create a new adapter that takes an empty list of books as input
        mAdapter = new BookAdapter(this, new ArrayList<Book>());
        ListView bookListView = (ListView) findViewById(R.id.list);
        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        bookListView.setAdapter(mAdapter);
        // Set an item click listener on the ListView, which sends an intent to a web browser
        // to open a website with more information about the selected book.
        bookListView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                        // Find the current book that was clicked on
                        Book currentBook = mAdapter.getItem(position);
                        // Convert the String URL into a URI object (to pass into the Intent constructor)
                        Uri bookUri = Uri.parse(currentBook.getmUrl());
                        // Create a new intent to view the book URI
                        Intent websiteIntent = new Intent(Intent.ACTION_VIEW, bookUri);
                        // Send the intent to launch a new activity
                        startActivity(websiteIntent);
                    }
                }
        );
        //find TextView for no data found / handle error message
        emptyView = (TextView) findViewById(R.id.empty_view);
        // Get a reference to the ConnectivityManager to check state of network connectivity
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        // Get details on the currently active default data network
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        // If there is a network connection, fetch data
        if (networkInfo != null && networkInfo.isConnected()) {
            // received intent key search
            Intent mainIntent = getIntent();
            String keySearch = mainIntent.getStringExtra("key_search");

            dataAsync task = new dataAsync();
            //URL concatenated with key search
            task.execute(REQUEST_URL+keySearch);

        } else {
            // Otherwise, display error
            // First, hide loading indicator so error message will be visible
            View loadingIndicator = findViewById(R.id.loading_indicator);
            loadingIndicator.setVisibility(View.GONE);
            // Update empty state with no connection error message
            emptyView.setText(R.string.no_internet_connection);
        }
    }

    private class dataAsync extends AsyncTask<String, Void, List<Book>> {
        @Override
        protected List<Book> doInBackground(String... urls) {
            // Don't perform the request if there are no URLs, or the first URL is null.
            if (urls.length < 1 || urls[0] == null) {
                return null;
            }
            List<Book> book = QueryUtils.fetchUrlData(urls[0]);
            return book;
        }

        @Override
        protected void onPostExecute(List<Book> books) {
            // Hide loading indicator because the data has been loaded
            View loadingIndicator = findViewById(R.id.loading_indicator);
            loadingIndicator.setVisibility(View.GONE);
            if (books == null) {
                return;
            }
            updateUi(books);
        }

        /**
         * Update the UI with the given book information.
         */
        private void updateUi(List<Book> books) {
            // Hide loading indicator because the data has been loaded
            View loadingIndicator = findViewById(R.id.loading_indicator);
            loadingIndicator.setVisibility(View.GONE);
            // If there is a valid list of {@link book}s, then add them to the adapter's
            // data set. This will trigger the ListView to update.
            if (books != null && !books.isEmpty()) {
                mAdapter.addAll(books);
            } else {
                // Clear the adapter of previous book data
                mAdapter.clear();
                // Set empty state text to display "No data found."
                emptyView.setText(R.string.no_data);
            }
        }
    }
}
