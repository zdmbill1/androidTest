package com.zdm.androidtest.search;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.provider.SearchRecentSuggestions;
import android.util.Log;
import android.widget.TextView;

import com.zdm.androidtest.R;

public class SearchUIActivity extends Activity {

	private TextView mQueryText,mAppDataText,mDeliveredByText;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search);
		
		 //两个TextView进行数据显示  
        mQueryText = (TextView) findViewById(R.id.txt_query);  
        mAppDataText = (TextView) findViewById(R.id.txt_appdata);  
  
        final Intent queryIntent = getIntent();  
        final String queryAction = queryIntent.getAction();  
        if (Intent.ACTION_SEARCH.equals(queryAction)) {  
            doSearchQuery(queryIntent, "onCreate()");  
        }  
        else {  
            mDeliveredByText.setText("onCreate(), but no ACTION_SEARCH intent");  
        } 
	}

	private void doSearchQuery(final Intent queryIntent, final String entryPoint) {  
		  
        //获取查询的值    
        final String queryString = queryIntent.getStringExtra(SearchManager.QUERY);  
        mQueryText.setText(queryString);  
  
        //保存查询的值，这样在下次搜索的时候会有以前搜索数据的提示  
        SearchRecentSuggestions suggestions = new SearchRecentSuggestions(this,   
                SearchSuggestionSampleProvider.AUTHORITY, SearchSuggestionSampleProvider.MODE);  
        suggestions.saveRecentQuery(queryString, null);  
          
        //获得额外递送过来的值  
        final Bundle appData = queryIntent.getBundleExtra(SearchManager.APP_DATA);  
        if (appData == null) {  
            mAppDataText.setText("<no app data bundle>");  
        }  
        if (appData != null) {  
            String testStr = appData.getString("demo_key");  
            mAppDataText.setText((testStr == null) ? "<no app data>" : testStr);  
        }  
    }  
	
	@Override  
    public void onNewIntent(final Intent newIntent) {  
        super.onNewIntent(newIntent);  
        Log.i("search", "SearchQueryResults-->onNewIntent()");  
        // get and process search query here  
        final Intent queryIntent = getIntent();  
        final String queryAction = queryIntent.getAction();  
        if (Intent.ACTION_SEARCH.equals(queryAction)) {  
            doSearchQuery(queryIntent, "onNewIntent()");  
        }  
        else {  
            mDeliveredByText.setText("onNewIntent(), but no ACTION_SEARCH intent");  
        }  
    }  

}
